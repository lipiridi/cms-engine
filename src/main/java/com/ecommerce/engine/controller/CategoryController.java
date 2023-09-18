package com.ecommerce.engine.controller;

import com.ecommerce.engine.dto.request.CategoryRequestDto;
import com.ecommerce.engine.dto.response.CategoryGridResponseDto;
import com.ecommerce.engine.dto.response.CategoryResponseDto;
import com.ecommerce.engine.model.SearchRequest;
import com.ecommerce.engine.model.SearchResponse;
import com.ecommerce.engine.service.CategoryService;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/search")
    public SearchResponse<CategoryGridResponseDto> getGridPage(@Valid @RequestBody SearchRequest searchRequest) {
        return categoryService.search(searchRequest);
    }

    @GetMapping("/{id}")
    public CategoryResponseDto get(@PathVariable long id) {
        return categoryService.get(id);
    }

    @PostMapping
    public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto update(@PathVariable long id, @Valid @RequestBody CategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        categoryService.delete(id);
    }

    @DeleteMapping("/delete")
    public void deleteMany(@RequestBody Set<Long> ids) {
        categoryService.deleteMany(ids);
    }
}