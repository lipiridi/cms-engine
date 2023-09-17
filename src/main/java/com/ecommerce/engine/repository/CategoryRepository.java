package com.ecommerce.engine.repository;

import com.ecommerce.engine.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
