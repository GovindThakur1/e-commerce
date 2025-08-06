package com.govind.ecommerce.repository;

import com.govind.ecommerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProductId(Long productId);
}
