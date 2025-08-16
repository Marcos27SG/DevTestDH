package com.marcos.notes.service.implement;

import com.marcos.notes.exception.ResourceNotFoundException;
import com.marcos.notes.dto.CreateNoteRequestDTO;
import com.marcos.notes.dto.NoteResponseDTO;
import com.marcos.notes.dto.UpdateNoteRequestDTO;
import com.marcos.notes.mapper.NoteMapper;
import com.marcos.notes.model.Note;
import com.marcos.notes.model.Tag;
import com.marcos.notes.model.User;
import com.marcos.notes.repository.NoteRepository;
import com.marcos.notes.repository.TagRepository;
import com.marcos.notes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final NoteMapper noteDtoMapperService;

    public NoteResponseDTO createNote(CreateNoteRequestDTO createNoteRequestDto) {
        log.info("Creating note with title: {}", createNoteRequestDto.getTitle());

        User user = findUserByIdOrThrow(createNoteRequestDto.getUserId());
        Set<Tag> tags = findTagsByIdsAndUserId(createNoteRequestDto.getTagIds(), user.getId());

        Note note = buildNoteFromCreateRequest(createNoteRequestDto, user, tags);
        Note savedNote = noteRepository.save(note);

        log.info("Note created successfully with ID: {}", savedNote.getId());
        return noteDtoMapperService.convertToResponseDto(savedNote);
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDTO> getAllNotesByUserId(Long userId) {
        log.info("Fetching all notes for user ID: {}", userId);

        List<Note> notes = noteRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notes.stream()
                .map(noteDtoMapperService::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDTO> getArchivedNotesByUserId(Long userId) {
        log.info("Fetching archived notes for user ID: {}", userId);

        List<Note> archivedNotes = noteRepository.findByUserIdAndIsArchivedAndIsDeletedFalse(userId, true);
        return archivedNotes.stream()
                .map(noteDtoMapperService::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDTO> searchNotesByTitle(Long userId, String searchTerm) {
        log.info("Searching notes by title for user ID: {} with term: {}", userId, searchTerm);

        List<Note> notes = noteRepository.findByUserIdAndTitleContainingIgnoreCaseAndIsDeletedFalse(userId, searchTerm);
        return notes.stream()
                .map(noteDtoMapperService::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDTO> getNotesByTags(Long userId, List<Long> tagIds) {
        log.info("Fetching notes by tags for user ID: {} with tag IDs: {}", userId, tagIds);

        List<Note> notes = noteRepository.findByUserIdAndTagIds(userId, tagIds);
        return notes.stream()
                .map(noteDtoMapperService::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public NoteResponseDTO updateNote(Long noteId, Long userId, UpdateNoteRequestDTO updateNoteRequestDto) {
        log.info("Updating note with ID: {} for user ID: {}", noteId, userId);

        Note existingNote = findNoteByIdAndUserIdOrThrow(noteId, userId);
        Set<Tag> tags = findTagsByIdsAndUserId(updateNoteRequestDto.getTagIds(), userId);

        updateNoteFields(existingNote, updateNoteRequestDto, tags);
        Note updatedNote = noteRepository.save(existingNote);

        log.info("Note updated successfully with ID: {}", updatedNote.getId());
        return noteDtoMapperService.convertToResponseDto(updatedNote);
    }

    public NoteResponseDTO archiveNote(Long noteId, Long userId) {
        log.info("Archiving note with ID: {} for user ID: {}", noteId, userId);

        Note note = findNoteByIdAndUserIdOrThrow(noteId, userId);
        note.setIsArchived(true);
        Note archivedNote = noteRepository.save(note);

        log.info("Note archived successfully with ID: {}", archivedNote.getId());
        return noteDtoMapperService.convertToResponseDto(archivedNote);
    }

    public NoteResponseDTO dearchiveNote(Long noteId, Long userId) {
        log.info("Dearchiving note with ID: {} for user ID: {}", noteId, userId);

        Note note = findNoteByIdAndUserIdOrThrow(noteId, userId);
        note.setIsArchived(false);
        Note dearchivedNote = noteRepository.save(note);

        log.info("Note dearchived successfully with ID: {}", dearchivedNote.getId());
        return noteDtoMapperService.convertToResponseDto(dearchivedNote);
    }

    public void deleteNote(Long noteId, Long userId) {
        log.info("Soft deleting note with ID: {} for user ID: {}", noteId, userId);

        Note note = findNoteByIdAndUserIdOrThrow(noteId, userId);
        note.setIsDeleted(true);
        noteRepository.save(note);

        log.info("Note soft deleted successfully with ID: {}", noteId);
    }

    // Private helper methods
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Note findNoteByIdAndUserIdOrThrow(Long noteId, Long userId) {
        return noteRepository.findByIdAndUserIdAndIsDeletedFalse(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + noteId + " for user: " + userId));
    }

    private Set<Tag> findTagsByIdsAndUserId(Set<Long> tagIds, Long userId) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }
        return tagRepository.findByIdInAndUserIdAndIsDeletedFalse(tagIds, userId);
    }

    private Note buildNoteFromCreateRequest(CreateNoteRequestDTO createNoteRequestDto, User user, Set<Tag> tags) {
        Note note = new Note();
        note.setTitle(createNoteRequestDto.getTitle());
        note.setContent(createNoteRequestDto.getContent());
        note.setUser(user);
        note.setTags(tags);
        note.setIsFavorite(createNoteRequestDto.getIsFavorite());
        note.setIsArchived(false);
        note.setIsDeleted(false);
        return note;
    }

    private void updateNoteFields(Note note, UpdateNoteRequestDTO updateRequest, Set<Tag> tags) {
        if (updateRequest.getTitle() != null) {
            note.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getContent() != null) {
            note.setContent(updateRequest.getContent());
        }
        if (updateRequest.getTagIds() != null) {
            note.setTags(tags);
        }
        if (updateRequest.getIsFavorite() != null) {
            note.setIsFavorite(updateRequest.getIsFavorite());
        }
    }
}
