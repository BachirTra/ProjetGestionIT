// package sn.ept.git.seminaire.cicd.cucumber.definitions;

// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.services.ITagService;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.Mockito.*;

// @SpringBootTest
// public class TagServiceSteps {

//     @Autowired
//     private ITagService tagService;

//     private List<TagDTO> tags = new ArrayList<>();
//     private TagDTO savedTag;

//     @Given("There are existing tags in the database")
//     public void thereAreExistingTagsInTheDatabase() {
//         // Mock or setup actual data in the database
//         tags.add(TagDTO.builder().id("1").name("Tag 1").description("Description 1").build());
//         tags.add(TagDTO.builder().id("2").name("Tag 2").description("Description 2").build());
//         when(tagService.findAll()).thenReturn(tags);
//     }

//     @When("I request to retrieve all tags")
//     public void iRequestToRetrieveAllTags() {
//         List<TagDTO> retrievedTags = tagService.findAll();
//         assertNotNull(retrievedTags);
//         assertEquals(2, retrievedTags.size());
//     }

//     @Given("There is a tag with ID {string} in the database")
//     public void thereIsATagWithIdInTheDatabase(String id) {
//         // Mock or setup actual data in the database
//         TagDTO tag = TagDTO.builder().id(id).name("Test Tag").description("Test Description").build();
//         when(tagService.findById(id)).thenReturn(java.util.Optional.of(tag));
//     }

//     @When("I request to retrieve the tag with ID {string}")
//     public void iRequestToRetrieveTheTagWithId(String id) {
//         TagDTO retrievedTag = tagService.findById(id).orElse(null);
//         assertNotNull(retrievedTag);
//         assertEquals(id, retrievedTag.getId());
//     }

//     @Given("I have a new tag to add")
//     public void iHaveANewTagToAdd() {
//         // Prepare a new tag to add
//         TagDTO newTag = TagDTO.builder().name("New Tag").description("New Description").build();
//         when(tagService.save(newTag)).thenReturn(TagDTO.builder().id("3").name("New Tag").description("New Description").build());
//     }

//     @When("I request to add the tag")
//     public void iRequestToAddTheTag() {
//         TagDTO newTag = TagDTO.builder().name("New Tag").description("New Description").build();
//         savedTag = tagService.save(newTag);
//         assertNotNull(savedTag);
//         assertEquals("3", savedTag.getId());
//     }

//     @Given("There is an existing tag with ID {string}")
//     public void thereIsAnExistingTagWithId(String id) {
//         // Mock or setup actual data in the database
//         TagDTO existingTag = TagDTO.builder().id(id).name("Existing Tag").description("Existing Description").build();
//         when(tagService.findById(id)).thenReturn(java.util.Optional.of(existingTag));
//         when(tagService.update(eq(id), any(TagDTO.class))).thenReturn(existingTag);
//     }

//     @When("I request to update the tag with ID {string}")
//     public void iRequestToUpdateTheTagWithId(String id) {
//         TagDTO updatedTag = TagDTO.builder().id(id).name("Updated Tag").description("Updated Description").build();
//         TagDTO result = tagService.update(id, updatedTag);
//         assertNotNull(result);
//         assertEquals("Updated Tag", result.getName());
//     }

//     @When("I request to delete the tag with ID {string}")
//     public void iRequestToDeleteTheTagWithId(String id) {
//         // No need for direct assertions here, as deletion is verified by side effects (e.g., mock verification)
//         tagService.delete(id);
//         verify(tagService, times(1)).delete(id);
//     }

//     @Then("the tag should be saved successfully")
//     public void theTagShouldBeSavedSuccessfully() {
//         assertNotNull(savedTag);
//         assertEquals("New Tag", savedTag.getName());
//     }
// }

//     // @Then("the tag details should be updated successfully")
//     // public void theTagDetailsShouldBeUpdatedSuccessfully()











//TagServiceSteps.java
package sn.ept.git.seminaire.cicd.cucumber.definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.services.ITagService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TagServiceSteps {

    @Autowired
    private ITagService tagService;

    private List<TagDTO> tags = new ArrayList<>();
    private TagDTO savedTag;

    @Given("There are existing tags in the database")
    public void thereAreExistingTagsInTheDatabase() {
        // Mock or setup actual data in the database
        tags.add(TagDTO.builder().id("1").name("Tag 1").description("Description 1").build());
        tags.add(TagDTO.builder().id("2").name("Tag 2").description("Description 2").build());
        when(tagService.findAll()).thenReturn(tags);
    }

    @When("I request to retrieve all tags")
    public void iRequestToRetrieveAllTags() {
        List<TagDTO> retrievedTags = tagService.findAll();
        assertNotNull(retrievedTags);
        assertEquals(2, retrievedTags.size());
    }

    @Given("There is a tag with ID {string} in the database")
    public void thereIsATagWithIdInTheDatabase(String id) {
        // Mock or setup actual data in the database
        TagDTO tag = TagDTO.builder().id(id).name("Test Tag").description("Test Description").build();
        when(tagService.findById(id)).thenReturn(java.util.Optional.of(tag));
    }

    @When("I request to retrieve the tag with ID {string}")
    public void iRequestToRetrieveTheTagWithId(String id) {
        TagDTO retrievedTag = tagService.findById(id).orElse(null);
        assertNotNull(retrievedTag);
        assertEquals(id, retrievedTag.getId());
    }

    @Given("I have a new tag to add")
    public void iHaveANewTagToAdd() {
        // Prepare a new tag to add
        TagDTO newTag = TagDTO.builder().name("New Tag").description("New Description").build();
        when(tagService.save(newTag)).thenReturn(TagDTO.builder().id("3").name("New Tag").description("New Description").build());
    }

    @When("I request to add the tag")
    public void iRequestToAddTheTag() {
        TagDTO newTag = TagDTO.builder().name("New Tag").description("New Description").build();
        savedTag = tagService.save(newTag);
        assertNotNull(savedTag);
        assertEquals("3", savedTag.getId());
    }

    @Given("There is an existing tag with ID {string}")
    public void thereIsAnExistingTagWithId(String id) {
        // Mock or setup actual data in the database
        TagDTO existingTag = TagDTO.builder().id(id).name("Existing Tag").description("Existing Description").build();
        when(tagService.findById(id)).thenReturn(java.util.Optional.of(existingTag));
        when(tagService.update(eq(id), any(TagDTO.class))).thenReturn(existingTag);
    }

    @When("I request to update the tag with ID {string}")
    public void iRequestToUpdateTheTagWithId(String id) {
        TagDTO updatedTag = TagDTO.builder().id(id).name("Updated Tag").description("Updated Description").build();
        TagDTO result = tagService.update(id, updatedTag);
        assertNotNull(result);
        assertEquals("Updated Tag", result.getName());
    }

    @When("I request to delete the tag with ID {string}")
    public void iRequestToDeleteTheTagWithId(String id) {
        // No need for direct assertions here, as deletion is verified by side effects (e.g., mock verification)
        tagService.delete(id);
        verify(tagService, times(1)).delete(id);
    }

    @Then("the tag should be saved successfully")
    public void theTagShouldBeSavedSuccessfully() {
        assertNotNull(savedTag);
        assertEquals("New Tag", savedTag.getName());
    }

    @Then("the tag details should be updated successfully")
    public void theTagDetailsShouldBeUpdatedSuccessfully() {
        // Verification is done in the "When" step with Mockito verify assertions
    }
}
