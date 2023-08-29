package pl.kurs.javatestr3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.javatestr3.JavaTestR3Application;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.commands.ShapeUpdateCommand;
import pl.kurs.javatestr3.repository.ShapeChangeRepository;
import pl.kurs.javatestr3.repository.ShapeRepository;
import pl.kurs.javatestr3.security.AppUser;
import pl.kurs.javatestr3.service.AppUserService;
import pl.kurs.javatestr3.service.ShapeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

    @Autowired
    private ShapeChangeRepository shapeChangeRepository;

    @Autowired
    private ShapeService shapeService;

    @Autowired
    private AppUserService appUserService;

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldAddCircle() throws Exception {
        //given - add a circle
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
                .andExpect(jsonPath("$.parameters.radius").value(5.0));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldAddRectangle() throws Exception {
        //given - ad a rectangle
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
                .andExpect(jsonPath("$.parameters.length").value("10.0"))
                .andExpect(jsonPath("$.parameters.width").value("7.0"));
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
                .andExpect(jsonPath("$.parameters.side").value("10.0"));
    }

    @Test
    @WithMockUser(roles = "UNKNOWNROLE")
    public void shouldReturn403ResponseCodeWhileTryingToCreateFigureAsAUserWithUnknownRole() throws Exception {
        //given - create a square
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 10.0));
        String json = objectMapper.writeValueAsString(squareCommand);

        //when-then
        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldGetAllFigures() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        //add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 7.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        //add a square
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 10.0));
        String squareJson = objectMapper.writeValueAsString(squareCommand);

        //add all shapes
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

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$[1].type").value("RECTANGLE"))
                .andExpect(jsonPath("$[2].type").value("SQUARE"));
    }


    @Test
    public void shouldReturnEmptyListWhenNoFiguresInADatabase() throws Exception {
        //given-when-then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterTypeGiven() throws Exception {
        //given - add a circle
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
        assertTrue(responseJson.contains("Invalid parameter type, expected Double"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongTypeGiven() throws Exception {
        //given add a circle
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
        assertTrue(responseJson.contains("Invalid shape type"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterOfSquareGiven() throws Exception {
        //given add a circle
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
        assertTrue(responseJson.contains("Square requires a 'side' parameter."));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterOfRectangleGiven() throws Exception {
        //given - add a circle
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
        assertTrue(responseJson.contains("Rectangle requires both 'length' and 'width' parameters."));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void shouldThrowExceptionWhenWrongParameterOfCircleGiven() throws Exception {
        //given - add a circle
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
        assertTrue(responseJson.contains("Circle requires a 'radius' parameter."));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldReturnNotFoundWhenWrongParamGiven() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isCreated())
                .andReturn();

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .param("type", "invalidType")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldGetCircleByParams() throws Exception {
        //given - add a circle
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

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .queryParam("createdBy", "creator")
                        .queryParam("type", "CIRCLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$[0].createdBy").value("creator"));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldGetSquareShapeByFittingParams() throws Exception {
        //given - add a circle
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 5.0));
        String squareJson = objectMapper.writeValueAsString(squareCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(squareJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("square"));

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .queryParam("createdBy", "creator")
                        .queryParam("type", "SQUARE")
                        .queryParam("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .queryParam("perimeterFrom", "1")
                        .queryParam("perimeterTo", "50")
                        .queryParam("areaFrom", "1")
                        .queryParam("sideFrom", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].createdBy").value("creator"))
                .andExpect(jsonPath("$[0].type").value("SQUARE"));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldGetRectangleByParams() throws Exception {
        //given - add a rectangle
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


        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .queryParam("type", "RECTANGLE")
                        .queryParam("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .queryParam("perimeterFrom", "1")
                        .queryParam("perimeterTo", "50")
                        .queryParam("areaFrom", "1")
                        .queryParam("lengthFrom", "9")
                        .queryParam("widthFrom", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].createdBy").value("creator"))
                .andExpect(jsonPath("$[0].type").value("RECTANGLE"));


    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldNotFindCircleByWrongParams() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String circleJson = objectMapper.writeValueAsString(circleCommand);

        MvcResult mvcResult = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("circle"))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        //when - then (incorrect radius given)
        MvcResult getResult = postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .queryParam("type", "CIRCLE")
                        .queryParam("createdBy", "creator")
                        .queryParam("radiusFrom", "6")
                        .queryParam("areaFrom", "78")
                        .queryParam("perimeterFrom", "31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();

        System.out.println(getResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldNotFindSquareByWrongParams() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("square");
        circleCommand.setParameters(Map.of("side", 5.0));
        String squareJson = objectMapper.writeValueAsString(circleCommand);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(squareJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("square"))
                .andReturn();

        //when - then (side value - too high)
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .queryParam("createdBy", "creator")
                        .queryParam("type", "SQUARE")
                        .queryParam("createdAtFrom", "2023-08-13T18:13:25.4133777")
                        .queryParam("perimeterFrom", "1")
                        .queryParam("perimeterTo", "50")
                        .queryParam("areaFrom", "1")
                        .queryParam("sideFrom", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldNotFindRectangleByWrongParams() throws Exception {
        //given - add a rectangle
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

        //when - then (wrong date for the createdAtFrom parameter)
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes")
                        .queryParam("type", "RECTANGLE")
                        .queryParam("createdBy", "creator")
                        .queryParam("createdAtFrom", "2090-01-01T00:00:00.0000000")
                        .queryParam("perimeterFrom", "1")
                        .queryParam("perimeterTo", "50")
                        .queryParam("areaFrom", "1")
                        .queryParam("lengthFrom", "10")
                        .queryParam("widthFrom", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldUpdateShapeWithAdminRole() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 11.0, "width", 7.0));

        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATOR"})
    public void shouldUpdateShapeWhenUpdatingAsACreator() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());

        //when - then
        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 11.0, "width", 7.0));

        String updateJson = objectMapper.writeValueAsString(updateCommand);

        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldChangeShapeVersionAfterUpdate() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 11.0, "width", 7.0));

        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parameters.length").value(11))
                .andExpect(jsonPath("$.parameters.width").value(7))
                .andExpect(jsonPath("$.version").value(shapeVersion + 1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldNotChangeShapeVersionAfterUpdatingWithTheSameParametersAsTheOldOnes() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 10.0, "width", 5.0));

        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parameters.length").value(10))
                .andExpect(jsonPath("$.parameters.width").value(5))
                .andExpect(jsonPath("$.version").value(shapeVersion));
    }

    @Test
    public void shouldReturn401ResponseCodeWhenUpdatingWithNoAuthorization() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 10.0, "width", 5.0));

        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldReturn403ResponseCodeWhenUpdatingWithoutPrivileges() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 10.0, "width", 5.0));
        String rectangleJson = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rectangleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("rectangle"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 10.0, "width", 5.0));

        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isForbidden());
    }

    //OPTIMISTIC LOCK TESTS
    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldChangedVersionOnlyOnce() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType(circleCommand.getShapeType());
        updateCommand.setParameters(Map.of("radius", 6.0));
        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isBadRequest());

        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].version").value(shapeVersion + 1));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldThrowOptimisticLockingException() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setShapeType(circleCommand.getShapeType());
        updateCommand.setParameters(Map.of("radius", 6.0));
        String updateJson = objectMapper.writeValueAsString(updateCommand);

        //when - then
        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        postman.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isBadRequest());

        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].version").value(shapeVersion + 1));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldReturnEmptyListWhenNoChangesInDatabase() throws Exception {
        //given-when-then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/1/changes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldSaveChangesForUpdatedCircle() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").password("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setVersion(shapeVersion);
        updateCommand.setShapeType("circle");
        updateCommand.setParameters(Map.of("radius", 6.0));

        UserDetails creator = appUserService.loadUserByUsername("creator");
        shapeService.updateShape(shapeId, updateCommand, (AppUser) creator);

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + shapeId + "/changes")
                        .with(user("creator").password("creator").roles("CREATOR")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].shapeType").value("CIRCLE"))
                .andExpect(jsonPath("$[0].modifiedField").value("radius"))
                .andExpect(jsonPath("$[0].oldValue").value(5.0))
                .andExpect(jsonPath("$[0].newValue").value(6.0));
    }

    @Test
    public void shouldSaveChangesForUpdatedSquare() throws Exception {
        //given - add a square
        CreateShapeCommand squareCommand = new CreateShapeCommand();
        squareCommand.setShapeType("square");
        squareCommand.setParameters(Map.of("side", 5.0));
        String json = objectMapper.writeValueAsString(squareCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").password("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setVersion(shapeVersion);
        updateCommand.setShapeType("square");
        updateCommand.setParameters(Map.of("side", 6.0));

        UserDetails creator = appUserService.loadUserByUsername("creator");
        shapeService.updateShape(shapeId, updateCommand, (AppUser) creator);

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + shapeId + "/changes")
                        .with(user("creator").password("creator").roles("CREATOR")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].shapeType").value("SQUARE"))
                .andExpect(jsonPath("$[0].modifiedField").value("side"))
                .andExpect(jsonPath("$[0].oldValue").value(5.0))
                .andExpect(jsonPath("$[0].newValue").value(6.0));
    }

    @Test
    public void shouldSaveChangesForUpdatedRectangle() throws Exception {
        //given - add a rectangle
        CreateShapeCommand rectangleCommand = new CreateShapeCommand();
        rectangleCommand.setShapeType("rectangle");
        rectangleCommand.setParameters(Map.of("length", 5.0, "width", 10.0));
        String json = objectMapper.writeValueAsString(rectangleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").password("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setVersion(shapeVersion);
        updateCommand.setShapeType("rectangle");
        updateCommand.setParameters(Map.of("length", 6.0, "width", 11.0));

        UserDetails creator = appUserService.loadUserByUsername("creator");
        shapeService.updateShape(shapeId, updateCommand, (AppUser) creator);

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + shapeId + "/changes")
                        .with(user("creator").password("creator").roles("CREATOR")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].shapeType").value("RECTANGLE"));
    }

    @Test
    public void shouldNotSaveChangesWhenTryingToUpdateCircleWithOldParameters() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").password("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setVersion(shapeVersion);
        updateCommand.setShapeType("circle");
        updateCommand.setParameters(Map.of("radius", 5.0));

        UserDetails creator = appUserService.loadUserByUsername("creator");
        shapeService.updateShape(shapeId, updateCommand, (AppUser) creator);

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + shapeId + "/changes")
                        .with(user("creator").password("creator").roles("CREATOR")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldNotSaveChangesWhenTryingToUpdateSquareWithOldParameters() throws Exception {
        //given - add a square
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("square");
        circleCommand.setParameters(Map.of("side", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").password("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setVersion(shapeVersion);
        updateCommand.setShapeType("square");
        updateCommand.setParameters(Map.of("side", 5.0));

        UserDetails creator = appUserService.loadUserByUsername("creator");
        shapeService.updateShape(shapeId, updateCommand, (AppUser) creator);

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + shapeId + "/changes")
                        .with(user("creator").password("creator").roles("CREATOR")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturn403ForbiddenWhenUserIsNotTheCreator() throws Exception {
        //given - add a square
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("square");
        circleCommand.setParameters(Map.of("side", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .with(user("creator").password("creator").roles("CREATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setVersion(shapeVersion);
        updateCommand.setShapeType("square");
        updateCommand.setParameters(Map.of("side", 5.0));

        UserDetails creator = appUserService.loadUserByUsername("creator");
        shapeService.updateShape(shapeId, updateCommand, (AppUser) creator);

        //when - then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + shapeId + "/changes")
                        .with(user("user").password("user").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    public void shouldThrowOptimisticLockingExceptionInConcurrentEnvironment() throws Exception {
        //given - add a circle
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));
        String json = objectMapper.writeValueAsString(circleCommand);

        MvcResult result = postman.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long shapeId = Long.valueOf(JsonPath.read(responseString, "$.id").toString());
        int shapeVersion = Integer.valueOf(JsonPath.read(responseString, "$.version").toString());

        //when
        ShapeUpdateCommand updateCommand = new ShapeUpdateCommand();
        updateCommand.setParameters(Map.of("radius", 6.0));
        String updateJson = objectMapper.writeValueAsString(updateCommand);

        int numberOfConcurrentRequests = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfConcurrentRequests);

        List<Future<HttpStatus>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfConcurrentRequests; i++) {
            UpdateRequestCall updateRequestCall = new UpdateRequestCall("/api/v1/shapes/" + shapeId, updateJson, postman);
            futures.add(executorService.submit(updateRequestCall));
        }

        executorService.shutdown();

        boolean optimisticLockingExceptionThrown = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        return HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                })
                .anyMatch(status -> status == HttpStatus.CONFLICT);

        //then
        assertTrue(optimisticLockingExceptionThrown);

        postman.perform(MockMvcRequestBuilders.get("/api/v1/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].version").value(shapeVersion));


    }

    class UpdateRequestCall implements Callable<HttpStatus> {
        private final String url;
        private final String requestBody;
        private final MockMvc postman;

        public UpdateRequestCall(String url, String requestBody, MockMvc postman) {
            this.url = url;
            this.requestBody = requestBody;
            this.postman = postman;
        }

        @Override
        public HttpStatus call() {
            try {
                MvcResult result = postman.perform(MockMvcRequestBuilders.put(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andReturn();
                return result.getResponse().getStatus() == HttpStatus.OK.value() ? HttpStatus.OK : HttpStatus.CONFLICT;
            } catch (Exception exc) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @AfterEach
    void cleanUp() {
        shapeChangeRepository.deleteAll();
        shapeRepository.deleteAll();
    }
}