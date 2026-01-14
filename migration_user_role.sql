-- Migration script: Chuyển từ many-to-many sang one-to-one với Enum
-- Role giờ là Enum (USER, ADMIN, DEV, TEST, BA, PM) lưu dưới dạng string
-- Chạy script này TRƯỚC KHI khởi động lại ứng dụng với code mới

-- Bước 1: Thêm cột role vào bảng users (nếu chưa có)
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(255);

-- Bước 2: Migrate dữ liệu từ bảng user_roles sang cột role
-- Lấy role đầu tiên của mỗi user (nếu có nhiều role)
UPDATE users u
SET role = (
    SELECT ur.role_name 
    FROM user_roles ur 
    WHERE ur.user_id = u.id 
    LIMIT 1
)
WHERE EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id
);

-- Bước 3: Set role mặc định là 'USER' cho những user không có role
UPDATE users
SET role = 'USER'
WHERE role IS NULL;

-- Bước 4: Xóa bảng user_roles (không cần thiết nữa)
DROP TABLE IF EXISTS user_roles;

-- Bước 5: Xóa bảng roles (không cần nữa vì dùng enum)
DROP TABLE IF EXISTS roles;

-- Script hoàn thành!
-- Role giờ được lưu dưới dạng string enum trong cột users.role
-- Các giá trị hợp lệ: USER, ADMIN, DEV, TEST, BA, PM

