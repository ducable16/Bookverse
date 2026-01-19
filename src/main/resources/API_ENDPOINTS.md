# BOOKVERSE API ENDPOINTS

Base URL: `http://localhost:8080`

---

## 🔐 Authentication

### Login
```
POST /api/auth/login
Body: { "email": "user@example.com", "password": "password123" }
Response: { "token": "...", "username": "...", "email": "...", "fullName": "..." }
```

### Register
```
POST /api/auth/register
Body: { "username": "user", "email": "email@test.com", "password": "pass123" }
Response: UserResponse
```

---

## 👤 Users

```
GET    /api/users/{id}           - Get user by ID
POST   /api/users/{id}           - Update user (Body: UserUpdateRequest)
```

---

## 📚 Books

```
POST   /api/book/create          - Create book (Admin)
POST   /api/book/update/{id}     - Update book (Admin)
POST   /api/book/delete/{id}     - Delete book (Admin)
GET    /api/book/detail/{id}     - Get book by ID
GET    /api/book/list            - Get all books
GET    /api/book/author/{authorId}?page=0&size=50  - Get books by author
GET    /api/book/category/{categoryId}             - Get books by category
GET    /api/book/search?keyword=...                - Search books
GET    /api/book/slug/{slug}     - Get book by slug
```

---

## ✍️ Authors

```
POST   /api/author/create        - Create author (Admin)
POST   /api/author/update/{id}   - Update author (Admin)
POST   /api/author/delete/{id}   - Delete author (Admin)
GET    /api/author/detail/{id}   - Get author by ID
GET    /api/author/list          - Get all authors
```

---

## 📂 Categories

```
POST   /api/category/create      - Create category (Admin)
POST   /api/category/update/{id} - Update category (Admin)
POST   /api/category/delete/{id} - Delete category (Admin)
GET    /api/category/detail/{id} - Get category by ID
GET    /api/category/list        - Get all categories
GET    /api/category/slug/{slug} - Get category by slug
```

---

## 💬 Comments

```
POST   /api/books/{bookId}/comments              - Add comment
GET    /api/books/{bookId}/comments              - Get all comments
PUT    /api/books/{bookId}/comments/{commentId}  - Update comment
DELETE /api/books/{bookId}/comments/{commentId}  - Delete comment
```

---

## 📊 Request/Response DTOs

### Auth
- **LoginRequest**: `{ email, password }`
- **UserRegisterRequest**: `{ username, email, password }`
- **LoginResponse**: `{ token, username, email, fullName }`

### User
- **UserResponse**: `{ id, username, fullName, email, avatarUrl }`
- **UserUpdateRequest**: `{ username?, fullName?, email?, password?, avatarUrl? }`

### Book
- **BookResponse**: `{ id, title, slug, coverImage, description, totalChapters, author: AuthorResponse, categories: CategoryResponse[] }`
- **BookRequest**: `{ title, coverImage, description, totalChapters, authorId, categoryIds: number[] }`

### Author
- **AuthorResponse**: `{ id, name, biography, avatarUrl }`
- **AuthorRequest**: `{ name, biography, avatarUrl }`

### Category
- **CategoryResponse**: `{ id, name, slug }`
- **CategoryRequest**: `{ name }`

### Comment
- **CommentResponse**: `{ id, userId, username, content, createdDate, replies: CommentResponse[] }`
- **CommentCreateRequest**: `{ content, bookId, parentId? }`

### Chapter
- **ChapterResponse**: `{ id, chapterNumber, title, content }`
- **ChapterRequest**: `{ chapterNumber, title, content, bookId }`

---

## 🔑 Authentication

**Header**: `Authorization: Bearer {token}`

**Public endpoints** (no auth):
- POST /api/auth/login
- POST /api/auth/register
- GET /api/book/*
- GET /api/category/*
- GET /api/author/*

**Admin only**:
- All POST/DELETE for books, authors, categories

---

## 📝 Response Wrapper

```json
{
  "code": 1000,
  "message": "Success",
  "data": { ... }
}
```

**Error Response**:
```json
{
  "code": 4xxx,
  "message": "Error message",
  "data": null
}
```
