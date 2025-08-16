package com.marcos.notes.mapper;

import com.marcos.notes.dto.NoteResponseDTO;
import com.marcos.notes.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteMapper {
    private final TagMapper tagDtoMapperService;

    public NoteResponseDTO convertToResponseDto(Note note) {
        NoteResponseDTO responseDto = new NoteResponseDTO();
        responseDto.setId(note.getId());
        responseDto.setTitle(note.getTitle());
        responseDto.setContent(note.getContent());
        responseDto.setUserId(note.getUser().getId());
        responseDto.setIsArchived(note.getIsArchived());
        responseDto.setIsFavorite(note.getIsFavorite());
        responseDto.setCreatedAt(note.getCreatedAt());
        responseDto.setUpdatedAt(note.getUpdatedAt());

        if (note.getTags() != null) {
            responseDto.setTags(note.getTags().stream()
                    .map(tagDtoMapperService::convertToResponseDto)
                    .collect(Collectors.toSet()));
        }

        return responseDto;
    }
}
