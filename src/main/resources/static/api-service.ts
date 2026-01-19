/**
 * Bookverse API Service Layer
 */

import axios, { AxiosInstance } from 'axios';
import type * as T from './api-types';

const BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Create axios instance
const api = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

// Add auth token to requests
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('auth_token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

// Handle 401 errors
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('auth_token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// ============ Auth Service ============
export const authService = {
    async login(data: T.LoginRequest): Promise<T.LoginResponse> {
        const res = await api.post<T.LoginResponse>('/api/auth/login', data);
        if (res.data.token) localStorage.setItem('auth_token', res.data.token);
        return res.data;
    },

    async register(data: T.UserRegisterRequest): Promise<T.UserResponse> {
        const res = await api.post<T.UserResponse>('/api/auth/register', data);
        return res.data;
    },

    logout() {
        localStorage.removeItem('auth_token');
    },

    isAuthenticated(): boolean {
        return !!localStorage.getItem('auth_token');
    },
};

// ============ User Service ============
export const userService = {
    async getUser(id: number): Promise<T.UserResponse> {
        const res = await api.get<T.UserResponse>(`/api/users/${id}`);
        return res.data;
    },

    async updateUser(id: number, data: T.UserUpdateRequest): Promise<T.UserResponse> {
        const res = await api.post<T.UserResponse>(`/api/users/${id}`, data);
        return res.data;
    },
};

// ============ Book Service ============
export const bookService = {
    async getAllBooks(): Promise<T.BookResponse[]> {
        const res = await api.get<T.BookResponse[]>('/api/book/list');
        return res.data;
    },

    async getBookById(id: number): Promise<T.BookResponse> {
        const res = await api.get<T.BookResponse>(`/api/book/detail/${id}`);
        return res.data;
    },

    async getBookBySlug(slug: string): Promise<T.BookResponse> {
        const res = await api.get<T.BookResponse>(`/api/book/slug/${slug}`);
        return res.data;
    },

    async searchBooks(keyword: string): Promise<T.BookResponse[]> {
        const res = await api.get<T.BookResponse[]>('/api/book/search', {
            params: { keyword },
        });
        return res.data;
    },

    async getBooksByAuthor(authorId: number, params?: T.PaginationParams): Promise<T.BookResponse[]> {
        const res = await api.get<T.BookResponse[]>(`/api/book/author/${authorId}`, { params });
        return res.data;
    },

    async getBooksByCategory(categoryId: number): Promise<T.BookResponse[]> {
        const res = await api.get<T.BookResponse[]>(`/api/book/category/${categoryId}`);
        return res.data;
    },

    async createBook(data: T.BookRequest): Promise<T.BookResponse> {
        const res = await api.post<T.BookResponse>('/api/book/create', data);
        return res.data;
    },

    async updateBook(id: number, data: T.BookRequest): Promise<T.BookResponse> {
        const res = await api.post<T.BookResponse>(`/api/book/update/${id}`, data);
        return res.data;
    },

    async deleteBook(id: number): Promise<void> {
        await api.post(`/api/book/delete/${id}`);
    },
};

// ============ Author Service ============
export const authorService = {
    async getAllAuthors(): Promise<T.AuthorResponse[]> {
        const res = await api.get<T.AuthorResponse[]>('/api/author/list');
        return res.data;
    },

    async getAuthorById(id: number): Promise<T.AuthorResponse> {
        const res = await api.get<T.AuthorResponse>(`/api/author/detail/${id}`);
        return res.data;
    },

    async createAuthor(data: T.AuthorRequest): Promise<T.AuthorResponse> {
        const res = await api.post<T.AuthorResponse>('/api/author/create', data);
        return res.data;
    },

    async updateAuthor(id: number, data: T.AuthorRequest): Promise<T.AuthorResponse> {
        const res = await api.post<T.AuthorResponse>(`/api/author/update/${id}`, data);
        return res.data;
    },

    async deleteAuthor(id: number): Promise<void> {
        await api.post(`/api/author/delete/${id}`);
    },
};

// ============ Category Service ============
export const categoryService = {
    async getAllCategories(): Promise<T.CategoryResponse[]> {
        const res = await api.get<T.CategoryResponse[]>('/api/category/list');
        return res.data;
    },

    async getCategoryById(id: number): Promise<T.CategoryResponse> {
        const res = await api.get<T.CategoryResponse>(`/api/category/detail/${id}`);
        return res.data;
    },

    async getCategoryBySlug(slug: string): Promise<T.CategoryResponse> {
        const res = await api.get<T.CategoryResponse>(`/api/category/slug/${slug}`);
        return res.data;
    },

    async createCategory(data: T.CategoryRequest): Promise<T.CategoryResponse> {
        const res = await api.post<T.CategoryResponse>('/api/category/create', data);
        return res.data;
    },

    async updateCategory(id: number, data: T.CategoryRequest): Promise<T.CategoryResponse> {
        const res = await api.post<T.CategoryResponse>(`/api/category/update/${id}`, data);
        return res.data;
    },

    async deleteCategory(id: number): Promise<void> {
        await api.post(`/api/category/delete/${id}`);
    },
};

// ============ Comment Service ============
export const commentService = {
    async getBookComments(bookId: number): Promise<T.CommentResponse[]> {
        const res = await api.get<T.CommentResponse[]>(`/api/books/${bookId}/comments`);
        return res.data;
    },

    async createComment(bookId: number, data: T.CommentCreateRequest): Promise<void> {
        await api.post(`/api/books/${bookId}/comments`, data);
    },

    async updateComment(bookId: number, commentId: number, content: string): Promise<void> {
        await api.put(`/api/books/${bookId}/comments/${commentId}`, { content });
    },

    async deleteComment(bookId: number, commentId: number): Promise<void> {
        await api.delete(`/api/books/${bookId}/comments/${commentId}`);
    },
};

// Export all services
export default {
    auth: authService,
    user: userService,
    book: bookService,
    author: authorService,
    category: categoryService,
    comment: commentService,
};
