package com.booking.fileservice.service;

import com.booking.fileservice.dto.request.ListingFileUploadRequest;
import com.booking.fileservice.dto.request.UpdateListingImageRequest;
import com.booking.fileservice.dto.response.UploadManyFileResponse;
import com.booking.fileservice.entity.ListingFile;
import com.booking.fileservice.exception.AppException;
import com.booking.fileservice.exception.ErrorCode;
import com.booking.fileservice.repository.ListingFileRepository;
import com.booking.fileservice.repository.httpclient.ListingClient;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingFileService {

    Cloudinary cloudinary;
    ListingFileRepository listingFileRepository;
    ListingClient listingClient;

    @NonFinal
    @Value("${app.folder.listing-folder}")
    String listingFolderName;

    List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    public UploadManyFileResponse uploadFile(ListingFileUploadRequest request) {
        try {
            for (MultipartFile file : request.getFiles() ) {
                validateFileType(file);
            }
            List<MultipartFile> files = request.getFiles();
            List<String> urlList = new ArrayList<>();
            List<String> publicIdList = new ArrayList<>();

            Map<?, ?> uploadOptions = ObjectUtils.asMap(
                "folder", listingFolderName 
            );
            
            for(MultipartFile file: files){
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
                urlList.add(uploadResult.get("url").toString());
                publicIdList.add(uploadResult.get("public_id").toString());
            }
            ListingFile listingFile = ListingFile.builder()
                                        .imgSrcs(publicIdList)
                                        .publicIds(publicIdList)
                                        .build();
            listingFileRepository.save(listingFile);
            
            UpdateListingImageRequest updateListingImageRequest = UpdateListingImageRequest
                                                .builder()
                                                .listingId("66bad64c45afae7903bcee37")
                                                .imgSrcs(urlList)
                                                .build();
            listingClient.updateListingImage(updateListingImageRequest);

            return UploadManyFileResponse.builder()
                                .publicIds(publicIdList)
                                .build();
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_UPLOAD_FILE);
        }
    }

    public void deleteFile(String imgId) {
        try {
            cloudinary.uploader().destroy(imgId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_UPLOAD_FILE);
        }
    }

    void validateFileType(MultipartFile file){
        String contentType = file.getContentType();

        if(contentType == null && !ALLOWED_IMAGE_TYPES.contains(contentType))
            throw new AppException(ErrorCode.NOT_IMAGE_FILE_TYPE);
    }
}