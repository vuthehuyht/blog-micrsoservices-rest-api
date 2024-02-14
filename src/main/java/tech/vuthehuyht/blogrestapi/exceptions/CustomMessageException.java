package tech.vuthehuyht.blogrestapi.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class CustomMessageException extends RuntimeException{
    @Setter
    @JsonProperty("message")
    private String message;

    @Setter
    @Getter
    @JsonProperty("code")
    private String code;

    @JsonIgnore
    private String cause;

    @JsonIgnore
    private Object stackTrace;

    @JsonIgnore
    private String localizedMessage;

    @JsonIgnore
    private List<String> suppressed;

    public CustomMessageException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
