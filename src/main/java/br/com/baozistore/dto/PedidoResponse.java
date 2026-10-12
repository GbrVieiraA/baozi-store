package br.com.baozistore.dto;
import br.com.baozistore.model.Pedido;
public record PedidoResponse(Long id, Long clienteId, Long produtoId, Integer quantidade) {
 public static PedidoResponse from(Pedido p) { return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getProduto().getId(), p.getQuantidade()); }
}
