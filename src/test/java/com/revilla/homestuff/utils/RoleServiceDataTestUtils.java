package com.revilla.homestuff.utils;

import com.revilla.homestuff.dto.RoleDto;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.util.enums.RoleName;

import java.util.Collections;

public class RoleServiceDataTestUtils {

    public static RoleDto getMockRoleDto(Long roleID, RoleName roleName) {
        return new RoleDto(roleID, roleName, Collections.emptySet());
    }

    public static Role getMockRole(RoleName roleName) {
        return getMockRole(null, roleName);
    }

    public static Role getMockRole(Long roleId, RoleName roleName) {
        return Role.builder()
                .roleId(roleId)
                .name(roleName)
                .users(Collections.emptySet())
                .build();
    }

}
