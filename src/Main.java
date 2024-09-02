package src;

import src.DAO.GenericDAO;
import src.Factories.ConexionBD;
import src.Factories.DatabaseFactory;
import src.entities.Cliente;
import src.sql.EsquemaTp1;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DatabaseFactory factory = DatabaseFactory.getFactory("derby");

        ConexionBD conexionBD = factory.createConnection();
        if(conexionBD == null){
            System.out.println("No se puede crear la BD");
            return;
        }
        Connection conn = conexionBD.connect();
        EsquemaTp1.crearEsquema(conn);
        GenericDAO<Cliente , Integer> crudCliente = new GenericDAO<>("cliente" , conn , Cliente.class);
        HashMap<String , Integer> idCliente = new HashMap<>();
        idCliente.put("idCliente", 1);
       Cliente cliente = crudCliente.findById(idCliente);
       System.out.println(cliente);
       cliente.setNombre("pepeEditado");
       crudCliente.update(cliente , idCliente);
       System.out.println(crudCliente.findById(idCliente));

    }
}
