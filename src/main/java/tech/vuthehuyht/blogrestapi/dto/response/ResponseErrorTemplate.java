package tech.vuthehuyht.blogrestapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseErrorTemplate(
        String message,
        String code,
        @JsonProperty("data") Object data
) {
}
