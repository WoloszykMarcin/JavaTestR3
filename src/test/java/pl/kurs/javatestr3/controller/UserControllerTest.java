package pl.kurs.javatestr3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.javatestr3.JavaTestR3Application;
import pl.kurs.javatestr3.commands.CreateUserCommand;
import pl.kurs.javatestr3.dto.UserFullDto;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JavaTestR3Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void shouldGetAllUsers() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value("Optimus"))
                .andExpect(jsonPath("$[0].lastName").value("Prime"))
                .andExpect(jsonPath("$[0].username").value("creator"))
                .andExpect(jsonPath("$[0].appRoles[0]").value("ROLE_CREATOR"))
                .andExpect(jsonPath("$[2].firstName").value("admin"))
                .andExpect(jsonPath("$[2].lastName").value("admin"))
                .andReturn();
    }

    @Test
    @Order(2)
    void shouldGetAllUsersWithPaginationAndPageSize1() throws Exception {
        int pageSize = 1;
        int pageNumber = 0;
        postman.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Optimus"))
                .andExpect(jsonPath("$[0].lastName").value("Prime"))
                .andReturn();
    }

    @Test
    @Order(3)
    void shouldGetAllUsersWithPaginationAndPageSize2() throws Exception {
        int pageSize = 2;
        int pageNumber = 0;
        postman.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Optimus"))
                .andExpect(jsonPath("$[0].lastName").value("Prime"))
                .andExpect(jsonPath("$[1].firstName").value("Anna"))
                .andExpect(jsonPath("$[1].lastName").value("Kowalska"))
                .andReturn();
    }

    @Test
    void shouldAddNewUserWithRoleUser() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("test");
        command.setLastName("test");
        command.setUsername("testUser");
        command.setPassword("test");
        command.setRoleName("ROLE_USER");

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserFullDto userFullDto = objectMapper.readValue(responseBody, UserFullDto.class);

        assertEquals(command.getFirstName(), userFullDto.getFirstName());
        assertEquals(command.getLastName(), userFullDto.getLastName());
        assertEquals(command.getUsername(), userFullDto.getUsername());
        assertTrue(userFullDto.getAppRoles().contains("ROLE_USER"));
    }

    @Test
    void shouldAddNewUserWithRoleAdmin() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("testadmin");
        command.setLastName("testadmin");
        command.setUsername("testadmin");
        command.setPassword("testadmin");
        command.setRoleName("ROLE_ADMIN");

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserFullDto userFullDto = objectMapper.readValue(responseBody, UserFullDto.class);

        assertEquals(command.getFirstName(), userFullDto.getFirstName());
        assertEquals(command.getLastName(), userFullDto.getLastName());
        assertEquals(command.getUsername(), userFullDto.getUsername());
        assertTrue(userFullDto.getAppRoles().contains("ROLE_ADMIN"));

    }

    @Test
    void shouldAddNewUserWithRoleCreator() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("testcreator");
        command.setLastName("testcreator");
        command.setUsername("testcreator");
        command.setPassword("testcreator");
        command.setRoleName("ROLE_CREATOR");

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserFullDto userFullDto = objectMapper.readValue(responseBody, UserFullDto.class);

        assertEquals(command.getFirstName(), userFullDto.getFirstName());
        assertEquals(command.getLastName(), userFullDto.getLastName());
        assertEquals(command.getUsername(), userFullDto.getUsername());
        assertTrue(userFullDto.getAppRoles().contains("ROLE_CREATOR"));
    }

    @Test
    void shouldAddNewUserWithRoleCreatorWhenRoleNotGiven() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("rolenotgiven");
        command.setLastName("rolenotgiven");
        command.setUsername("rolenotgiven");
        command.setPassword("rolenotgiven");
        command.setRoleName("");

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserFullDto userFullDto = objectMapper.readValue(responseBody, UserFullDto.class);

        assertEquals(command.getFirstName(), userFullDto.getFirstName());
        assertEquals(command.getLastName(), userFullDto.getLastName());
        assertEquals(command.getUsername(), userFullDto.getUsername());
        assertTrue(userFullDto.getAppRoles().contains("ROLE_CREATOR"));
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
                .andExpect(status().isCreated())
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