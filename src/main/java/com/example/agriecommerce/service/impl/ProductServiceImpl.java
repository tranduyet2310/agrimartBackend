package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.*;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.ProductService;
import com.example.agriecommerce.utils.AES;
import com.example.agriecommerce.utils.Encryption;
import com.example.agriecommerce.utils.SupplierCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final WarehouseReposiotry warehouseReposiotry;
    private final SupplierRepository supplierRepository;
    private final Encryption encryption;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ModelMapper modelMapper,
                              CloudinaryService cloudinaryService,
                              ImageRepository imageRepository,
                              CategoryRepository categoryRepository,
                              SubCategoryRepository subCategoryRepository,
                              WarehouseReposiotry warehouseReposiotry,
                              SupplierRepository supplierRepository,
                              Encryption encryption) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.warehouseReposiotry = warehouseReposiotry;
        this.supplierRepository = supplierRepository;
        this.encryption = encryption;
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
        product.setSold(0L);

        String categoryName = productDto.getCategoryName();
        String subcategoryName = productDto.getSubCategoryName();
        String warehouseName = productDto.getWarehouseName();

        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("category does not exists " + categoryName)
        );
        SubCategory subCategory = subCategoryRepository.findBySubcategoryName(subcategoryName).orElseThrow(
                () -> new ResourceNotFoundException("subcategory does not exists " + subcategoryName)
        );
        Warehouse warehouse = warehouseReposiotry.findByWarehouseNameAndSupplierId(warehouseName, supplierId).orElseThrow(
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
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"));
            images.add(image);
            if (i == 0) {
                product.setProductImage((String) result.get("secure_url"));
            }
        }
        product.setImages(images);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto createProductV2(Long supplierId, ProductDto productDto, List<MultipartFile> files) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        String secretKey = supplier.getAesKey();
        String iv = supplier.getIv();

        ProductDto decryptDto = encryption.decryptProductDto(productDto, secretKey, iv);

        Product product = new Product();
        product.setProductName(decryptDto.getProductName());
        product.setDescription(decryptDto.getDescription());
        product.setStandardPrice(decryptDto.getStandardPrice());
        product.setDiscountPrice(decryptDto.getDiscountPrice());
        product.setQuantity(decryptDto.getQuantity());
        product.setActive(decryptDto.isActive());
        product.setNew(decryptDto.isNew());
        product.setAvailable(decryptDto.isAvailable());
        product.setSold(0L);

        String categoryName = decryptDto.getCategoryName();
        String subcategoryName = decryptDto.getSubCategoryName();
        String warehouseName = decryptDto.getWarehouseName();

        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("category does not exists " + categoryName)
        );
        SubCategory subCategory = subCategoryRepository.findBySubcategoryName(subcategoryName).orElseThrow(
                () -> new ResourceNotFoundException("subcategory does not exists " + subcategoryName)
        );
        Warehouse warehouse = warehouseReposiotry.findByWarehouseNameAndSupplierId(warehouseName, supplierId).orElseThrow(
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
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"));
            images.add(image);
            if (i == 0) {
                product.setProductImage((String) result.get("secure_url"));
            }
        }
        product.setImages(images);

        Product savedProduct = productRepository.save(product);

        ProductDto result = modelMapper.map(savedProduct, ProductDto.class);

        return encryption.encryptProductDto(result, secretKey, iv);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductResponse getProductBySupplierId(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findBySupplierId(supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not have any products")
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductBySupplierIdV2(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findBySupplierId(supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not have any products")
        );

        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();
        List<ProductDto> encryptedContent = new ArrayList<>();

        String secretKey = supplier.getAesKey();
        String iv = supplier.getIv();

        System.out.println("secretKey "+secretKey);
        System.out.println("iv "+iv);

        for (ProductDto d : content){
            encryptedContent.add(encryption.encryptProductDto(d, secretKey, iv));
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(encryptedContent);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findByIsActive(true, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Don't have any active products")
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findByCategoryIdAndIsActive(categoryId, true, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Don't have any product with category id " + categoryId)
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductBySubcategoryId(Long subcategoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findBySubCategoryIdAndIsActive(subcategoryId, true, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Don't have any product with subcategory id " + subcategoryId)
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsWithDiscount(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findProductsWithDiscount(pageable).orElseThrow(
                () -> new ResourceNotFoundException("Don't have any product with discountPrice > 0")
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getUpcomingProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findUpcomingProduct(pageable).orElseThrow(
                () -> new ResourceNotFoundException("There are no upcoming products yet")
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProduct(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.searchProduct(query, pageable).orElseThrow(
                () -> new ResourceNotFoundException("There are no results for that condition " + query)
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductBySupplier(String query, Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        String productName = "%"+query+"%";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.searchProductBySupplier(productName, supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("There are no results for that condition " + query)
        );

        List<Product> products = productPage.getContent();
        List<ProductDto> content = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPage(productPage.getTotalPages());
        productResponse.setLast(productPage.isLast());

        return productResponse;
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
        Warehouse warehouse = warehouseReposiotry.findByWarehouseNameAndSupplierId(warehouseName, supplierId).orElseThrow(
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
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"));
            images.add(image);
            if (i == 0) {
                product.setProductImage((String) result.get("secure_url"));
            }
        }
        imageList.addAll(images);
        product.setImages(imageList);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProductInfo(Long supplierId, Long productId, ProductDto productDto) {
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
        Warehouse warehouse = warehouseReposiotry.findByWarehouseNameAndSupplierId(warehouseName, supplierId).orElseThrow(
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

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ResultDto deleteProduct(Long supplierId, Long productId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        productRepository.delete(product);

        return new ResultDto(true, "Delete product successfully");
    }

    @Override
    public ProductDto increaseSoldNumber(Long productId, Long quantity) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        if (quantity > 0){
            Long newValue = product.getSold() + quantity;
            product.setSold(newValue);
        }
        product.setId(productId);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto decreaseQuantity(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        if (quantity > 0){
            Integer newValue = product.getQuantity() - quantity;
            product.setQuantity(newValue);
        }
        product.setId(productId);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public List<CategoryDto> getCategoryBySupplierId(Long supplierId) {
        List<SupplierCategory> list = productRepository.getCategoryBySupplierId(supplierId);

        List<SupplierCategoryDto> categoryDtos = new ArrayList<>();

        for (SupplierCategory s : list){
            SupplierCategoryDto dto = new SupplierCategoryDto();
            dto.setCategoryId(s.getCategoryId());
            dto.setImageUrl(s.getImageUrl());
            dto.setCategoryName(s.getCategoryName());
            dto.setSubcategoryId(s.getSubcategoryId());
            dto.setSubcategoryName(s.getSubcategoryName());
            categoryDtos.add(dto);
        }

        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (SupplierCategoryDto sc : categoryDtos){
            boolean check = false;
            for (CategoryDto categoryDto : categoryDtoList){
                if (categoryDto.getCategoryName().equals(sc.getCategoryName())){
                    List<SubCategoryDto> dtos = categoryDto.getSubCategoryList();
                    SubCategoryDto dto = new SubCategoryDto();
                    dto.setId(sc.getSubcategoryId());
                    dto.setSubcategoryName(sc.getSubcategoryName());
                    dto.setCategoryName(sc.getCategoryName());
                    dtos.add(dto);
                    categoryDto.setSubCategoryList(dtos);
                    check = true;
                }
            }
            if (!check){
                createNewCategoryDto(sc, categoryDtoList);
            }
        }

        return categoryDtoList;
    }

    private void createNewCategoryDto(SupplierCategoryDto sc, List<CategoryDto> categoryDtoList){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(sc.getCategoryId());
        categoryDto.setCategoryName(sc.getCategoryName());
        categoryDto.setCategoryImage(sc.getImageUrl());

        List<SubCategoryDto> subCategoryDtos = new ArrayList<>();
        SubCategoryDto dto = new SubCategoryDto();
        dto.setId(sc.getSubcategoryId());
        dto.setSubcategoryName(sc.getSubcategoryName());
        dto.setCategoryName(sc.getCategoryName());
        subCategoryDtos.add(dto);

        categoryDto.setSubCategoryList(subCategoryDtos);
        categoryDtoList.add(categoryDto);
    }

    @Override
    public ResultDto countTotalProducts(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Long result = productRepository.countProducts(supplierId);
        if (result == null) result = 0L;
        return new ResultDto(true, result.toString());
    }

    @Override
    public ResultDto countSoldProducts(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Long result = productRepository.countSoldProduct(supplierId);
        if (result == null) result = 0L;
        return new ResultDto(true, result.toString());
    }

    @Override
    public ProductDto updateProductState(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        boolean isActive = product.isActive();
        product.setActive(!isActive);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ComparationDto getStatisticProduct(int month, int year) {
        ComparationDto dto = new ComparationDto();
        int previousMonth = month - 1;
        int previousYear = year;
        if (previousMonth == 0){
            previousMonth = 12;
            previousYear = year - 1;
        }
        long current = productRepository.countProductsByMonthAndYear(month, year);
        long previous = productRepository.countProductsByMonthAndYear(previousMonth, previousYear);
        long total = productRepository.countAllProductsByYears(previousYear);
        long gaps = current - previous;
        if (gaps < 0) gaps *=-1;

        dto.setCurrent((double) current);
        dto.setPrevious((double) previous);
        dto.setGaps((double) gaps);
        dto.setTotal((double) total);

        return dto;
    }
}
