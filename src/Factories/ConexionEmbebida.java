package src.Factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionEmbebida implements ConexionBD{
    private String url;
    public ConexionEmbebida(String url) {
        this.url = url;
    }
    @Override
    public Connection connect() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    }

