package faeterj.sistema_loja.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ItensPedidoModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_item_id;
 
    @ManyToOne
    @JoinColumn(name = "order_id")
    private PedidosModel pedido;

    @ManyToOne
    @JoinColumn(name = "sku")
    private ProdutosModel produto;

    private int quantity_purchased;

    private double item_price;
}
