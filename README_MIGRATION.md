# 📚 User Single Role Migration - Documentation Index

## 🎯 Mục đích
Thay đổi hệ thống User-Role từ **Many-to-Many** sang **Many-to-One**  
→ Mỗi User chỉ có **1 Role duy nhất**

---

## 📖 Tài liệu

### 🇻🇳 Tiếng Việt (Quick Start)
| File | Mô tả | Dành cho |
|------|-------|----------|
| **[TOM_TAT_THAY_DOI.md](TOM_TAT_THAY_DOI.md)** | Tóm tắt ngắn gọn, dễ hiểu | 👤 Mọi người |

### 📋 Technical Documentation
| File | Mô tả | Dành cho |
|------|-------|----------|
| **[CHANGELOG_USER_ROLE.md](CHANGELOG_USER_ROLE.md)** | Chi tiết đầy đủ về thay đổi | 👨‍💻 Developers |
| **[MIGRATION_GUIDE.md](MIGRATION_GUIDE.md)** | Hướng dẫn migration từng bước | 🔧 DevOps/DBA |
| **[TEST_MIGRATION.md](TEST_MIGRATION.md)** | Test cases và validation | 🧪 QA/Testers |
| **[CHECKLIST_DEPLOYMENT.md](CHECKLIST_DEPLOYMENT.md)** | Checklist deploy production | 🚀 Release Manager |

### 💾 Database
| File | Mô tả | Dành cho |
|------|-------|----------|
| **[migration_user_role.sql](migration_user_role.sql)** | SQL script migration | 💾 DBA |

---

## 🚀 Quick Start

### Tôi là Developer, tôi cần làm gì?

**Bước 1:** Đọc tóm tắt
```bash
📄 Đọc: TOM_TAT_THAY_DOI.md
```

**Bước 2:** Review code changes
```bash
📄 Đọc: CHANGELOG_USER_ROLE.md
```

**Bước 3:** Pull latest code
```bash
git pull origin main
```

**Bước 4:** Chạy thử
```bash
./mvnw spring-boot:run
```

---

### Tôi là DevOps/DBA, tôi cần deploy?

**Bước 1:** Đọc hướng dẫn migration
```bash
📄 Đọc: MIGRATION_GUIDE.md
```

**Bước 2:** Follow checklist
```bash
📄 Làm theo: CHECKLIST_DEPLOYMENT.md
```

**Bước 3:** Backup database
```bash
pg_dump -U postgres -d bookverse > backup.sql
```

**Bước 4:** Run migration
```bash
psql -U postgres -d bookverse -f migration_user_role.sql
```

**Bước 5:** Deploy new code
```bash
./mvnw clean package
java -jar target/bookverse-0.0.1-SNAPSHOT.jar
```

---

### Tôi là QA/Tester, tôi cần test gì?

**Bước 1:** Đọc test cases
```bash
📄 Đọc: TEST_MIGRATION.md
```

**Bước 2:** Run manual tests
- Test registration → role "USER"
- Test login → success
- Test JWT token

**Bước 3:** Verify database
```sql
SELECT * FROM users;
SELECT * FROM roles;
```

---

## 🔍 Tìm thông tin nhanh

### ❓ Câu hỏi thường gặp

**Q: Thay đổi này ảnh hưởng gì đến user hiện tại?**  
A: Xem `MIGRATION_GUIDE.md` → Section "Option 2"

**Q: Role mặc định là gì?**  
A: "USER" - Xem `TOM_TAT_THAY_DOI.md` → Section "Tính năng mới"

**Q: Làm sao để rollback?**  
A: Xem `CHECKLIST_DEPLOYMENT.md` → Section "Rollback Plan"

**Q: Cần test những gì?**  
A: Xem `TEST_MIGRATION.md` → Section "Checklist hoàn thành"

**Q: Code thay đổi ở đâu?**  
A: Xem `CHANGELOG_USER_ROLE.md` → Section "Các file đã thay đổi"

**Q: Database schema thay đổi thế nào?**  
A: Xem `CHANGELOG_USER_ROLE.md` → Section "Database Schema Changes"

---

## 📊 Tóm tắt thay đổi

### Before (Many-to-Many):
```java
@ManyToMany
private Set<Role> roles;  // Nhiều roles
```

### After (Many-to-One):
```java
@ManyToOne
private Role role;  // Chỉ 1 role ✅
```

### Database:
- ❌ Table `user_roles` (deleted)
- ✅ Column `users.role_name` (new)
- ✅ Foreign key to `roles.name`

---

## 🎯 Các file code đã sửa

### Core Changes:
1. `src/main/java/com/bookverse/entity/User.java`
2. `src/main/java/com/bookverse/utils/CustomUserDetails.java`
3. `src/main/java/com/bookverse/service/AuthService.java`

### New File:
4. `src/main/java/com/bookverse/repository/RoleRepository.java`

**Chi tiết:** Xem `CHANGELOG_USER_ROLE.md`

---

## ⚠️ Lưu ý quan trọng

1. **Breaking Change** - Không backward compatible
2. **Backup Required** - Nhớ backup database trước khi deploy
3. **Migration Script** - Phải chạy nếu có data cũ
4. **Default Role** - User mới = role "USER"
5. **Testing** - Phải test kỹ trước khi deploy production

---

## 📞 Support

### Nếu gặp vấn đề:

1. **Lỗi khi build:**
   - Check Java version: `java -version` (cần Java 17+)
   - Clean và rebuild: `./mvnw clean install`

2. **Lỗi database:**
   - Check connection: `psql -U postgres -d bookverse`
   - Review migration logs
   - Xem `MIGRATION_GUIDE.md`

3. **Lỗi runtime:**
   - Check application logs
   - Verify database schema
   - Run tests từ `TEST_MIGRATION.md`

4. **Cần rollback:**
   - Follow `CHECKLIST_DEPLOYMENT.md` → "Rollback Plan"
   - Restore từ backup

---

## 📈 Status

| Item | Status |
|------|--------|
| Code Changes | ✅ Complete |
| Documentation | ✅ Complete |
| Migration Script | ✅ Complete |
| Test Cases | ✅ Complete |
| Review | ⏳ Pending |
| Deployment | ⏳ Pending |

---

## 👥 Team

- **Developer:** [Tên của bạn]
- **Date:** 2026-01-14
- **Version:** 1.0
- **Status:** Ready for Review

---

## 📝 Workflow đề xuất

### 1. Development (Local)
```
TOM_TAT_THAY_DOI.md → Code Review → Test Local → ✅
```

### 2. Staging Deployment
```
MIGRATION_GUIDE.md → Backup → Run Migration → Deploy → Test → ✅
```

### 3. Production Deployment
```
CHECKLIST_DEPLOYMENT.md → Follow all steps → Sign-off → ✅
```

---

**🎉 Bắt đầu từ:** [TOM_TAT_THAY_DOI.md](TOM_TAT_THAY_DOI.md) (Tiếng Việt)  
**📚 Chi tiết kỹ thuật:** [CHANGELOG_USER_ROLE.md](CHANGELOG_USER_ROLE.md)

