package com.marcos.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Set<TagResponseDTO> tags;
    private Boolean isArchived;
    private Boolean isFavorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
