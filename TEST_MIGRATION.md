# Test Migration - User Single Role

## 1. Test đăng ký User mới

### Request:
```bash
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Expected Response:
```json
{
  "id": 1,
  "username": "testuser",
  "fullName": null,
  "email": "test@example.com",
  "avatarUrl": null
}
```

### Verify trong Database:
```sql
-- Kiểm tra user có role "USER" mặc định
SELECT u.username, u.email, u.role_name, r.description
FROM users u
LEFT JOIN roles r ON u.role_name = r.name
WHERE u.username = 'testuser';
```

Expected result:
```
username  | email              | role_name | description
----------|-------------------|-----------|------------------
testuser  | test@example.com  | USER      | Default user role
```

## 2. Test đăng nhập

### Request:
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Expected Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "test@example.com",
  "username": "testuser",
  "fullName": null
}
```

## 3. Test JWT Token Authorities

Decode JWT token và kiểm tra authorities có chứa "ROLE_USER":

```bash
# Sử dụng jwt.io hoặc command line tool
echo "<your-jwt-token>" | cut -d'.' -f2 | base64 -d
```

Hoặc kiểm tra trong code khi sử dụng token:
- `CustomUserDetails.getAuthorities()` phải trả về `[ROLE_USER]`

## 4. Test Migration từ dữ liệu cũ

### Chuẩn bị dữ liệu test (nếu có data cũ):
```sql
-- Giả sử có user với nhiều roles trong cấu trúc cũ
-- Bảng user_roles có:
-- user_id | role_name
-- --------|----------
-- 1       | ADMIN
-- 1       | USER
-- 2       | USER
```

### Sau khi chạy migration script:
```sql
-- Kiểm tra kết quả
SELECT id, username, role_name FROM users WHERE id IN (1, 2);
```

Expected:
```
id | username | role_name
---|----------|----------
1  | admin    | ADMIN      -- Giữ role đầu tiên
2  | user2    | USER
```

## 5. Test với role khác (ADMIN, TEST, etc.)

### Tạo role mới:
```sql
INSERT INTO roles (name, description) VALUES ('ADMIN', 'Administrator role');
```

### Update user thành ADMIN:
```sql
UPDATE users SET role_name = 'ADMIN' WHERE username = 'testuser';
```

### Verify authorities:
Login lại và kiểm tra token có chứa "ROLE_ADMIN"

## 6. Test Edge Cases

### 6.1. User không có role
```sql
-- Tạo user không có role (shouldn't happen with new code)
INSERT INTO users (id, username, email, password, role_name) 
VALUES (999, 'noRole', 'norole@test.com', 'hash', NULL);
```

Expected: Lỗi foreign key constraint hoặc application sẽ handle gracefully

### 6.2. Role không tồn tại
```sql
UPDATE users SET role_name = 'NONEXISTENT' WHERE id = 1;
```

Expected: Lỗi foreign key constraint

## 7. Checklist hoàn thành

- [ ] User mới đăng ký có role "USER" mặc định
- [ ] Login thành công và JWT token chứa đúng role
- [ ] CustomUserDetails.getAuthorities() trả về đúng single role
- [ ] Database không còn bảng user_roles
- [ ] Tất cả users đều có role_name không NULL
- [ ] Foreign key constraint hoạt động đúng
- [ ] Không còn reference đến Set<Role> roles trong code
- [ ] Application chạy không lỗi

## 8. Rollback Test (nếu cần)

```bash
# Restore từ backup
psql -U postgres -d bookverse < backup_before_migration.sql

# Revert code về commit trước
git checkout <previous-commit>

# Restart application
./mvnw spring-boot:run
```

## Kết luận

✅ Migration thành công nếu tất cả các test trên pass
❌ Nếu có lỗi, check logs và rollback nếu cần

