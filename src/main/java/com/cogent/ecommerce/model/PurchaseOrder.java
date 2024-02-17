package com.cogent.ecommerce.model;

import jakarta.persistence.*;

@Entity
//@Table(
//        name = "product_account_key",
//        uniqueConstraints = {@UniqueConstraint(columnNames = {"product", "account"})}
//)
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Account account;

    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
