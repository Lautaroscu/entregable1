package src.DAO;

import lombok.Getter;
import lombok.Setter;
import src.exceptions.DAOException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Setter
@Getter
public class GenericDAO<T, K> implements DAO<T, K> {

    private String tableName;
    private Connection coneccion;
    private Class<T> type;


    public GenericDAO(Connection conn, Class<T> type) throws DAOException {
        this.tableName = type.getSimpleName();
        this.coneccion = conn;
        this.type = type;
    }


    @Override
    public void delete(Map<String, K> id) throws DAOException {
        try {
            StringBuilder sql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
            StringBuilder condition = new StringBuilder();
            for (String pkName : id.keySet()) {
                condition.append(pkName).append(" = ? , ");
            }
            condition.delete(condition.length() - 1, condition.length());

            sql.append(condition);
            PreparedStatement preparedStatement = coneccion.prepareStatement(sql.toString());
            int i = 0;
            for (K value : id.values()) {
                preparedStatement.setObject(i + 1, value);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el registro", e);
        }
    }

    @Override
    public void save(T obj) throws DAOException {
        try {
            Class<?> objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();

            StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder valuesPart = new StringBuilder(" VALUES (");
            for (Field field : fields) {
                field.setAccessible(true);
                query.append(field.getName()).append(", ");
                valuesPart.append("?, ");
            }

            query.delete(query.length() - 2, query.length());
            valuesPart.delete(valuesPart.length() - 2, valuesPart.length());
            query.append(")");
            valuesPart.append(")");

            String fullQuery = query.toString() + valuesPart.toString();

            PreparedStatement preparedStatement = coneccion.prepareStatement(fullQuery);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                preparedStatement.setObject(i + 1, field.get(obj));
            }

            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            throw new DAOException("Error al guardar el registro", e);
        }
    }

    @Override
    public T findById(Map<String, K> id) throws DAOException {
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
            StringBuilder condicionQuery = new StringBuilder();
            for (String pkName : id.keySet()) {
                condicionQuery.append(pkName).append(" = ? AND ");
            }
            condicionQuery.delete(condicionQuery.length() - 5, condicionQuery.length());
    sql.append(condicionQuery);
            PreparedStatement preparedStatement = coneccion.prepareStatement(sql.toString());
            int i = 0;
            for (K value : id.values()) {
                System.out.println(value);
                preparedStatement.setObject(++i, value);
            }
        System.out.println(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapper(resultSet);
            }
            return null;
        } catch (SQLException  e) {
            throw new DAOException("Error al encontrar el registro por ID", e);
        }
    }

    @Override
    public void update(T obj, Map<String, K> id) throws DAOException {
        try {
            Class<?> objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();

            StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
            for (Field field : fields) {
                field.setAccessible(true);
                query.append(field.getName()).append(" = ?, ");
            }

            query.delete(query.length() - 2, query.length());

            query.append(" WHERE ");
            for (String pkName : id.keySet()) {
                query.append(pkName).append(" = ? AND ");
            }
            query.delete(query.length() - 5, query.length());

            PreparedStatement preparedStatement = coneccion.prepareStatement(query.toString());

            int index = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                preparedStatement.setObject(++index, field.get(obj));
            }

            for (K value : id.values()) {
                preparedStatement.setObject(++index, value);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            throw new DAOException("Error al actualizar el registro", e);
        }
    }

    @Override
    public List<T> findAll() throws DAOException {
        try {
            List<T> results = new ArrayList<>();
            PreparedStatement ps = coneccion.prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                T instance = mapper(rs);
                results.add(instance);
            }

            return results;
        } catch (SQLException  e) {
            throw new DAOException("Error al obtener todos los registros", e);
        }
    }

    protected T mapper(ResultSet rs) throws DAOException {
        try {
            T instance = type.getDeclaredConstructor().newInstance();

            for (Field field : type.getDeclaredFields()) {
                System.out.println(field.getName());
                field.setAccessible(true);
                Object value = rs.getObject(field.getName());
                field.set(instance, value);
            }
            return instance;
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new DAOException("Error al mapear el resultado", e);
        }
    }
}
