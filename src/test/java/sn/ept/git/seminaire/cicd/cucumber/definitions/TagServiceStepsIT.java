package sn.ept.git.seminaire.cicd.cucumber.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Slf4j
public class TagServiceStepsIT {

    private final static String BASE_URI = "http://localhost";
    public static final String API_PATH = "/cicd/api/tags";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    
    @LocalServerPort
    private int port;
    private String name;
    private String description;
    private Response response;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private TagRepository tagRepository;

    @BeforeAll
    public static void beforeAll() {
        objectMapper.findAndRegisterModules();
    }

    @Before
    public void init() {
        tagRepository.deleteAll();
    }

    protected RequestSpecification request() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        return given()
                .contentType(ContentType.JSON)
                .log()
                .all();
    }

    @Given("acicd_tags table contains data:")
    public void tableTagContainsData(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        List<Tag> tagsList = data
        .stream()
        .map((Map<String, String> line) -> Tag.builder()
        .id(line.get(ID))
        .name(line.get(NAME))
        .description(line.get(DESCRIPTION))
        .version(0)
        .createdDate(Instant.now(Clock.systemUTC()))
        .lastModifiedDate(Instant.now(Clock.systemUTC()))
        .build()
    )
    .collect(Collectors.toList());
    tagRepository.saveAllAndFlush(tagsList);
    }

    @When("call find tag by id with id={string}")
    public void callFindTagByIdWithId(String id) {
        response = request()
                .when().get(API_PATH + "/" + id);
    }

    @Then("the returned http status is {int}")
    public void theHttpStatusIs(int status) {
        response.then()
                .assertThat()
                .statusCode(status);
    }

    @And("the returned tag has properties name={string} and description={string}")
    public void theReturnedTagHasPropertiesNameAndDescription(String name, String description) {
        response.then()
                .assertThat()
                .body(NAME, CoreMatchers.equalTo(name))
                .body(DESCRIPTION, CoreMatchers.equalTo(description));
    }

    @When("call find all tags with page = {int} and size = {int} and sort={string}")
    public void callFindAllTags(int page, int size, String sort) {
    log.info("Calling find all tags with page={}, size={}, sort={}", page, size, sort);
    response = request().contentType(ContentType.JSON)
            .log().all()
            .when().get(API_PATH + String.format("?page=%d&size=%d&sort=%s", page, size, sort));
    log.info("Response status: {}", response.getStatusCode());
    log.info("Response body: {}", response.getBody().asString());
    }

    @And("the returned list has {int} elements")
    public void theReturnedListHasElements(int size) {
        Assertions.assertThat(response.jsonPath().getList("content"))
                .hasSize(size);
    }

    @And("that list contains values:")
    public void thatListContainsValues(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    List<Map<String, Object>> responseContent = response.jsonPath().getList("content");
    log.info("Response content: {}", responseContent);
    
    data.forEach(expectedItem -> {
        boolean found = responseContent.stream().anyMatch(actualItem -> 
            expectedItem.get(NAME).equals(actualItem.get(NAME)) &&
            expectedItem.get(DESCRIPTION).equals(actualItem.get(DESCRIPTION))
        );
        Assertions.assertThat(found).isTrue();
    });
    }

    @When("call add tag")
    public void callAddTag() {
        TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
        response = request()
                .body(requestBody)
                .when().post(API_PATH);
    }

    @And("the created tag has properties name={string} and description={string}")
    public void theCreatedTagHasPropertiesNameAndDescription(String name, String description) {
        response.then()
                .assertThat()
                .body(NAME, CoreMatchers.equalTo(name))
                .body(DESCRIPTION, CoreMatchers.equalTo(description));
    }

    @When("call update tag with id={string}")
    public void callUpdateTagWithId(String id) {
        TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
        response = request()
                .body(requestBody)
                .when().put(API_PATH + "/" + id);
    }

    @And("the updated tag has properties name={string} and description={string}")
    public void theUpdatedTagHasPropertiesNameAndDescription(String name, String description) {
        response.then()
                .assertThat()
                .body(NAME, CoreMatchers.equalTo(name))
                .body(DESCRIPTION, CoreMatchers.equalTo(description));
    }

    @When("call delete tag with id={string}")
    public void callDeleteTagWithId(String id) {
        response = request()
                .when().delete(API_PATH + "/" + id);
    }

    @And("name = {string}")
    public void name(String name) {
        this.name = name;
    }

    @And("description = {string}")
    public void description(String description) {
        this.description = description;
    }
}
