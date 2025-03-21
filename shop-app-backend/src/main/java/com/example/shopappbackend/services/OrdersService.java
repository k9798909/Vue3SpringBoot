package com.example.shopappbackend.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.shopappbackend.dto.OrderDetailsDto;
import com.example.shopappbackend.dto.OrderDto;
import com.example.shopappbackend.dto.ProductDto;
import com.example.shopappbackend.enums.OrderStatus;
import com.example.shopappbackend.exception.LogicRuntimeException;
import com.example.shopappbackend.model.Cart;
import com.example.shopappbackend.model.OrderDetail;
import com.example.shopappbackend.model.Orders;
import com.example.shopappbackend.model.Product;
import com.example.shopappbackend.model.Users;
import com.example.shopappbackend.model.OrderDetail.OrderDetailPK;
import com.example.shopappbackend.repository.OrdersRepository;
import com.example.shopappbackend.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrdersService {
    private final OrdersRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, List<Cart>> redisTemplate;
    private final UsersService usersService;

    public List<OrderDto> findByUsername(String username) {
        Users users = usersService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到登入者訂單資訊"));

        List<Orders> orders = orderRepository.findByUserId(users.getId());

        List<OrderDto> orderDtos = orders.stream().map(t -> {
            String status = OrderStatus.toName(t.getStatus());

            List<OrderDetailsDto> dts = t.getOrderDetails().stream().map(dt -> {
                Product product = dt.getProduct();

                ProductDto productDto = new ProductDto();
                productDto.setId(product.getId());
                productDto.setName(product.getName());
                productDto.setDescription(product.getDescription());
                productDto.setPrice(product.getPrice());
                productDto.setQuantity(product.getQuantity());

                OrderDetailsDto dto = new OrderDetailsDto();
                dto.setProductDto(productDto);
                dto.setQuantity(dt.getQuantity());
                dto.setPrice(dt.getPrice());

                return dto;
            }).collect(Collectors.toList());

            OrderDto dto = new OrderDto();
            dto.setOrderId(t.getOrderId());
            dto.setOrderDate(t.getOrderDate());
            dto.setTotalPrice(t.getTotalPrice());
            dto.setStatus(status);
            dto.setOrderDetails(dts);

            return dto;
        }).collect(Collectors.toList());

        return orderDtos;
    }

    @Transactional
    public void checkOut(String username) {
        Users users = usersService.findByUsername(username)
                .orElseThrow(() -> new LogicRuntimeException("結帳者未登入"));

        var ops = redisTemplate.opsForValue().getOperations();
        List<Cart> cartList = ops.opsForValue().get(username);
        if (cartList == null || cartList.isEmpty()) {
            throw new LogicRuntimeException("購物清單內無商品");
        }

        // 訂單
        Orders order = new Orders();
        order.setUserId(users.getId());
        order.setStatus("0");
        order.setOrderDate(LocalDateTime.now());

        // 訂單明細
        AtomicLong count = new AtomicLong(0);
        List<OrderDetail> dts = cartList.stream()
                .map(cart -> {
                    Product product = productRepository.findById(cart.getProductId())
                            .orElseThrow(() -> new LogicRuntimeException("結帳商品 " + cart.getProductId() + " 資訊不存在"));

                    OrderDetailPK pk = new OrderDetailPK();
                    pk.setDetailId(count.addAndGet(1));
                    pk.setOrders(order);

                    OrderDetail dt = new OrderDetail();
                    dt.setOrderId(pk);
                    dt.setProduct(product);
                    dt.setPrice(product.getPrice());
                    dt.setQuantity(cart.getQuantity());
                    return dt;
                }).collect(Collectors.toList());
        order.setOrderDetails(dts);

        // 總金額
        BigDecimal totalPrice = dts.stream()
                .map(dt -> dt.getPrice().multiply(new BigDecimal(dt.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        // 儲存
        orderRepository.save(order);
    }

}
