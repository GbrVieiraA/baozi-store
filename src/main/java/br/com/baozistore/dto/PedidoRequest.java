package br.com.baozistore.dto;
import jakarta.validation.constraints.*;
public record PedidoRequest(@NotNull @Positive Long clienteId, @NotNull @Positive Long produtoId, @NotNull @Min(1) Integer quantidade) {}
