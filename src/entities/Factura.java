package src.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Factura {

    private  int idFactura;
    private  int idCliente;

    public Factura(int idFactura, int idCliente) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
    }

}
