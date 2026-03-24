package com.accomputers.api.application.ports.output;

public interface GoogleServicePort {
    GoogleVerifiedProfile verify(String credential);

    record GoogleVerifiedProfile(
            String email,
            String firstName,
            String lastName) {
    }
}

