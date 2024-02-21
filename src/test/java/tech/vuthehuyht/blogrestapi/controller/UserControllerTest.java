package tech.vuthehuyht.blogrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tech.vuthehuyht.blogrestapi.dto.request.UserRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void controllerInitializedCorrectly() {
        assertThat(userController).isNotNull();
    }

    @Test
    void testMissUsername() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("demo@example.com");
        request.setPassword("password");
        request.setFullName("Demo");

        mockMvc.perform(post("/api/v1/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPasswordBlank() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@example.com");
        request.setPassword("");
        request.setFullName("Demo");

        mockMvc.perform(post("/api/v1/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissPasswordPolicy() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@example.com");
        request.setPassword("1234");
        request.setFullName("Demo");

        mockMvc.perform(post("/api/v1/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidEmail() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@");
        request.setPassword("1234");
        request.setFullName("Demo");

        mockMvc.perform(post("/api/v1/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUserRegisterRequest_ValidRequest() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("demo");
        request.setEmail("demo@example.com");
        request.setPassword("123456789");
        request.setFullName("Demo");

        mockMvc.perform(post("/api/v1/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated());
    }
}
