package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.FavoriteDto;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("{userId}/add/{productId}")
    public ResponseEntity<FavoriteDto> createFavortieProduct(@PathVariable("userId") Long userId,
                                                             @PathVariable("productId") Long productId) {
        FavoriteDto favoriteDto = favoriteService.createFavoriteProduct(userId, productId);
        return new ResponseEntity<>(favoriteDto, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<FavoriteDto> getFavoriteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(favoriteService.getFavoriteProductById(id));
    }

    @GetMapping("{userId}/list")
    public ResponseEntity<List<FavoriteDto>> getAllFavoritesByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(favoriteService.getAllFavoriteByUserId(userId));
    }

    @DeleteMapping("{userId}/delete/{id}")
    public ResponseEntity<String> deleteFavoriteById(@PathVariable("userId") Long userId,
                                                     @PathVariable("id") Long id) {
        favoriteService.deleteFavoriteProductById(id, userId);
        return ResponseEntity.ok("Favorite product deleted successfully");
    }

    @DeleteMapping("{userId}/{productId}")
    public ResponseEntity<ResultDto> deleteFavoriteByUserAndProductId(@PathVariable("userId") Long userId,
                                                                      @PathVariable("productId") Long productId) {
        ResultDto resultDto = favoriteService.deleteFavoriteProduct(userId, productId);
        return ResponseEntity.ok(resultDto);
    }

}
