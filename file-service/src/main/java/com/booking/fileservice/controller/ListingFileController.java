package com.booking.fileservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.booking.fileservice.dto.ApiResponse;
import com.booking.fileservice.dto.request.ListingFileUploadRequest;
import com.booking.fileservice.dto.response.UploadManyFileResponse;
import com.booking.fileservice.service.ListingFileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/listing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingFileController {
    ListingFileService listingFileService;

    @GetMapping("/form")
    public ModelAndView form() {
        ModelAndView mav = new ModelAndView("uploadmany");
        return mav;
    }
    
    @PostMapping("/upload")
    public ApiResponse<UploadManyFileResponse> uploadFile(@ModelAttribute ListingFileUploadRequest file) {
        return ApiResponse.<UploadManyFileResponse>builder()
                          .result(listingFileService.uploadFile(file))
                          .build();
    }

    @DeleteMapping("/my/delete/{publicId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String publicId) {
        listingFileService.deleteFile(publicId);
        return ResponseEntity.noContent().build();
    }

}
