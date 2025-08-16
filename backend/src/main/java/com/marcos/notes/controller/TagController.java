package com.marcos.notes.controller;

import com.marcos.notes.dto.CreateTagRequestDTO;
import com.marcos.notes.dto.TagResponseDTO;
import com.marcos.notes.service.implement.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(@RequestBody CreateTagRequestDTO createTagRequestDto) {
        log.info("REST request to create tag: {}", createTagRequestDto.getTitle());
        TagResponseDTO createdTag = tagService.createTag(createTagRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TagResponseDTO>> getAllTagsByUserId(@PathVariable Long userId) {
        log.info("REST request to get all tags for user: {}", userId);
        List<TagResponseDTO> tags = tagService.getAllTagsByUserId(userId);
        return ResponseEntity.ok(tags);
    }

    @DeleteMapping("/{tagId}/user/{userId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId, @PathVariable Long userId) {
        log.info("REST request to delete tag: {} for user: {}", tagId, userId);
        tagService.deleteTag(tagId, userId);
        return ResponseEntity.noContent().build();
    }
}
