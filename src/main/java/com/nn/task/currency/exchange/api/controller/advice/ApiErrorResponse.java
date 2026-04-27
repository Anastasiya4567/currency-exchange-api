package com.nn.task.currency.exchange.api.controller.advice;

import java.time.LocalDateTime;

public record ApiErrorResponse(String code, String message, LocalDateTime timestamp) implements ApiError {}
