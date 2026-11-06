package com.eb.apiendpoint.dto;

import java.math.BigDecimal;

public record EventRequest(String type,
                           String origin,
                           String destination,
                           BigDecimal amount) {
}
