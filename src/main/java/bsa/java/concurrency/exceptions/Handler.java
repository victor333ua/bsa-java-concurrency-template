package bsa.java.concurrency.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public final class Handler extends ResponseEntityExceptionHandler {
/*
    @ExceptionHandler(GifNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(GifNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        Map.of(
                                "error", ex.getMessage() == null ? "Unprocessable request parameters" : ex.getMessage()
                        )
                );
    }
*/
    @ExceptionHandler(MyIoException.class)
    public ResponseEntity<Object> handleIOException(MyIoException ex) {
        return ResponseEntity
                .unprocessableEntity()
                .body(Map.of("error",
                        ex.getMessage() == null ? "Unprocessable request parameters" : ex.getMessage()
                        )
                );
    }
}
