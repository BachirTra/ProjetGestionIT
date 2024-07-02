// package sn.ept.git.seminaire.cicd.services;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import sn.ept.git.seminaire.cicd.entities.Tag;
// import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
// import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
// import sn.ept.git.seminaire.cicd.mappers.TagMapper;
// import sn.ept.git.seminaire.cicd.models.TagDTO;
// import sn.ept.git.seminaire.cicd.repositories.TagRepository;
// import sn.ept.git.seminaire.cicd.services.impl.TagServiceImpl;

// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class TagServiceTest {

//     @Mock
//     private TagRepository repository;

//     @Mock
//     private TagMapper mapper;

//     @InjectMocks
//     private TagServiceImpl service;

//     private Tag tag;
//     private TagDTO tagDTO;

//     @BeforeEach
//     void setUp() {
//         tag = new Tag();
//         tag.setId(UUID.randomUUID().toString());
//         tag.setName("Test Tag");
//         tag.setDescription("Test Description");

//         tagDTO = new TagDTO();
//         tagDTO.setId(tag.getId());
//         tagDTO.setName(tag.getName());
//         tagDTO.setDescription(tag.getDescription());
//     }

//     @Test
//     void save_ShouldSaveTag_WhenTagDoesNotExist() {
//         when(repository.findByName(anyString())).thenReturn(Optional.empty());
//         when(repository.saveAndFlush(any(Tag.class))).thenReturn(tag);
//         when(mapper.toEntity(any(TagDTO.class))).thenReturn(tag);
//         when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

//         TagDTO savedTag = service.save(tagDTO);

//         assertNotNull(savedTag);
//         assertEquals(tagDTO.getName(), savedTag.getName());
//         verify(repository, times(1)).saveAndFlush(any(Tag.class));
//     }

//     @Test
//     void save_ShouldThrowException_WhenTagExists() {
//         when(repository.findByName(anyString())).thenReturn(Optional.of(tag));

//         assertThrows(ItemExistsException.class, () -> service.save(tagDTO));
//     }

//     @Test
//     void delete_ShouldDeleteTag_WhenTagExists() {
//         when(repository.findById(anyString())).thenReturn(Optional.of(tag));

//         assertDoesNotThrow(() -> service.delete(tag.getId()));
//         verify(repository, times(1)).deleteById(tag.getId());
//     }

//     @Test
//     void delete_ShouldThrowException_WhenTagDoesNotExist() {
//         when(repository.findById(anyString())).thenReturn(Optional.empty());

//         assertThrows(ItemNotFoundException.class, () -> service.delete(tag.getId()));
//     }

//     @Test
//     void findById_ShouldReturnTag_WhenTagExists() {
//         when(repository.findById(anyString())).thenReturn(Optional.of(tag));
//         when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

//         Optional<TagDTO> foundTag = service.findById(tag.getId());

//         assertTrue(foundTag.isPresent());
//         assertEquals(tagDTO.getId(), foundTag.get().getId());
//     }

//     @Test
//     void findById_ShouldReturnEmpty_WhenTagDoesNotExist() {
//         when(repository.findById(anyString())).thenReturn(Optional.empty());

//         Optional<TagDTO> foundTag = service.findById(tag.getId());

//         assertTrue(foundTag.isEmpty());
//     }

//     @Test
//     void update_ShouldUpdateTag_WhenTagExists() {
//         when(repository.findById(anyString())).thenReturn(Optional.of(tag));
//         when(repository.findByNameWithIdNotEquals(anyString(), anyString())).thenReturn(Optional.empty());
//         when(repository.saveAndFlush(any(Tag.class))).thenReturn(tag);
//         when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

//         TagDTO updatedTag = service.update(tag.getId(), tagDTO);

//         assertNotNull(updatedTag);
//         assertEquals(tagDTO.getName(), updatedTag.getName());
//         verify(repository, times(1)).saveAndFlush(any(Tag.class));
//     }

//     @Test
//     void update_ShouldThrowException_WhenTagDoesNotExist() {
//         when(repository.findById(anyString())).thenReturn(Optional.empty());

//         assertThrows(ItemNotFoundException.class, () -> service.update(tag.getId(), tagDTO));
//     }

//     @Test
//     void update_ShouldThrowException_WhenTagNameExists() {
//         when(repository.findById(anyString())).thenReturn(Optional.of(tag));
//         when(repository.findByNameWithIdNotEquals(anyString(), anyString())).thenReturn(Optional.of(tag));

//         assertThrows(ItemExistsException.class, () -> service.update(tag.getId(), tagDTO));
//     }
// }



//TagServiceTest
package sn.ept.git.seminaire.cicd.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;
import sn.ept.git.seminaire.cicd.services.impl.TagServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository repository;

    @Mock
    private TagMapper mapper;

    @InjectMocks
    private TagServiceImpl service;

    private Tag tag;
    private TagDTO tagDTO;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(UUID.randomUUID().toString());
        tag.setName("Test Tag");
        tag.setDescription("Test Description");

        tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        tagDTO.setDescription(tag.getDescription());
    }

    @Test
    void save_ShouldSaveTag_WhenTagDoesNotExist() {
        when(repository.findByName(anyString())).thenReturn(Optional.empty());
        when(repository.saveAndFlush(any(Tag.class))).thenReturn(tag);
        when(mapper.toEntity(any(TagDTO.class))).thenReturn(tag);
        when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        TagDTO savedTag = service.save(tagDTO);

        assertNotNull(savedTag);
        assertEquals(tagDTO.getName(), savedTag.getName());
        verify(repository, times(1)).saveAndFlush(any(Tag.class));
    }

    @Test
    void save_ShouldThrowException_WhenTagExists() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(tag));

        assertThrows(ItemExistsException.class, () -> service.save(tagDTO));
    }

    @Test
    void delete_ShouldDeleteTag_WhenTagExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(tag));

        assertDoesNotThrow(() -> service.delete(tag.getId()));
        verify(repository, times(1)).deleteById(tag.getId());
    }

    @Test
    void delete_ShouldThrowException_WhenTagDoesNotExist() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.delete(tag.getId()));
    }

    @Test
    void findById_ShouldReturnTag_WhenTagExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(tag));
        when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        Optional<TagDTO> foundTag = service.findById(tag.getId());

        assertTrue(foundTag.isPresent());
        assertEquals(tagDTO.getId(), foundTag.get().getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTagDoesNotExist() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        Optional<TagDTO> foundTag = service.findById(tag.getId());

        assertTrue(foundTag.isEmpty());
    }

    @Test
    void update_ShouldUpdateTag_WhenTagExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(tag));
        when(repository.findByNameWithIdNotEquals(anyString(), anyString())).thenReturn(Optional.empty());
        when(repository.saveAndFlush(any(Tag.class))).thenReturn(tag);
        when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        TagDTO updatedTag = service.update(tag.getId(), tagDTO);

        assertNotNull(updatedTag);
        assertEquals(tagDTO.getName(), updatedTag.getName());
        verify(repository, times(1)).saveAndFlush(any(Tag.class));
    }

    @Test
    void update_ShouldThrowException_WhenTagDoesNotExist() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.update(tag.getId(), tagDTO));
    }

    @Test
    void update_ShouldThrowException_WhenTagNameExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(tag));
        when(repository.findByNameWithIdNotEquals(anyString(), anyString())).thenReturn(Optional.of(tag));

        assertThrows(ItemExistsException.class, () -> service.update(tag.getId(), tagDTO));
    }

    @Test
    void deleteAll_ShouldDeleteAllTags() {
        assertDoesNotThrow(() -> service.deleteAll());
        verify(repository, times(1)).deleteAll();
    }

    @Test
    void deleteAll_ShouldThrowException_WhenRepositoryFails() {
        doThrow(RuntimeException.class).when(repository).deleteAll();

        assertThrows(RuntimeException.class, () -> service.deleteAll());
    }

    @Test
    void findAll_ShouldReturnAllTags() {
        when(repository.findAll()).thenReturn(List.of(tag));
        when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        List<TagDTO> tags = service.findAll();

        assertNotNull(tags);
        assertEquals(1, tags.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTagsExist() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<TagDTO> tags = service.findAll();

        assertNotNull(tags);
        assertTrue(tags.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAllPageable_ShouldReturnPageOfTags() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(tag)));
        when(mapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        Page<TagDTO> tagPage = service.findAll(pageable);

        assertNotNull(tagPage);
        assertEquals(1, tagPage.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void findAllPageable_ShouldReturnEmptyPage_WhenNoTagsExist() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<TagDTO> tagPage = service.findAll(pageable);

        assertNotNull(tagPage);
        assertTrue(tagPage.isEmpty());
        verify(repository, times(1)).findAll(pageable);
    }
}
