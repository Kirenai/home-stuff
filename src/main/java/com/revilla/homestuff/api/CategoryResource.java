package com.revilla.homestuff.api;

import com.revilla.homestuff.dto.CategoryDto;
import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.security.AuthUserDetails;
import com.revilla.homestuff.security.CurrentUser;
import com.revilla.homestuff.service.CategoryService;
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

/**
 * CategoryResource
 *
 * @author Kirenai
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CategoryDto>> getCategories(
            @PageableDefault(size = 7)
            @SortDefault.SortDefaults(value = {
                    @SortDefault(sort = "categoryId", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        log.info("Invoking CategoryResource.getCategories method");
        List<CategoryDto> response = this.categoryService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Long categoryId,
            @CurrentUser AuthUserDetails userDetails
    ) {
        log.info("Invoking CategoryResource.getCategory method");
        CategoryDto response = this.categoryService.findOne(categoryId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto
    ) {
        log.info("Invoking CategoryResource.createCategory method");
        CategoryDto response = this.categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateUser(
            @PathVariable Long categoryId,
            @RequestBody CategoryDto categoryDto
    ) {
        log.info("Invoking CategoryResource.updateUser method");
        ApiResponseDto response = this.categoryService.update(categoryId, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
