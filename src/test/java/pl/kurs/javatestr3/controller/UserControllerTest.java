package pl.kurs.javatestr3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.javatestr3.JavaTestR3Application;
import pl.kurs.javatestr3.commands.CreateUserCommand;
import pl.kurs.javatestr3.dto.StatusDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JavaTestR3Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddNewUserWithRoleUser() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("test");
        command.setLastName("test");
        command.setUsername("testUser");
        command.setPassword("test");
        command.setRoleName("ROLE_USER");

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        StatusDto statusDto = objectMapper.readValue(responseBody, StatusDto.class);

        assertEquals("new User with name testUser successfully added with role: ROLE_USER", statusDto.getStatus());
    }

    @Test
    void shouldAddNewUserWithRoleAdmin() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("testadmin");
        command.setLastName("testadmin");
        command.setUsername("testadmin");
        command.setPassword("testadmin");
        command.setRoleName("ROLE_ADMIN");

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        StatusDto statusDto = objectMapper.readValue(responseBody, StatusDto.class);

        assertEquals("new User with name testadmin successfully added with role: ROLE_ADMIN", statusDto.getStatus());
    }

    @Test
    void shouldAddNewUserWithRoleCreator() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("testcreator");
        command.setLastName("testcreator");
        command.setUsername("testcreator");
        command.setPassword("testcreator");
        command.setRoleName("ROLE_CREATOR");

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        StatusDto statusDto = objectMapper.readValue(responseBody, StatusDto.class);

        assertEquals("new User with name testcreator successfully added with role: ROLE_CREATOR", statusDto.getStatus());
    }

    @Test
    void shouldAddNewUserWithRoleCreatorWhenRoleNotGiven() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("rolenotgiven");
        command.setLastName("rolenotgiven");
        command.setUsername("rolenotgiven");
        command.setPassword("rolenotgiven");
        command.setRoleName("");

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        StatusDto statusDto = objectMapper.readValue(responseBody, StatusDto.class);

        assertEquals("new User with name rolenotgiven successfully added with role: ROLE_CREATOR", statusDto.getStatus());
    }

    @Test
    void shouldReturn404NotFoundResponseWhenNonExistentRole() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("nonexistent");
        command.setLastName("nonexistent");
        command.setUsername("nonexistent");
        command.setPassword("nonexistent");
        command.setRoleName("NONEXISTENT_ROLE");

        postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldResponseBeFirstNameIsEmptyWhenFirstNameIsEmpty() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("");
        command.setLastName("testcreator2");
        command.setUsername("testcreator2");
        command.setPassword("testcreator2");
        command.setRoleName("ROLE_CREATOR");

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("\"code\":\"FIRSTNAME_IS_EMPTY\""));
        assertTrue(responseBody.contains("\"field\":\"firstName\""));
    }

    @Test
    void shouldResponseBeFirstNameIsEmptyWhenLastNameIsEmpty() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("testcreator3");
        command.setLastName("");
        command.setUsername("testcreator3");
        command.setPassword("testcreator3");
        command.setRoleName("ROLE_CREATOR");

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("\"code\":\"LASTNAME_IS_EMPTY\""));
        assertTrue(responseBody.contains("\"field\":\"lastName\""));
    }

    @Test
    void shouldResponseBeUserNameNotUniqueWhenUserAlreadyExists() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("duplicate");
        command.setLastName("duplicate");
        command.setUsername("duplicate");
        command.setPassword("duplicate");
        command.setRoleName("ROLE_CREATOR");

        postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = postman.perform(post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("\"code\":\"USERNAME_NOT_UNIQUE\""));
        assertTrue(responseBody.contains("\"field\":\"username\""));
    }
}