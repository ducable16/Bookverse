# 👥 Admin User Management APIs Documentation

## Overview

Hệ thống Admin User Management APIs cho phép admin quản lý users với đầy đủ tính năng:
- ✅ List users với search, filter, pagination
- ✅ View user details
- ✅ Create new user
- ✅ Update user information
- ✅ Change user role
- ✅ Block/Unblock user
- ✅ Soft delete user
- ✅ User statistics

**Security:** Tất cả API endpoints yêu cầu **ADMIN role** (`@PreAuthorize("hasRole('ADMIN')")`)

---

## Authentication

Tất cả requests phải có JWT token với ADMIN role trong header:

```
Authorization: Bearer <admin-jwt-token>
```

---

## API Endpoints

### 1. Get All Users (List + Search + Filter)

**Endpoint:** `GET /api/admin/users`

**Description:** Lấy danh sách users với search, filter và pagination

**Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| keyword | String | No | null | Search trong username, email, fullName |
| role | Role | No | null | Filter theo role (USER, ADMIN) |
| isActive | Boolean | No | null | Filter theo status (true=active, false=blocked) |
| isDeleted | Boolean | No | null | Filter theo deleted status |
| sortBy | String | No | "createdDate" | Sort field: username, email, createdDate |
| sortDirection | String | No | "DESC" | ASC or DESC |
| page | Integer | No | 0 | Page number (0-based) |
| size | Integer | No | 20 | Page size |

**Example Request:**
```bash
GET /api/admin/users?keyword=john&role=USER&isActive=true&page=0&size=10
```

**Example Response:**
```json
{
  "users": [
    {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe",
      "email": "john@example.com",
      "avatarUrl": "https://...",
      "role": "USER",
      "isActive": true,
      "isDeleted": false,
      "createdDate": "2024-01-15T10:30:00",
      "updatedDate": "2024-01-20T15:45:00",
      "createdUser": "admin",
      "updatedUser": "admin"
    }
  ],
  "totalUsers": 150,
  "currentPage": 0,
  "totalPages": 15
}
```

---

### 2. Get User By ID

**Endpoint:** `GET /api/admin/users/{id}`

**Description:** Lấy chi tiết user theo ID

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Example Request:**
```bash
GET /api/admin/users/1
```

**Example Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "email": "john@example.com",
  "avatarUrl": "https://...",
  "role": "USER",
  "isActive": true,
  "isDeleted": false,
  "createdDate": "2024-01-15T10:30:00",
  "updatedDate": "2024-01-20T15:45:00",
  "createdUser": "admin",
  "updatedUser": "admin"
}
```

---

### 3. Create User

**Endpoint:** `POST /api/admin/users`

**Description:** Tạo user mới (bởi admin)

**Request Body:**
```json
{
  "username": "new_user",
  "email": "newuser@example.com",
  "password": "password123",
  "fullName": "New User",
  "role": "USER",
  "avatarUrl": "https://..."
}
```

**Validation:**
- `username`: Required, 3-50 characters
- `email`: Required, valid email format
- `password`: Required, min 6 characters
- `role`: Required, must be valid Role enum (USER, ADMIN)

**Example Request:**
```bash
POST /api/admin/users
Content-Type: application/json

{
  "username": "jane_smith",
  "email": "jane@example.com",
  "password": "securepass123",
  "fullName": "Jane Smith",
  "role": "USER"
}
```

**Example Response:**
```json
{
  "id": 101,
  "username": "jane_smith",
  "fullName": "Jane Smith",
  "email": "jane@example.com",
  "avatarUrl": null,
  "role": "USER",
  "isActive": true,
  "isDeleted": false,
  "createdDate": "2024-01-25T10:00:00",
  "updatedDate": "2024-01-25T10:00:00",
  "createdUser": "admin",
  "updatedUser": null
}
```

---

### 4. Update User

**Endpoint:** `PUT /api/admin/users/{id}`

**Description:** Cập nhật thông tin user

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Request Body:** (All fields optional)
```json
{
  "username": "updated_username",
  "email": "updated@example.com",
  "fullName": "Updated Name",
  "avatarUrl": "https://new-avatar.jpg"
}
```

**Example Request:**
```bash
PUT /api/admin/users/1
Content-Type: application/json

{
  "fullName": "John Updated Doe",
  "avatarUrl": "https://new-avatar.com/john.jpg"
}
```

**Example Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Updated Doe",
  "email": "john@example.com",
  "avatarUrl": "https://new-avatar.com/john.jpg",
  "role": "USER",
  "isActive": true,
  "isDeleted": false,
  "createdDate": "2024-01-15T10:30:00",
  "updatedDate": "2024-01-25T11:00:00",
  "createdUser": "admin",
  "updatedUser": "admin"
}
```

---

### 5. Change User Role

**Endpoint:** `PUT /api/admin/users/{id}/role`

**Description:** Thay đổi role của user

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| role | Role | Yes | New role (USER, ADMIN) |

**Example Request:**
```bash
PUT /api/admin/users/1/role?role=ADMIN
```

**Example Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "email": "john@example.com",
  "avatarUrl": "https://...",
  "role": "ADMIN",
  "isActive": true,
  "isDeleted": false,
  "createdDate": "2024-01-15T10:30:00",
  "updatedDate": "2024-01-25T12:00:00",
  "createdUser": "admin",
  "updatedUser": "admin"
}
```

---

### 6. Toggle User Status (Block/Unblock)

**Endpoint:** `PUT /api/admin/users/{id}/toggle-status`

**Description:** Block/Unblock user (toggle `isActive` field)

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Example Request:**
```bash
PUT /api/admin/users/1/toggle-status
```

**Example Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "email": "john@example.com",
  "avatarUrl": "https://...",
  "role": "USER",
  "isActive": false,
  "isDeleted": false,
  "createdDate": "2024-01-15T10:30:00",
  "updatedDate": "2024-01-25T13:00:00",
  "createdUser": "admin",
  "updatedUser": "admin"
}
```

**Note:** Nếu user đang active (isActive=true), sẽ chuyển thành blocked (isActive=false) và ngược lại.

---

### 7. Delete User (Soft Delete)

**Endpoint:** `DELETE /api/admin/users/{id}`

**Description:** Soft delete user (set `isDeleted=true`, `isActive=false`)

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Example Request:**
```bash
DELETE /api/admin/users/1
```

**Response:** `204 No Content`

**Note:** Đây là soft delete, user vẫn còn trong database nhưng không thể login hoặc sử dụng hệ thống.

---

### 8. Get User Statistics

**Endpoint:** `GET /api/admin/users/statistics`

**Description:** Lấy thống kê users

**Example Request:**
```bash
GET /api/admin/users/statistics
```

**Example Response:**
```json
{
  "totalUsers": 1000,
  "activeUsers": 850,
  "blockedUsers": 100,
  "deletedUsers": 50,
  "usersByRole": {
    "USER": 900,
    "ADMIN": 100
  }
}
```

---

## Response Fields Explained

### AdminUserResponse

| Field | Type | Description |
|-------|------|-------------|
| id | Long | User ID |
| username | String | Username (unique) |
| fullName | String | Full name |
| email | String | Email address (unique) |
| avatarUrl | String | Avatar image URL |
| role | Role | User role (USER, ADMIN) |
| isActive | Boolean | Active status (true=active, false=blocked) |
| isDeleted | Boolean | Deleted status (soft delete) |
| createdDate | LocalDateTime | Created timestamp |
| updatedDate | LocalDateTime | Last updated timestamp |
| createdUser | String | Username who created this user |
| updatedUser | String | Username who last updated this user |

---

## Error Codes

| Error Code | HTTP Status | Message | Description |
|------------|-------------|---------|-------------|
| 1004 | 409 | Username already exists | Username đã tồn tại |
| 10041 | 409 | Email already exists | Email đã tồn tại |
| 1007 | 404 | User not found | Không tìm thấy user |
| 1009 | 403 | You do not have permission | Không có quyền access (không phải ADMIN) |

---

## Search & Filter Examples

### Example 1: Search by keyword
```bash
GET /api/admin/users?keyword=john
# Tìm users có "john" trong username, email hoặc fullName
```

### Example 2: Filter by role
```bash
GET /api/admin/users?role=ADMIN
# Chỉ lấy users có role ADMIN
```

### Example 3: Filter active users
```bash
GET /api/admin/users?isActive=true
# Chỉ lấy users đang active
```

### Example 4: Filter deleted users
```bash
GET /api/admin/users?isDeleted=true
# Lấy users đã bị soft delete
```

### Example 5: Combined filters
```bash
GET /api/admin/users?keyword=john&role=USER&isActive=true&sortBy=createdDate&sortDirection=DESC&page=0&size=10
# Search "john", role USER, active only, sort by date DESC, page 0, 10 items
```

---

## Sorting Options

| Sort By | Description |
|---------|-------------|
| username | Sort by username (alphabetically) |
| email | Sort by email |
| createdDate | Sort by created date (default) |

**Sort Direction:** `ASC` (ascending) or `DESC` (descending)

---

## Testing with cURL

### 1. Get all users
```bash
curl -X GET "http://localhost:9090/api/admin/users" \
  -H "Authorization: Bearer <admin-jwt-token>"
```

### 2. Create user
```bash
curl -X POST "http://localhost:9090/api/admin/users" \
  -H "Authorization: Bearer <admin-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "role": "USER"
  }'
```

### 3. Update user
```bash
curl -X PUT "http://localhost:9090/api/admin/users/1" \
  -H "Authorization: Bearer <admin-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Updated Name"
  }'
```

### 4. Change role
```bash
curl -X PUT "http://localhost:9090/api/admin/users/1/role?role=ADMIN" \
  -H "Authorization: Bearer <admin-jwt-token>"
```

### 5. Block/Unblock user
```bash
curl -X PUT "http://localhost:9090/api/admin/users/1/toggle-status" \
  -H "Authorization: Bearer <admin-jwt-token>"
```

### 6. Delete user
```bash
curl -X DELETE "http://localhost:9090/api/admin/users/1" \
  -H "Authorization: Bearer <admin-jwt-token>"
```

### 7. Get statistics
```bash
curl -X GET "http://localhost:9090/api/admin/users/statistics" \
  -H "Authorization: Bearer <admin-jwt-token>"
```

---

## Security Notes

1. **Authentication Required:** Mọi request phải có valid JWT token
2. **Authorization:** Chỉ users có ADMIN role mới access được
3. **Password Hashing:** Password được hash với BCrypt trước khi lưu
4. **Soft Delete:** Delete không xóa data khỏi database, chỉ set flag
5. **Audit Trail:** Mọi thay đổi đều được track với createdUser/updatedUser

---

## Best Practices

1. **Search Optimization:** Use pagination để tránh load quá nhiều data
2. **Filter Usage:** Kết hợp filters để giảm kết quả trả về
3. **Role Management:** Cẩn thận khi thay đổi role, đặc biệt là ADMIN role
4. **Delete với caution:** Soft delete an toàn hơn hard delete
5. **Statistics Caching:** Có thể cache statistics để improve performance

---

## Frontend Integration Example

```javascript
// React example
const AdminUserService = {
  // Get all users
  getAllUsers: async (filters) => {
    const params = new URLSearchParams(filters);
    const response = await fetch(
      `http://localhost:9090/api/admin/users?${params}`,
      {
        headers: {
          'Authorization': `Bearer ${getAdminToken()}`
        }
      }
    );
    return response.json();
  },

  // Create user
  createUser: async (userData) => {
    const response = await fetch('http://localhost:9090/api/admin/users', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${getAdminToken()}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userData)
    });
    return response.json();
  },

  // Toggle status
  toggleUserStatus: async (userId) => {
    const response = await fetch(
      `http://localhost:9090/api/admin/users/${userId}/toggle-status`,
      {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${getAdminToken()}`
        }
      }
    );
    return response.json();
  }
};
```

---

## Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/admin/users` | GET | List users |
| `/api/admin/users/{id}` | GET | Get user details |
| `/api/admin/users` | POST | Create user |
| `/api/admin/users/{id}` | PUT | Update user |
| `/api/admin/users/{id}/role` | PUT | Change role |
| `/api/admin/users/{id}/toggle-status` | PUT | Block/Unblock |
| `/api/admin/users/{id}` | DELETE | Soft delete |
| `/api/admin/users/statistics` | GET | Get statistics |

**Status:** ✅ Ready to use  
**Date:** 2026-01-19  
**Security:** ADMIN role required
