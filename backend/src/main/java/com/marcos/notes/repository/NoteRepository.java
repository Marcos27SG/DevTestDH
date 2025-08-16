package com.marcos.notes.repository;

import com.marcos.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserIdAndIsDeletedFalse(Long userId);

    List<Note> findByUserIdAndIsArchivedAndIsDeletedFalse(Long userId, Boolean isArchived);

    List<Note> findByUserIdAndTitleContainingIgnoreCaseAndIsDeletedFalse(Long userId, String title);

    @Query("SELECT n FROM Note n JOIN n.tags t WHERE n.user.id = :userId AND t.id IN :tagIds AND n.isDeleted = false")
    List<Note> findByUserIdAndTagIds(@Param("userId") Long userId, @Param("tagIds") List<Long> tagIds);

    Optional<Note> findByIdAndUserIdAndIsDeletedFalse(Long noteId, Long userId);

    @Query("SELECT DISTINCT n FROM Note n LEFT JOIN FETCH n.tags WHERE n.user.id = :userId AND n.isDeleted = false ORDER BY n.createdAt DESC")
    List<Note> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
