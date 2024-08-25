package com.booking.identityservice.mapper;

import org.mapstruct.Mapper;

import com.booking.identityservice.dto.request.PermissionRequest;
import com.booking.identityservice.dto.response.PermissionResponse;
import com.booking.identityservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}