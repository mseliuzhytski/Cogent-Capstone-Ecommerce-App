package com.cogent.ecommerce.service;

import com.cogent.ecommerce.controller.SalesItemDTO;
import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.model.SalesItem;
import com.cogent.ecommerce.repository.SalesItemJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesItemService {


    @Autowired
    private SalesItemJpaRepository salesItemJpaRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    public List<SalesItem> getAllSales(){
        return salesItemJpaRepository.findAll();
    }

    public List<SalesItem> getAllSalesOfUser(int accountId){
        return salesItemJpaRepository.findByAccountId(accountId);
    }

    //when a sale is made stock needs to be decreased on product!
    //ensures all is committed
    @Transactional
    public List<SalesItem> addNewSalesItem(int accountId,List<SalesItemDTO> salesItems){

        Account accountOfSale = accountService.getAccountById(accountId).orElse(null);
        long timeOfOrder = Instant.now().toEpochMilli();
        List<SalesItem> listOfSalesToSave = new ArrayList<>();

        if(accountOfSale!=null){
            for(SalesItemDTO x:salesItems){
                SalesItem newSalesItem = new SalesItem();
                newSalesItem.setQuantitySold(x.getQuantitySold());
                newSalesItem.setAccount(accountOfSale);
                newSalesItem.setTimeRecorded(timeOfOrder);
                newSalesItem.setTotalPrice(x.getTotalPrice());
                Product productToRemoveStock = productService.getProductById(x.getProductId()).orElse(null);
                if(productToRemoveStock!=null){
                    int stock = productToRemoveStock.getStock();
                    stock = stock - x.getQuantitySold();//potential error handling for negative
                    productToRemoveStock.setStock(stock);
                    productService.updateStock(productToRemoveStock);
                    newSalesItem.setProduct(productToRemoveStock);
                }//potential error handling for product null
                listOfSalesToSave.add(newSalesItem);
            }//potential error handling for account null
            return salesItemJpaRepository.saveAll(listOfSalesToSave);
        }

        return null;

    }


}
