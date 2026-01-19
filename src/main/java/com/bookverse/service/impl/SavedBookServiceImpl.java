package com.bookverse.service.impl;

import com.bookverse.dto.request.SavedBookRequest;
import com.bookverse.dto.response.SavedBookResponse;
import com.bookverse.entity.Book;
import com.bookverse.entity.SavedBook;
import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.SavedBookRepository;
import com.bookverse.repository.UserRepository;
import com.bookverse.service.SavedBookService;
import com.bookverse.utils.SavedBookMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SavedBookServiceImpl implements SavedBookService {

    private final SavedBookRepository savedBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public SavedBookResponse saveBook(SavedBookRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Validate book exists
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        // Check if already saved
        if (savedBookRepository.existsByUserIdAndBookId(request.getUserId(), request.getBookId())) {
            throw new AppException(ErrorCode.BOOK_ALREADY_SAVED);
        }

        // Create and save
        SavedBook savedBook = new SavedBook();
        savedBook.setUser(user);
        savedBook.setBook(book);
        savedBook.setSavedAt(LocalDateTime.now());

        SavedBook saved = savedBookRepository.save(savedBook);

        return SavedBookMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void unsaveBook(Long savedBookId) {
        SavedBook savedBook = savedBookRepository.findById(savedBookId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SAVED_BOOK_NOT_FOUND));

        // Soft delete
        savedBook.setIsDeleted(true);
        savedBookRepository.save(savedBook);
    }

    @Override
    public List<SavedBookResponse> getUserSavedBooks(Long userId) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        List<SavedBook> savedBooks = savedBookRepository.findAllByUserId(userId);

        return savedBooks.stream()
                .map(SavedBookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSaved(Long userId, Long bookId) {
        return savedBookRepository.existsByUserIdAndBookId(userId, bookId);
    }
}
