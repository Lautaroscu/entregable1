package src.Factories;

import java.sql.Connection;

public abstract class DatabaseFactory {
    public abstract ConexionBD createConnection();

    public static DatabaseFactory getFactory(String db) {
        switch (db){
            case "postgres":
                return new PostgresFactory();
            case "derby" :
                return new DerbyFactory();
            default:
                return null;

        }
    }
}
