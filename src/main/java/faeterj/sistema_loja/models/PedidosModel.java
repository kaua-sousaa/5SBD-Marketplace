package faeterj.sistema_loja.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class PedidosModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;

    @Column(nullable = false)
    private LocalDate order_date;

    @Column(nullable = false)
    private LocalDate payments_date;

    @Column(nullable = false)
    private String ship_service_level;

    @Column(nullable = false)
    private String ship_address_1;

    @Column(nullable = false)
    private String ship_address_2;

    @Column(nullable = false)
    private String ship_address_3;

    @Column(nullable = false)
    private String ship_city;

    @Column(nullable = false)
    private String ship_state;

    @Column(nullable = false)
    private String ship_postal_code;

    @Column(nullable = false)
    private String ship_country;

}
