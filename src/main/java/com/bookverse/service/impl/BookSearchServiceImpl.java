package com.bookverse.service.impl;

import com.bookverse.dto.request.BookSearchRequest;
import com.bookverse.dto.response.SearchPageResponse;
import com.bookverse.dto.response.SearchResultResponse;
import com.bookverse.entity.Book;
import com.bookverse.entity.Category;
import com.bookverse.entity.Chapter;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.ChapterRepository;
import com.bookverse.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public SearchPageResponse searchByTitle(String keyword, BookSearchRequest filters) {
        Pageable pageable = createPageable(filters);
        Page<Book> bookPage = bookRepository.searchByTitleWithFilters(
            keyword,
            filters.getAuthorId(),
            filters.getCategoryId(),
            pageable
        );

        List<SearchResultResponse> results = bookPage.getContent().stream()
            .map(book -> mapToSearchResult(book, keyword, "TITLE", null))
            .collect(Collectors.toList());

        return buildPageResponse(results, bookPage, keyword);
    }

    @Override
    public SearchPageResponse searchByAuthor(String keyword, BookSearchRequest filters) {
        Pageable pageable = createPageable(filters);
        Page<Book> bookPage = bookRepository.searchByAuthorName(
            keyword,
            filters.getCategoryId(),
            pageable
        );

        List<SearchResultResponse> results = bookPage.getContent().stream()
            .map(book -> mapToSearchResult(book, keyword, "AUTHOR", null))
            .collect(Collectors.toList());

        return buildPageResponse(results, bookPage, keyword);
    }

    @Override
    public SearchPageResponse searchByCategory(Long categoryId, String keyword, BookSearchRequest filters) {
        Pageable pageable = createPageable(filters);
        Page<Book> bookPage = bookRepository.searchInCategory(
            categoryId,
            keyword,
            pageable
        );

        List<SearchResultResponse> results = bookPage.getContent().stream()
            .map(book -> mapToSearchResult(book, keyword, "CATEGORY", null))
            .collect(Collectors.toList());

        return buildPageResponse(results, bookPage, keyword != null ? keyword : "");
    }

    @Override
    public SearchPageResponse searchInContent(String keyword, BookSearchRequest filters) {
        Pageable pageable = createPageable(filters);
        Page<Book> bookPage = chapterRepository.searchBooksInChapterContent(
            keyword,
            filters.getAuthorId(),
            filters.getCategoryId(),
            pageable
        );

        List<SearchResultResponse> results = bookPage.getContent().stream()
            .map(book -> {
                // Tìm chapter có match để highlight
                List<Chapter> matchingChapters = chapterRepository.findMatchingChapters(book.getId(), keyword);
                Chapter firstMatch = matchingChapters.isEmpty() ? null : matchingChapters.get(0);
                return mapToSearchResult(book, keyword, "CONTENT", firstMatch);
            })
            .collect(Collectors.toList());

        return buildPageResponse(results, bookPage, keyword);
    }

    @Override
    public SearchPageResponse searchAll(BookSearchRequest request) {
        String keyword = request.getKeyword();
        if (keyword == null || keyword.trim().isEmpty()) {
            return SearchPageResponse.builder()
                .results(new ArrayList<>())
                .totalResults(0L)
                .currentPage(0)
                .totalPages(0)
                .query("")
                .build();
        }

        Pageable pageable = createPageable(request);
        
        // Search trong title trước (priority cao nhất)
        Page<Book> bookPage = bookRepository.searchByTitleWithFilters(
            keyword,
            request.getAuthorId(),
            request.getCategoryId(),
            pageable
        );

        List<SearchResultResponse> results = new ArrayList<>();

        // Nếu có kết quả từ title, dùng luôn
        if (!bookPage.isEmpty()) {
            results = bookPage.getContent().stream()
                .map(book -> mapToSearchResult(book, keyword, "TITLE", null))
                .collect(Collectors.toList());
        } else {
            // Nếu không có kết quả từ title, search trong content
            bookPage = chapterRepository.searchBooksInChapterContent(
                keyword,
                request.getAuthorId(),
                request.getCategoryId(),
                pageable
            );

            results = bookPage.getContent().stream()
                .map(book -> {
                    List<Chapter> matchingChapters = chapterRepository.findMatchingChapters(book.getId(), keyword);
                    Chapter firstMatch = matchingChapters.isEmpty() ? null : matchingChapters.get(0);
                    return mapToSearchResult(book, keyword, "CONTENT", firstMatch);
                })
                .collect(Collectors.toList());
        }

        return buildPageResponse(results, bookPage, keyword);
    }

    // Helper methods

    private Pageable createPageable(BookSearchRequest filters) {
        int page = filters.getPage() != null ? filters.getPage() : 0;
        int size = filters.getSize() != null ? filters.getSize() : 20;
        
        String sortBy = filters.getSortBy() != null ? filters.getSortBy() : "title";
        String sortDirection = filters.getSortDirection() != null ? filters.getSortDirection() : "ASC";

        Sort sort;
        if ("relevance".equalsIgnoreCase(sortBy)) {
            // Relevance sorting - mặc định sort by createdAt DESC
            sort = Sort.by(Sort.Direction.DESC, "createdDate");
        } else if ("createdDate".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(
                "DESC".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                "createdDate"
            );
        } else {
            // Default: sort by title
            sort = Sort.by(
                "DESC".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                "title"
            );
        }

        return PageRequest.of(page, size, sort);
    }

    private SearchResultResponse mapToSearchResult(Book book, String keyword, String matchType, Chapter matchedChapter) {
        List<String> categoryNames = book.getCategories().stream()
            .map(Category::getName)
            .collect(Collectors.toList());

        String snippet = generateSnippet(book, keyword, matchType, matchedChapter);

        SearchResultResponse.SearchResultResponseBuilder builder = SearchResultResponse.builder()
            .bookId(book.getId())
            .title(book.getTitle())
            .slug(book.getSlug())
            .coverImage(book.getCoverImage())
            .authorName(book.getAuthor() != null ? book.getAuthor().getName() : "Unknown")
            .categoryNames(categoryNames)
            .snippet(snippet)
            .matchType(matchType)
            .totalChapters(book.getTotalChapters());

        if (matchedChapter != null) {
            builder.matchedChapterId(matchedChapter.getId())
                   .matchedChapterTitle(matchedChapter.getTitle());
        }

        return builder.build();
    }

    private String generateSnippet(Book book, String keyword, String matchType, Chapter matchedChapter) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return book.getDescription() != null && book.getDescription().length() > 150
                ? book.getDescription().substring(0, 150) + "..."
                : book.getDescription();
        }

        String text = null;
        
        switch (matchType) {
            case "TITLE":
                text = book.getTitle();
                break;
            case "AUTHOR":
                text = book.getAuthor() != null ? book.getAuthor().getName() : "";
                break;
            case "CONTENT":
                if (matchedChapter != null && matchedChapter.getContent() != null) {
                    text = matchedChapter.getContent();
                } else {
                    text = book.getDescription();
                }
                break;
            default:
                text = book.getDescription();
        }

        if (text == null || text.isEmpty()) {
            return "";
        }

        // Tìm vị trí của keyword (case insensitive)
        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        int keywordIndex = lowerText.indexOf(lowerKeyword);

        if (keywordIndex == -1) {
            // Không tìm thấy keyword, trả về đầu text
            return text.length() > 150 ? text.substring(0, 150) + "..." : text;
        }

        // Extract ~150 characters xung quanh keyword
        int snippetLength = 150;
        int start = Math.max(0, keywordIndex - 50);
        int end = Math.min(text.length(), keywordIndex + keyword.length() + 100);

        String snippet = text.substring(start, end);

        // Add ellipsis
        if (start > 0) snippet = "..." + snippet;
        if (end < text.length()) snippet = snippet + "...";

        // Highlight keyword (case insensitive replacement)
        snippet = highlightKeyword(snippet, keyword);

        return snippet;
    }

    private String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null) return text;
        
        // Simple highlight: wrap với **keyword**
        // Case insensitive replace
        String pattern = "(?i)" + java.util.regex.Pattern.quote(keyword);
        return text.replaceAll(pattern, "**$0**");
    }

    private SearchPageResponse buildPageResponse(List<SearchResultResponse> results, Page<Book> bookPage, String query) {
        return SearchPageResponse.builder()
            .results(results)
            .totalResults(bookPage.getTotalElements())
            .currentPage(bookPage.getNumber())
            .totalPages(bookPage.getTotalPages())
            .query(query)
            .build();
    }
}
