package com.accomputers.api.application.dtos;

import java.util.List;

public record PageDTO<T>(List<T> data, Long total) {
}
