package com.bookverse.repository;

import com.bookverse.entity.Book;
import com.bookverse.entity.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByBookId(Long bookId);

    Optional<Chapter> findByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);

    boolean existsByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);

    long countByBookId(Long bookId);

    // Full-text search in chapter content - trả về các book có chapter match
    @Query("""
    SELECT b FROM Book b
    WHERE
        (
            :keyword IS NULL OR
            EXISTS (
                SELECT 1 FROM Chapter c
                WHERE c.book = b
                  AND (
                      LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
            )
        )
      AND (:authorId IS NULL OR b.author.id = :authorId)
      AND (
           :categoryId IS NULL OR
           EXISTS (
               SELECT 1 FROM b.categories cat
               WHERE cat.id = :categoryId
           )
      )
""")
    Page<Book> searchBooksInChapterContent(
            @Param("keyword") String keyword,
            @Param("authorId") Long authorId,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("SELECT c FROM Chapter c " +
           "WHERE c.book.id = :bookId " +
           "AND (LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY c.chapterNumber ASC")
    List<Chapter> findMatchingChapters(
        @Param("bookId") Long bookId,
        @Param("keyword") String keyword
    );
}
