package src.DAO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import src.entities.Factura_Producto;
import src.exceptions.DAOException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

public class FacturaProductoDAO extends GenericDAO<Factura_Producto, Integer>
{

    public FacturaProductoDAO(Connection conn) throws DAOException {
        super(conn, Factura_Producto.class);
        String urlFP = "./src/dataset/facturas-productos.csv";
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new
                    FileReader(urlFP));
            for(CSVRecord row: parser) {
                int idFactura = Integer.parseInt(row.get("idFactura"));
                int idProducto = Integer.parseInt(row.get("idProducto"));

                int cantidad = Integer.parseInt(row.get("cantidad"));

                Factura_Producto facturaProducto = new Factura_Producto(idFactura, idProducto , cantidad);
                this.save(facturaProducto);
            }
        }catch (IOException e) {
            throw new DAOException("file not found" , e);
        }
    }

}
