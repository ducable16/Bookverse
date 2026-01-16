package com.bookverse.controller;

import com.bookverse.dto.request.ReadingHistoryRequest;
import com.bookverse.dto.response.ReadingHistoryResponse;
import com.bookverse.service.ReadingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reading-history")
@RequiredArgsConstructor
public class ReadingHistoryController {

    private final ReadingHistoryService readingHistoryService;

    @PostMapping("/save")
    public ReadingHistoryResponse save(@RequestBody ReadingHistoryRequest request) {
        return readingHistoryService.save(request);
    }

    @GetMapping("/user/{userId}")
    public List<ReadingHistoryResponse> getAllByUser(@PathVariable Long userId) {
        return readingHistoryService.getAllByUser(userId);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ReadingHistoryResponse getByUserAndBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return readingHistoryService.getByUserAndBook(userId, bookId);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        readingHistoryService.delete(id);
    }
}
