package com.revilla.homestuff.util;

import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.entity.Consumption;
import com.revilla.homestuff.entity.Nourishment;
import com.revilla.homestuff.entity.Role;
import com.revilla.homestuff.entity.User;
import com.revilla.homestuff.exception.unauthorize.UnauthorizedPermissionException;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.util.enums.MessageAction;
import com.revilla.homestuff.util.enums.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * GeneralUtil
 *
 * @author Kirenai
 */
@Slf4j
public class GeneralUtil {

    public static <E> void validateAuthorizationPermissionOrThrow(
            @NotNull E obj,
            @NotNull AuthUserDetails userDetails,
            @NotNull MessageAction action) {
        log.info("Invoking GeneralUtil.validateAuthorizationPermissionOrThrow method");
        String errorMessage = null;
        if (obj instanceof User user) {
            if (user.getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this profile";
        }
        if (obj instanceof Nourishment nourishment) {
            if (nourishment.getUser().getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this nourishment";
        }
        if (obj instanceof Consumption consumption) {
            if (consumption.getUser().getUserId().equals(userDetails.getUserId())
                    || userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this consumption";
        }
        if (obj instanceof Role) {
            if (userDetails.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
                return;
            }
            errorMessage = "You don't have the permission to " + action.name() + " this role";
        }
        throw new UnauthorizedPermissionException(errorMessage);
    }

    public static <E> ApiResponseDto responseMessageAction(@NotNull Class<E> clazz,
                                                           @NotNull String messageAction) {
        return new ApiResponseDto(Boolean.TRUE, simpleNameClass(clazz) + " " + messageAction);
    }

    public static String simpleNameClass(@NotNull Class<?> clazzGeneric) {
        return clazzGeneric.getSimpleName();
    }

}
