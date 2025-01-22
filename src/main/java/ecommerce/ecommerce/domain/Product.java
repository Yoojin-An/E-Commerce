package ecommerce.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

//@Getter
@Entity
@Builder
@AllArgsConstructor
//@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<StockHistory> stockHistory = new ArrayList<>();

    protected Product() {}

    public Product(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
        this.stockHistory = new ArrayList<>();
    }

    public static Product of(String name, Integer stock) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (stock == null || stock <= 0) {
            throw new IllegalArgumentException("Product Stock must be greater than zero.");
        }
        return new Product(name, stock);
    }

    public static Product of(Long id, String name, Integer stock) {
        Product product = Product.of(name, stock);
        product.id = id;
        return product;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<StockHistory> getStockHistory() {
        return this.stockHistory;
    }

    public Product increaseStock(Integer quantity) {
        this.stock += quantity;
        addStockHistory(quantity, StockHistoryType.INCREASE);
        return this;
    }

    public Product decreaseStock(Integer quantity) {
        if ( this.stock < quantity ) {
            throw new IllegalArgumentException("Not enough stock to decrease");
        }
        this.stock -= quantity;
        addStockHistory(quantity, StockHistoryType.DECREASE);
        return this;
    }

//    public Product updateStock(Integer quantity, String operation) {
//        if ( quantity <= 0 ) {
//            throw new IllegalArgumentException("");
//        }
//
//        StockHistoryType type = toStockHistoryType(operation);
//        if ( type == StockHistoryType.INCREASE ) {
//            this.stock += quantity;
//        } else if ( type == StockHistoryType.DECREASE ) {
//            if ( this.stock < quantity ) {
//                throw new IllegalArgumentException("Not enough stock to decrease");
//            }
//            this.stock -= quantity;
//        }
//        addStockHistory(quantity, type);
//
//        return this;
//    }

    public void addStockHistory(Integer quantity, StockHistoryType type) {
        StockHistory stockHistory = StockHistory.of(quantity, type);
        this.stockHistory.add(stockHistory);
    }
}
