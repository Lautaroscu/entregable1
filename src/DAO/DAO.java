package src.DAO;

import src.exceptions.DAOException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DAO<T , K>{
 void close() throws SQLException;

void delete(Map<String , K> id) throws DAOException;
void save(T t) throws DAOException;
T findById(Map<String , K> id) throws DAOException;
void update(T t , Map<String , K> id) throws DAOException;
List<T> findAll() throws DAOException;
}
