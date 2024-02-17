package com.cogent.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    /** The discount code that must be entered by the user in order to use it. */
    private String discountCode;

    /** Discount percent as an integer from 0 to 100 */
    private int discountPercent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }
}
