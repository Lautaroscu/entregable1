package src.Factories;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresFactory extends DatabaseFactory{
    public PostgresFactory() {

    }
    @Override
    public ConexionBD createConnection() {
        return new Conexion("jdbc:postgresql://localhost:5432/dbtp1", "postgres", "lau");
    }
}
