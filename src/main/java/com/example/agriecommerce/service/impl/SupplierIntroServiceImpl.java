package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.SupplierIntro;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.SupplierIntroDto;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.repository.SupplierIntroRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.SupplierIntroService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SupplierIntroServiceImpl implements SupplierIntroService {
    private final SupplierIntroRepository introRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierIntroServiceImpl(SupplierIntroRepository introRepository,
                                    ModelMapper modelMapper,
                                    CloudinaryService cloudinaryService,
                                    ImageRepository imageRepository,
                                    SupplierRepository supplierRepository) {
        this.introRepository = introRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierIntroDto createSupplierIntro(Long supplierId, SupplierIntroDto introDto, List<MultipartFile> files) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );

        SupplierIntro supplierIntro = new SupplierIntro();
        supplierIntro.setDescription(introDto.getDescription());
        supplierIntro.setType(introDto.getType());
        supplierIntro.setSupplier(supplier);

        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            Map result = cloudinaryService.upload(file);
            // save to images table
            Image image = new Image((String) result.get("original_filename"),
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"));
            images.add(image);
        }
        supplierIntro.setImages(images);

        SupplierIntro savedSupplierIntro = introRepository.save(supplierIntro);

        return modelMapper.map(savedSupplierIntro, SupplierIntroDto.class);
    }

    @Override
    public List<SupplierIntroDto> getAllSupplierIntro(Long supplierId) {
        List<SupplierIntro> supplierIntros = introRepository.findBySupplierId(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not have any introduction")
        );
        return supplierIntros.stream()
                .map(supplierIntro -> modelMapper.map(supplierIntro, SupplierIntroDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SupplierIntroDto updateSupplierIntro(Long supplierId, Long introId, SupplierIntroDto introDto, List<MultipartFile> files) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );
        SupplierIntro supplierIntro = introRepository.findById(introId).orElseThrow(
                () -> new ResourceNotFoundException("supplierIntro does not exists")
        );

        supplierIntro.setId(introId);
        supplierIntro.setDescription(introDto.getDescription());
        supplierIntro.setType(introDto.getType());
        supplierIntro.setSupplier(supplier);

        List<Image> imageList = supplierIntro.getImages();
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
        for (MultipartFile file : files) {
            Map result = cloudinaryService.upload(file);
            // save to images table
            Image image = new Image((String) result.get("original_filename"),
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"));
            images.add(image);
        }
        imageList.addAll(images);
        supplierIntro.setImages(imageList);


        SupplierIntro updatedSupplierIntro = introRepository.save(supplierIntro);

        return modelMapper.map(updatedSupplierIntro, SupplierIntroDto.class);
    }

    @Override
    public SupplierIntroDto updateDescriptionIntro(Long introId, SupplierIntroDto introDto) {
        Supplier supplier = supplierRepository.findById(introDto.getSupplierId()).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );
        SupplierIntro supplierIntro = introRepository.findById(introId).orElseThrow(
                () -> new ResourceNotFoundException("supplierIntro does not exists")
        );

        supplierIntro.setId(introId);
        supplierIntro.setDescription(introDto.getDescription());
        supplierIntro.setType(introDto.getType());
        supplierIntro.setSupplier(supplier);

        SupplierIntro updatedSupplierIntro = introRepository.save(supplierIntro);

        return modelMapper.map(updatedSupplierIntro, SupplierIntroDto.class);
    }
}
