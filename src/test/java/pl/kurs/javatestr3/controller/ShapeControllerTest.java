package pl.kurs.javatestr3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.javatestr3.JavaTestR3Application;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.repository.ShapeRepository;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JavaTestR3Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class ShapeControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShapeRepository shapeRepository;

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldAddCircle() throws Exception {
        //given
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        //when-then
        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("circle"))
                .andExpect(jsonPath("$.radius").value("5.0"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldAddRectangle() throws Exception {
        //given
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 7.0));
        String json = objectMapper.writeValueAsString(rectangleCommand);

        //when-then
        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andExpect(jsonPath("$.length").value("10.0"))
                .andExpect(jsonPath("$.width").value("7.0"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldAddSquare() throws Exception {
        //given
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 10.0));
        String json = objectMapper.writeValueAsString(squareCommand);

        //when-then
        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("square"))
                .andExpect(jsonPath("$.side").value("10.0"));
    }

    @Test
    @WithMockUser(roles = "UNKNOWNROLE")
    public void shouldReturn403ResponseCodeWhileTryingToCreateFigureAsAUserWithUnknownRole() throws Exception {
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 10.0));
        String json = objectMapper.writeValueAsString(squareCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldGetAllFigures() throws Exception {
        // Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        // Create a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 7.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        // Create a square
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 10.0));
        String squareJson = objectMapper.writeValueAsString(squareCommand);

        // Add all shapes
        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isCreated())
                .andReturn();

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andReturn();

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(squareJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Retrieve all figures
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$[1].type").value("RECTANGLE"))
                .andExpect(jsonPath("$[2].type").value("SQUARE"));
    }


    @Test
    @WithMockUser(roles = "UNKNOWNROLE")
    public void shouldNotReturn403ResponseCodeWhileTryingToGetFiguresAsAUserWithUnknownRole() throws Exception {
        // Given-when-then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldReturn404NotFoundResponseCodeWhenNoFiguresInADatabase() throws Exception {
        // Given-when-then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterTypeGiven() throws Exception {
        //given
        //Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        //when
        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseJson.contains("Invalid parameter type, expected Double"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongTypeGiven() throws Exception {
        //given
        //Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circleeeee");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        //when
        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseJson.contains("Invalid shape type"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterOfSquareGiven() throws Exception {
        //given
        //Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("square");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        //when
        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseJson.contains("Square requires a 'side' parameter."));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterOfRectangleGiven() throws Exception {
        //given
        //Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("rectangle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        //when
        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseJson.contains("Rectangle requires both 'length' and 'width' parameters."));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterOfCircleGiven() throws Exception {
        //given
        //Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("length", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        //when
        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseJson.contains("Circle requires a 'radius' parameter."));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldReturnNotFoundWhenWrongParamGiven() throws Exception {
        //given
        //Create a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isCreated())
                .andReturn();

        // When - then - Perform the GET request with query parameters
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("type", "invalidType")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldGetCircleByParams() throws Exception {
        // Given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("circle"));

        // Prepare the query parameters to match the working URL
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("createdBy", "creator")
                        .param("type", "circle")
                        .param("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .param("perimeterFrom", "1")
                        .param("perimeterTo", "50")
                        .param("areaFrom", "1")
                        .param("radiusFrom", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$[0].createdBy").value("CREATOR"));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldGetSquareShapeByParams() throws Exception {
        // Given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("square");
        circleCommand.setParameters(Map.of("side", 5.0));
        String squareJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(squareJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("square"));

        // Prepare the query parameters to match the working URL
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("createdBy", "creator")
                        .param("type", "square")
                        .param("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .param("perimeterFrom", "1")
                        .param("perimeterTo", "50")
                        .param("areaFrom", "1")
                        .param("sideFrom", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("SQUARE"))
                .andExpect(jsonPath("$[0].createdBy").value("CREATOR"));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldGetRectangleByParams() throws Exception {
        // Given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"));

        // Prepare the query parameters to match the working URL
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("createdBy", "creator")
                        .param("type", "rectangle")
                        .param("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .param("perimeterFrom", "1")
                        .param("perimeterTo", "50")
                        .param("areaFrom", "1")
                        .param("lengthFrom", "9")
                        .param("widthFrom", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdBy").value("creator"))
                .andExpect(jsonPath("$[0].type").value("rectangle"));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldNotFindCircleByParams() throws Exception {
        // Given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("circle"));

        // Prepare the query parameters with an error: querying for a triangle
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("createdBy", "creator")
                        .param("type", "triangle")
                        .param("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .param("perimeterFrom", "1")
                        .param("perimeterTo", "50")
                        .param("areaFrom", "1")
                        .param("radiusFrom", "6"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldNotFindSquareByParams() throws Exception {
        // Given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("square");
        circleCommand.setParameters(Map.of("side", 5.0));
        String squareJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(squareJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("square"));

        // Prepare the query parameters with an error: side value too high
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("createdBy", "creator")
                        .param("type", "square")
                        .param("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .param("perimeterFrom", "1")
                        .param("perimeterTo", "50")
                        .param("areaFrom", "1")
                        .param("sideFrom", "50"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldNotFindRectangleByParams() throws Exception {
        // Given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"));

        // Prepare the query parameters with an error: wrong date for the createdAtFrom parameter
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("createdBy", "creator")
                        .param("type", "rectangle")
                        .param("createdAtFrom", "2022-01-01T00:00:00.0000000")
                        .param("perimeterFrom", "1")
                        .param("perimeterTo", "50")
                        .param("areaFrom", "1")
                        .param("lengthFrom", "11")
                        .param("widthFrom", "4"))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    void cleanUp() {
        shapeRepository.deleteAll();
    }
}