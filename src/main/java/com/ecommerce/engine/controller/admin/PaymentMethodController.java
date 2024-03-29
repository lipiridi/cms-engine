package com.ecommerce.engine.controller.admin;

import com.ecommerce.engine.dto.admin.grid.PaymentMethodGridDto;
import com.ecommerce.engine.dto.admin.request.PaymentMethodRequestDto;
import com.ecommerce.engine.dto.admin.response.PaymentMethodResponseDto;
import com.ecommerce.engine.search.SearchRequest;
import com.ecommerce.engine.search.SearchResponse;
import com.ecommerce.engine.service.admin.PaymentMethodService;
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
@RequestMapping("/admin/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping("/search/{id}")
    public SearchResponse<PaymentMethodGridDto> getGridPageCache(@PathVariable UUID id) {
        return paymentMethodService.search(id, null);
    }

    @PostMapping("/search")
    public SearchResponse<PaymentMethodGridDto> getGridPage(@Valid @RequestBody SearchRequest searchRequest) {
        return paymentMethodService.search(null, searchRequest);
    }

    @GetMapping("/{id}")
    public PaymentMethodResponseDto get(@PathVariable long id) {
        return paymentMethodService.get(id);
    }

    @PostMapping
    public PaymentMethodResponseDto create(@Valid @RequestBody PaymentMethodRequestDto requestDto) {
        return paymentMethodService.save(requestDto);
    }

    @PutMapping("/{id}")
    public PaymentMethodResponseDto update(
            @PathVariable long id, @Valid @RequestBody PaymentMethodRequestDto requestDto) {
        return paymentMethodService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        paymentMethodService.delete(id);
    }

    @DeleteMapping("/delete")
    public void deleteMany(@RequestBody Set<Long> ids) {
        paymentMethodService.deleteMany(ids);
    }
}
