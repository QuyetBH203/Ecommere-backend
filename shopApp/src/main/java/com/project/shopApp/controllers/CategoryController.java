package com.project.shopApp.controllers;

import com.project.shopApp.components.LocalizationUtils;
import com.project.shopApp.dtos.CategoryDTO;
import com.project.shopApp.models.Category;
import com.project.shopApp.responses.CategoryResponse;
import com.project.shopApp.responses.UpdateCategoryResponse;
import com.project.shopApp.services.category.CategoryService;
import com.project.shopApp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;


import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //Neu tham so truyen vao la mot Object thi sao? Data Tranfer Object => DTO
    public ResponseEntity<CategoryResponse> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ){
        CategoryResponse categoryResponse=new CategoryResponse();
        if(result.hasErrors()){
            List<String> errorMessages=result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            categoryResponse.setErrors(errorMessages);
            categoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED));

            return ResponseEntity.badRequest().body(categoryResponse);

        }
        Category category=categoryService.createCategory(categoryDTO);
        categoryResponse.setCategory(category);
        categoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY));
        return ResponseEntity.ok(categoryResponse);
    }
    //Hien thi tat ca categories
    @GetMapping("")    //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }




    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ){
        UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse();
        categoryService.updateCategory(id, categoryDTO);
        updateCategoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));
        return ResponseEntity.ok(updateCategoryResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }
}
