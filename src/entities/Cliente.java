package src.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;
    public Cliente(int idCliente, String nombre, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
    }
    public Cliente() {}

    @Override
    public String toString() {
        return  idCliente + "\t" + nombre + "\t" + email + "\n";

    }
}
