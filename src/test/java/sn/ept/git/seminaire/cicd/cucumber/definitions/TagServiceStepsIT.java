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
// import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
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

    // @When("call find all tags with page = {int} and size = {int} and sort={string}")
    // public void callFindAllTags(int page, int size, String sort) {
    //     response = request().contentType(ContentType.JSON)
    //             .log()
    //             .all()
    //             .when().get(API_PATH + String.format("?page=%d&size=%d&sort=%s", page, size, sort));
    // }
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

    // @And("that list contains values:")
    // public void thatListContainsValues(DataTable dataTable) {
    //     List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    //     data.forEach(line -> response.then().assertThat()
    //             .body("content*.name", Matchers.hasItem(line.get(NAME).trim()))
    //             .body("content*.description", Matchers.hasItem(line.get(DESCRIPTION).trim())));
    // }
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











// package sn.ept.git.seminaire.cicd.cucumber.definitions;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.cucumber.datatable.DataTable;
// import io.cucumber.java.Before;
// import io.cucumber.java.BeforeAll;
// import io.cucumber.java.en.And;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import io.restassured.RestAssured;
// import io.restassured.http.ContentType;
// import io.restassured.response.Response;
// import io.restassured.specification.RequestSpecification;
// import lombok.extern.slf4j.Slf4j;
// import org.assertj.core.api.Assertions;
// import org.hamcrest.CoreMatchers;
// import org.hamcrest.Matchers;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.transaction.annotation.Transactional;
// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.entities.Tag;
// import sn.ept.git.seminaire.cicd.repositories.TagRepository;

// import java.time.Clock;
// import java.time.Instant;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// import static io.restassured.RestAssured.given;

// @Slf4j
// public class TagServiceStepsIT {

//     private final static String BASE_URI = "http://localhost";
//     public static final String API_PATH = "/cicd/api/tags";
//     public static final String ID = "id";
//     public static final String NAME = "name";
//     public static final String DESCRIPTION = "description";
    
//     @LocalServerPort
//     private int port;
//     private String name;
//     private String description;
//     private Response response;
//     private static final ObjectMapper objectMapper = new ObjectMapper();
    
//     @Autowired
//     private TagRepository tagRepository;

//     @BeforeAll
//     public static void beforeAll() {
//         objectMapper.findAndRegisterModules();
//     }

//     @Before
//     public void setup() {
//         RestAssured.port = port;
//         RestAssured.baseURI = BASE_URI;
//         tagRepository.deleteAllInBatch();
//     }

//     protected RequestSpecification request() {
//         return given()
//                 .contentType(ContentType.JSON)
//                 .log()
//                 .all();
//     }

//     @Transactional
//     @Given("acicd_tags table contains data:")
//     public void tableTagContainsData(DataTable dataTable) {
//         List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
//         List<Tag> tagsList = data
//             .stream()
//             .map((Map<String, String> line) -> Tag.builder()
//                 .id(line.get(ID))
//                 .name(line.get(NAME))
//                 .description(line.get(DESCRIPTION))
//                 .version(0)
//                 .createdDate(Instant.now(Clock.systemUTC()))
//                 .lastModifiedDate(Instant.now(Clock.systemUTC()))
//                 .build()
//             )
//             .collect(Collectors.toList());
//         tagRepository.saveAllAndFlush(tagsList);
//         try {
//             Thread.sleep(100);
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//     }

//     @When("call find tag by id with id={string}")
//     public void callFindTagByIdWithId(String id) {
//         response = request()
//                 .when().get(API_PATH + "/" + id);
//     }

//     @Then("the returned http status is {int}")
//     public void theHttpStatusIs(int status) {
//         response.then()
//                 .assertThat()
//                 .statusCode(status);
//     }

//     @And("the returned tag has properties name={string} and description={string}")
//     public void theReturnedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//     }

//     @When("call find all tags with page = {int} and size = {int} and sort={string}")
//     public void callFindAllTags(int page, int size, String sort) {
//         log.info("Calling find all tags with page={}, size={}, sort={}", page, size, sort);
//         response = request().contentType(ContentType.JSON)
//                 .log().all()
//                 .when().get(API_PATH + String.format("?page=%d&size=%d&sort=%s", page, size, sort));
//         log.info("Response status: {}", response.getStatusCode());
//         log.info("Response body: {}", response.getBody().asString());
//     }

//     @And("the returned list has {int} elements")
//     public void theReturnedListHasElements(int size) {
//         Assertions.assertThat(response.jsonPath().getList("content"))
//                 .hasSize(size);
//     }

//     @And("that list contains values:")
//     public void thatListContainsValues(DataTable dataTable) {
//         List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
//         List<Map<String, Object>> actualContent = response.jsonPath().getList("content");
//         log.info("Expected data: {}", expectedData);
//         log.info("Actual content: {}", actualContent);

//         for (Map<String, String> expected : expectedData) {
//             boolean found = actualContent.stream().anyMatch(actual ->
//                 actual.get(NAME).toString().equalsIgnoreCase(expected.get(NAME)) &&
//                 actual.get(DESCRIPTION).toString().equalsIgnoreCase(expected.get(DESCRIPTION))
//             );
//             Assertions.assertThat(found).as("Item not found: " + expected).isTrue();
//         }
//     }

//     @When("call add tag")
//     public void callAddTag() {
//         TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//         response = request()
//                 .body(requestBody)
//                 .when().post(API_PATH);
//     }

//     @And("the created tag has properties name={string} and description={string}")
//     public void theCreatedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//     }

//     // @When("call update tag with id={string}")
//     // public void callUpdateTagWithId(String id) {
//     //     TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//     //     response = request()
//     //             .body(requestBody)
//     //             .when().put(API_PATH + "/" + id);
//     // }
//     @When("call update tag with id={string}")
//     public void callUpdateTagWithId(String id) {
//     TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//     response = request()
//             .body(requestBody)
//             .when().put(API_PATH + "/" + id);
//     // Verification here
//     response.then()
//             .assertThat()
//             .statusCode(202);
//     }
//     @Then("the tag details should be updated successfully")
//     public void theTagDetailsShouldBeUpdatedSuccessfully() {
//     // Verification is already done in the "When" step with response.then().statusCode(202);
//     }



//     @And("the updated tag has properties name={string} and description={string}")
//     public void theUpdatedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//     }

//     @When("call delete tag with id={string}")
//     public void callDeleteTagWithId(String id) {
//         response = request()
//                 .when().delete(API_PATH + "/" + id);
//     }

//     @And("name = {string}")
//     public void name(String name) {
//         this.name = name;
//     }

//     @And("description = {string}")
//     public void description(String description) {
//         this.description = description;
//     }
// }




// package sn.ept.git.seminaire.cicd.cucumber.definitions;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.cucumber.datatable.DataTable;
// import io.cucumber.java.Before;
// import io.cucumber.java.BeforeAll;
// import io.cucumber.java.en.And;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import io.restassured.RestAssured;
// import io.restassured.http.ContentType;
// import io.restassured.response.Response;
// import io.restassured.specification.RequestSpecification;
// import lombok.extern.slf4j.Slf4j;
// import org.assertj.core.api.Assertions;
// import org.hamcrest.CoreMatchers;
// import org.hamcrest.Matchers;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.transaction.annotation.Transactional;
// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.entities.Tag;
// import sn.ept.git.seminaire.cicd.repositories.TagRepository;

// import java.time.Clock;
// import java.time.Instant;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// import static io.restassured.RestAssured.given;

// @Slf4j
// public class TagServiceStepsIT {

//     private final static String BASE_URI = "http://localhost";
//     public static final String API_PATH = "/cicd/api/tags";
//     public static final String ID = "id";
//     public static final String NAME = "name";
//     public static final String DESCRIPTION = "description";
    
//     @LocalServerPort
//     private int port;
//     private String name;
//     private String description;
//     private Response response;
//     private static final ObjectMapper objectMapper = new ObjectMapper();
    
//     @Autowired
//     private TagRepository tagRepository;

//     @BeforeAll
//     public static void beforeAll() {
//         objectMapper.findAndRegisterModules();
//     }

//     @Before
//     public void setup() {
//         RestAssured.port = port;
//         RestAssured.baseURI = BASE_URI;
//         tagRepository.deleteAllInBatch();
//     }

//     protected RequestSpecification request() {
//         return given()
//                 .contentType(ContentType.JSON)
//                 .log()
//                 .all();
//     }

//     @Transactional
//     @Given("acicd_tags table contains data:")
//     public void tableTagContainsData(DataTable dataTable) {
//         List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
//         List<Tag> tagsList = data
//             .stream()
//             .map((Map<String, String> line) -> Tag.builder()
//                 .id(line.get(ID))
//                 .name(line.get(NAME))
//                 .description(line.get(DESCRIPTION))
//                 .version(0)
//                 .createdDate(Instant.now(Clock.systemUTC()))
//                 .lastModifiedDate(Instant.now(Clock.systemUTC()))
//                 .build()
//             )
//             .collect(Collectors.toList());
//         tagRepository.saveAllAndFlush(tagsList);
//         try {
//             Thread.sleep(100);
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//     }

//     @When("call find tag by id with id={string}")
//     public void callFindTagByIdWithId(String id) {
//         response = request()
//                 .when().get(API_PATH + "/" + id);
//     }

//     @Then("the returned http status is {int}")
//     public void theHttpStatusIs(int status) {
//         response.then()
//                 .assertThat()
//                 .statusCode(status);
//     }

//     @And("the returned tag has properties name={string} and description={string}")
//     public void theReturnedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//     }

//     @When("call find all tags with page = {int} and size = {int} and sort={string}")
//     public void callFindAllTags(int page, int size, String sort) {
//         log.info("Calling find all tags with page={}, size={}, sort={}", page, size, sort);
//         response = request().contentType(ContentType.JSON)
//                 .log().all()
//                 .when().get(API_PATH + String.format("?page=%d&size=%d&sort=%s", page, size, sort));
//         log.info("Response status: {}", response.getStatusCode());
//         log.info("Response body: {}", response.getBody().asString());
//     }

//     @And("the returned list has {int} elements")
//     public void theReturnedListHasElements(int size) {
//         Assertions.assertThat(response.jsonPath().getList("content"))
//                 .hasSize(size);
//     }

//     @And("that list contains values:")
//     public void thatListContainsValues(DataTable dataTable) {
//         List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
//         List<Map<String, Object>> actualContent = response.jsonPath().getList("content");
//         log.info("Expected data: {}", expectedData);
//         log.info("Actual content: {}", actualContent);

//         for (Map<String, String> expected : expectedData) {
//             boolean found = actualContent.stream().anyMatch(actual ->
//                 actual.get(NAME).toString().equalsIgnoreCase(expected.get(NAME)) &&
//                 actual.get(DESCRIPTION).toString().equalsIgnoreCase(expected.get(DESCRIPTION))
//             );
//             Assertions.assertThat(found).as("Item not found: " + expected).isTrue();
//         }
//     }

//     @When("call add tag")
//     public void callAddTag() {
//         TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//         response = request()
//                 .body(requestBody)
//                 .when().post(API_PATH);
//     }

//     @And("the created tag has properties name={string} and description={string}")
//     public void theCreatedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//     }

//     @When("call update tag with id={string}")
//     public void callUpdateTagWithId(String id) {
//         TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//         response = request()
//                 .body(requestBody)
//                 .when().put(API_PATH + "/" + id);
//     }

//     @And("the updated tag has properties name={string} and description={string}")
//     public void theUpdatedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//     }

//     @When("call delete tag with id={string}")
//     public void callDeleteTagWithId(String id) {
//         response = request()
//                 .when().delete(API_PATH + "/" + id);
//     }

//     @And("name = {string}")
//     public void name(String name) {
//         this.name = name;
//     }

//     @And("description = {string}")
//     public void description(String description) {
//         this.description = description;
//     }
// }




// package sn.ept.git.seminaire.cicd.cucumber.definitions;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.cucumber.datatable.DataTable;
// import io.cucumber.java.Before;
// import io.cucumber.java.BeforeAll;
// import io.cucumber.java.en.And;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import io.restassured.RestAssured;
// import io.restassured.http.ContentType;
// import io.restassured.response.Response;
// import io.restassured.specification.RequestSpecification;
// import lombok.extern.slf4j.Slf4j;
// import org.assertj.core.api.Assertions;
// import org.hamcrest.CoreMatchers;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.transaction.annotation.Transactional;
// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.entities.Tag;
// import sn.ept.git.seminaire.cicd.repositories.TagRepository;

// import java.time.Clock;
// import java.time.Instant;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// import static io.restassured.RestAssured.given;

// @Slf4j
// public class TagServiceStepsIT {

//     private final static String BASE_URI = "http://localhost";
//     public static final String API_PATH = "/cicd/api/tags";
//     public static final String ID = "id";
//     public static final String NAME = "name";
//     public static final String DESCRIPTION = "description";
    
//     @LocalServerPort
//     private int port;
//     private String name;
//     private String description;
//     private Response response;
//     private static final ObjectMapper objectMapper = new ObjectMapper();
    
//     @Autowired
//     private TagRepository tagRepository;

//     @BeforeAll
//     public static void beforeAll() {
//         objectMapper.findAndRegisterModules();
//     }

//     @Before
//     public void setup() {
//         RestAssured.port = port;
//         RestAssured.baseURI = BASE_URI;
//         tagRepository.deleteAllInBatch();
//     }

//     protected RequestSpecification request() {
//         return given()
//                 .contentType(ContentType.JSON)
//                 .log()
//                 .all();
//     }

//     @Transactional
//     @Given("acicd_tags table contains data:")
//     public void tableTagContainsData(DataTable dataTable) {
//         List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
//         List<Tag> tagsList = data
//             .stream()
//             .map((Map<String, String> line) -> Tag.builder()
//                 .id(line.get(ID))
//                 .name(line.get(NAME))
//                 .description(line.get(DESCRIPTION))
//                 .version(0)
//                 .createdDate(Instant.now(Clock.systemUTC()))
//                 .lastModifiedDate(Instant.now(Clock.systemUTC()))
//                 .build()
//             )
//             .collect(Collectors.toList());
//         tagRepository.saveAllAndFlush(tagsList);
//         log.info("Inserted {} tags into the database", tagsList.size());
//     }

//     @When("call find tag by id with id={string}")
//     public void callFindTagByIdWithId(String id) {
//         boolean exists = tagRepository.existsById(id);
//         log.info("Tag with id {} exists: {}", id, exists);
        
//         response = request()
//                 .when().get(API_PATH + "/" + id);
        
//         log.info("Find by id response status: {}", response.getStatusCode());
//         log.info("Find by id response body: {}", response.getBody().asString());
//     }

//     @Then("the returned http status is {int}")
//     public void theHttpStatusIs(int status) {
//         response.then()
//                 .assertThat()
//                 .statusCode(status);
//         log.info("Verified HTTP status: {}", status);
//     }

//     @And("the returned tag has properties name={string} and description={string}")
//     public void theReturnedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//         log.info("Verified tag properties: name={}, description={}", name, description);
//     }

//     @When("call find all tags with page = {int} and size = {int} and sort={string}")
//     public void callFindAllTags(int page, int size, String sort) {
//         response = request()
//                 .queryParam("page", page)
//                 .queryParam("size", size)
//                 .queryParam("sort", sort)
//                 .when().get(API_PATH);
//         log.info("Find all tags response status: {}", response.getStatusCode());
//         log.info("Find all tags response body: {}", response.getBody().asString());
//     }

//     @And("the returned list has {int} elements")
//     public void theReturnedListHasElements(int size) {
//         List<Object> content = response.jsonPath().getList("content");
//         Assertions.assertThat(content).hasSize(size);
//         log.info("Verified list size: {}", size);
//     }

//     // @And("that list contains values:")
//     // public void thatListContainsValues(DataTable dataTable) {
//     //     List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
//     //     List<Map<String, Object>> actualContent = response.jsonPath().getList("content");
        
//     //     for (int i = 0; i < expectedData.size(); i++) {
//     //         Map<String, String> expected = expectedData.get(i);
//     //         Map<String, Object> actual = actualContent.get(i);
//     //         log.info("Comparing expected: {} with actual: {}", expected, actual);
//     //         Assertions.assertThat(actual.get(NAME)).isEqualToIgnoringCase(expected.get(NAME));
//     //         Assertions.assertThat(actual.get(DESCRIPTION)).isEqualToIgnoringCase(expected.get(DESCRIPTION));
//     //     }
//     //     log.info("Verified all list items");
//     // }
//     @And("that list contains values:")
//     public void thatListContainsValues(DataTable dataTable) {
//         List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
//         List<Map<String, Object>> actualContent = response.jsonPath().getList("content");
        
//         Assertions.assertThat(actualContent).hasSameSizeAs(expectedData);
        
//         for (int i = 0; i < expectedData.size(); i++) {
//             Map<String, String> expected = expectedData.get(i);
//             Map<String, Object> actual = actualContent.get(i);
//             log.info("Comparing expected: {} with actual: {}", expected, actual);
            
//             Assertions.assertThat(actual.get(NAME).toString())
//                 .as("Comparing name at index " + i)
//                 .isEqualToIgnoringCase(expected.get(NAME));
            
//             Assertions.assertThat(actual.get(DESCRIPTION).toString())
//                 .as("Comparing description at index " + i)
//                 .isEqualToIgnoringCase(expected.get(DESCRIPTION));
//         }
//         log.info("Verified all list items");
//     }

//     @When("call add tag")
//     public void callAddTag() {
//         TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//         log.info("Adding tag with name: {}", this.name);
        
//         response = request()
//                 .body(requestBody)
//                 .when().post(API_PATH);
        
//         log.info("Add tag response status: {}", response.getStatusCode());
//         log.info("Add tag response body: {}", response.getBody().asString());
//     }

//     @And("the created tag has properties name={string} and description={string}")
//     public void theCreatedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//         log.info("Verified created tag properties: name={}, description={}", name, description);
//     }

//     @When("call update tag with id={string}")
//     public void callUpdateTagWithId(String id) {
//         TagDTO requestBody = TagDTO.builder().name(this.name).description(this.description).build();
//         log.info("Updating tag with id: {}", id);
        
//         response = request()
//                 .body(requestBody)
//                 .when().put(API_PATH + "/" + id);
        
//         log.info("Update tag response status: {}", response.getStatusCode());
//         log.info("Update tag response body: {}", response.getBody().asString());
//     }

//     @And("the updated tag has properties name={string} and description={string}")
//     public void theUpdatedTagHasPropertiesNameAndDescription(String name, String description) {
//         response.then()
//                 .assertThat()
//                 .body(NAME, CoreMatchers.equalTo(name))
//                 .body(DESCRIPTION, CoreMatchers.equalTo(description));
//         log.info("Verified updated tag properties: name={}, description={}", name, description);
//     }

//     @When("call delete tag with id={string}")
//     public void callDeleteTagWithId(String id) {
//         boolean exists = tagRepository.existsById(id);
//         log.info("Tag with id {} exists before deletion: {}", id, exists);
        
//         response = request()
//                 .when().delete(API_PATH + "/" + id);
        
//         log.info("Delete tag response status: {}", response.getStatusCode());
//     }

//     @And("name = {string}")
//     public void name(String name) {
//         this.name = name;
//         log.info("Set name: {}", name);
//     }

//     @And("description = {string}")
//     public void description(String description) {
//         this.description = description;
//         log.info("Set description: {}", description);
//     }
// }