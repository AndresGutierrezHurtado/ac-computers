package com.accomputers.api.infrastructure.http.responses;

import java.util.List;

public record PaginatedResponseDTO<T>(String message, boolean success, Long total, List<T> data) {
}
