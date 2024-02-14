package tech.vuthehuyht.blogrestapi.services;

import tech.vuthehuyht.blogrestapi.dto.request.UserRequest;
import tech.vuthehuyht.blogrestapi.dto.response.ResponseErrorTemplate;

public interface UserService {
    ResponseErrorTemplate createUser(UserRequest request);

    ResponseErrorTemplate findById(Long id);

    ResponseErrorTemplate findByUsername(String username);
}
