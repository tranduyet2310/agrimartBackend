package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.CartDto;
import com.example.agriecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("{userId}/{productId}")
    public ResponseEntity<CartDto> addToCart(@PathVariable("userId") Long userId,
                                             @PathVariable("productId") Long productId) {
        CartDto cartDto = cartService.addToCart(userId, productId);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<CartDto>> getAllItems(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(cartService.getAllItemsByUserId(userId));
    }

    @PatchMapping
    public ResponseEntity<CartDto> changeQuantity(@RequestParam("userId") Long userId,
                                                  @RequestParam("productId") Long productId,
                                                  @RequestParam("quantity") Integer quantity) {
        return ResponseEntity.ok(cartService.changeQuantity(userId, productId, quantity));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable("id") Long id){
        cartService.removeFromCart(id);
        return ResponseEntity.ok("Item deleted successfully");
    }
}
