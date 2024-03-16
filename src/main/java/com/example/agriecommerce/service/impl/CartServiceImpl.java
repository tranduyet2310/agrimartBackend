package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Cart;
import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.CartDto;
import com.example.agriecommerce.repository.CartRepository;
import com.example.agriecommerce.repository.ProductRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDto addToCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(1);

        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    public List<CartDto> getAllItemsByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not have any item in cart")
        );
        return carts.stream()
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartDto changeQuantity(Long userId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUserIdAndProductId(userId, productId).orElseThrow(
                () -> new ResourceNotFoundException("Item does not found in db")
        );

        cart.setQuantity(quantity);
        Cart updatedCart = cartRepository.save(cart);

        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeFromCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("item does not found in db")
        );
        cartRepository.delete(cart);
    }
}
