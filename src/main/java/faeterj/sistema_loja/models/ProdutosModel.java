package faeterj.sistema_loja.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ProdutosModel {
    
    @Id
    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private double item_price;

    @Column(nullable = false)
    private int quantity_in_stock;
}
