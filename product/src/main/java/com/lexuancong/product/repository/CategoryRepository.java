package com.lexuancong.product.repository;

import com.lexuancong.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContainingIgnoreCase(String name);

//    @Query("select category from Category  category " +
//            "where category.name = ? 1 and (?2 is null or ?2 = category.id)")
//    Category findExistedName(String name,Long id);
    Category findByNameAndIdNot(String name, Long id);

    Optional<Category> findBySlug(String slug);
}
