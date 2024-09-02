package src.DAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DAO<T , K>{
void delete(Map<String , K> id) throws SQLException;
void save(T t) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
T findById(Map<String , K> id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
void update(T t , Map<String , K> id) throws IllegalAccessException, SQLException;
List<T> findAll() throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
