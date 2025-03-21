package com.example.shopappbackend.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.shopappbackend.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.shopappbackend.dto.ProductDto;
import com.example.shopappbackend.model.Product;
import com.example.shopappbackend.model.ProductImage;
import com.example.shopappbackend.repository.ProductImageRepository;
import com.example.shopappbackend.repository.ProductRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(productMapper::toProductDto).toList();
    }

    public ProductDto findByProId(String proId) {
        return productRepository.findById(proId)
                .map(productMapper::toProductDto)
                .orElseThrow(() -> new NullPointerException("查無對應商品"));
    }

    public byte[] findImgByProId(String proId) {
        Optional<ProductImage> queryProImg = productImageRepository.findByProductId(proId);
        return queryProImg.map(ProductImage::getImage).orElseThrow(() -> new RuntimeException("查無圖片"));
    }

    public List<ProductDto> findByName(String name) {
        return productRepository.findByNameContaining(name).stream().map(productMapper::toProductDto).collect(Collectors.toList());
    }

}
