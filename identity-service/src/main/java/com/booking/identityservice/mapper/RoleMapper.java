package com.booking.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.booking.identityservice.dto.request.RoleRequest;
import com.booking.identityservice.dto.response.RoleResponse;
import com.booking.identityservice.entity.Role;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
