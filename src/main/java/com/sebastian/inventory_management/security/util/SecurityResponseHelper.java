package com.sebastian.inventory_management.security.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public final class SecurityResponseHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private SecurityResponseHelper() {

    }

    public static void writeErrorResponse(
            HttpServletResponse response,
            int status,
            String error,
            String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
