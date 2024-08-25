📢 api: http://localhost:8000
📢 Chú Ý:
    Có 2 cấu trúc cho một response tiêu chuẩn như sau:

        interface ApiResponse<T> {
            code: number;      📌 Chú Thích: code 1000 là thành công, tất cả các code khác là lỗi 
            message?: string;  
            result?: T;          
        }
            📢: T là schemas trong swagger, áp dụng cho đa phần các schemas, ngoại lệ được cập nhật bên dưới
            📢: khi call api thành công nhưng không cần giá trị trả về sẽ nhận một chuỗi biểu thị thành công

            📝: ví dụ khi cho schemas UserResponse: 

                interface ApiResponse<UserResponse> {
                    code: number;      
                    message?: string;  
                    result?: UserResponse;          
                }
                axios.get('http://localhost:8000/identity/my-info')
                .then((res: ApiResponse<UserResponse>) => {
                    console.log(res);
                })
            
        interface PageResponse<T> {
        currentPage: number;       📌 Chú Thích: Trang hiện tại
        totalPages: number;        📌 Chú Thích: Tổng số trang
        pageSize: number;          📌 Chú Thích: Kích thước mỗi trang
        totalElements: number;     📌 Chú Thích: Tổng số phần tử

        data: T[];                 
        }

        📢: Áp dụng cho có NHIỀU kết quả trả về schemas ListingResponse
        📢: nếu tên operationID CÓ CHỮ ByPage thì dùng cấu trúc PageResponse<T> 



