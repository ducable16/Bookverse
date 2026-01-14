package com.bookverse.repository;

import com.bookverse.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByBookId(Long bookId);

    Optional<Chapter> findByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);

    boolean existsByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);

    long countByBookId(Long bookId);
}
