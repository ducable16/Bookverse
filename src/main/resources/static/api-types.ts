/**
 * TypeScript API Types for Bookverse
 */

// ============ Common ============
export interface ApiResponse<T = any> {
    code: number;
    message: string;
    data?: T;
}

export interface PaginationParams {
    page?: number;
    size?: number;
}

// ============ Auth ============
export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    username: string;
    email: string;
    fullName: string;
}

export interface UserRegisterRequest {
    username: string;
    email: string;
    password: string;
}

// ============ User ============
export interface UserResponse {
    id: number;
    username: string;
    fullName: string | null;
    email: string;
    avatarUrl: string | null;
}

export interface UserUpdateRequest {
    username?: string;
    fullName?: string;
    email?: string;
    password?: string;
    avatarUrl?: string;
}

// ============ Author ============
export interface AuthorResponse {
    id: number;
    name: string;
    biography: string;
    avatarUrl: string | null;
}

export interface AuthorRequest {
    name: string;
    biography: string;
    avatarUrl?: string;
}

// ============ Category ============
export interface CategoryResponse {
    id: number;
    name: string;
    slug: string;
}

export interface CategoryRequest {
    name: string;
}

// ============ Book ============
export interface BookResponse {
    id: number;
    title: string;
    slug: string;
    coverImage: string;
    description: string;
    totalChapters: number;
    author: AuthorResponse;
    categories: CategoryResponse[];
}

export interface BookRequest {
    title: string;
    coverImage: string;
    description: string;
    totalChapters: number;
    authorId: number;
    categoryIds: number[];
}

// ============ Chapter ============
export interface ChapterResponse {
    id: number;
    chapterNumber: number;
    title: string;
    content: string;
}

export interface ChapterRequest {
    chapterNumber: number;
    title: string;
    content: string;
    bookId: number;
}

// ============ Comment ============
export interface CommentResponse {
    id: number;
    userId: number;
    username: string;
    content: string;
    createdDate: string;
    replies: CommentResponse[];
}

export interface CommentCreateRequest {
    content: string;
    bookId: number;
    parentId?: number;
}

// ============ Reading History ============
export interface ReadingHistoryResponse {
    id: number;
    user: UserResponse;
    book: BookResponse;
    lastReadChapter: number;
    lastReadTime: string;
}

export interface ReadingHistoryRequest {
    userId: number;
    bookId: number;
    lastReadChapter: number;
}
