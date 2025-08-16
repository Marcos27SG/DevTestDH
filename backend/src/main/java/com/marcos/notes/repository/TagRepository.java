package com.marcos.notes.repository;

import com.marcos.notes.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<Tag> findByIdAndUserIdAndIsDeletedFalse(Long tagId, Long userId);

    Optional<Tag> findByUserIdAndTitleAndIsDeletedFalse(Long userId, String title);

    Set<Tag> findByIdInAndUserIdAndIsDeletedFalse(Set<Long> tagIds, Long userId);
}
