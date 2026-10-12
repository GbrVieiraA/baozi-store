package br.com.baozistore.repository;
import br.com.baozistore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
 boolean existsByClienteId(Long id);
 boolean existsByProdutoId(Long id);
}
