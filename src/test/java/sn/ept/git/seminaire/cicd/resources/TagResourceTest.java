package sn.ept.git.seminaire.cicd.resources;

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

    // Tests ajoutés
    @Test
    void findAll_ShouldReturnEmptyPage_WhenNoTagsExist() {
        // Given
        Page<TagDTO> emptyPage = new PageImpl<>(Collections.emptyList());
        when(service.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        ResponseEntity<Page<TagDTO>> response = resource.findAll(Pageable.unpaged());

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void create_ShouldThrowException_WhenTagDTOIsInvalid() {
        // Given
        tagDTO.setName(null); // Making the tag invalid

        // When/Then
        assertThrows(Exception.class, () -> {
            resource.create(tagDTO);
        });
    }

    @Test
    void update_ShouldThrowException_WhenTagDTOIsInvalid() {
        // Given
        tagDTO.setName(null); // Making the tag invalid

        // When/Then
        assertThrows(Exception.class, () -> {
            resource.update("1", tagDTO);
        });
    }

}
