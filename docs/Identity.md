📢 khi đăng nhập thành công sẽ trả về 2 loại token: access token và refresh token.
  - access token luôn được gửi cùng request cần xác thực như thêm sửa xóa phòng.
  - refresh token chỉ được dùng duy nhất để `POST` lên `api/identity/auth/refresh` lấy access token mới.
## Luồng chạy như sau:
1.  request số 1 + access token còn hạn -> thành công, không có vấn đề, dừng lại luồng này.
2.  request tiếp theo (đặt là số 2) + access token HẾT HẠN -> chưa dừng ngay

3.  dùng interceptor của axios để gửi thêm 1 request nữa sau khi đã thất bại, đó là gửi refresh token 
    lên `POST api/identity/auth/refresh`
4.  nếu refresh token còn sống thì server sẽ trả về access token mới

5.  lấy access token mới lưu cookie và thử lại request số 2
6.  nếu bị lỗi nữa mới chính thức thất bại



# Use Case: Đăng Nhập Người Dùng 📌
## Mô tả: 
Đăng nhập người dùng

## Yêu Cầu Đầu Vào
- Đọc `operationId`:  login 

## Luồng Chính
1. Người dùng nhập email và mật khẩu.
2. Gửi yêu cầu `POST` tới `api/identity/auth/login`.
3. Thành công sẽ trả về response chứa token và trạng thái đã xác thực

## Luồng phụ
1. Email hoặc mật khẩu sai, Trả lỗi 401 - Unauthenticated

# Use Case: Đăng Xuất 📌
## Mô tả: 
Đăng xuất
## Yêu Cầu Đầu Vào
-  Đọc `operationId`:  logout

## Luồng Chính
1. Gửi yêu cầu `POST` tới `api/identity/users/my-info`.
2. Nếu hợp lệ sẽ invalid jwt token đang dùng.
3. Thoát state đăng nhập
 
## Luồng phụ
1. Trả về 401-UNAUTHORIZED nếu token hết hạn


# Use Case: Làm Mới Access Token 📌
## Mô tả: 
Khi accessToken hết hạn gửi tới endpoint này để làm mới
## Yêu Cầu Đầu Vào
-  Đọc `operationId`:  refreshToken

## Luồng Chính
1. Gửi yêu cầu `POST` tới `api/identity/auth/refresh`.
2. Nếu hợp lệ sẽ trả về một JWT token mới.
 
## Luồng phụ
1. Trả về 401-UNAUTHORIZED nếu refresh_token không hợp lệ hoặc hết hạn.

// cần sửa
# Use Case: Đăng Nhập Bằng Đường Link 📌
## Mô tả: 
Cung cấp tính năng đăng nhập bằng đường link được gửi qua email
## Yêu Cầu Đầu Vào
-  Đọc `operationId`:  sendLinkLogin, verifyLinkLogin
## Luồng Chính
1. Gửi yêu cầu `POST` tới `api/identity/auth/send-link-login`.
2. Server trả về email chứa đường link để xác thực



