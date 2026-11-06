package com.eb.apiendpoint.dto;

import com.eb.apiendpoint.model.Account;

public record TransferResponse(
        Account origin,
        Account destination
) {
}
