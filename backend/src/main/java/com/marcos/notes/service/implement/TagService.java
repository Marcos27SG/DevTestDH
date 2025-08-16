package com.marcos.notes.service.implement;

import com.marcos.notes.exception.DuplicateResourceException;
import com.marcos.notes.exception.ResourceNotFoundException;
import com.marcos.notes.dto.CreateTagRequestDTO;
import com.marcos.notes.dto.TagResponseDTO;
import com.marcos.notes.mapper.TagMapper;
import com.marcos.notes.model.Tag;
import com.marcos.notes.model.User;
import com.marcos.notes.repository.TagRepository;
import com.marcos.notes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TagMapper tagDtoMapperService;

    public TagResponseDTO createTag(CreateTagRequestDTO createTagRequestDto) {
        log.info("Creating tag with title: {} for user ID: {}",
                createTagRequestDto.getTitle(), createTagRequestDto.getUserId());

        User user = findUserByIdOrThrow(createTagRequestDto.getUserId());
        validateTagTitleIsUnique(createTagRequestDto.getTitle(), user.getId());

        Tag tag = buildTagFromCreateRequest(createTagRequestDto, user);
        Tag savedTag = tagRepository.save(tag);

        log.info("Tag created successfully with ID: {}", savedTag.getId());
        return tagDtoMapperService.convertToResponseDto(savedTag);
    }

    @Transactional(readOnly = true)
    public List<TagResponseDTO> getAllTagsByUserId(Long userId) {
        log.info("Fetching all tags for user ID: {}", userId);

        List<Tag> tags = tagRepository.findByUserIdAndIsDeletedFalse(userId);
        return tags.stream()
                .map(tagDtoMapperService::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteTag(Long tagId, Long userId) {
        log.info("Soft deleting tag with ID: {} for user ID: {}", tagId, userId);

        Tag tag = findTagByIdAndUserIdOrThrow(tagId, userId);
        tag.setIsDeleted(true);
        tagRepository.save(tag);

        log.info("Tag soft deleted successfully with ID: {}", tagId);
    }

    // Private helper methods
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Tag findTagByIdAndUserIdOrThrow(Long tagId, Long userId) {
        return tagRepository.findByIdAndUserIdAndIsDeletedFalse(tagId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + tagId + " for user: " + userId));
    }

    private void validateTagTitleIsUnique(String title, Long userId) {
        tagRepository.findByUserIdAndTitleAndIsDeletedFalse(userId, title)
                .ifPresent(existingTag -> {
                    throw new DuplicateResourceException("Tag with title '" + title + "' already exists for this user");
                });
    }

    private Tag buildTagFromCreateRequest(CreateTagRequestDTO createTagRequestDto, User user) {
        Tag tag = new Tag();
        tag.setTitle(createTagRequestDto.getTitle());
        tag.setUser(user);
        tag.setIsDeleted(false);
        return tag;
    }
}
