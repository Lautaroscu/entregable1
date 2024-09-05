package src.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaProducto {
        private int idFactura;
        private int idProducto;
        private int cantidad;

        public FacturaProducto(int idFactura, int idProducto, int cantidad) {
            this.idFactura = idFactura;
            this.idProducto = idProducto;
            this.cantidad = cantidad;
        }
}
