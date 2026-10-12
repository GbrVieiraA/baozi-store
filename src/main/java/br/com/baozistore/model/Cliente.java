package br.com.baozistore.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
@Entity
@Table(name = "clientes")
public class Cliente {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 public Cliente() {}
 public Long getId() { return id; }
 @NotBlank @Size(max = 120) @Column(nullable = false, length = 120) private String nome;
 public String getNome() { return nome; }
 public void setNome(String nome) { this.nome = nome; }
 @NotNull @PastOrPresent @Column(nullable = false) private LocalDate clienteDesde;
 public LocalDate getClienteDesde() { return clienteDesde; }
 public void setClienteDesde(LocalDate clienteDesde) { this.clienteDesde = clienteDesde; }
}
