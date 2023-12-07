package com.example.backend.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.dto.OrderDetailsDto;
import com.example.backend.dto.OrderDto;
import com.example.backend.dto.ProductDto;
import com.example.backend.enums.OrderStatus;
import com.example.backend.exception.LogicRuntimeException;
import com.example.backend.model.Cart;
import com.example.backend.model.OrderDetail;
import com.example.backend.model.Orders;
import com.example.backend.model.Product;
import com.example.backend.model.Users;
import com.example.backend.model.OrderDetail.OrderDetailPK;
import com.example.backend.repository.OrdersRepository;
import com.example.backend.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdersService {
    private OrdersRepository orderRepository;
    private ProductRepository productRepository;
    private RedisTemplate<String, List<Cart>> redisTemplate;
    private UsersService usersService;

    public OrdersService(OrdersRepository orderRepository, ProductRepository productRepository,
            RedisTemplate<String, List<Cart>> redisTemplate, UsersService usersService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
        this.usersService = usersService;
    }

    public List<OrderDto> findByUsername(String username) {
        Users users = usersService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到登入者訂單資訊"));

        Example<Orders> ex = Example.of(new Orders());
        ex.getProbe().setUserId(users.getId());
        List<Orders> orders = orderRepository.findAll(ex);

        List<OrderDto> orderDtos = orders.stream().map(t -> {
            String orderDate = t.getOrderDate().format(DateTimeFormatter.ISO_DATE).toString();
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
            dto.setOrderDate(orderDate);
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
