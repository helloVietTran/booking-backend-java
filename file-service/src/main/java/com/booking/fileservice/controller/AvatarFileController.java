package com.booking.fileservice.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.booking.fileservice.dto.ApiResponse;
import com.booking.fileservice.dto.request.AvatarFileUploadRequest;
import com.booking.fileservice.dto.response.UploadResponse;
import com.booking.fileservice.service.AvatarFileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarFileController {

    AvatarFileService avatarFileService;

    //@RestController và @Controller có biểu hiện khác nhau 
    @GetMapping("/form")
    public ModelAndView form() {
        ModelAndView mav = new ModelAndView("upload");
        return mav;
    }

    @PostMapping("/upload")
    public ApiResponse<UploadResponse> uploadFile(@ModelAttribute AvatarFileUploadRequest file) {
        return ApiResponse.<UploadResponse>builder()
                          .result(avatarFileService.uploadFile(file))
                          .build();
    }

    @DeleteMapping("/my/delete/{publicId}")
    public ApiResponse<String> deleteFile(@PathVariable String publicId) {
        avatarFileService.deleteFile(publicId);
        return ApiResponse.<String>builder().result("Delete your avatar sucessfull").build();
    }
}

