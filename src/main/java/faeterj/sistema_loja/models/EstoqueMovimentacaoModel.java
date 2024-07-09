package faeterj.sistema_loja.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class EstoqueMovimentacaoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private ProdutosModel produto;

    private int quantidadeMovimentada;
    private int saldoEstoque;
    private LocalDate dataMovimentacao;
}