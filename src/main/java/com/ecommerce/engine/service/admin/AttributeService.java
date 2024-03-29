package com.ecommerce.engine.service.admin;

import com.ecommerce.engine.dto.admin.grid.AttributeGridDto;
import com.ecommerce.engine.dto.admin.request.AttributeRequestDto;
import com.ecommerce.engine.dto.admin.response.AttributeResponseDto;
import com.ecommerce.engine.entity.Attribute;
import com.ecommerce.engine.exception.NotFoundException;
import com.ecommerce.engine.repository.AttributeRepository;
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
public class AttributeService implements EntityPresenceService<Long> {

    private final AttributeRepository repository;
    private final SearchService<Attribute, AttributeGridDto> searchService;
    private final ForeignKeysChecker foreignKeysChecker;

    public AttributeResponseDto get(long id) {
        Attribute attribute = findById(id);
        return new AttributeResponseDto(attribute);
    }

    public AttributeResponseDto save(AttributeRequestDto requestDto) {
        Attribute attribute = new Attribute(requestDto);
        Attribute saved = repository.save(attribute);
        return new AttributeResponseDto(saved);
    }

    public AttributeResponseDto update(long id, AttributeRequestDto requestDto) {
        findById(id);

        Attribute attribute = new Attribute(requestDto);
        attribute.setId(id);
        Attribute saved = repository.save(attribute);
        return new AttributeResponseDto(saved);
    }

    public void delete(long id) {
        foreignKeysChecker.checkUsages(Attribute.TABLE_NAME, id);
        repository.deleteById(id);
    }

    public void deleteMany(Set<Long> ids) {
        ids.forEach(id -> foreignKeysChecker.checkUsages(Attribute.TABLE_NAME, id));
        repository.deleteAllById(ids);
    }

    private Attribute findById(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Attribute.TABLE_NAME, id));
    }

    public SearchResponse<AttributeGridDto> search(UUID id, SearchRequest searchRequest) {
        return searchService.search(id, searchRequest, SearchEntity.ATTRIBUTE, Attribute.class, AttributeGridDto::new);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ATTRIBUTE;
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }
}
