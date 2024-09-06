package src.DAO;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import src.entities.Cliente;
import src.exceptions.DAOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class ClienteDAO extends GenericDAO<Cliente , Integer> {

public ClienteDAO(Connection conn) throws DAOException {
  super(conn , Cliente.class);
  String urlCLientes = "./src/dataset/clientes.csv";
  try {
      CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new
            FileReader(urlCLientes));
    for(CSVRecord row: parser) {
      int id = Integer.parseInt(row.get("idCliente"));
      String nombre = row.get("nombre");
      String mail = row.get("email");
      Cliente cliente = new Cliente(id, nombre, mail);
      this.save(cliente);
    }
    }catch (IOException e) {
    throw new DAOException("file not found" , e);
  }

  }
  }


