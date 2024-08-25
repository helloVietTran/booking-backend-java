# Use Case: Tạo Mới Danh Sách (Listing) 📌
## Mô tả:
Tạo mới một danh sách (listing) trên hệ thống.

## Yêu Cầu Đầu Vào
Đọc `operationId`: createListing
## Luồng Chính
1. Gửi yêu cầu `POST` tới `/api/listing/new-listing`.
2. Nếu hợp lệ, hệ thống sẽ tạo một danh sách mới và trả về thông tin của danh sách đó.
## Luồng Phụ
1. Trả về 400-BAD REQUEST nếu dữ liệu gửi lên không hợp lệ.
2. Trả về 500-INTERNAL SERVER ERROR nếu có lỗi từ hệ thống.



# Use Case: Xóa Nhà Cho Thuê Của Chính Người Dùng 📌
## Mô tả:
Xóa một danh sách (listing) mà chính người dùng đã tạo.

## Yêu Cầu Đầu Vào
Đọc `operationId`: deleteMyListing
## Luồng Chính
1. Gửi yêu cầu `DELETE` tới `/api/listing/my/delete/{listingId}`.
Nếu hợp lệ, hệ thống sẽ xóa danh sách (listing) được chỉ định.
## Luồng Phụ
1. Trả về 404-NOT FOUND nếu listingId không tồn tại hoặc không thuộc quyền sở hữu của người dùng.
2. Trả về 500-INTERNAL SERVER ERROR nếu có lỗi từ hệ thống.



# Use Case: Lấy Tất Cả Các Thể Loại Nhà 📌
## Mô tả:
Lấy danh sách tất cả các thể loại nhà có sẵn trên hệ thống.

## Yêu Cầu Đầu Vào
Đọc `operationId`: getAllListingType
## Luồng Chính
1. Gửi yêu cầu `GET` tới `/api/listing/listing-type`.
2. Hệ thống trả về danh sách các thể loại nhà.

## Luồng Phụ
1. Trả về 500-INTERNAL SERVER ERROR nếu có lỗi từ hệ thống.





# Use Case: Lấy Một Nhà Cho Thuê Bằng ID 📌
## Mô tả:
Lấy thông tin chi tiết của một nhà cho thuê dựa trên listingId được cung cấp.

## Yêu Cầu Đầu Vào
Đọc `operationId`: getListing
## Luồng Chính
1. Gửi yêu cầu `GET` tới `/api/listing/{listingId}`.
2. Hệ thống trả về thông tin chi tiết của nhà cho thuê tương ứng với listingId.
## Luồng Phụ
1. Trả về 404-NOT FOUND nếu listingId không tồn tại.
2. Trả về 500-INTERNAL SERVER ERROR nếu có lỗi từ hệ thống.