package com.cogent.ecommerce.loader;

import com.cogent.ecommerce.repository.*;
import com.cogent.ecommerce.model.*;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.security.RegisterRequest;
import com.cogent.ecommerce.service.CategoryService;
import com.cogent.ecommerce.service.ProductService;
import com.cogent.ecommerce.service.BulkUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseLoader {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private BulkUploadService bulkUploadService;

    @Autowired
    private AuthService authService;

    @Value( "${admin.username}" )
	private String adminUsername;

    @Value( "${admin.password}" )
    private String adminPassword;

    @Value( "${admin.email}" )
    private String adminEmail;

    @Value( "${user.username}" )
    private String userUsername;

    @Value( "${user.password}" )
    private String userPassword;

    @Value( "${user.email}" )
    private String userEmail;

    private Account rootAccount;
    private Account userAccount;

    public final static String BULK_UPLOAD_FILE = "src/main/resources/bulk_upload.xlsx";

    public void loadSeededData() throws DataAccessException {
        loadAccounts();
        loadCategories();
        loadProducts();
        loadPurchaseOrders();
        loadWishlist();
        loadDiscount();
        bulkUpload();
        loadSalesItem();
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

        RegisterRequest request =
                RegisterRequest
                        .builder()
                        .username(userUsername)
                        .password(userPassword)
                        .email(userEmail).build();
        if (!authService.registerUser(request)) {
            System.err.println("There was an error creating the user");
        }
        Account account = accountJpaRepository.getAccountByUsername(userUsername);
        account.setDiscount(discount);
        accountRepository.saveAccount(account);
        this.userAccount = account;

        discount = new Discount();
        discount.setDiscountCode("sc2000");
        discount.setDiscountPercent(25);
        discountRepository.saveDiscount(discount);

        discount = new Discount();
        discount.setDiscountCode("oakland_as");
        discount.setDiscountPercent(10);
        discountRepository.saveDiscount(discount);
    }

    public void loadSalesItem() {
        long now = Instant.now().toEpochMilli();
        long dayAgo = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        long threeDaysAgo = Instant.now().minus(3, ChronoUnit.DAYS).toEpochMilli();

        addSalesItem(rootAccount, 1, 3, threeDaysAgo);
        addSalesItem(rootAccount, 2, 2, dayAgo);
        addSalesItem(rootAccount, 3, 1, now);

        addSalesItem(userAccount, 3, 2, threeDaysAgo);
        addSalesItem(userAccount, 4, 1, dayAgo);
        addSalesItem(userAccount, 5, 1, now);
    }

    private SalesItem addSalesItem(Account account, int productId, int quantity, long timeAdded) {
        Product product = productRepository.getById(productId);
        SalesItem salesItem = new SalesItem();
        salesItem.setAccount(account);
        salesItem.setProduct(product);
        salesItem.setQuantitySold(quantity);
        salesItem.setTimeRecorded(timeAdded);
        salesItem.setTotalPrice(product.getPrice() * quantity);
        return salesRepository.saveSalesItem(salesItem);
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
        RegisterRequest request = RegisterRequest
                .builder().username(adminUsername).password(adminPassword)
                .email(adminEmail).build();
        authService.registerAdmin(request);

        this.rootAccount = accountJpaRepository.getAccountByUsername(adminUsername);
    }

    private void loadProducts() {

        Product product = new Product();
        product.setName("Pencil");
        //product.setCategory("Office Supplies");
        product.setCategoriesList(new HashSet<>());
        product.setPrice(1.99);
        product.setDateAdded(Instant.now().toEpochMilli());
        product.setImageLocation("pencil.jpeg");
        product.setStock(10);

        productRepository.saveProduct(product);
        productService.addCategoryToProduct(product, "Office Supplies");

        product = new Product();
        product.setName("Eraser");
        //product.setCategory("Office Supplies");
        product.setPrice(2.99);
        product.setDateAdded(Instant.now().toEpochMilli());
        product.setImageLocation("eraser.jpeg");
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
