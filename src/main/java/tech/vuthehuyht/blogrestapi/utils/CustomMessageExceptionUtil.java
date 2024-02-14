package tech.vuthehuyht.blogrestapi.utils;

import org.springframework.http.HttpStatus;
import tech.vuthehuyht.blogrestapi.exceptions.CustomMessageException;

public class CustomMessageExceptionUtil {
    private CustomMessageExceptionUtil() {}

    public static CustomMessageException handleUnauthorized() {
        CustomMessageException customMessageException =  new CustomMessageException();
        customMessageException.setMessage("");
        customMessageException.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        return customMessageException;
    }
}
