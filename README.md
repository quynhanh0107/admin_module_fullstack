# Hệ Thống Quản Lý Học Vụ (Course Management System)

Một hệ thống quản lý đào tạo toàn diện được xây dựng với kiến trúc Client-Server, áp dụng mô hình bảo mật Phân quyền linh hoạt (RBAC - Role Based Access Control). Dự án giúp số hóa quy trình quản lý khóa học, đăng ký môn học và theo dõi điểm số giữa Nhà trường, Giáo viên và Học sinh.
---

## 1. Tổng quan dự án
Hệ thống được thiết kế linh hoạt với giao diện động (Dynamic UI). Thanh menu và các tính năng sẽ tự động thay đổi dựa trên Vai trò (Role) và Quyền hạn (Action) của người đăng nhập. 

**Công nghệ sử dụng:**
* **Frontend:** Angular 17+ (Bootstrap 5).
* **Backend:** Java Spring Boot, Spring Security, JWT (JSON Web Token).
* **Database & Cache:** PostgreSQL (Lưu trữ dữ liệu chính) và Redis (Quản lý Refresh Token).

---

## 2. Đối tượng & Chức năng chính

Hệ thống phục vụ 3 nhóm đối tượng chính với các quyền hạn riêng biệt:

### Quản Trị Viên (Admin)
* **Quản lý Khóa học:** Xem, thêm, sửa, xóa thông tin các môn học/khóa học.
* **Quản lý Người dùng:** Tạo tài khoản mới, cấp vai trò (Role) cho Giáo viên, Học sinh.
* **Quản lý Phân quyền:** Tạo Vai trò mới, tùy chỉnh các quyền (Action) chi tiết cho từng Vai trò (VD: `VIEW_COURSE`, `INPUT_GRADES`, `REGISTER_DEGREE`...).

### Giáo Viên (Teacher)
* **Xem lịch phân công:** Xem danh sách các môn học/lớp học mình đang phụ trách.
* **Nhập điểm:** Xem danh sách học sinh trong lớp và thực hiện nhập điểm hàng loạt nhanh chóng.

### Học Sinh (Student)
* **Đăng ký môn học:** Xem danh sách các môn đang mở và thực hiện đăng ký tham gia.
* **Bảng điểm cá nhân:** Theo dõi điểm số các môn đã học, nhận biết môn chưa có điểm và xem Điểm trung bình (GPA).

---

## 3. Thông tin tài khoản Đăng nhập (Test Accounts)

Để trải nghiệm các góc nhìn khác nhau của hệ thống, vui lòng sử dụng các tài khoản đã được cấp quyền sẵn dưới đây:

| Vai trò | Tên đăng nhập (Username) | Mật khẩu (Password) | Chức năng nổi bật sẽ thấy |
| :--- | :--- | :--- | :--- |
| **Admin** | `quynhanhle` | `123456` | Toàn quyền Quản trị (Khóa học, Người dùng, Quyền) |
| **Giáo viên** | `giaovien2` | `123456` | Bảng Nhập điểm cho học sinh |
| **Học sinh** | `hocsinh1` | `123456` | Đăng ký môn học & Xem bảng điểm cá nhân |

---

## 4. Hướng dẫn Cài đặt & Chạy dự án

Để chạy dự án trên máy cá nhân, bạn cần cài đặt sẵn: `Node.js`, `Angular CLI`, `JDK (Java 17+)`, `Maven`, `MySQL` và `Redis`.

### Bước 1: Khởi động Database & Cache
1. Mở **pgAdmin4** và tạo một database mới (VD: `adminmodule_db`).
2. Khởi động **Redis Server** (Cần thiết để lưu trữ Refresh Token).

### Bước 2: Chạy Backend (Spring Boot)
1. Di chuyển vào thư mục Backend.
2. Mở file `src/main/resources/application.properties` và cấu hình lại thông tin kết nối PostgreSQL/Redis cho khớp với máy của bạn:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5433/rbac_db
   spring.datasource.username=root
   spring.datasource.password=mật_khẩu_của_bạn
   
   spring.redis.host=localhost
   spring.redis.port=6379

3. Mở Terminal tại thư mục Backend và chạy lệnh:

```bash
   ./mvnw clean spring-boot:run
```

4. Đợi Terminal báo `Started ...Application in ... seconds` (Mặc định chạy ở `http://localhost:8080`).

### Bước 3: Chạy Frontend (Angular)
1. Mở một Terminal mới, di chuyển vào thư mục Frontend.
2. Cài đặt các thư viện phụ thuộc:

```bash
   npm install
```

3. Khởi động server Frontend:

```bash
   ng serve
```

4. Mở trình duyệt web và truy cập vào: `http://localhost:4200`

### Bước 4: Trải nghiệm
Sử dụng các tài khoản Test ở Mục 3 để đăng nhập và trải nghiệm hệ thống!
