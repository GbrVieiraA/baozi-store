package br.com.baozistore.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
@Entity
@Table(name = "pedidos")
public class Pedido {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 public Pedido() {}
 public Long getId() { return id; }
  @ManyToOne(optional = false, fetch = FetchType.EAGER) @JoinColumn(name = "cliente_id", nullable = false) private Cliente cliente;
 public Cliente getCliente() { return cliente; }
 public void setCliente(Cliente cliente) { this.cliente = cliente; }
  @ManyToOne(optional = false, fetch = FetchType.EAGER) @JoinColumn(name = "produto_id", nullable = false) private Produto produto;
 public Produto getProduto() { return produto; }
 public void setProduto(Produto produto) { this.produto = produto; }
 @NotNull @Min(1) @Column(nullable = false) private Integer quantidade;
 public Integer getQuantidade() { return quantidade; }
 public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
