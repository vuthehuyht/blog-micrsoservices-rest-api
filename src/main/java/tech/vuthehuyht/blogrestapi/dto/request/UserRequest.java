package tech.vuthehuyht.blogrestapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password is at least 8 characters")
    private String password;

    @Email(message = "Email is invalid format")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("full_name")
    private String fullName;
}
