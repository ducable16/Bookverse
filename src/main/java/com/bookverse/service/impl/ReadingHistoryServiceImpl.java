package com.bookverse.service.impl;

import com.bookverse.dto.request.ReadingHistoryRequest;
import com.bookverse.dto.response.ReadingHistoryResponse;
import com.bookverse.entity.Book;
import com.bookverse.entity.ReadingHistory;
import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.ReadingHistoryRepository;
import com.bookverse.repository.UserRepository;
import com.bookverse.service.ReadingHistoryService;
import com.bookverse.utils.ReadingHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingHistoryServiceImpl implements ReadingHistoryService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ReadingHistoryResponse save(ReadingHistoryRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        Optional<ReadingHistory> existingHistory = readingHistoryRepository
                .findByUserIdAndBookId(request.getUserId(), request.getBookId());

        ReadingHistory readingHistory;

        if (existingHistory.isPresent()) {
            readingHistory = existingHistory.get();
            readingHistory.setLastReadChapter(request.getLastReadChapter());
        } else {
            readingHistory = new ReadingHistory();
            readingHistory.setUser(user);
            readingHistory.setBook(book);
            readingHistory.setLastReadChapter(request.getLastReadChapter());
        }

        return ReadingHistoryMapper.toResponse(readingHistoryRepository.save(readingHistory));
    }

    @Override
    @Transactional(readOnly = true)
    public ReadingHistoryResponse getByUserAndBook(Long userId, Long bookId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND);
        }

        ReadingHistory readingHistory = readingHistoryRepository
                .findByUserIdAndBookId(userId, bookId)
                .orElse(null);

        if (readingHistory == null) {
            return null;
        }

        return ReadingHistoryMapper.toResponse(readingHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadingHistoryResponse> getAllByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return readingHistoryRepository.findByUserId(userId)
                .stream()
                .map(ReadingHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!readingHistoryRepository.existsById(id)) {
            throw new EntityNotFoundException(ErrorCode.HISTORY_NOT_FOUND);
        }
        readingHistoryRepository.deleteById(id);
    }
}
