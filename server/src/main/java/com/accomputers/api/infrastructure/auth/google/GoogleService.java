package com.accomputers.api.infrastructure.auth.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.accomputers.api.application.ports.output.GoogleServicePort;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

@Component
public class GoogleService implements GoogleServicePort {

    private final RestClient restClient;
    private final String googleClientId;

    public GoogleService(
            @Value("${google.client-id:}") String googleClientId) {
        this.restClient = RestClient.create();
        this.googleClientId = googleClientId != null ? googleClientId.trim() : "";
    }

    @Override
    public GoogleVerifiedProfile verify(String credential) {
        if (credential == null || credential.trim().isEmpty()) {
            throw new InvalidValueObjectException("Google credential", null, "cannot be null or empty");
        }
        if (googleClientId.isEmpty()) {
            throw new IllegalStateException("google.client-id is not configured");
        }

        GoogleTokenInfoResponse body = restClient.get()
                .uri("https://oauth2.googleapis.com/tokeninfo?id_token={token}", credential)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new InvalidValueObjectException("Google credential", null, "is invalid");
                })
                .body(GoogleTokenInfoResponse.class);

        if (body == null) {
            throw new InvalidValueObjectException("Google credential", null, "is invalid");
        }
        if (body.aud() == null || !googleClientId.equals(body.aud().trim())) {
            throw new InvalidValueObjectException("Google credential", null, "has invalid audience");
        }
        if (body.email() == null || body.email().trim().isEmpty()) {
            throw new InvalidValueObjectException("Google credential", null, "does not contain email");
        }
        if (body.emailVerified() != null && !"true".equalsIgnoreCase(body.emailVerified().trim())) {
            throw new InvalidValueObjectException("Google credential", null, "email is not verified");
        }

        String firstName = body.givenName();
        String lastName = body.familyName();

        if ((firstName == null || firstName.isBlank()) && body.name() != null && !body.name().isBlank()) {
            String[] parts = body.name().trim().split("\\s+", 2);
            firstName = parts.length > 0 ? parts[0] : "";
            lastName = parts.length > 1 ? parts[1] : "";
        }

        return new GoogleVerifiedProfile(body.email().trim(), safe(firstName), safe(lastName));
    }

    private static String safe(String value) {
        return value != null ? value.trim() : "";
    }
}

