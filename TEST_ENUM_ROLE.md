# 🧪 Test Enum Role Implementation

## Quick Test Guide

### ✅ Test 1: Register User - Kiểm tra role mặc định

**Request:**
```bash
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "email": "test1@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "username": "testuser1",
  "fullName": null,
  "email": "test1@example.com",
  "avatarUrl": null,
  "role": "USER"  ← PHẢI có role USER
}
```

**✅ Pass nếu:** Response có field `role` và giá trị là `"USER"`

---

### ✅ Test 2: Login - Kiểm tra trả về role

**Request:**
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test1@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser1",
  "email": "test1@example.com",
  "fullName": null,
  "role": "USER"  ← PHẢI có role USER
}
```

**✅ Pass nếu:** 
- Response có field `role`
- Giá trị là `"USER"`
- Token hợp lệ

---

### ✅ Test 3: Database - Kiểm tra enum được lưu dưới dạng string

**Query:**
```sql
SELECT username, role, pg_typeof(role) FROM users WHERE username = 'testuser1';
```

**Expected Result:**
```
username  | role | pg_typeof
----------|------|----------
testuser1 | USER | text
```

**✅ Pass nếu:** 
- Column `role` có giá trị `"USER"` (string)
- Type là `text` hoặc `character varying`

---

### ✅ Test 4: Update Role - Kiểm tra enum values

**Test các giá trị hợp lệ:**
```sql
-- Test ADMIN role
UPDATE users SET role = 'ADMIN' WHERE username = 'testuser1';
SELECT role FROM users WHERE username = 'testuser1';
-- Expected: ADMIN

-- Test DEV role
UPDATE users SET role = 'DEV' WHERE username = 'testuser1';
SELECT role FROM users WHERE username = 'testuser1';
-- Expected: DEV

-- Test USER role (back to default)
UPDATE users SET role = 'USER' WHERE username = 'testuser1';
SELECT role FROM users WHERE username = 'testuser1';
-- Expected: USER
```

**✅ Pass nếu:** Tất cả update và select thành công

---

### ✅ Test 5: Login với role khác - Kiểm tra response

**Setup:**
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'testuser1';
```

**Request:**
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test1@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGci...",
  "username": "testuser1",
  "email": "test1@example.com",
  "fullName": null,
  "role": "ADMIN"  ← PHẢI là ADMIN
}
```

**✅ Pass nếu:** Response role là `"ADMIN"`

---

### ✅ Test 6: JWT Token - Kiểm tra authorities

**Decode JWT token từ login response:**

```javascript
// Sử dụng jwt.io hoặc decode manually
const token = "eyJhbGci...";
const payload = JSON.parse(atob(token.split('.')[1]));
console.log(payload);
```

**Expected Payload (không chứa role vì role không lưu trong JWT):**
```json
{
  "userId": 1,
  "email": "test1@example.com",
  "username": "testuser1",
  "iat": 1234567890,
  "exp": 1234567890
}
```

**Kiểm tra Authorities trong Spring Security:**
- `CustomUserDetails.getAuthorities()` phải trả về `[ROLE_ADMIN]` hoặc `[ROLE_USER]`

**✅ Pass nếu:** Authorities có prefix `ROLE_` + enum value

---

### ✅ Test 7: Get User - Kiểm tra UserResponse có role

**Request:**
```bash
curl -X GET http://localhost:9090/api/users/1 \
  -H "Authorization: Bearer <token>"
```

**Expected Response:**
```json
{
  "id": 1,
  "username": "testuser1",
  "fullName": null,
  "email": "test1@example.com",
  "avatarUrl": null,
  "role": "ADMIN"  ← PHẢI có role
}
```

**✅ Pass nếu:** Response có field `role`

---

### ✅ Test 8: Tables - Kiểm tra không còn bảng roles

**Query:**
```sql
-- Kiểm tra bảng roles không tồn tại
SELECT * FROM roles;
```

**Expected Result:**
```
ERROR: relation "roles" does not exist
```

**Query:**
```sql
-- Kiểm tra bảng user_roles không tồn tại
SELECT * FROM user_roles;
```

**Expected Result:**
```
ERROR: relation "user_roles" does not exist
```

**✅ Pass nếu:** Cả 2 table đều không tồn tại (error message)

---

### ✅ Test 9: Schema - Kiểm tra cấu trúc table users

**Query:**
```sql
\d users
```

**Expected Result:**
```
Column       | Type                     | Nullable
-------------|--------------------------|----------
id           | bigint                   | not null
username     | character varying(255)   | 
full_name    | character varying(255)   |
email        | character varying(255)   |
password     | character varying(255)   |
avatar_url   | character varying(255)   |
role         | character varying(255)   |  ← Column này phải có
...
```

**✅ Pass nếu:** 
- Column `role` tồn tại
- Type là `character varying` hoặc `varchar`
- Không có foreign key constraint

---

### ✅ Test 10: Application Startup

**Command:**
```bash
./mvnw spring-boot:run
```

**Expected Console Output:**
```
...
Hibernate: create table users (..., role varchar(255), ...)
...
Started BookverseApplication in X seconds
```

**✅ Pass nếu:**
- Application starts without errors
- No warnings về missing tables
- Hibernate tạo column `role` (nếu ddl-auto=create)

---

## 🎯 Full Test Checklist

Run tất cả tests và đánh dấu:

- [ ] Test 1: Register trả về role "USER"
- [ ] Test 2: Login trả về role
- [ ] Test 3: Database lưu role dưới dạng string
- [ ] Test 4: Update role thành công với các enum values
- [ ] Test 5: Login với role ADMIN trả về đúng
- [ ] Test 6: JWT authorities có ROLE_ prefix
- [ ] Test 7: Get user trả về role
- [ ] Test 8: Bảng roles và user_roles không tồn tại
- [ ] Test 9: Schema có column role
- [ ] Test 10: Application khởi động thành công

---

## 🚨 Common Issues & Solutions

### Issue 1: Role là null trong response
**Cause:** User không có role trong database  
**Solution:** 
```sql
UPDATE users SET role = 'USER' WHERE role IS NULL;
```

### Issue 2: Error "invalid input value for enum"
**Cause:** Postgres có enum type (không nên dùng)  
**Solution:** 
```sql
-- Drop enum type nếu có
DROP TYPE IF EXISTS role_enum;

-- Đảm bảo column là VARCHAR
ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(255);
```

### Issue 3: Login không trả về role
**Cause:** Code chưa update  
**Solution:** Check file `AuthService.java` line 57 có `.role(user.getRole())`

### Issue 4: Register không trả về role
**Cause:** `UserServiceImpl.mapToResponse()` chưa map role  
**Solution:** Check file có `.role(user.getRole())`

### Issue 5: Application không start
**Cause:** Còn import `entity.Role` hoặc `RoleRepository`  
**Solution:** 
```bash
# Search và xóa các import này
grep -r "import.*entity.Role" src/
grep -r "RoleRepository" src/
```

---

## 📊 Performance Check

### Query Performance:
```sql
-- Before (with JOIN)
EXPLAIN ANALYZE SELECT u.*, r.name FROM users u LEFT JOIN roles r ON u.role_id = r.id;

-- After (no JOIN)
EXPLAIN ANALYZE SELECT u.* FROM users u;
```

**✅ Pass nếu:** Query time giảm (không cần JOIN)

---

## ✅ Success Criteria

**All tests PASS nếu:**

1. ✅ Register trả về `role: "USER"`
2. ✅ Login trả về `role` của user
3. ✅ Database lưu role dưới dạng string
4. ✅ Bảng `roles` và `user_roles` không tồn tại
5. ✅ Application starts without errors
6. ✅ No linter errors
7. ✅ JWT authorities có ROLE_ prefix
8. ✅ Mọi enum value đều hoạt động (USER, ADMIN, DEV, TEST, BA, PM)

---

**Status:** Ready for Testing ✅  
**Last Updated:** 2026-01-14

