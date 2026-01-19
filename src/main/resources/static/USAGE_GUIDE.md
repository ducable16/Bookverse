# Bookverse API - Quick Usage Guide

## 📦 Setup

```bash
npm install axios
```

Copy files to frontend project:
```
src/services/
├── api-types.ts
└── api-service.ts
```

`.env`:
```
REACT_APP_API_URL=http://localhost:8080
```

---

## 🚀 Usage Examples

### Authentication

```typescript
import api from '@/services/api-service';

// Login
const response = await api.auth.login({ 
  email: 'user@test.com', 
  password: 'pass123' 
});
// Token auto-stored in localStorage

// Register
const user = await api.auth.register({
  username: 'john',
  email: 'john@test.com',
  password: 'pass123'
});

// Logout
api.auth.logout();

// Check auth
if (api.auth.isAuthenticated()) {
  // User is logged in
}
```

### Get Books

```typescript
// All books
const books = await api.book.getAllBooks();

// Book by ID
const book = await api.book.getBookById(1);

// Search
const results = await api.book.searchBooks('adventure');

// By category
const categoryBooks = await api.book.getBooksByCategory(5);

// By author with pagination
const authorBooks = await api.book.getBooksByAuthor(3, { 
  page: 0, 
  size: 20 
});
```

### Comments

```typescript
// Get comments
const comments = await api.comment.getBookComments(bookId);

// Add comment
await api.comment.createComment(bookId, {
  content: 'Great book!',
  bookId: bookId,
  parentId: null // or commentId for reply
});

// Update comment
await api.comment.updateComment(bookId, commentId, 'Updated text');

// Delete comment
await api.comment.deleteComment(bookId, commentId);
```

### React Component Example

```typescript
import { useState, useEffect } from 'react';
import api from '@/services/api-service';
import { BookResponse } from '@/services/api-types';

function BooksList() {
  const [books, setBooks] = useState<BookResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.book.getAllBooks()
      .then(setBooks)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      {books.map(book => (
        <div key={book.id}>
          <h3>{book.title}</h3>
          <p>by {book.author.name}</p>
        </div>
      ))}
    </div>
  );
}
```

### Error Handling

```typescript
try {
  const book = await api.book.getBookById(id);
} catch (error: any) {
  const message = error.response?.data?.message || 'Error occurred';
  console.error(message);
}
```

---

## 💾 Saved Books (Bookmarks)

### Save a Book
Add a book to user's saved collection.
```typescript
const response = await api.post('/api/saved-books/save', {
  userId: 1,
  bookId: 5
});
```

### Get User's Saved Books
```typescript
// Get all books saved by a user
const savedBooks = await api.get('/api/saved-books/user/1');
savedBooks.forEach(saved => {
  console.log(`${saved.book.title} - saved at ${saved.savedAt}`);
});
```

### Check if Book is Saved
```typescript
const response = await api.get('/api/saved-books/check?userId=1&bookId=5');
if (response.result) {
  console.log('Book is saved!');
}
```

### Unsave a Book
```typescript
// Remove from saved books (pass the savedBookId)
await api.post('/api/saved-books/unsave?savedBookId=123');
```

---

## 📖 Reading History

### Save History (Create/Update)
Upserts reading history based on user and book.
```typescript
const response = await api.post('/api/reading-history/save', {
  userId: 1,
  bookId: 5,
  lastReadChapter: 10
});
```

### Get User History
```typescript
// Get all books read by user
const history = await api.get('/api/reading-history/user/1');
```

### Get Specific Book History
```typescript
const entry = await api.get('/api/reading-history/user/1/book/5');
if (entry) {
  console.log(`Last read chapter: ${entry.lastReadChapter}`);
}
```

### Delete History
```typescript
// Delete a specific history entry
await api.post('/api/reading-history/delete/1');
```

---

## 🔑 Auth Headers

Token is automatically attached to all requests after login.

**Manually set token:**
```typescript
localStorage.setItem('auth_token', 'your-token');
```

---

## 📚 Full API Reference

See [API_ENDPOINTS.md](../API_ENDPOINTS.md) for complete endpoint documentation.
