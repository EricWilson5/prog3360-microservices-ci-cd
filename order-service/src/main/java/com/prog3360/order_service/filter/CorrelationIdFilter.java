package com.prog3360.order_service.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 1. Try to get the ID from the incoming header [cite: 42]
        String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);

        // 2. If no header is present, generate a new one [cite: 42]
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        // 3. Store it in the MDC so the logs can see it [cite: 43, 44]
        try {
            MDC.put(MDC_KEY, correlationId);
            chain.doFilter(request, response);
        } finally {

            MDC.remove(MDC_KEY);
        }
    }
}