package src.Factories;

public class DerbyFactory extends DatabaseFactory{
    public DerbyFactory() {

    }
 @Override
    public ConexionBD createConnection() {
        return new ConexionEmbebida("jdbc:derby:tp1;create=true");
    }


}
