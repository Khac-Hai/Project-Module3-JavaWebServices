package re.edu.mapper;

import re.edu.dto.response.ApiResponse;

import java.time.LocalDateTime;

public class MapToAPIResponse {
    public static ApiResponse mapTo(Object data, Object errors, int status, String message) {
        return ApiResponse.builder()
                .status(status)
                .message(message)
                .data(data)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
