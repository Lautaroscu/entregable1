package src.DAO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import src.entities.Cliente;
import src.entities.Factura;
import src.exceptions.DAOException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

public class FacturaDAO extends GenericDAO<Factura, Integer>{
    public FacturaDAO(Connection con) throws DAOException {
        super(con , Factura.class);

        String urlFacturas = "./src/dataset/facturas.csv";
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new
                    FileReader(urlFacturas));
            for(CSVRecord row: parser) {
                int id = Integer.parseInt(row.get("idFactura"));
                int idCliente = Integer.parseInt(row.get("idCliente"));

                Factura factura = new Factura(id, idCliente);
                this.save(factura);
            }
        }catch (IOException e) {
            throw new DAOException("file not found" , e);
        }

    }
    }

