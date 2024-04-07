package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Field;
import com.example.agriecommerce.entity.FieldDetail;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.FieldDetailDto;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.repository.FieldDetailRepository;
import com.example.agriecommerce.repository.FieldRepository;
import com.example.agriecommerce.service.FieldDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.example.agriecommerce.utils.AppConstants.*;

@Service
public class FieldDetailServiceImpl implements FieldDetailService {
    private final FieldDetailRepository fieldDetailRepository;
    private final FieldRepository fieldRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public FieldDetailServiceImpl(FieldDetailRepository fieldDetailRepository, FieldRepository fieldRepository, ModelMapper modelMapper) {
        this.fieldDetailRepository = fieldDetailRepository;
        this.fieldRepository = fieldRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FieldDetailDto createFieldDetail(Long fieldId, FieldDetailDto fieldDetailDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );

        String cropsType = field.getCropsType();
        if (cropsType.equals(LONG_TERM_PLANT)){
            int record = fieldDetailRepository.countByFieldId(fieldId);
            if (record >= MAX_LONG_TERM_PLANT)
                throw new AgriMartException(HttpStatus.BAD_REQUEST,"Exceeded total record in DB");
        } else if (cropsType.equals(SHORT_TERM_PLANT)) {
            int record = fieldDetailRepository.countByFieldId(fieldId);
            if (record >= MAX_SHORT_TERM_PLANT)
                throw new AgriMartException(HttpStatus.BAD_REQUEST,"Exceeded total record in DB");
        }

        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.setCropsStatus(fieldDetailDto.getCropsStatus());
        fieldDetail.setDetails(fieldDetailDto.getDetails());
        fieldDetail.setField(field);

        FieldDetail savedFieldDetail = fieldDetailRepository.save(fieldDetail);

        return modelMapper.map(savedFieldDetail, FieldDetailDto.class);
    }

    @Override
    public FieldDetailDto updateFieldDetail(Long fieldId, FieldDetailDto fieldDetailDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );
        FieldDetail detail = fieldDetailRepository.findById(fieldDetailDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("field detail does not exists")
        );

        detail.setId(fieldDetailDto.getId());
        detail.setField(field);
        detail.setDetails(fieldDetailDto.getDetails());
        detail.setCropsStatus(fieldDetailDto.getCropsStatus());

        FieldDetail updatedFieldDetail = fieldDetailRepository.save(detail);

        return modelMapper.map(updatedFieldDetail, FieldDetailDto.class);
    }

    @Override
    public ResultDto deleteFieldDetail(Long fieldDetailId) {
        FieldDetail detail = fieldDetailRepository.findById(fieldDetailId).orElseThrow(
                () -> new ResourceNotFoundException("field detail does not exists")
        );
        fieldDetailRepository.delete(detail);
        return new ResultDto(true, "Delete field detail successfully");
    }
}
