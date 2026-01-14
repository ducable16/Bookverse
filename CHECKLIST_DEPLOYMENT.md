# ✅ Checklist Deployment - User Single Role

## 📋 Pre-Deployment

### 1. Code Review
- [x] **User.java** - Đổi từ `Set<Role>` sang `Role` 
- [x] **CustomUserDetails.java** - Cập nhật `getAuthorities()`
- [x] **AuthService.java** - Thêm logic gán role mặc định
- [x] **RoleRepository.java** - Repository mới đã tạo
- [x] Không có lỗi linter
- [x] Code compile thành công

### 2. Documentation
- [x] **TOM_TAT_THAY_DOI.md** - Tóm tắt ngắn gọn
- [x] **CHANGELOG_USER_ROLE.md** - Chi tiết đầy đủ
- [x] **MIGRATION_GUIDE.md** - Hướng dẫn migration
- [x] **TEST_MIGRATION.md** - Test cases
- [x] **migration_user_role.sql** - SQL script
- [x] **CHECKLIST_DEPLOYMENT.md** - File này

### 3. Database Preparation
- [ ] Backup database hiện tại
  ```bash
  pg_dump -U postgres -d bookverse > backup_$(date +%Y%m%d_%H%M%S).sql
  ```
- [ ] Kiểm tra size database
  ```sql
  SELECT pg_size_pretty(pg_database_size('bookverse'));
  ```
- [ ] Kiểm tra số lượng users hiện tại
  ```sql
  SELECT COUNT(*) FROM users;
  ```

---

## 🚀 Deployment Steps

### Option A: Development (Fresh Database)

#### Step 1: Clean build
```bash
./mvnw clean install
```
- [ ] Build successful
- [ ] No compilation errors

#### Step 2: Start application
```bash
./mvnw spring-boot:run
```
- [ ] Application starts successfully
- [ ] No exceptions in logs
- [ ] Tables created: `users`, `roles`
- [ ] Table `user_roles` KHÔNG tồn tại

#### Step 3: Test registration
```bash
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```
- [ ] Response 200 OK
- [ ] User created với role "USER"

#### Step 4: Verify database
```sql
SELECT * FROM users;
SELECT * FROM roles;
```
- [ ] User có `role_name = 'USER'`
- [ ] Role "USER" tồn tại trong bảng `roles`

---

### Option B: Production (Keep Existing Data)

#### Step 1: Pre-deployment
```bash
# 1. Backup
pg_dump -U postgres -d bookverse > backup_before_migration.sql

# 2. Verify backup
ls -lh backup_before_migration.sql
```
- [ ] Backup file created
- [ ] File size > 0

#### Step 2: Update configuration
**File:** `src/main/resources/application.properties`
```properties
# Đổi từ:
spring.jpa.hibernate.ddl-auto=create

# Sang:
spring.jpa.hibernate.ddl-auto=update
```
- [ ] Config updated

#### Step 3: Build application
```bash
./mvnw clean package -DskipTests
```
- [ ] Build successful
- [ ] JAR file created in `target/`

#### Step 4: Stop application
```bash
# Stop running instance
kill -9 $(lsof -t -i:9090)
```
- [ ] Application stopped

#### Step 5: Run migration
```bash
psql -U postgres -d bookverse -f migration_user_role.sql
```
- [ ] Script executed successfully
- [ ] No errors in output
- [ ] Check logs:
  ```
  ALTER TABLE
  UPDATE <number>
  UPDATE <number>
  INSERT 0 1
  ALTER TABLE
  DROP TABLE
  ```

#### Step 6: Verify migration
```sql
-- Check schema
\d users
-- Should see: role_name column with FK

-- Check data
SELECT username, role_name FROM users LIMIT 10;
-- All users should have role_name

-- Check old table deleted
SELECT * FROM user_roles;
-- Should error: relation "user_roles" does not exist
```
- [ ] Column `role_name` exists
- [ ] All users have role
- [ ] Table `user_roles` deleted

#### Step 7: Start new application
```bash
java -jar target/bookverse-0.0.1-SNAPSHOT.jar
# Hoặc
./mvnw spring-boot:run
```
- [ ] Application starts successfully
- [ ] No exceptions
- [ ] Hibernate validates schema OK

#### Step 8: Test existing users
```bash
# Login với user cũ
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "<existing-user-email>",
    "password": "<password>"
  }'
```
- [ ] Login successful
- [ ] JWT token received
- [ ] Token contains correct role

#### Step 9: Test new registration
```bash
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "new@example.com",
    "password": "password123"
  }'
```
- [ ] Registration successful
- [ ] User has role "USER"

---

## 🧪 Post-Deployment Testing

### 1. Database Integrity
```sql
-- Test 1: Tất cả users có role
SELECT COUNT(*) FROM users WHERE role_name IS NULL;
-- Expected: 0

-- Test 2: Foreign key constraint
SELECT COUNT(*) FROM users u 
WHERE u.role_name NOT IN (SELECT name FROM roles);
-- Expected: 0

-- Test 3: Role "USER" exists
SELECT * FROM roles WHERE name = 'USER';
-- Expected: 1 row

-- Test 4: User count unchanged
SELECT COUNT(*) FROM users;
-- Expected: Same as before migration
```
- [ ] All tests pass

### 2. API Testing
**Xem chi tiết trong:** `TEST_MIGRATION.md`

- [ ] Register new user → role "USER"
- [ ] Login existing user → success
- [ ] JWT token has correct role
- [ ] CustomUserDetails.getAuthorities() works

### 3. Application Logs
```bash
tail -f logs/spring.log
# Hoặc check console output
```
- [ ] No errors
- [ ] No warnings about missing tables
- [ ] Hibernate schema validation OK

---

## 🔄 Rollback Plan (If Needed)

### If deployment fails:

#### Step 1: Stop application
```bash
kill -9 $(lsof -t -i:9090)
```

#### Step 2: Restore database
```bash
psql -U postgres -d bookverse < backup_before_migration.sql
```

#### Step 3: Revert code
```bash
git checkout <previous-commit>
./mvnw clean install
./mvnw spring-boot:run
```

#### Step 4: Verify
- [ ] Application starts
- [ ] Old structure restored
- [ ] Users can login

---

## 📊 Success Criteria

### ✅ Deployment Successful If:

1. **Code:**
   - [ ] Application starts without errors
   - [ ] No linter errors
   - [ ] All imports resolved

2. **Database:**
   - [ ] Table `users` has column `role_name`
   - [ ] Table `user_roles` deleted
   - [ ] All users have role (no NULL)
   - [ ] Foreign key constraint active

3. **Functionality:**
   - [ ] Existing users can login
   - [ ] New users can register with role "USER"
   - [ ] JWT tokens contain correct role
   - [ ] No authentication errors

4. **Performance:**
   - [ ] No performance degradation
   - [ ] Query times similar or better
   - [ ] No memory leaks

5. **Documentation:**
   - [ ] All changes documented
   - [ ] Team notified
   - [ ] README updated (if needed)

---

## 📞 Support Contacts

### If issues occur:
1. Check logs: `logs/spring.log`
2. Review: `MIGRATION_GUIDE.md`
3. Run tests: `TEST_MIGRATION.md`
4. Contact: [Your contact info]

---

## 📝 Sign-off

### Deployment Performed By:
- **Name:** _________________
- **Date:** _________________
- **Time:** _________________
- **Environment:** [ ] Dev [ ] Staging [ ] Production
- **Status:** [ ] Success [ ] Failed [ ] Rolled Back

### Verification By:
- **Name:** _________________
- **Date:** _________________
- **Verified:** [ ] Yes [ ] No

### Notes:
_____________________________________________________________
_____________________________________________________________
_____________________________________________________________

---

**Status:** Ready for deployment ✅
**Last Updated:** 2026-01-14

