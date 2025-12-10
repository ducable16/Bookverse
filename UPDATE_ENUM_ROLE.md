# 🔄 Update: Role thành Enum

## ✅ Hoàn thành

Đã cập nhật hệ thống để **Role là Enum** thay vì Entity, và **Login/Register trả về role**.

---

## 🎯 Những gì đã thay đổi

### 1️⃣ **Role Entity → Role Enum**

**TRƯỚC:**
```java
// Entity Role
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private String name;
    private String description;
}

// User entity
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "role_name")
private Role role;
```

**SAU:**
```java
// Enum Role
public enum Role {
    USER, ADMIN, DEV, TEST, BA, PM
}

// User entity
@Enumerated(EnumType.STRING)
@Column(name = "role")
private Role role;
```

---

### 2️⃣ **Login Response thêm Role**

**TRƯỚC:**
```java
@Data
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private String fullName;
}
```

**SAU:**
```java
@Data
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private String fullName;
    private Role role;  // ✨ NEW
}
```

**API Response Example:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser",
  "email": "test@example.com",
  "fullName": "Test User",
  "role": "USER"
}
```

---

### 3️⃣ **User Response thêm Role**

**TRƯỚC:**
```java
@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;
}
```

**SAU:**
```java
@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;
    private Role role;  // ✨ NEW
}
```

**API Response Example:**
```json
{
  "id": 1,
  "username": "testuser",
  "fullName": "Test User",
  "email": "test@example.com",
  "avatarUrl": "https://...",
  "role": "USER"
}
```

---

## 📝 Các file đã sửa

### ✅ Core Changes:

1. **`enums/Role.java`**
   - Thêm `USER` vào enum
   - Order: `USER, ADMIN, DEV, TEST, BA, PM`

2. **`entity/User.java`**
   - Đổi từ `@ManyToOne` sang `@Enumerated(EnumType.STRING)`
   - Column name: `role_name` → `role`
   - Import `com.bookverse.enums.Role`

3. **`dto/response/LoginResponse.java`**
   - Thêm field `private Role role;`

4. **`dto/response/UserResponse.java`**
   - Thêm field `private Role role;`

5. **`service/AuthService.java`**
   - Xóa `RoleRepository` dependency
   - Login: thêm `.role(user.getRole())`
   - Register: gán `user.setRole(Role.USER)`

6. **`service/impl/UserServiceImpl.java`**
   - `mapToResponse()` thêm `.role(user.getRole())`

7. **`utils/CustomUserDetails.java`**
   - Đổi từ `role.getName()` sang `role.name()`

### ❌ Deleted Files:

8. **`entity/Role.java`** - Xóa (dùng enum thay thế)
9. **`repository/RoleRepository.java`** - Xóa (không cần nữa)

---

## 🗄️ Database Changes

### Schema mới:

**Table `users`:**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    avatar_url VARCHAR(255),
    role VARCHAR(255),  -- String enum: 'USER', 'ADMIN', 'DEV', 'TEST', 'BA', 'PM'
    ...
);
```

### Bảng đã xóa:
- ❌ `roles` table (không cần nữa)
- ❌ `user_roles` junction table (đã xóa từ trước)

---

## 🚀 Cách sử dụng

### 1. Đăng ký User mới
```bash
POST /api/auth/register
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "id": 1,
  "username": "testuser",
  "fullName": null,
  "email": "test@example.com",
  "avatarUrl": null,
  "role": "USER"  ← Role mặc định
}
```

### 2. Login
```bash
POST /api/auth/login
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGci...",
  "username": "testuser",
  "email": "test@example.com",
  "fullName": null,
  "role": "USER"  ← Trả về role
}
```

### 3. Update Role (Manual in DB)
```sql
-- Đổi user thành ADMIN
UPDATE users SET role = 'ADMIN' WHERE username = 'testuser';

-- Các giá trị hợp lệ:
-- 'USER', 'ADMIN', 'DEV', 'TEST', 'BA', 'PM'
```

---

## 📊 Migration Script

**File: `migration_user_role.sql`** (đã cập nhật)

```sql
-- Đổi cột role_name thành role
ALTER TABLE users RENAME COLUMN role_name TO role;

-- Xóa bảng roles (không cần nữa)
DROP TABLE IF EXISTS roles;

-- Role giờ là string enum trong users.role
-- Các giá trị: USER, ADMIN, DEV, TEST, BA, PM
```

---

## 🎨 Benefits

### ✅ Ưu điểm:

1. **Đơn giản hơn:**
   - Không cần bảng `roles`
   - Không cần `RoleRepository`
   - Code ít hơn, dễ maintain

2. **Performance:**
   - Không cần JOIN với bảng roles
   - Lưu trực tiếp dưới dạng string

3. **Type Safety:**
   - Enum đảm bảo type safe trong code
   - Compile-time checking

4. **Frontend Friendly:**
   - Login/Register trả về role ngay
   - Không cần API call thêm để lấy role

5. **Flexible:**
   - Dễ thêm role mới vào enum
   - Không cần migration phức tạp

---

## 🧪 Testing

### Test 1: Register trả về role
```bash
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Expected:** Response có `"role": "USER"`

### Test 2: Login trả về role
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Expected:** Response có `"role": "USER"`

### Test 3: Database check
```sql
-- Check role được lưu dưới dạng string
SELECT username, role FROM users;
```

**Expected:**
```
username  | role
----------|------
admin     | ADMIN
user1     | USER
```

---

## ⚠️ Lưu ý quan trọng

1. **Role values:**
   - Phải là một trong: `USER`, `ADMIN`, `DEV`, `TEST`, `BA`, `PM`
   - Case sensitive (phải viết hoa)

2. **Database:**
   - Lưu dưới dạng string trong column `role`
   - Không có foreign key constraint
   - Không validate ở DB level (validate ở application level)

3. **Default role:**
   - User mới = `Role.USER`

4. **Migration:**
   - Nếu có data cũ với bảng `roles`, chạy migration script
   - Xóa bảng `roles` sau khi migrate

---

## 📋 Checklist

- [x] Role enum có USER
- [x] User entity dùng `@Enumerated(EnumType.STRING)`
- [x] LoginResponse có field role
- [x] UserResponse có field role
- [x] AuthService.login() trả về role
- [x] AuthService.register() trả về role
- [x] UserServiceImpl.mapToResponse() map role
- [x] CustomUserDetails dùng role.name()
- [x] Xóa Role entity
- [x] Xóa RoleRepository
- [x] Update migration script
- [x] No linter errors

---

## 🎉 Kết quả

✅ **Role giờ là Enum**
- Lưu dưới dạng string trong DB
- Type safe trong code

✅ **Login/Register trả về role**
- Frontend có thể hiển thị role ngay
- Không cần API call thêm

✅ **Code đơn giản hơn**
- Bớt 1 entity
- Bớt 1 repository
- Bớt 1 table trong DB

---

**Status:** ✅ **COMPLETED & TESTED**
**Date:** 2026-01-14

