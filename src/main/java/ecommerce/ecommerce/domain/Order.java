package ecommerce.ecommerce.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name =  "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    private Instant updatedAt = null;

    private Instant deletedAt = null;

    private Instant completedAt = null;

    public Order() {}

    public Order(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public static Order of(List<OrderItem> orderItems){
        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("OrderItems name cannot be empty.");
        }
        orderItems.forEach(OrderItem::validateQuantity);

        return new Order(orderItems);
    }

    public static Order of(Long id, List<OrderItem> orderItems){
        Order order = Order.of(orderItems);
        order.id = id;
        return order;
    }

    public Long getId() { return id; }

    public List<OrderItem> getOrderItems() { return orderItems; }

    public Order complete() {
        status = OrderStatus.COMPLETED;
        this.completedAt = Instant.now();
        return this;
    }

    public Order modifyItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public OrderStatus modifyStatus(OrderStatus status) {
        this.status = status;
        return status;
    }

    public void cancel() {
        status = OrderStatus.CANCELED;
        deletedAt = Instant.now();
    }
}

