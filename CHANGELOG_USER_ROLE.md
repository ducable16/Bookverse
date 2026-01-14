# Changelog - User Single Role Implementation

## Ngày: 2026-01-14

### 📋 Tóm tắt thay đổi
Cập nhật hệ thống để mỗi User chỉ có **1 role duy nhất** thay vì nhiều roles (many-to-many → many-to-one).

---

## 🔄 Các file đã thay đổi

### 1. Entity Layer

#### `src/main/java/com/bookverse/entity/User.java`
**Trước:**
```java
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(name = "user_roles", 
    joinColumns = @JoinColumn(name = "user_id"), 
    inverseJoinColumns = @JoinColumn(name = "role_name"))
private Set<Role> roles = new HashSet<>();
```

**Sau:**
```java
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "role_name")
private Role role;
```

**Lý do:** Đơn giản hóa model, mỗi user chỉ cần 1 role. Bỏ imports `HashSet` và `Set`.

---

### 2. Security Layer

#### `src/main/java/com/bookverse/utils/CustomUserDetails.java`
**Trước:**
```java
public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toSet());
}
```

**Sau:**
```java
public Collection<? extends GrantedAuthority> getAuthorities() {
    if (user.getRole() != null) {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
        );
    }
    return Collections.emptyList();
}
```

**Lý do:** Xử lý single role thay vì collection. Thêm null check để tránh lỗi.

---

### 3. Service Layer

#### `src/main/java/com/bookverse/service/AuthService.java`
**Thêm:**
- Import `Role` entity và `RoleRepository`
- Inject `RoleRepository` vào constructor

**Cập nhật method `register()`:**
```java
// Gán role mặc định là "USER" khi đăng ký
Role defaultRole = roleRepository.findByName("USER")
        .orElseGet(() -> {
            Role newRole = new Role("USER", "Default user role");
            return roleRepository.save(newRole);
        });

user.setRole(defaultRole);
```

**Lý do:** Tự động gán role "USER" cho user mới đăng ký. Tạo role nếu chưa tồn tại.

---

### 4. Repository Layer

#### `src/main/java/com/bookverse/repository/RoleRepository.java` ✨ NEW
```java
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
```

**Lý do:** Repository mới để quản lý Role entities.

---

## 🗄️ Database Schema Changes

### Trước (Many-to-Many):
```
users                    user_roles              roles
+----+--------+    +--------+----------+    +------+-------------+
| id | name   |    | user_id| role_name|    | name | description |
+----+--------+    +--------+----------+    +------+-------------+
| 1  | admin  |    | 1      | ADMIN    |    | ADMIN| Admin role  |
| 2  | user1  |    | 1      | USER     |    | USER | User role   |
+----+--------+    | 2      | USER     |    +------+-------------+
                   +--------+----------+
```

### Sau (Many-to-One):
```
users                              roles
+----+--------+-----------+    +------+-------------+
| id | name   | role_name |    | name | description |
+----+--------+-----------+    +------+-------------+
| 1  | admin  | ADMIN     |--->| ADMIN| Admin role  |
| 2  | user1  | USER      |--->| USER | User role   |
+----+--------+-----------+    +------+-------------+

❌ user_roles table (DELETED)
```

---

## 📝 Migration Files

### `migration_user_role.sql`
Script SQL để migrate dữ liệu từ cấu trúc cũ sang mới:
- Thêm cột `role_name` vào table `users`
- Copy role đầu tiên từ `user_roles` sang `users.role_name`
- Set role mặc định "USER" cho users không có role
- Thêm foreign key constraint
- Xóa bảng `user_roles`

### `MIGRATION_GUIDE.md`
Hướng dẫn chi tiết các bước migration cho các môi trường khác nhau (dev, staging, production).

### `TEST_MIGRATION.md`
Test cases và checklist để verify migration thành công.

---

## 🎯 Benefits

### ✅ Ưu điểm:
1. **Đơn giản hóa:** Code dễ đọc, dễ maintain hơn
2. **Performance:** Giảm số lượng JOIN queries
3. **Business Logic:** Phù hợp với yêu cầu "1 user = 1 role"
4. **Database:** Ít bảng hơn, schema đơn giản hơn
5. **Security:** Rõ ràng hơn về quyền hạn của user

### ⚠️ Trade-offs:
1. **Flexibility:** Không thể gán nhiều roles cho 1 user (theo yêu cầu)
2. **Migration:** Cần chạy migration script cho dữ liệu cũ

---

## 🧪 Testing

### Unit Tests (Recommended to add):
```java
@Test
public void testUserHasSingleRole() {
    User user = new User();
    Role role = new Role("USER", "Default role");
    user.setRole(role);
    
    assertEquals(1, userDetails.getAuthorities().size());
    assertTrue(userDetails.getAuthorities().contains(
        new SimpleGrantedAuthority("ROLE_USER")
    ));
}
```

### Integration Tests:
- Xem file `TEST_MIGRATION.md` để có danh sách đầy đủ test cases

---

## 🔐 Security Implications

- JWT tokens vẫn hoạt động bình thường
- `getAuthorities()` trả về single role thay vì collection
- Spring Security vẫn hoạt động với `@PreAuthorize`, `hasRole()`, etc.

---

## 📌 Notes

1. **Default Role:** Mọi user mới sẽ có role "USER" mặc định
2. **Role Required:** Mỗi user PHẢI có role (không được NULL do foreign key)
3. **Backward Incompatible:** Breaking change - cần migration
4. **Config:** Nếu `ddl-auto=create`, data sẽ bị xóa hết. Đổi sang `update` nếu cần giữ data

---

## 🚀 Deployment Steps

1. ✅ Review code changes
2. ✅ Backup database
3. ✅ Update `spring.jpa.hibernate.ddl-auto=update` (nếu giữ data)
4. ✅ Run migration script: `migration_user_role.sql`
5. ✅ Deploy new code
6. ✅ Run tests từ `TEST_MIGRATION.md`
7. ✅ Verify mọi thứ hoạt động
8. ✅ Monitor logs for errors

---

## 👥 Author
- **Date:** 2026-01-14
- **Ticket:** User Single Role Implementation
- **Review:** Pending

---

## 📞 Support

Nếu có vấn đề sau khi migration:
1. Check application logs
2. Verify database schema
3. Run test cases
4. Rollback nếu cần (từ backup)

---

**Status:** ✅ Ready for Deployment

