package com.govind.ecommerce.service.product;

import com.govind.ecommerce.dto.ImageDto;
import com.govind.ecommerce.dto.ProductDto;
import com.govind.ecommerce.exception.AlreadyExistsException;
import com.govind.ecommerce.exception.ProductNotFoundException;
import com.govind.ecommerce.model.Category;
import com.govind.ecommerce.model.Image;
import com.govind.ecommerce.model.Product;
import com.govind.ecommerce.repository.CategoryRepository;
import com.govind.ecommerce.repository.ImageRepository;
import com.govind.ecommerce.repository.ProductRepository;
import com.govind.ecommerce.request.AddProductRequest;
import com.govind.ecommerce.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the Category is found in the database.
        // If found, set it as a new product's category.
        // If not, then save it as a new category first and then set it as the new product's category.

        if (productExists(request.getName(), request.getBrand())) {
            throw new AlreadyExistsException(request.getName() + " " + request.getBrand() + " already exists");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }


    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        if (request.getName() != null)
            existingProduct.setName(request.getName());
        if (request.getBrand() != null)
            existingProduct.setBrand(request.getBrand());
        if (request.getPrice() != null)
            existingProduct.setPrice(request.getPrice());
        if (request.getInventory() != 0)
            existingProduct.setInventory(request.getInventory());
        if (request.getDescription() != null)
            existingProduct.setDescription(request.getDescription());
        if (request.getCategory().getName() != null) {
            Category category = categoryRepository.findByName(request.getCategory().getName());
            existingProduct.setCategory(category);
        }
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository
                .findById(id)
                .ifPresentOrElse(
                        productRepository::delete,
                        () -> {
                            throw new ProductNotFoundException("Product to be deleted not found");
                        }
                );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String categoryName, String brand) {
        return productRepository.findByCategoryNameAndBrand(categoryName, brand);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String productName) {
        return productRepository.findByBrandAndName(brand, productName);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String productName) {
        return productRepository.countByBrandAndName(brand, productName);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
