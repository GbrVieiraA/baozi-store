package br.com.baozistore.exception;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<Map<String,Object>> negocio(ResponseStatusException ex) { return erro(ex.getStatusCode().value(), ex.getReason()); }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<Map<String,Object>> validar(MethodArgumentNotValidException ex) {
  Map<String,String> campos=new TreeMap<>(); ex.getBindingResult().getFieldErrors().forEach(e -> campos.put(e.getField(), e.getDefaultMessage()));
  Map<String,Object> body=new LinkedHashMap<>(); body.put("status",400); body.put("mensagem","Dados inválidos"); body.put("campos",campos);
  return ResponseEntity.badRequest().body(body);
 }
 @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
 public ResponseEntity<Map<String,Object>> formato(Exception ex) { return erro(400,"JSON, data ou identificador em formato inválido"); }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<Map<String,Object>> integridade(DataIntegrityViolationException ex) { return erro(409,"Operação viola a integridade dos dados"); }
 private ResponseEntity<Map<String,Object>> erro(int status,String mensagem) { return ResponseEntity.status(status).body(Map.of("status",status,"mensagem",mensagem == null ? "Erro na requisição" : mensagem)); }
}
