package com.cogent.ecommerce.loader;

import com.cogent.ecommerce.repository.AccountRepository;
import com.cogent.ecommerce.repository.DiscountRepository;
import com.cogent.ecommerce.repository.ProductRepository;
import com.cogent.ecommerce.repository.SalesRepository;
import com.cogent.ecommerce.model.*;
import com.cogent.ecommerce.service.CategoryService;
import com.cogent.ecommerce.service.ProductService;
import com.cogent.ecommerce.service.BulkUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseLoader {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private BulkUploadService bulkUploadService;

    public final static String BULK_UPLOAD_FILE = "src/main/resources/bulk_upload.xlsx";

    public void loadSeededData() throws DataAccessException {
        loadAccounts();
        loadCategories();
        loadProducts();
        loadPurchaseOrders();
        loadWishlist();
        loadSalesItem();
        loadDiscount();
        bulkUpload();
    }

    public void loadCategories() {
        Category category = new Category();
        category.setName("Office Supplies");
        categoryService.addCategory(category);

        category = new Category();
        category.setName("Books");
        categoryService.addCategory(category);

//        category = new Category();
//        category.setName("Games");
//        categoryService.addCategory(category);
//
//        category = new Category();
//        category.setName("Produce");
//        categoryService.addCategory(category);
    }

    public void bulkUpload() {
        try {
            bulkUploadService.bulkUpload(BULK_UPLOAD_FILE);
        } catch (BulkUploadService.CsvParseException e) {
            System.out.println("--- Could not perform bulk upload ---");
            System.err.println(e.getMessage());
        }
    }

    public void loadDiscount() {
        Discount discount = new Discount();
        discount.setDiscountCode("abc");
        discount.setDiscountPercent(15);
        discountRepository.saveDiscount(discount);
    }

    public void loadSalesItem() {
        Account account = accountRepository.getById(1);
        Product product = productRepository.getById(2);
        SalesItem salesItem = new SalesItem();
        salesItem.setAccount(account);
        salesItem.setProduct(product);
        salesItem.setQuantitySold(5);
        salesItem.setTimeRecorded(Instant.now().toEpochMilli());
        salesItem.setTotalPrice(9.95);
        salesRepository.saveSalesItem(salesItem);
    }

    public void loadWishlist() {
        Account account = accountRepository.getById(1);
        Product product = productRepository.getById(2);
        Wishlist wishlist = new Wishlist();
        wishlist.setAccount(account);
        wishlist.setProduct(product);
        productRepository.saveWishlist(wishlist);

//        account = accountRepository.getById(1);
//        product = productRepository.getById(2);
//        wishlist = new Wishlist();
//        wishlist.setAccount(account);
//        wishlist.setProduct(product);
//        productRepository.saveWishlist(wishlist);
    }
    public void loadPurchaseOrders() {
        Account account = accountRepository.getById(1);
        Product product = productRepository.getById(2);
        System.out.println("Account " + account);
        System.out.println("Product " + product);
        PurchaseOrder po = new PurchaseOrder();
        po.setAccount(account);
        po.setProduct(product);
        po.setQuantity(5);
        System.out.println("purchase order 1");
        productRepository.savePurchaseOrder(po);
        System.out.println("purchase order 2");

        account = accountRepository.getById(2);
        product = productRepository.getById(2);
        po = new PurchaseOrder();
        po.setAccount(account);
        po.setProduct(product);
        po.setQuantity(10);
        productRepository.savePurchaseOrder(po);

//        account = accountRepository.getById(2);
//        product = productRepository.getById(2);
//        po = new PurchaseOrder();
//        po.setAccount(account);
//        po.setProduct(product);
//        po.setQuantity(5);
//        productRepository.savePurchaseOrder(po);
    }

    public void loadAccounts() {
        Account account = new Account();
        account.setAdmin(false);
        account.setUser(true);
        account.setUsername("user");
        account.setEmail("user1@gmail.com");
        account.setPassword("abc123");
        accountRepository.saveAccount(account);

        account = new Account();
        account.setAdmin(true);
        account.setUser(true);
        account.setUsername("admin");
        account.setEmail("admin1@gmail.com");
        account.setPassword("abc123");
        accountRepository.saveAccount(account);
    }

    private void loadProducts() {

        Product product = new Product();
        product.setName("Pencil");
        //product.setCategory("Office Supplies");
        product.setCategoriesList(new HashSet<>());
        product.setPrice(1.99);
        product.setDateAdded(Instant.now().toEpochMilli());
        product.setImageLocation("pencil.jpg");
        product.setStock(10);

        productRepository.saveProduct(product);
        productService.addCategoryToProduct(product, "Office Supplies");

        product = new Product();
        product.setName("Eraser");
        //product.setCategory("Office Supplies");
        product.setPrice(2.99);
        product.setDateAdded(Instant.now().toEpochMilli());
        product.setImageLocation("eraser.jpg");
        product.setStock(10);

        productRepository.saveProduct(product);
        productService.addCategoryToProduct(product, "Office Supplies");
    }

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    void updatedLoadProducts(){

        Category category = new Category();
        category.setName("Men's Clothing");
        categoryService.addCategory(category);

        Category category2 = new Category();
        category2.setName("Clothing");
        categoryService.addCategory(category2);

        Category category3 = new Category();
        category3.setName("Jackets");
        categoryService.addCategory(category3);

        Product p1 = new Product();
        p1.setName("FUBU Orange Windbreaker");
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);
        p1.setCategoriesList(categories);
        p1.setPrice(109.99);
        p1.setDetails("FUBU's new Windbreaker. Orange like an orange.");
        p1.setDateAdded(Instant.now().toEpochMilli());
        p1.setImageLocation("orangeFUBUWindbreaker.png");
        p1.setStock(5);

        productService.addProducts(p1);
        //have to test the add categories thru restapi impl with postman

    }

}
