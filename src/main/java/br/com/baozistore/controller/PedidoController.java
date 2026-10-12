package br.com.baozistore.controller;
import br.com.baozistore.model.*;
import br.com.baozistore.dto.*;
import br.com.baozistore.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/pedidos")
public class PedidoController {
 private final PedidoRepository pedidos; private final ClienteRepository clientes; private final ProdutoRepository produtos;
 public PedidoController(PedidoRepository p, ClienteRepository c, ProdutoRepository r) { pedidos=p; clientes=c; produtos=r; }
 @PostMapping @Transactional public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest dados) {
  Cliente cliente=clientes.findById(dados.clienteId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
  Produto produto=produtos.findById(dados.produtoId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
  if (!Boolean.TRUE.equals(produto.getEstoque())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponível");
  Pedido pedido=new Pedido(); pedido.setCliente(cliente); pedido.setProduto(produto); pedido.setQuantidade(dados.quantidade());
  Pedido salvo=pedidos.save(pedido);
  return ResponseEntity.created(URI.create("/pedidos/"+salvo.getId())).body(PedidoResponse.from(salvo));
 }
 @GetMapping public List<PedidoResponse> listar() { return pedidos.findAll(org.springframework.data.domain.Sort.by("id")).stream().map(PedidoResponse::from).toList(); }
 private Pedido buscar(Long id) { return pedidos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado")); }
 @GetMapping("/{id}") public PedidoResponse consultar(@PathVariable Long id) { return PedidoResponse.from(buscar(id)); }
 @DeleteMapping("/{id}") @Transactional public ResponseEntity<Void> excluir(@PathVariable Long id) { pedidos.delete(buscar(id)); return ResponseEntity.noContent().build(); }
}
