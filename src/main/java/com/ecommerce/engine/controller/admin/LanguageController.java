package com.ecommerce.engine.controller.admin;

import com.ecommerce.engine.dto.admin.request.LanguageRequestDto;
import com.ecommerce.engine.dto.admin.response.LanguageResponseDto;
import com.ecommerce.engine.search.SearchRequest;
import com.ecommerce.engine.search.SearchResponse;
import com.ecommerce.engine.service.admin.LanguageService;
import jakarta.validation.Valid;
import java.util.Set;
import java.util.UUID;
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
@RequestMapping("/admin/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/search/{id}")
    public SearchResponse<LanguageResponseDto> getGridPageCache(@PathVariable UUID id) {
        return languageService.search(id, null);
    }

    @PostMapping("/search")
    public SearchResponse<LanguageResponseDto> getGridPage(@Valid @RequestBody SearchRequest searchRequest) {
        return languageService.search(null, searchRequest);
    }

    @GetMapping("/{id}")
    public LanguageResponseDto get(@PathVariable int id) {
        return languageService.get(id);
    }

    @PostMapping
    public LanguageResponseDto create(@Valid @RequestBody LanguageRequestDto requestDto) {
        return languageService.save(requestDto);
    }

    @PutMapping("/{id}")
    public LanguageResponseDto update(@PathVariable int id, @Valid @RequestBody LanguageRequestDto requestDto) {
        return languageService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        languageService.delete(id);
    }

    @DeleteMapping("/delete")
    public void deleteMany(@RequestBody Set<Integer> ids) {
        languageService.deleteMany(ids);
    }
}
