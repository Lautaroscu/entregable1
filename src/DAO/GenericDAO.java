package src.DAO;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Setter
@Getter
public class GenericDAO<T , K> implements DAO<T ,K>{

    private String tableName;
    private Connection coneccion;
    private  Class<T> type;

    public GenericDAO(String tn , Connection conn , Class<T> type) throws SQLException {
        this.tableName = tn;
        this.type = type;
        this.coneccion = conn;


    }







    @Override
    public void delete(Map<String , K> id) throws SQLException {
       StringBuilder sql = new StringBuilder("DELETE FROM "+tableName+" WHERE ");
       StringBuilder condition = new StringBuilder();
       for(String pkName : id.keySet()){
           condition.append(pkName).append(" = ? , ");
       }
       condition.delete(condition.length() -1 , condition.length());

       sql.append(condition);
       PreparedStatement preparedStatement = coneccion.prepareStatement(sql.toString());
       int i = 0;
       for (K value : id.values()) {
           preparedStatement.setObject(i+1 , value);
       }
    }

    @Override
    public void save(T obj) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();

        // Construcción de la consulta SQL
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder valuesPart = new StringBuilder(" VALUES (");
        for (Field field : fields) {
            field.setAccessible(true);
            query.append(field.getName()).append(", ");
            valuesPart.append("?, ");
        }

        query.delete(query.length() - 2, query.length()); // Elimina la última coma y espacio
        valuesPart.delete(valuesPart.length() - 2, valuesPart.length()); // Elimina la última coma y espacio
        query.append(")");
        valuesPart.append(")");

        // La consulta completa
        String fullQuery = query.toString() + valuesPart.toString();
        System.out.println("Query generada: " + fullQuery);

        // Preparar y ejecutar el PreparedStatement

            PreparedStatement preparedStatement = coneccion.prepareStatement(fullQuery);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                preparedStatement.setObject(i + 1, field.get(obj));

            }

      preparedStatement.executeUpdate();

    }

    @Override
    public T findById(Map<String, K> id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String condicionQuery = "";


        for(String pkName : id.keySet()) {
            condicionQuery += pkName + " = ?";
        }
        PreparedStatement preparedStatement = coneccion.prepareStatement("SELECT * FROM " + tableName + " WHERE " + condicionQuery);
        int i = 0;
        for(K values : id.values()) {
            preparedStatement.setObject(i+1 , values);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return  mapper(resultSet);
        }
        return null;

    }

    @Override
    public void update(T obj , Map<String , K> id) throws IllegalAccessException, SQLException {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();

        // Construcción de la parte SET de la consulta SQL
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (Field field : fields) {
            field.setAccessible(true);
            query.append(field.getName()).append(" = ?, ");
        }

        // Eliminar la última coma y espacio
        query.delete(query.length() - 2, query.length());

        // Construcción de la condición WHERE
        query.append(" WHERE ");
        for (String pkName : id.keySet()) {
            query.append(pkName).append(" = ? AND ");
        }

        // Eliminar el último "AND "
        query.delete(query.length() - 5, query.length());

        // Preparar la consulta SQL
        PreparedStatement preparedStatement = coneccion.prepareStatement(query.toString());

        // Setear los valores de los campos del objeto
        int index = 0;
        for (Field field : fields) {
            field.setAccessible(true);

            System.out.println(field.getName());
            preparedStatement.setObject(++index,field.get(obj));
        }

        // Setear los valores de las claves primarias
        for (K value : id.values()) {
            preparedStatement.setObject(++index , value);
        }

        preparedStatement.executeUpdate();

    }

    @Override
    public List<T> findAll() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();
        PreparedStatement ps = coneccion.prepareStatement("SELECT * FROM " + tableName);
        ResultSet rs = ps.executeQuery();


        while (rs.next()) {
        T instance = mapper(rs);
            results.add(instance); // Agregar la instancia a la lista de resultados
        }

        return results;
    }

private  T mapper(ResultSet rs) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
    T instance = type.getDeclaredConstructor().newInstance(); // Crear una nueva instancia de T
    // Mapear los campos del ResultSet a los campos de la instancia T
    for (Field field : type.getDeclaredFields()) {
        field.setAccessible(true); // Permitir acceso a campos privados
        Object value = rs.getObject(field.getName()); // Obtener el valor de la columna correspondiente
        field.set(instance, value); // Asignar el valor al campo de la instancia
    }
    return instance;

}
}
