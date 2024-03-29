package com.ecommerce.engine.service.admin;

import com.ecommerce.engine.dto.admin.grid.CustomerGroupGridDto;
import com.ecommerce.engine.dto.admin.request.CustomerGroupRequestDto;
import com.ecommerce.engine.dto.admin.response.CustomerGroupResponseDto;
import com.ecommerce.engine.entity.CustomerGroup;
import com.ecommerce.engine.exception.NotFoundException;
import com.ecommerce.engine.repository.CustomerGroupRepository;
import com.ecommerce.engine.search.SearchEntity;
import com.ecommerce.engine.search.SearchRequest;
import com.ecommerce.engine.search.SearchResponse;
import com.ecommerce.engine.search.SearchService;
import com.ecommerce.engine.service.EntityPresenceService;
import com.ecommerce.engine.service.ForeignKeysChecker;
import com.ecommerce.engine.validation.EntityType;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerGroupService implements EntityPresenceService<Long> {

    private final CustomerGroupRepository repository;
    private final SearchService<CustomerGroup, CustomerGroupGridDto> searchService;
    private final ForeignKeysChecker foreignKeysChecker;

    public CustomerGroupResponseDto get(long id) {
        CustomerGroup customerGroup = findById(id);
        return new CustomerGroupResponseDto(customerGroup);
    }

    public CustomerGroupResponseDto save(CustomerGroupRequestDto requestDto) {
        CustomerGroup customerGroup = new CustomerGroup(requestDto);
        CustomerGroup saved = repository.save(customerGroup);
        return new CustomerGroupResponseDto(saved);
    }

    public CustomerGroupResponseDto update(long id, CustomerGroupRequestDto requestDto) {
        findById(id);

        CustomerGroup customerGroup = new CustomerGroup(requestDto);
        customerGroup.setId(id);
        CustomerGroup saved = repository.save(customerGroup);
        return new CustomerGroupResponseDto(saved);
    }

    public void delete(long id) {
        foreignKeysChecker.checkUsages(CustomerGroup.TABLE_NAME, id);
        repository.deleteById(id);
    }

    public void deleteMany(Set<Long> ids) {
        ids.forEach(id -> foreignKeysChecker.checkUsages(CustomerGroup.TABLE_NAME, id));
        repository.deleteAllById(ids);
    }

    private CustomerGroup findById(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(CustomerGroup.TABLE_NAME, id));
    }

    public SearchResponse<CustomerGroupGridDto> search(UUID id, SearchRequest searchRequest) {
        return searchService.search(
                id, searchRequest, SearchEntity.CUSTOMER_GROUP, CustomerGroup.class, CustomerGroupGridDto::new);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.CUSTOMER_GROUP;
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }
}
