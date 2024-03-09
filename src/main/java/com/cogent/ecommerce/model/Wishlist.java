package com.cogent.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;

    private double priceOnAdd;

    public double getPriceOnAdd() {
        return priceOnAdd;
    }

    public void setPriceOnAdd(double priceOnAdd) {
        this.priceOnAdd = priceOnAdd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
