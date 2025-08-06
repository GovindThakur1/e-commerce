package com.govind.ecommerce.service.product;

import com.govind.ecommerce.dto.ProductDto;
import com.govind.ecommerce.model.Product;
import com.govind.ecommerce.request.AddProductRequest;
import com.govind.ecommerce.request.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);

    Product updateProduct(ProductUpdateRequest request, Long productId);

    void deleteProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String categoryName);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String categoryName, String brand);

    List<Product> getProductsByName(String productName);

    List<Product> getProductsByBrandAndName(String brand, String productName);

    Long countProductsByBrandAndName(String brand, String productName);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
