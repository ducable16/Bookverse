package com.bookverse.repository;

import com.bookverse.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

