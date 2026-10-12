package br.com.baozistore.controller;
import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/clientes")
public class ClienteController {
 private final ClienteRepository repository;
 private final PedidoRepository pedidos;
 public ClienteController(ClienteRepository repository, PedidoRepository pedidos) { this.repository = repository; this.pedidos = pedidos; }
 @PostMapping public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente novo) {
  Cliente salvo = repository.save(novo);
  return ResponseEntity.created(URI.create("/clientes/" + salvo.getId())).body(salvo);
 }
 @GetMapping public List<Cliente> listar() { return repository.findAll(org.springframework.data.domain.Sort.by("id")); }
 @GetMapping("/{id}") public Cliente consultar(@PathVariable Long id) {
  return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
 }
 @DeleteMapping("/{id}") @Transactional public ResponseEntity<Void> excluir(@PathVariable Long id) {
  Cliente entidade = consultar(id);
  if (pedidos.existsByClienteId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente vinculado a um pedido; exclua o pedido primeiro");
  repository.delete(entidade); return ResponseEntity.noContent().build();
 }
}
