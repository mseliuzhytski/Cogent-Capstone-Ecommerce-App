package com.cogent.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.model.Wishlist;
import com.cogent.ecommerce.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = "http://localhost:4200")
public class WishlistController {

	@Autowired
	WishlistService wishlistService;
	
	@GetMapping("/getlist")
	public List<Wishlist> getWishlist(@RequestHeader("Authorization") String authHeader){
		return wishlistService.getWishlistItems(authHeader);
	}
	
	@PostMapping("/additem")
	public ResponseEntity<?> addWhishlistItem(@RequestBody Product item, @RequestHeader("Authorization") String authHeader){
		return wishlistService.addWishlistItem(item, authHeader);
	}
	
	@PutMapping("/removeitem")
	public ResponseEntity<?> removeWishlistItem(@RequestBody Product item, @RequestHeader("Authorization") String authHeader){
		return wishlistService.removeWishlistItem(item, authHeader);
	}
}
