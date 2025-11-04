package ua.cn.stu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplier")
public class Supplier {

    public Supplier() {}

    public Supplier(String name, String contact, String address, String specialization) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.specialization = specialization;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_name")
    private String name;

    @Column(name = "supplier_contact")
    private String contact;

    @Column(name = "supplier_address")
    private String address;

    @Column(name = "supplier_specialization")
    private String specialization;

    @OneToMany(mappedBy = "supplier",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    private Set<Goods> goodsSet = new HashSet<>();

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonIgnore
    public Set<Goods> getGoodsSet() {
        return goodsSet;
    }

    public void setGoodsSet(Set<Goods> goodsSet) {
        this.goodsSet = goodsSet;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void addGoods(Goods goods) {
        goodsSet.add(goods);
    }

    public void removeGoods(Goods goods) {
        goodsSet.remove(goods);
    }

}
