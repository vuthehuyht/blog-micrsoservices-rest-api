package tech.vuthehuyht.blogrestapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserResponse(
        String username,
        String email,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("roles") List<String> roles,
        @JsonProperty("login_attempt") int loginAttempt
) {
}
