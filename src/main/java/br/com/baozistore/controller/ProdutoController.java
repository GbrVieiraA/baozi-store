package br.com.baozistore.controller;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/produtos")
public class ProdutoController {
 private final ProdutoRepository repository;
 private final PedidoRepository pedidos;
 public ProdutoController(ProdutoRepository repository, PedidoRepository pedidos) { this.repository = repository; this.pedidos = pedidos; }
 @PostMapping public ResponseEntity<Produto> criar(@Valid @RequestBody Produto novo) {
  Produto salvo = repository.save(novo);
  return ResponseEntity.created(URI.create("/produtos/" + salvo.getId())).body(salvo);
 }
 @GetMapping public List<Produto> listar() { return repository.findAll(org.springframework.data.domain.Sort.by("id")); }
 @GetMapping("/{id}") public Produto consultar(@PathVariable Long id) {
  return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
 }
 @DeleteMapping("/{id}") @Transactional public ResponseEntity<Void> excluir(@PathVariable Long id) {
  Produto entidade = consultar(id);
  if (pedidos.existsByProdutoId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto vinculado a um pedido; exclua o pedido primeiro");
  repository.delete(entidade); return ResponseEntity.noContent().build();
 }
}
