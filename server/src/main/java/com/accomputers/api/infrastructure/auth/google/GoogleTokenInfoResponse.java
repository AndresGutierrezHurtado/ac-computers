package com.accomputers.api.infrastructure.auth.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenInfoResponse(
        String aud,
        String email,
        @JsonProperty("email_verified") String emailVerified,
        @JsonProperty("given_name") String givenName,
        @JsonProperty("family_name") String familyName,
        String name) {
}

