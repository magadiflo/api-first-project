package dev.magadiflo.accounts.app.exception.handler;

import dev.magadiflo.accounts.app.exception.model.AccountNotFoundException;
import dev.magadiflo.accounts.app.exception.model.BusinessRuleException;
import dev.magadiflo.accounts.app.exception.model.InvalidStatusTransitionException;
import dev.magadiflo.accounts.app.exception.response.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleAccountNotFoundException(AccountNotFoundException ex) {
        log.debug("AccountNotFoundException: {}", ex.getMessage());
        var problemDetailResponse = this.buildProblemDetail(HttpStatus.NOT_FOUND, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://banco-demo.example/errors/account-not-found"));
        });

        return Mono.just(ResponseEntity
                .status(problemDetailResponse.getStatus())
                .body(problemDetailResponse));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBusinessRuleException(BusinessRuleException ex) {
        log.debug("BusinessRuleException: {}", ex.getMessage());
        var problemDetailResponse = this.buildProblemDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://banco-demo.example/errors/business-rule-violation"));
        });

        return Mono.just(ResponseEntity
                .status(problemDetailResponse.getStatus())
                .body(problemDetailResponse));
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex) {
        log.debug("InvalidStatusTransitionException: {}", ex.getMessage());
        var problemDetailResponse = this.buildProblemDetail(HttpStatus.CONFLICT, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://banco-demo.example/errors/invalid-status-transition"));
        });

        return Mono.just(ResponseEntity
                .status(problemDetailResponse.getStatus())
                .body(problemDetailResponse));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleWebExchangeBindException(WebExchangeBindException ex) {
        log.debug("WebExchangeBindException: {}", ex.getMessage());

        // Agrupando errores por campo
        Map<String, List<FieldError>> validationErrorsByField = ex.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.toList()
                ));

        // Un solo error por campo
        List<ValidationError> errors = validationErrorsByField.entrySet()
                .stream()
                .map(entry ->
                        new ValidationError(
                                entry.getKey(),
                                entry.getValue().getFirst().getDefaultMessage()
                        )
                ).toList();

        var problemDetailResponse = this.buildProblemDetail(HttpStatus.BAD_REQUEST, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://banco-demo.example/errors/validation-error"));
            problemDetail.setDetail("Uno o más campos no superaron la validación");
            problemDetail.setProperty("errors", errors);
        });

        return Mono.just(ResponseEntity
                .status(problemDetailResponse.getStatus())
                .body(problemDetailResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ProblemDetail>> handleGenericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        var problemDetailResponse = this.buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex, problemDetail -> {
            problemDetail.setType(URI.create("https://banco-demo.example/errors/internal-error"));
            problemDetail.setDetail("Ocurrió un error inesperado. Por favor contacte al administrador");
        });
        return Mono.just(ResponseEntity
                .status(problemDetailResponse.getStatus())
                .body(problemDetailResponse));
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, Exception ex, Consumer<ProblemDetail> problemDetailConsumer) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setProperty("timestamp", Instant.now().truncatedTo(ChronoUnit.MICROS));

        problemDetailConsumer.accept(problemDetail);
        return problemDetail;
    }
}
