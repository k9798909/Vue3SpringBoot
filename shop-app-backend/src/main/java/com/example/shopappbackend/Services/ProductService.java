package com.example.shopappbackend.Services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.shopappbackend.dto.ProductDto;
import com.example.shopappbackend.model.Product;
import com.example.shopappbackend.model.ProductImage;
import com.example.shopappbackend.repository.ProductImageRepository;
import com.example.shopappbackend.repository.ProductRepository;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(convert()).toList();
    }

    public ProductDto findByProId(String proId) {
        return productRepository.findById(proId).stream().map(convert()).findAny()
                .orElseThrow(() -> new NullPointerException("查無對應商品"));
    }

    public byte[] findImgByProId(String proId) {
        ProductImage proImg = new ProductImage();
        proImg.setProductId(proId);
        Example<ProductImage> ex = Example.of(proImg);
        Optional<ProductImage> queryProImg = productImageRepository.findOne(ex);
        return queryProImg.map(ProductImage::getImage).orElseThrow(() -> new RuntimeException("查無圖片"));
    }

    public List<ProductDto> findByName(String name) {
        return productRepository.findByNameContaining(name).stream().map(convert()).collect(Collectors.toList());
    }

    private Function<Product, ProductDto> convert() {
        return product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setQuantity(product.getQuantity());
            return dto;
        };
    }

}
