package sn.ept.git.seminaire.cicd.resources;


// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.Mockito.*;

// // import java.net.URI;
// import java.util.Collections;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Disabled;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// // import org.junit.runner.RunWith;
// // import org.springframework.boot.test.context.SpringBootTest;
// import org.mockito.junit.jupiter.MockitoExtension; // Use MockitoExtension for JUnit 5

// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// // import org.mockito.junit.MockitoJUnitRunner;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// // import org.springframework.test.context.junit4.SpringRunner;
// // import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.services.ITagService;
// // import sn.ept.git.seminaire.cicd.utils.ResponseUtil;
// // import sn.ept.git.seminaire.cicd.utils.UrlMapping;



// // @RunWith(MockitoJUnitRunner.class)
// @ExtendWith(MockitoExtension.class)

// // @SpringBootTest // Use for Spring Boot Test
// // @RunWith(SpringRunner.class) // Use SpringRunner for @SpringBootTest

// public class TagResourceTest {

//     @Mock
//     private ITagService tagService;

//     @InjectMocks
//     private TagResource tagResource;

//     @BeforeEach
//     public void setUp() {
//         // Setup mock behavior for each test method
//     }

//     @Test
//     public void testFindAll() {
//         Pageable pageable = Pageable.unpaged(); // Example pageable

//         Page<TagDTO> tagPage = new PageImpl<>(Collections.emptyList()); // Example empty page
//         when(tagService.findAll(pageable)).thenReturn(tagPage);

//         ResponseEntity<Page<TagDTO>> response = tagResource.findAll(pageable);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         // Add more assertions as needed
//     }

//     @Test
//     public void testFindById() {
//         String id = "some-id";
//         TagDTO tagDTO = new TagDTO(); // Example TagDTO
//         when(tagService.findById(id)).thenReturn(Optional.of(tagDTO));

//         ResponseEntity<TagDTO> response = tagResource.findById(id);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         // Add more assertions as needed
//     }

//     @Disabled
//     @Test
//     public void testCreate() {
//         TagDTO dto = new TagDTO(); // Example TagDTO to create
//         TagDTO created = new TagDTO();
//         created.setId("generated-id"); // Example created TagDTO with ID

//         when(tagService.save(dto)).thenReturn(created);

//         ResponseEntity<TagDTO> response = tagResource.create(dto);

//         assertEquals(HttpStatus.CREATED, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertEquals("generated-id", response.getBody().getId());
//         // Add more assertions as needed
//     }

//     @Test
//     public void testDelete() {
//         String id = "some-id";

//         ResponseEntity<TagDTO> response = tagResource.delete(id);

//         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//         // Add more assertions as needed
//     }

//     @Test
//     public void testUpdate() {
//         String id = "some-id";
//         TagDTO dto = new TagDTO(); // Example TagDTO to update
//         TagDTO updated = new TagDTO();
//         updated.setId(id); // Example updated TagDTO

//         when(tagService.update(id, dto)).thenReturn(updated);

//         ResponseEntity<TagDTO> response = tagResource.update(id, dto);

//         assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertEquals(id, response.getBody().getId());
//         // Add more assertions as needed
//     }
// }

















// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.services.ITagService;

// import java.util.Arrays;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class TagResourceTest {

//     @Mock
//     private ITagService tagService;

//     @InjectMocks
//     private TagResource tagResource;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void findAll_ShouldReturnPageOfTags() {
//         Pageable pageable = mock(Pageable.class);
//         Page<TagDTO> page = new PageImpl<>(Arrays.asList(new TagDTO(), new TagDTO()));
//         when(tagService.findAll(pageable)).thenReturn(page);

//         ResponseEntity<Page<TagDTO>> response = tagResource.findAll(pageable);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(page, response.getBody());
//     }

//     @Test
//     void findAll_ShouldReturnEmptyPage() {
//         Pageable pageable = mock(Pageable.class);
//         Page<TagDTO> emptyPage = Page.empty();
//         when(tagService.findAll(pageable)).thenReturn(emptyPage);

//         ResponseEntity<Page<TagDTO>> response = tagResource.findAll(pageable);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isEmpty());
//     }

//     @Test
//     void findById_ShouldReturnTag_WhenTagExists() {
//         String id = "1";
//         TagDTO tagDTO = new TagDTO();
//         when(tagService.findById(id)).thenReturn(Optional.of(tagDTO));

//         ResponseEntity<TagDTO> response = tagResource.findById(id);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(tagDTO, response.getBody());
//     }

//     @Test
//     void findById_ShouldReturnNotFound_WhenTagDoesNotExist() {
//         String id = "1";
//         when(tagService.findById(id)).thenReturn(Optional.empty());

//         ResponseEntity<TagDTO> response = tagResource.findById(id);

//         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     }

//     // @Test
//     // void create_ShouldReturnCreatedTag() {
//     //     TagDTO inputDTO = new TagDTO();
//     //     TagDTO createdDTO = new TagDTO();
//     //     createdDTO.setId("1");
//     //     when(tagService.save(inputDTO)).thenReturn(createdDTO);

//     //     ResponseEntity<TagDTO> response = tagResource.create(inputDTO);

//     //     assertEquals(HttpStatus.CREATED, response.getStatusCode());
//     //     assertEquals(createdDTO, response.getBody());
//     //     assertNotNull(response.getHeaders().getLocation());
//     // }

//     @Test
// void create_ShouldReturnCreatedTag() {
//     TagDTO inputDTO = new TagDTO();
//     TagDTO createdDTO = new TagDTO();
//     createdDTO.setId("1");
//     when(tagService.save(inputDTO)).thenReturn(createdDTO);

//     ResponseEntity<TagDTO> response = tagResource.create(inputDTO);

//     assertEquals(HttpStatus.CREATED, response.getStatusCode());
//     assertEquals(createdDTO, response.getBody());
//     assertTrue(response.getHeaders().getLocation().getPath().endsWith("/1"));
// }

//     @Test
//     void create_ShouldHandleException() {
//         TagDTO inputDTO = new TagDTO();
//         when(tagService.save(inputDTO)).thenThrow(new RuntimeException("Error"));

//         assertThrows(RuntimeException.class, () -> tagResource.create(inputDTO));
//     }

//     @Test
//     void delete_ShouldReturnNoContent() {
//         String id = "1";
//         doNothing().when(tagService).delete(id);

//         ResponseEntity<TagDTO> response = tagResource.delete(id);

//         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//     }

//     @Test
//     void delete_ShouldHandleException() {
//         String id = "1";
//         doThrow(new RuntimeException("Error")).when(tagService).delete(id);

//         assertThrows(RuntimeException.class, () -> tagResource.delete(id));
//     }

//     @Test
//     void update_ShouldReturnUpdatedTag() {
//         String id = "1";
//         TagDTO inputDTO = new TagDTO();
//         TagDTO updatedDTO = new TagDTO();
//         when(tagService.update(id, inputDTO)).thenReturn(updatedDTO);

//         ResponseEntity<TagDTO> response = tagResource.update(id, inputDTO);

//         assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//         assertEquals(updatedDTO, response.getBody());
//     }

//     // @Test
//     // void update_ShouldReturnNotFound_WhenTagDoesNotExist() {
//     //     String id = "1";
//     //     TagDTO inputDTO = new TagDTO();
//     //     when(tagService.update(id, inputDTO)).thenReturn(null);

//     //     ResponseEntity<TagDTO> response = tagResource.update(id, inputDTO);

//     //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     // }

//     @Test
// void update_ShouldReturnNotFound_WhenTagDoesNotExist() {
//     String id = "1";
//     TagDTO inputDTO = new TagDTO();
//     when(tagService.update(id, inputDTO)).thenReturn(null);

//     ResponseEntity<TagDTO> response = tagResource.update(id, inputDTO);

//     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     assertNull(response.getBody());
// }
// }



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.services.ITagService;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
// import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagResourceTest {

    @Mock
    private ITagService service;

    @InjectMocks
    private TagResource resource;

    private TagDTO tagDTO;

    @BeforeEach
    void setUp() {
        tagDTO = new TagDTO();
        tagDTO.setId("1");
        tagDTO.setName("Test Tag");
        tagDTO.setDescription("Test Description");
    }

    @Test
    void findAll_ShouldReturnAllTags() {
        // Given
        Page<TagDTO> tagPage = new PageImpl<>(Collections.singletonList(tagDTO));
        when(service.findAll(any(Pageable.class))).thenReturn(tagPage);

        // When
        ResponseEntity<Page<TagDTO>> response = resource.findAll(Pageable.unpaged());

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findById_ShouldReturnTag_WhenTagExists() {
        // Given
        when(service.findById(anyString())).thenReturn(Optional.of(tagDTO));

        // When
        ResponseEntity<TagDTO> response = resource.findById("1");

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tagDTO.getName(), response.getBody().getName());
        verify(service, times(1)).findById(anyString());
    }
    @Disabled
    @Test
    void create_ShouldCreateNewTag() {
        // Given
        when(service.save(any(TagDTO.class))).thenReturn(tagDTO);

        // When
        ResponseEntity<TagDTO> response = resource.create(tagDTO);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tagDTO.getId(), response.getBody().getId());
        assertEquals(UrlMapping.Tag.ADD + "/" + tagDTO.getId(), response.getHeaders().getLocation().getPath());
        verify(service, times(1)).save(any(TagDTO.class));
    }

    @Test
    void delete_ShouldDeleteTag_WhenTagExists() {
        // When
        ResponseEntity<TagDTO> response = resource.delete("1");

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(anyString());
    }

    @Test
    void update_ShouldUpdateTag_WhenTagExists() {
        // Given
        when(service.update(anyString(), any(TagDTO.class))).thenReturn(tagDTO);

        // When
        ResponseEntity<TagDTO> response = resource.update("1", tagDTO);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(tagDTO.getName(), response.getBody().getName());
        verify(service, times(1)).update(anyString(), any(TagDTO.class));
    }
}
