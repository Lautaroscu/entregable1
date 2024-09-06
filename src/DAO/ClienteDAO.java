package src.DAO;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import src.DTO.ClienteDTO;
import src.entities.Cliente;
import src.exceptions.DAOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
  public List<ClienteDTO> getClientesFacturacion() throws DAOException {
  ArrayList<ClienteDTO> clientesFacturacion = new ArrayList<>();
  try {
    String query = "SELECT c.idcliente, c.nombre , c.email , COUNT(*) as cant_facturas  FROM cliente c join factura f on c.idcliente = f.idcliente\n" +
            "GROUP BY c.idcliente ORDER BY cant_facturas DESC;";
    PreparedStatement ps = this.getConeccion().prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
        Cliente cliente = this.mapper(rs);
        int cantFacturas = rs.getInt("cant_facturas");
        ClienteDTO dto = new ClienteDTO(cliente , cantFacturas);
        clientesFacturacion.add(dto);
    }
    close();
    return clientesFacturacion;

  }catch (SQLException e)  {
    throw new DAOException("file not found" , e);
  }




  }
  }


