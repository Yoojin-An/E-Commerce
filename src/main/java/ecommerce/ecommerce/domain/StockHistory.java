package ecommerce.ecommerce.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Builder
@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stock_history")
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private StockHistoryType type;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected StockHistory() {}

    public StockHistory(Integer quantity, StockHistoryType type) {
        this.quantity = quantity;
        this.type = type;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Integer getQuantity() { return quantity; }
    public StockHistoryType getType() { return type; }
    public Instant getCreatedAt() { return createdAt; }

    public static StockHistory of(Integer quantity, StockHistoryType type) {
        return new StockHistory(quantity, type);
    }
}
