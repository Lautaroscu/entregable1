package src.DTO;

import src.entities.Cliente;

import java.io.Serializable;

public record ClienteDTO(Cliente cliente , int cantFacturas) implements Serializable {
@Override
    public String toString() {
    return cliente.toString() + " cantidad de facturas : " +  cantFacturas + "\n";
}

}
