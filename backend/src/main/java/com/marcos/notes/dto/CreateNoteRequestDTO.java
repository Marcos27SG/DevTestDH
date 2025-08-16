package com.marcos.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequestDTO {
    private String title;

    private String content;


    private Long userId;

    private Set<Long> tagIds;

    private Boolean isFavorite = false;
}
