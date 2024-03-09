package com.cogent.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import com.cogent.ecommerce.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.model.Wishlist;
import com.cogent.ecommerce.repository.AccountRepository;
import com.cogent.ecommerce.repository.WishlistJpaRepository;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.security.JwtService;

@Service
public class WishlistService {

	@Autowired
	private WishlistJpaRepository wishlistJpaRepository;
	
	@Autowired
	private AccountJpaRepository accountJpaRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
    private AuthService authService;
    
	@Autowired
    private JwtService jwtService;
	
	public List<Wishlist> getWishlistItems(String authHeader){
		String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
		Account account = accountJpaRepository.findById(aid).orElse(null);
		if(account == null){return null;}

		List<Wishlist> wl = wishlistJpaRepository.findAll();
		List<Wishlist> user_wl = new ArrayList<>();
		for(Wishlist i : wl) {
			if(i.getAccount().getId() == account.getId()) { user_wl.add(i);}
		}
		return user_wl;
	}
	
	public ResponseEntity<?> addWishlistItem(Product item, String authHeader){
		String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
		System.out.println("account id: "+aid);
        Account account = accountJpaRepository.findById(aid).orElse(null);
		if(account == null){return ResponseEntity.badRequest().body("Account is NULL");}
        
        Wishlist w = new Wishlist();
        w.setAccount(account);
        w.setProduct(item);
        if(wishlistJpaRepository.existsByAccount(account) &&
        	wishlistJpaRepository.existsByProduct(item)) {
        	return ResponseEntity.badRequest().body("Product Already Exists in Wishlist");
        }
		wishlistJpaRepository.save(w);
		return ResponseEntity.ok().body(w);
	}
	
	public ResponseEntity<?> removeWishlistItem(int item, String authHeader){
		String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        Account account = accountJpaRepository.findById(aid).orElse(null);
		if(account == null){return ResponseEntity.badRequest().body("Account is NULL");}

//        List<Wishlist> wl = wishlistJpaRepository.findAll();
//		for(Wishlist i : wl) {
//			if(i.getAccount().getId() == account.getId() &&
//			   i.getProduct().getId() == item.getId()) {
//				int id = i.getId();
//				wishlistJpaRepository.deleteById(id);
//				return ResponseEntity.ok().body("Item removed");
//			}
//		}
		Product product = productService.getProductById(item).orElse(null);
		if(product==null){
			return ResponseEntity.badRequest().body("Item not found");
		}
		Wishlist wishlist = wishlistJpaRepository
				.findByAccountIdAndProductId(aid,item).orElse(null);
		if(wishlist!=null){
			wishlistJpaRepository.delete(wishlist);
			return ResponseEntity.ok().body("Item removed");
		}

		return ResponseEntity.badRequest().body("Item not found");
	}


	public ResponseEntity<?> addToWishList(int productId, String authHeader) {
		String token = authHeader.substring(7);
		String username = jwtService.getUsernameFromToken(token);
		int aid = authService.getAccountIdFromUsername(username);
		System.out.println("account id: " + aid);
		Account account = accountJpaRepository.findById(aid).orElse(null);
		if (account == null) {
			return ResponseEntity.badRequest().body("Account is NULL");
		}
		Wishlist wishlistItem = new Wishlist();
		wishlistItem.setAccount(account);
		if(wishlistJpaRepository.existsByAccountIdAndProductId(aid,productId)){
			return ResponseEntity.badRequest().body("Product exists in user's wishlist");
		}
		Product product = productService.getProductById(productId).orElse(null);
		wishlistItem.setProduct(product);
		if(product==null){
			return ResponseEntity.badRequest().body("Product does not exist");
		}
		wishlistItem.setPriceOnAdd(product.getPrice());
		wishlistJpaRepository.save(wishlistItem);
		return ResponseEntity.ok().body(wishlistItem);
	}

}
