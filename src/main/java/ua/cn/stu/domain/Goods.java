package ua.cn.stu.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "goods")
public class Goods {

    public Goods(){}

    public Goods(String name, BigDecimal price, int quantity, Supplier supplier) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        setSupplier(supplier);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_name", nullable = false)
    private String name;

    @Column(name = "goods_price")
    private BigDecimal price;

    @Column(name = "goods_quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Supplier supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier newSupplier) {
        if (this.supplier == newSupplier){
            return;
        }

        if (this.supplier != null) {
            this.supplier.removeGoods(this);
        }

        if (newSupplier != null) {
            newSupplier.addGoods(this);
        }

        this.supplier = newSupplier;
    }
}
