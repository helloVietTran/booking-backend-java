package com.booking.fileservice.service;

import com.booking.fileservice.dto.request.AvatarFileUploadRequest;
import com.booking.fileservice.dto.request.UpdateAvatarRequest;
import com.booking.fileservice.dto.response.UploadResponse;
import com.booking.fileservice.entity.AvatarFile;
import com.booking.fileservice.exception.AppException;
import com.booking.fileservice.exception.ErrorCode;
import com.booking.fileservice.repository.AvatarFileRepository;
import com.booking.fileservice.repository.httpclient.ProfileClient;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarFileService {

    Cloudinary cloudinary;
    AvatarFileRepository avatarFileRepository;
    ProfileClient profileClient;

    @NonFinal
    @Value("${app.folder.avatar-folder}")
    String avatarFolderName;

    List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    public UploadResponse uploadFile(AvatarFileUploadRequest request) {
        validateFileType(request.getFile());

        try {
            MultipartFile file = request.getFile();

            Map<?, ?> uploadOptions = ObjectUtils.asMap(
                "folder", avatarFolderName 
            );
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);

            AvatarFile avatarFile = AvatarFile.builder()
                                    .imgSrc(uploadResult.get("url").toString())
                                    .publicId(uploadResult.get("public_id").toString())
                                    .build();

            UpdateAvatarRequest updateAvatarRequest = UpdateAvatarRequest.builder()
                                            .imgSrc(uploadResult.get("url").toString())
                                            .userId("d1d7ef30-cc8f-4d72-bee3-22c8d3a5b52a")
                                            .build();

            profileClient.updateAvatar(updateAvatarRequest);

            avatarFileRepository.save(avatarFile);


            return UploadResponse.builder()
                                .publicId(uploadResult.get("public_id").toString())                
                                .build();
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_UPLOAD_FILE);
        }
    }

    public void deleteFile(String publicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId;
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            userId =  jwt.getClaim("id");
        }else{
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Optional<AvatarFile> avatarFileOptional= avatarFileRepository.findByUserId(userId);
        if(!avatarFileOptional.isPresent()){
            throw new AppException(ErrorCode.AVATAR_NOT_FOUND);
        }
        AvatarFile avatarFile = avatarFileOptional.get();

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            avatarFile.setImgSrc("");
            avatarFileRepository.save(avatarFile);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_UPLOAD_FILE);
        }
    }
    
    void validateFileType(MultipartFile file){
        String contentType = file.getContentType();

        if(contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType))
            throw new AppException(ErrorCode.NOT_IMAGE_FILE_TYPE);
    }

}
