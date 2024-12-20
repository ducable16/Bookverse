package com.bookverse.repository;

import com.bookverse.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Page<Book> findByAuthorId(Long authorId, Pageable pageable);

    List<Book> findByCategoriesId(Long categoryId);

    List<Book> findByTitleContainingIgnoreCase(String title);

    // Search by title with optional filters
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN b.author a " +
           "LEFT JOIN b.categories c " +
           "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "AND (:authorId IS NULL OR a.id = :authorId) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<Book> searchByTitleWithFilters(
        @Param("keyword") String keyword,
        @Param("authorId") Long authorId,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    // Search by author name
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN b.author a " +
           "LEFT JOIN b.categories c " +
           "WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<Book> searchByAuthorName(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    // Get books by category with optional keyword
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN b.categories c " +
           "WHERE c.id = :categoryId " +
           "AND (:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Book> searchInCategory(
        @Param("categoryId") Long categoryId,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}

