# Use Case: Đăng Ký Người Dùng 📌
## Mô tả: 
Đăng kí mới người dùng
## Yêu Cầu Đầu Vào
-  Đọc `operationId`:  createUser 

## Luồng Chính
1. Người dùng nhập email và mật khẩu.
2. Gửi yêu cầu `POST` tới `api/identity/users/registration`.
3. Nhận phản hồi từ API.
4. Server gửi email chào mừng tới người dùng.
 
## Luồng phụ
1. Email người dùng nhập vào đã được đăng kí rồi
2. Trả lỗi 400 - bad request

# Use Case: Đổi Mật Khẩu 📌
## Mô tả: 
Người dùng muốn đổi mật khẩu
## Yêu Cầu Đầu Vào
- Đọc `operationId`:  resetPassword, changePassword 

## Luồng Chính
1. Người dùng nhập nhấn nút `Đổi mật khẩu`.
2. Gửi yêu cầu `POST` tới `api/identity/users/reset-password`(không cần gửi kèm dữ liệu gì vì lấy từ token rồi).
3. Server gửi email chứa link để reset mật khẩu.
4. Người dùng ấn link trong email sẽ redirect tới trang đổi mật khẩu có ô `nhập mật khẩu mới` và `nhập lại mật khẩu mới` có uri `http:/localhost3000/change-password?token=abc`.(tạo trang đổi mật khẩu)
5. Lấy token từ query name `token` kèm với `mật khẩu mới`, `nhập lại mật khẩu mới` từ ô input.
6.  Gửi yêu cầu `POST` tới `api/identity/users/change-password` với dữ liệu vừa lấy.

## Luồng phụ
1. Trường hợp token hết hạn sẽ trả về lỗi 401-UNAUTHORIZED


# Use Case: Lấy Một Người Dùng Bằng Id 📌
## Mô tả: 
Lấy một người dùng bằng id
## Yêu Cầu Đầu Vào
- **Endpoint**: `POST api/identity/users/{userId}`
-  Đọc `operationId`:  getOneUser 

## Luồng Chính
1. Người dùng nhập email và mật khẩu.
2. Gửi yêu cầu `GET` tới `api/identity/users/{userId}`.
3. Nhận phản hồi từ API.
 
## Luồng phụ
1. Không tìm thấy, trả về lỗi 404-NOTFOUND

# Use Case: Lấy Thông Tin Bản Thân 📌
## Mô tả: 
Lấy thông tin bản thân
## Yêu Cầu Đầu Vào
-  Đọc `operationId`:  getMyInfo 

## Luồng Chính
1. Gửi yêu cầu `GET` tới `api/identity/users/my-info`.
2. Nhận phản hồi từ API.
 
## Luồng phụ
1. Trả về lỗi 404-NOTFOUND nếu không tìm thấy

# Use Case: Xóa account 📌
## Mô tả: 
Lấy thông tin bản thân
## Yêu Cầu Đầu Vào
-  Đọc `operationId`:  getMyInfo 

## Luồng Chính
1. Gửi yêu cầu `DELETE` tới `api/identity/users/my`.
2. Nhận phản hồi từ API.
 
## Luồng phụ
1. Trả về lỗi 404-NOTFOUND nếu không tìm thấy user để xóa
