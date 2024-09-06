package src.DAO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import src.entities.Factura;
import src.entities.Producto;
import src.exceptions.DAOException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProdcutoDAO extends GenericDAO<Producto , Integer> {
    public ProdcutoDAO(Connection con) throws DAOException {
        super(con , Producto.class);

        String urlProductos = "./src/dataset/productos.csv";
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new
                    FileReader(urlProductos));
            for(CSVRecord row: parser) {
                int id = Integer.parseInt(row.get("idProducto"));
                String nombre = row.get("nombre");
                float costo = Float.parseFloat(row.get("valor"));

                Producto producto = new Producto(id, nombre , costo);
                this.save(producto);
            }
        }catch (IOException e) {
            throw new DAOException("file not found" , e);
        }

    }
    public  Producto getProductoMasRecaudo() throws DAOException {
        String query ="SELECT p.idproducto , p.nombre , p.valor , (SUM(fp.cantidad) * p.valor) as recaudacion  FROM factura_producto fp inner join producto p on p.idproducto = fp.idproducto\n" +
                "GROUP BY p.idproducto , p.nombre , p.valor  order by recaudacion desc limit 1";

        try {
            PreparedStatement preparedStatement = this.getConeccion().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("idproducto");
                String nombre = resultSet.getString("nombre");
                float costo = resultSet.getFloat("valor");
                Producto producto = new Producto(id, nombre , costo);
                close();
                return producto;
            }




        }catch (SQLException e){
            throw new DAOException("prodcut does not exist" , e);
        }
    return null;
    }
}
