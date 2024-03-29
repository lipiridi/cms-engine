package com.ecommerce.engine.repository;

import com.ecommerce.engine.entity.SearchRequestCache;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRequestCacheRepository extends JpaRepository<SearchRequestCache, UUID> {}
