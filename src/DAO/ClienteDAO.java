package src.DAO;

import src.entities.Cliente;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class ClienteDAO extends GenericDAO<Cliente , Integer> {

    public ClienteDAO(String tn, Connection conn, Class<Cliente> type) throws SQLException {
        super(tn, conn, type);
    }
    public void insertCliente(Cliente obj) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super.save(obj);
    }

}
