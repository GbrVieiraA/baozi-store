package br.com.baozistore.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
@Entity
@Table(name = "produtos")
public class Produto {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 public Produto() {}
 public Long getId() { return id; }
 @NotBlank @Size(max = 120) @Column(nullable = false, length = 120) private String nome;
 public String getNome() { return nome; }
 public void setNome(String nome) { this.nome = nome; }
 @NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2) @Column(nullable = false, precision = 10, scale = 2) private BigDecimal preco;
 public BigDecimal getPreco() { return preco; }
 public void setPreco(BigDecimal preco) { this.preco = preco; }
 @NotNull @Column(nullable = false) private Boolean estoque;
 public Boolean getEstoque() { return estoque; }
 public void setEstoque(Boolean estoque) { this.estoque = estoque; }
}
