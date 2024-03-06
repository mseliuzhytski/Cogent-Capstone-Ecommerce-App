package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Discount;
import com.cogent.ecommerce.repository.DiscountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountJpaRepository repo;

    public List<Discount> getAllDiscounts() {
        return repo.findAll();
    }

    public Optional<Discount> getDiscountById(int id) {
        return repo.findById(id);
    }

    public Discount addDiscount(Discount discount){
        return repo.save(discount);
    }

    public Discount deleteById(int id) {
        Optional<Discount> discount = repo.findById(id);
        if (discount.isPresent()) {
            repo.delete(discount.get());
            return discount.get();
        } else {
            return null;
        }
    }

    public Discount updateDiscount(Discount account, int id) {
        Optional<Discount> oldDiscount = repo.findById(id);
        if (oldDiscount.isPresent()) {
            account.setId(id);
            repo.save(account);
            return account;
        } else {
            return null;
        }
    }

    public Discount getDiscountByCode(String code) {
        return repo.getDiscountByDiscountCode(code);
    }

}
