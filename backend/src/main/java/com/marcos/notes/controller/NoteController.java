package com.marcos.notes.controller;

import com.marcos.notes.dto.CreateNoteRequestDTO;
import com.marcos.notes.dto.NoteResponseDTO;
import com.marcos.notes.dto.UpdateNoteRequestDTO;
import com.marcos.notes.service.implement.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponseDTO> createNote( @RequestBody CreateNoteRequestDTO createNoteRequestDto) {
        log.info("REST request to create note: {}", createNoteRequestDto.getTitle());
        NoteResponseDTO createdNote = noteService.createNote(createNoteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponseDTO>> getAllNotesByUserId(@PathVariable Long userId) {
        log.info("REST request to get all notes for user: {}", userId);
        List<NoteResponseDTO> notes = noteService.getAllNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/user/{userId}/archived")
    public ResponseEntity<List<NoteResponseDTO>> getArchivedNotesByUserId(@PathVariable Long userId) {
        log.info("REST request to get archived notes for user: {}", userId);
        List<NoteResponseDTO> archivedNotes = noteService.getArchivedNotesByUserId(userId);
        return ResponseEntity.ok(archivedNotes);
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<NoteResponseDTO>> searchNotesByTitle(
            @PathVariable Long userId,
            @RequestParam String query) {
        log.info("REST request to search notes by title for user: {} with query: {}", userId, query);
        List<NoteResponseDTO> notes = noteService.searchNotesByTitle(userId, query);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/user/{userId}/tags")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByTags(
            @PathVariable Long userId,
            @RequestParam List<Long> tagIds) {
        log.info("REST request to get notes by tags for user: {} with tag IDs: {}", userId, tagIds);
        List<NoteResponseDTO> notes = noteService.getNotesByTags(userId, tagIds);
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/{noteId}/user/{userId}")
    public ResponseEntity<NoteResponseDTO> updateNote(
            @PathVariable Long noteId,
            @PathVariable Long userId,
            @RequestBody UpdateNoteRequestDTO updateNoteRequestDto) {
        log.info("REST request to update note: {} for user: {}", noteId, userId);
        NoteResponseDTO updatedNote = noteService.updateNote(noteId, userId, updateNoteRequestDto);
        return ResponseEntity.ok(updatedNote);
    }

    @PatchMapping("/{noteId}/user/{userId}/archive")
    public ResponseEntity<NoteResponseDTO> archiveNote(@PathVariable Long noteId, @PathVariable Long userId) {
        log.info("REST request to archive note: {} for user: {}", noteId, userId);
        NoteResponseDTO archivedNote = noteService.archiveNote(noteId, userId);
        return ResponseEntity.ok(archivedNote);
    }

    @PatchMapping("/{noteId}/user/{userId}/dearchive")
    public ResponseEntity<NoteResponseDTO> dearchiveNote(@PathVariable Long noteId, @PathVariable Long userId) {
        log.info("REST request to dearchive note: {} for user: {}", noteId, userId);
        NoteResponseDTO dearchivedNote = noteService.dearchiveNote(noteId, userId);
        return ResponseEntity.ok(dearchivedNote);
    }

    @DeleteMapping("/{noteId}/user/{userId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId, @PathVariable Long userId) {
        log.info("REST request to delete note: {} for user: {}", noteId, userId);
        noteService.deleteNote(noteId, userId);
        return ResponseEntity.noContent().build();
    }
}
