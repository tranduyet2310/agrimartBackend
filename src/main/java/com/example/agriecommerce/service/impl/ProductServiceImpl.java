package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.ProductDto;
import com.example.agriecommerce.repository.*;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private ModelMapper modelMapper;
    private CloudinaryService cloudinaryService;
    private ImageRepository imageRepository;
    private CategoryRepository categoryRepository;
    private SubCategoryRepository subCategoryRepository;
    private WarehouseReposiotry warehouseReposiotry;
    private SupplierRepository supplierRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ModelMapper modelMapper,
                              CloudinaryService cloudinaryService,
                              ImageRepository imageRepository,
                              CategoryRepository categoryRepository,
                              SubCategoryRepository subCategoryRepository,
                              WarehouseReposiotry warehouseReposiotry,
                              SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.warehouseReposiotry = warehouseReposiotry;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public ProductDto createProduct(Long supplierId, ProductDto productDto, List<MultipartFile> files) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setStandardPrice(productDto.getStandardPrice());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setQuantity(productDto.getQuantity());
        product.setActive(productDto.isActive());
        product.setNew(productDto.isNew());
        product.setAvailable(productDto.isAvailable());

        String categoryName = productDto.getCategoryName();
        String subcategoryName = productDto.getSubCategoryName();
        String warehouseName = productDto.getWarehouseName();

        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("category does not exists " + categoryName)
        );
        SubCategory subCategory = subCategoryRepository.findBySubcategoryName(subcategoryName).orElseThrow(
                () -> new ResourceNotFoundException("subcategory does not exists " + subcategoryName)
        );
        Warehouse warehouse = warehouseReposiotry.findByWarehouseName(warehouseName).orElseThrow(
                () -> new ResourceNotFoundException("warehouse does not exists " + warehouseName)
        );
        // check valid value field
        product.setCategory(category);
        List<SubCategory> subCategories = category.getSubCategories();
        if (subCategories.contains(subCategory)) {
            product.setSubCategory(subCategory);
        } else throw new AgriMartException(HttpStatus.BAD_REQUEST, "Subcategory does not match with Category");

        List<Warehouse> warehouses = supplier.getWarehouses();
        if (warehouses.contains(warehouse)) {
            product.setWarehouse(warehouse);
        } else throw new AgriMartException(HttpStatus.BAD_REQUEST, "Warehouse does not belong to Supplier");
        product.setSupplier(supplier);

        List<Image> images = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            Map result = cloudinaryService.upload(files.get(i));
            // save to images table
            Image image = new Image((String) result.get("original_filename"),
                    (String) result.get("url"),
                    (String) result.get("public_id"));
            images.add(image);
            if (i == 0) {
                product.setProductImage((String) result.get("url"));
            }
        }
        product.setImages(images);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getProductBySupplierId(Long supplierId) {
        List<Product> products = productRepository.findBySupplierId(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not have any products")
        );
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto activeProduct(Long suppilerId, Long productId) {
        Supplier supplier = supplierRepository.findById(suppilerId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", suppilerId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        product.setId(productId);
        product.setSupplier(supplier);
        product.setActive(Boolean.TRUE);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto inactiveProduct(Long suppilerId, Long productId) {
        Supplier supplier = supplierRepository.findById(suppilerId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", suppilerId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        product.setId(productId);
        product.setSupplier(supplier);
        product.setActive(Boolean.FALSE);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long supplierId, Long productId, ProductDto productDto, List<MultipartFile> files) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        product.setId(productId);
        product.setSupplier(supplier);

        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setStandardPrice(productDto.getStandardPrice());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setQuantity(productDto.getQuantity());
        product.setActive(productDto.isActive());
        product.setNew(productDto.isNew());
        product.setAvailable(productDto.isAvailable());

        String categoryName = productDto.getCategoryName();
        String subcategoryName = productDto.getSubCategoryName();
        String warehouseName = productDto.getWarehouseName();

        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("category does not exists " + categoryName)
        );
        SubCategory subCategory = subCategoryRepository.findBySubcategoryName(subcategoryName).orElseThrow(
                () -> new ResourceNotFoundException("subcategory does not exists " + subcategoryName)
        );
        Warehouse warehouse = warehouseReposiotry.findByWarehouseName(warehouseName).orElseThrow(
                () -> new ResourceNotFoundException("warehouse does not exists " + warehouseName)
        );
        // check valid value field
        product.setCategory(category);
        List<SubCategory> subCategories = category.getSubCategories();
        if (subCategories.contains(subCategory)) {
            product.setSubCategory(subCategory);
        } else throw new AgriMartException(HttpStatus.BAD_REQUEST, "Subcategory does not match with Category");

        List<Warehouse> warehouses = supplier.getWarehouses();
        if (warehouses.contains(warehouse)) {
            product.setWarehouse(warehouse);
        } else throw new AgriMartException(HttpStatus.BAD_REQUEST, "Warehouse does not belong to Supplier");

        List<Image> imageList = product.getImages();
        for (Image image : imageList) {
            String imageUrl = image.getImageUrl();
            Image oldImage = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                    () -> new ResourceNotFoundException("Image does not exists")
            );
            String cloudinaryImageId = oldImage.getImageId();
            cloudinaryService.delete(cloudinaryImageId);
            imageRepository.delete(oldImage);
        }
        imageList.clear();

        List<Image> images = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            Map result = cloudinaryService.upload(files.get(i));
            // save to images table
            Image image = new Image((String) result.get("original_filename"),
                    (String) result.get("url"),
                    (String) result.get("public_id"));
            images.add(image);
            if (i == 0) {
                product.setProductImage((String) result.get("url"));
            }
        }
        imageList.addAll(images);
        product.setImages(imageList);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long supplierId, Long productId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        productRepository.delete(product);
    }


}
