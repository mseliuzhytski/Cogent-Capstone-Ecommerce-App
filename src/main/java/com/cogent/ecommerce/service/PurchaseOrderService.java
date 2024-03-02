package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.model.PurchaseOrder;
import com.cogent.ecommerce.repository.AccountJpaRepository;
import com.cogent.ecommerce.repository.ProductJpaRepository;
import com.cogent.ecommerce.repository.PurchaseOrderJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderJpaRepository purchaseOrderJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;


    public List<PurchaseOrder> getPurchaseOrdersOfUser(int accountId){
        return purchaseOrderJpaRepository.findByAccountId(accountId);
    }

    public PurchaseOrder addItemToCart(int quantity, int accountId, int productId){

        Account accountOfCart = accountJpaRepository.findById(accountId).orElse(null);
        Product productToAdd = productJpaRepository.findById(productId).orElse(null);

        if(accountOfCart!=null && productToAdd!=null){

            //duplicate product handling:
            if(purchaseOrderJpaRepository.findByAccountIdAndProductId(accountId,productId).isPresent()){
                return null;
            }
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setAccount(accountOfCart);
            purchaseOrder.setProduct(productToAdd);
            purchaseOrder.setQuantity(quantity);
            return purchaseOrderJpaRepository.save(purchaseOrder);
        }
        return null;
    }

    public PurchaseOrder changeQuantity(int newQuantity, int accountId, int productId){

        PurchaseOrder purchaseOrderToChange = purchaseOrderJpaRepository.findByAccountIdAndProductId(accountId,
                productId).orElse(null);

        if(purchaseOrderToChange!=null){
            purchaseOrderToChange.setQuantity(newQuantity);
            return purchaseOrderJpaRepository.save(purchaseOrderToChange);
        }
        return null;
    }

    public boolean deleteCartItem(int accountId, int productId){

        PurchaseOrder purchaseOrderToRemove = purchaseOrderJpaRepository.findByAccountIdAndProductId(accountId,
                productId).orElse(null);

        if(purchaseOrderToRemove!=null){
            purchaseOrderJpaRepository.delete(purchaseOrderToRemove);
            return true;
        }
        return false;
    }

    @Transactional
    public void clearCart(int accountId){

        purchaseOrderJpaRepository.deleteByAccountId(accountId);

    }


}
