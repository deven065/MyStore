package com.ecom.productcatalog.dto;

public record LoginResponse(
        String message,

        UserResponse user
) {
}
