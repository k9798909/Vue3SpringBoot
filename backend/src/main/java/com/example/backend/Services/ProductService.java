package com.example.backend.Services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ProductDto;
import com.example.backend.model.Product;
import com.example.backend.model.ProductImage;
import com.example.backend.repository.ProductImageRepository;
import com.example.backend.repository.ProductRepository;

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
        return product -> new ProductDto(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getQuantity());
    }

}
