package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.UserDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.CurrentUser;
import com.revilla.homestuff.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults(value = {
                    @SortDefault(sort = "userId", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        log.info("Invoking UserResource.getUsers method");
        List<UserDto> response = this.userService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId,
                                           @CurrentUser AuthUserDetails userDetails) {
        log.info("Invoking UserResource.getUser method");
        UserDto response = this.userService.findOne(userId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto data) {
        log.info("Invoking UserResource.saveUser method");
        UserDto response = this.userService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponseDto> updateUser(@PathVariable Long userId,
                                              @RequestBody UserDto userDto,
                                              @CurrentUser AuthUserDetails userDetails) {
        log.info("Invoking UserResource.updateUser method");
        ApiResponseDto response = this.userService.update(userId, userDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable Long userId,
                                              @CurrentUser AuthUserDetails userDetails) {
        log.info("Invoking UserResource.deleteUser method");
        ApiResponseDto response = this.userService.delete(userId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
