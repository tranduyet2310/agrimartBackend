package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Favorite;
import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.FavoriteDto;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.repository.FavoriteRepository;
import com.example.agriecommerce.repository.ProductRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.FavoriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository,
                               ModelMapper modelMapper) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FavoriteDto createFavoriteProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        Favorite savedFavorite = favoriteRepository.save(favorite);

        return modelMapper.map(savedFavorite, FavoriteDto.class);
    }

    @Override
    public FavoriteDto getFavoriteProductById(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("favorite product", "id", id)
        );
        return modelMapper.map(favorite, FavoriteDto.class);
    }

    @Override
    public List<FavoriteDto> getAllFavoriteByUserId(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not have any favorite product")
        );
        return favorites.stream()
                .map(favorite -> modelMapper.map(favorite, FavoriteDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFavoriteProductById(Long id, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("favorite product", "id", id)
        );

        if (!Objects.equals(favorite.getUser().getId(), userId)) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "User id does not match");
        }
        favoriteRepository.delete(favorite);
    }

    @Override
    public ResultDto deleteFavoriteProduct(Long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId).orElseThrow(
                () -> new ResourceNotFoundException("favorite does not exists with userId, productId: " + userId + "-" + productId)
        );
        favoriteRepository.delete(favorite);

        ResultDto resultDto = new ResultDto();
        resultDto.setSuccessful(true);
        resultDto.setMessage("Delete favorite products successfully");

        return resultDto;
    }
}
