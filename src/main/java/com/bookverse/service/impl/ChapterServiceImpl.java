package com.bookverse.service.impl;

import com.bookverse.dto.request.ChapterRequest;
import com.bookverse.dto.response.ChapterResponse;
import com.bookverse.entity.Book;
import com.bookverse.entity.Chapter;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.ChapterRepository;
import com.bookverse.service.ChapterService;
import com.bookverse.utils.ChapterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final BookRepository bookRepository;

    @Override
    public ChapterResponse create(ChapterRequest request) {
        // Validate book exists
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        // Check if chapter number already exists for this book
        if (chapterRepository.existsByBookIdAndChapterNumber(request.getBookId(), request.getChapterNumber())) {
            throw new AppException(ErrorCode.BOOK_ALREADY_EXISTS) {
            };
        }

        // Create new chapter
        Chapter chapter = new Chapter();
        chapter.setChapterNumber(request.getChapterNumber());
        chapter.setTitle(request.getTitle());
        chapter.setContent(request.getContent());
        chapter.setBook(book);

        chapterRepository.save(chapter);

        // Update book's total chapters count
        book.setTotalChapters((int) chapterRepository.countByBookId(book.getId()));
        bookRepository.save(book);

        return ChapterMapper.toResponse(chapter);
    }

    @Override
    public ChapterResponse update(Long id, ChapterRequest request) {
        // Validate chapter exists
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CHAPTER_NOT_FOUND));

        // Validate book exists
        Book newBook = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        Book oldBook = chapter.getBook();
        boolean bookChanged = !oldBook.getId().equals(newBook.getId());
        boolean chapterNumberChanged = !chapter.getChapterNumber().equals(request.getChapterNumber());

        // If chapter number is changing, check for conflicts
        if (chapterNumberChanged || bookChanged) {
            if (chapterRepository.existsByBookIdAndChapterNumber(request.getBookId(), request.getChapterNumber())) {
                throw new AppException(ErrorCode.BOOK_ALREADY_EXISTS) {
                };
            }
        }

        // Update chapter fields
        chapter.setChapterNumber(request.getChapterNumber());
        chapter.setTitle(request.getTitle());
        chapter.setContent(request.getContent());
        chapter.setBook(newBook);

        chapterRepository.save(chapter);

        // Update total chapters count for both books if book changed
        if (bookChanged) {
            oldBook.setTotalChapters((int) chapterRepository.countByBookId(oldBook.getId()));
            bookRepository.save(oldBook);
        }
        newBook.setTotalChapters((int) chapterRepository.countByBookId(newBook.getId()));
        bookRepository.save(newBook);

        return ChapterMapper.toResponse(chapter);
    }

    @Override
    public void delete(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CHAPTER_NOT_FOUND));

        Book book = chapter.getBook();

        // Soft delete
        chapter.setIsDeleted(true);
        chapter.setIsActive(false);
        chapterRepository.save(chapter);

        // Update book's total chapters count
        book.setTotalChapters((int) chapterRepository.countByBookId(book.getId()));
        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public ChapterResponse getById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CHAPTER_NOT_FOUND));
        return ChapterMapper.toResponse(chapter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChapterResponse> getByBookId(Long bookId) {
        // Validate book exists
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        return chapterRepository.findByBookId(bookId)
                .stream()
                .map(ChapterMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChapterResponse getByBookIdAndChapterNumber(Long bookId, Integer chapterNumber) {
        Chapter chapter = chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CHAPTER_NOT_FOUND));
        return ChapterMapper.toResponse(chapter);
    }
}
