# Hướng dẫn Migration: User chỉ có 1 Role duy nhất

## Thay đổi

Hệ thống đã được cập nhật để mỗi User chỉ có **1 role duy nhất** thay vì nhiều roles như trước.

### Các file đã thay đổi:

1. **User.java** - Thay đổi từ `Set<Role> roles` thành `Role role`
2. **CustomUserDetails.java** - Cập nhật `getAuthorities()` để xử lý single role
3. **AuthService.java** - Thêm logic gán role mặc định "USER" khi đăng ký
4. **RoleRepository.java** - Repository mới để quản lý roles

## Hướng dẫn Migration

### Option 1: Nếu đang ở môi trường Development với ddl-auto=create

Nếu `spring.jpa.hibernate.ddl-auto=create` trong file `application.properties`, Hibernate sẽ tự động tạo lại toàn bộ schema khi khởi động ứng dụng.

**Lưu ý:** Cách này sẽ **XÓA TOÀN BỘ DỮ LIỆU** hiện có!

### Option 2: Nếu muốn giữ lại dữ liệu hiện có

1. **Backup database trước**:
   ```bash
   pg_dump -U postgres -d bookverse > backup_before_migration.sql
   ```

2. **Thay đổi ddl-auto sang update**:
   ```properties
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Chạy migration script**:
   ```bash
   psql -U postgres -d bookverse -f migration_user_role.sql
   ```

4. **Khởi động lại ứng dụng**:
   ```bash
   ./mvnw spring-boot:run
   ```

### Option 3: Nếu đang ở Production

1. **Backup database**
2. **Chạy migration script trong môi trường staging trước**
3. **Test kỹ lưỡng**
4. **Deploy lên production**

## Cấu trúc Database mới

### Bảng `users`
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    avatar_url VARCHAR(255),
    role_name VARCHAR(255),  -- THAY ĐỔI: Cột mới
    FOREIGN KEY (role_name) REFERENCES roles(name)
);
```

### Bảng `user_roles` (ĐÃ XÓA)
Bảng junction table `user_roles` không còn được sử dụng nữa.

## Role mặc định

Khi user đăng ký mới, hệ thống sẽ tự động gán role "USER". Nếu role này chưa tồn tại, nó sẽ được tạo tự động.

## Kiểm tra sau khi Migration

1. Kiểm tra mọi user đều có role:
   ```sql
   SELECT username, role_name FROM users;
   ```

2. Kiểm tra foreign key constraint:
   ```sql
   SELECT * FROM users WHERE role_name NOT IN (SELECT name FROM roles);
   ```
   (Kết quả phải là 0 rows)

3. Test đăng nhập và đăng ký user mới

## Lưu ý quan trọng

- ⚠️ **BACKUP DATABASE** trước khi migration!
- Nếu user có nhiều roles, migration script sẽ chỉ giữ lại **role đầu tiên**
- Mọi user không có role sẽ được gán role "USER" mặc định
- Kiểm tra lại các API endpoint sử dụng roles để đảm bảo tương thích

## Rollback (nếu cần)

Nếu cần rollback về cấu trúc cũ, restore từ backup:
```bash
psql -U postgres -d bookverse < backup_before_migration.sql
```

