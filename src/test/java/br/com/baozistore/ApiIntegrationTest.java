package br.com.baozistore;
import br.com.baozistore.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1", "spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
class ApiIntegrationTest {
 @Autowired MockMvc mvc; @Autowired ObjectMapper json;
 @Autowired PedidoRepository pedidos; @Autowired ClienteRepository clientes; @Autowired ProdutoRepository produtos;
 @BeforeEach void limpar() { pedidos.deleteAll(); clientes.deleteAll(); produtos.deleteAll(); }
 long criar(String rota,String corpo) throws Exception {
  String resposta=mvc.perform(post(rota).contentType(MediaType.APPLICATION_JSON).content(corpo)).andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn().getResponse().getContentAsString();
  return json.readTree(resposta).get("id").asLong();
 }
 @Test void fluxoCompletoPreservaRelacionamentos() throws Exception {
  long c=criar("/clientes","{\"nome\":\"GabrielVieiraDeAbreu5033399\",\"clienteDesde\":\"2026-10-12\"}");
  long p=criar("/produtos","{\"nome\":\"Baozi de carne suína\",\"preco\":8.50,\"estoque\":true}");
  long pedido=criar("/pedidos","{\"clienteId\":"+c+",\"produtoId\":"+p+",\"quantidade\":5}");
  for(String rota:new String[]{"/clientes","/produtos","/pedidos"}) mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
  mvc.perform(get("/clientes/"+c)).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("GabrielVieiraDeAbreu5033399"));
  mvc.perform(get("/produtos/"+p)).andExpect(status().isOk()).andExpect(jsonPath("$.preco").value(8.5));
  mvc.perform(get("/pedidos/"+pedido)).andExpect(status().isOk()).andExpect(jsonPath("$.clienteId").value(c)).andExpect(jsonPath("$.produtoId").value(p)).andExpect(jsonPath("$.quantidade").value(5));
  mvc.perform(delete("/clientes/"+c)).andExpect(status().isConflict());
  mvc.perform(delete("/produtos/"+p)).andExpect(status().isConflict());
  mvc.perform(delete("/pedidos/"+pedido)).andExpect(status().isNoContent());
  mvc.perform(delete("/clientes/"+c)).andExpect(status().isNoContent());
  mvc.perform(delete("/produtos/"+p)).andExpect(status().isNoContent());
  mvc.perform(get("/pedidos/"+pedido)).andExpect(status().isNotFound());
 }
 @Test void rejeitaDadosInvalidosEProdutoIndisponivel() throws Exception {
  mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"\",\"clienteDesde\":\"2999-01-01\"}")).andExpect(status().isBadRequest());
  mvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Baozi\",\"preco\":-1,\"estoque\":true}")).andExpect(status().isBadRequest());
  mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("{\"clienteId\":1,\"produtoId\":1,\"quantidade\":0}")).andExpect(status().isBadRequest());
  mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("{\"clienteId\":999,\"produtoId\":999,\"quantidade\":1}")).andExpect(status().isNotFound());
  long c=criar("/clientes","{\"nome\":\"Cliente teste\",\"clienteDesde\":\"2026-01-01\"}");
  long p=criar("/produtos","{\"nome\":\"Baozi\",\"preco\":8.50,\"estoque\":false}");
  mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("{\"clienteId\":"+c+",\"produtoId\":"+p+",\"quantidade\":5}")).andExpect(status().isConflict());
  mvc.perform(get("/clientes/abc")).andExpect(status().isBadRequest());
 }
}
