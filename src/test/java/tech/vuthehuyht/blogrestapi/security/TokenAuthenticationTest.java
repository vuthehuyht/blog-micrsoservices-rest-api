package tech.vuthehuyht.blogrestapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tech.vuthehuyht.blogrestapi.dto.request.UserRequest;
import tech.vuthehuyht.blogrestapi.services.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TokenAuthenticationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void testNotAllowAccessToUnauthenticatedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")).andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticateWithUsername() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@example.com");
        request.setPassword("1234");
        request.setFullName("Demo");

        userService.createUser(request);

        String authRequest = "{\"username\":\"demo\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(authRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticateWithPassword() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@example.com");
        request.setPassword("1234");
        request.setFullName("Demo");

        userService.createUser(request);

        String authRequest = "{\"password\":\"1234\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(authRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticateWithValidRequest() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@example.com");
        request.setPassword("1234");
        request.setFullName("Demo");

        userService.createUser(request);

        String authRequest = "{\"username\":\"demo\",\"password\":\"1234\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(authRequest))
                .andExpect(status().isOk());
    }
}
