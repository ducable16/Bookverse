package com.bookverse.controller;

import com.bookverse.dto.request.ChapterRequest;
import com.bookverse.dto.response.ChapterResponse;
import com.bookverse.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping("/create")
    public ChapterResponse create(@RequestBody ChapterRequest request) {
        return chapterService.create(request);
    }

    @PostMapping("/update/{id}")
    public ChapterResponse update(@PathVariable Long id, @RequestBody ChapterRequest request) {
        return chapterService.update(id, request);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        chapterService.delete(id);
    }

    @GetMapping("/detail/{id}")
    public ChapterResponse getById(@PathVariable Long id) {
        return chapterService.getById(id);
    }

    @GetMapping("/book/{bookId}")
    public List<ChapterResponse> getByBookId(@PathVariable Long bookId) {
        return chapterService.getByBookId(bookId);
    }

    @GetMapping("/book/{bookId}/number/{chapterNumber}")
    public ChapterResponse getByBookIdAndChapterNumber(
            @PathVariable Long bookId,
            @PathVariable Integer chapterNumber) {
        return chapterService.getByBookIdAndChapterNumber(bookId, chapterNumber);
    }
}
