package com.marcos.notes.mapper;

import com.marcos.notes.dto.TagResponseDTO;
import com.marcos.notes.model.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagMapper {
    public TagResponseDTO convertToResponseDto(Tag tag) {
        TagResponseDTO responseDto = new TagResponseDTO();
        responseDto.setId(tag.getId());
        responseDto.setTitle(tag.getTitle());
        responseDto.setUserId(tag.getUser().getId());
        responseDto.setCreatedAt(tag.getCreatedAt());
        return responseDto;
    }
}
