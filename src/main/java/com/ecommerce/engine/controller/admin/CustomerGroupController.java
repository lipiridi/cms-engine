package com.ecommerce.engine.controller.admin;

import com.ecommerce.engine.dto.admin.grid.CustomerGroupGridDto;
import com.ecommerce.engine.dto.admin.request.CustomerGroupRequestDto;
import com.ecommerce.engine.dto.admin.response.CustomerGroupResponseDto;
import com.ecommerce.engine.search.SearchRequest;
import com.ecommerce.engine.search.SearchResponse;
import com.ecommerce.engine.service.admin.CustomerGroupService;
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
@RequestMapping("/admin/customer-groups")
@RequiredArgsConstructor
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    @GetMapping("/search/{id}")
    public SearchResponse<CustomerGroupGridDto> getGridPageCache(@PathVariable UUID id) {
        return customerGroupService.search(id, null);
    }

    @PostMapping("/search")
    public SearchResponse<CustomerGroupGridDto> getGridPage(@Valid @RequestBody SearchRequest searchRequest) {
        return customerGroupService.search(null, searchRequest);
    }

    @GetMapping("/{id}")
    public CustomerGroupResponseDto get(@PathVariable long id) {
        return customerGroupService.get(id);
    }

    @PostMapping
    public CustomerGroupResponseDto create(@Valid @RequestBody CustomerGroupRequestDto requestDto) {
        return customerGroupService.save(requestDto);
    }

    @PutMapping("/{id}")
    public CustomerGroupResponseDto update(
            @PathVariable long id, @Valid @RequestBody CustomerGroupRequestDto requestDto) {
        return customerGroupService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        customerGroupService.delete(id);
    }

    @DeleteMapping("/delete")
    public void deleteMany(@RequestBody Set<Long> ids) {
        customerGroupService.deleteMany(ids);
    }
}
