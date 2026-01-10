package com.sebastian.inventory_management.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO imutável para respostas de erro padronizadas.
 * Elimina duplicação de código no GlobalExceptionHandler.
 * 
 * Padrão aplicado: Extract Class + Factory Method
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;

    /**
     * Factory Method para criar ErrorResponse a partir de HttpStatus.
     * 
     * @param status  HttpStatus da resposta
     * @param error   Descrição curta do erro
     * @param message Mensagem detalhada do erro
     * @return ErrorResponse preenchido com timestamp atual
     */
    public static ErrorResponse of(HttpStatus status, String error, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .error(error)
                .message(message)
                .build();
    }
}
