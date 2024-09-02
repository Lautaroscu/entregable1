package src.sql;



import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class EsquemaTp1 {
    public static void crearEsquema(Connection conn){

        try {
            String dbProductName = conn.getMetaData().getDatabaseProductName().toLowerCase();

            String keyWordExists =dbProductName.contains("postgres") ? "IF NOT EXISTS" : "";

            System.out.println("Creando esquema...");
            Statement st = conn.createStatement();



                String tablaCliente = "CREATE TABLE " +keyWordExists  + " Cliente (" +
                        "idCliente INT PRIMARY KEY, " +
                        "nombre VARCHAR(500), " +
                        "email VARCHAR(150))";

            String tablaFactura = "CREATE TABLE "+ keyWordExists +" Factura (\n" +
                    "    idFactura INT PRIMARY KEY,\n" +
                    "    idCliente INT,\n" +
                    "    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)\n" +
                    ")";
            String tablaProducto = "CREATE TABLE "+ keyWordExists + " Producto (\n" +
                    "    idProducto INT PRIMARY KEY,\n" +
                    "    nombre VARCHAR(45),\n" +
                    "    valor FLOAT\n" +
                    ")";
            String tablaFacturaProducto = "CREATE TABLE " + keyWordExists +  " Factura_Producto (\n" +
                    "    idFactura INT,\n" +
                    "    idProducto INT,\n" +
                    "    cantidad INT,\n" +
                    "    PRIMARY KEY (idFactura, idProducto),\n" +
                    "    FOREIGN KEY (idFactura) REFERENCES Factura(idFactura),\n" +
                    "    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)\n" +
                    ")";

            st.execute(tablaCliente);
            st.execute(tablaFactura);
            st.execute(tablaProducto);
            st.execute(tablaFacturaProducto);

        }catch (
                SQLException e
        ){
            System.out.println(e);
        }


    }
}
