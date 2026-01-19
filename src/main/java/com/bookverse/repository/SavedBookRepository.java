package com.bookverse.repository;

import com.bookverse.entity.SavedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {

    @Query("SELECT sb FROM SavedBook sb WHERE sb.user.id = :userId AND sb.isDeleted = false ORDER BY sb.savedAt DESC")
    List<SavedBook> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT sb FROM SavedBook sb WHERE sb.user.id = :userId AND sb.book.id = :bookId AND sb.isDeleted = false")
    Optional<SavedBook> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT CASE WHEN COUNT(sb) > 0 THEN true ELSE false END FROM SavedBook sb WHERE sb.user.id = :userId AND sb.book.id = :bookId AND sb.isDeleted = false")
    boolean existsByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
