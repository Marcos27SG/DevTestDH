package com.marcos.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNoteRequestDTO {
    private String title;

    private String content;

    private Set<Long> tagIds;

    private Boolean isFavorite;
}
