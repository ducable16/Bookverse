package com.bookverse.repository;

import com.bookverse.entity.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    List<BookComment> findByBookIdAndParentIsNullAndIsDeletedFalseOrderByCreatedDateDesc(Long bookId);

    List<BookComment> findByParentIdAndIsDeletedFalseOrderByCreatedDateAsc(Long parentId);
}

