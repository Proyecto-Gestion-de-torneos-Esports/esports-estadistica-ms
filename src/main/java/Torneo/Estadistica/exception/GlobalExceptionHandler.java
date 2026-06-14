package Torneo.Estadistica.exception;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((FieldError error) ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, String>> handleFeignNotFoundException(FeignException.NotFound ex) {
        Map<String, String> error = new LinkedHashMap<>();
        String urlComprobar = ex.request().url();


        if (urlComprobar.contains("/api/partidas")) {
            error.put("error", "Partida no encontrada");
            error.put("mensaje", "La partida no existe o fue eliminada");

        } else if(urlComprobar.contains("/api/usuarios")) {
            error.put("error","Usuario no encontrado");
            error.put("mensaje","El usuario no existe o fue eliminado");
        }else {
            error.put("error", "Recurso no encontrado");
            error.put("mensaje", "El recurso solicitado en el microservicio externo no existe.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

