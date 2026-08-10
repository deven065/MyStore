package com.ecom.productcatalog.dto;

public record LoginResponse(
        //  JWT token generated after successful authentication.
        String token,

        //  Information about the authenticated user.
        UserResponse user
) {
}
