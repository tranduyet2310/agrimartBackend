package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.CooperationRepository;
import com.example.agriecommerce.repository.FieldRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.service.FieldService;
import com.example.agriecommerce.utils.AppConstants;
import com.example.agriecommerce.utils.CropsNameStatistic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final SupplierRepository supplierRepository;
    private final CooperationRepository cooperationRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository,
                            SupplierRepository supplierRepository,
                            ModelMapper modelMapper,
                            CooperationRepository cooperationRepository) {
        this.fieldRepository = fieldRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.cooperationRepository = cooperationRepository;
    }

    @Override
    public FieldDto createNewCrops(Long supplierId, FieldDto fieldDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );

        Field field = new Field();
        List<FieldDetail> fieldDetails = new ArrayList<>();

        field.setCropsName(fieldDto.getCropsName());
        field.setArea(fieldDto.getArea());
        field.setCropsType(fieldDto.getCropsType());
        field.setSeason(fieldDto.getSeason());
        field.setEstimateYield(fieldDto.getEstimateYield());
        field.setFieldDetails(fieldDetails);
        field.setSupplier(supplier);
        field.setIsComplete(false);
        field.setEstimatePrice(0L);
        field.setEstimateYield(0.0);

        Field savedField = fieldRepository.save(field);

        return modelMapper.map(savedField, FieldDto.class);
    }

    @Override
    public FieldDto updateCropsField(Long fieldId, FieldDto fieldDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );

        field.setCropsName(fieldDto.getCropsName());
        field.setArea(fieldDto.getArea());
        field.setCropsType(fieldDto.getCropsType());
        field.setSeason(fieldDto.getSeason());

        Field updatedField = fieldRepository.save(field);

        return modelMapper.map(updatedField, FieldDto.class);
    }

    @Override
    public FieldDto updateYield(Long fieldId, FieldDto fieldDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );

        Double currentYield = field.getEstimateYield();
        Double newYieldValue = fieldDto.getEstimateYield();

        if (newYieldValue < currentYield) {
            Double gapsValue = currentYield - newYieldValue;
            List<Cooperation> cooperationList = cooperationRepository.findByFieldIdAndDateCreated(fieldId);
            terminatedCooperation(cooperationList, gapsValue);
        }

        field.setEstimateYield(fieldDto.getEstimateYield());
        field.setEstimatePrice(fieldDto.getEstimatePrice());

        Field updatedField = fieldRepository.save(field);

        return modelMapper.map(updatedField, FieldDto.class);
    }

    @Override
    public FieldDto completeCrops(Long fieldId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );
        field.setIsComplete(true);
        Field savedField = fieldRepository.save(field);
        return modelMapper.map(savedField, FieldDto.class);
    }

    @Override
    public List<FieldDto> getAllFieldBySupplierId(Long supplierId) {
        List<Field> fields = fieldRepository.findByIdAndIsComplete(supplierId, false).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not have any filed")
        );

        return fields.stream()
                .map(field -> modelMapper.map(field, FieldDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FieldDto getFieldById(Long fieldId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );
        return modelMapper.map(field, FieldDto.class);
    }

    @Override
    public FieldResponse getAllFields(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Field> fieldPage = fieldRepository.findAll(pageable);

        List<Field> fields = fieldPage.getContent();
        List<FieldDto> content = fields.stream().map(field -> modelMapper.map(field, FieldDto.class)).toList();

        FieldResponse fieldResponse = new FieldResponse();
        fieldResponse.setContent(content);
        fieldResponse.setPageNo(fieldPage.getNumber());
        fieldResponse.setPageSize(fieldPage.getSize());
        fieldResponse.setTotalElements(fieldPage.getTotalElements());
        fieldResponse.setTotalPage(fieldPage.getTotalPages());
        fieldResponse.setLast(fieldPage.isLast());

        return fieldResponse;
    }

    private void terminatedCooperation(List<Cooperation> cooperationList, Double gapsValue) {
        for (Cooperation cooperation : cooperationList) {
            Double currentYield = cooperation.getRequireYield();
            if (gapsValue > 0) {
                if (currentYield >= gapsValue) {
                    cooperation.setCooperationStatus(OrderStatus.CANCELLED);
                    cooperation.setRequireYield(0.0);
                    cooperationRepository.save(cooperation);
                    break;
                } else {
                    gapsValue -= currentYield;
                    cooperation.setCooperationStatus(OrderStatus.CANCELLED);
                    cooperation.setRequireYield(0.0);
                    cooperationRepository.save(cooperation);
                }
            }
        }
    }

    @Override
    public ComparationDto getStatisticField(int month, int year) {
        ComparationDto dto = new ComparationDto();
        int previousMonth = month - 1;
        int previousYear = year;
        if (previousMonth == 0) {
            previousMonth = 12;
            previousYear = year - 1;
        }
        long current = fieldRepository.countFieldByMonthAndYear(month, year);
        long previous = fieldRepository.countFieldByMonthAndYear(previousMonth, previousYear);
        long total = fieldRepository.countTotalField(previousYear);

        long gaps = current - previous;
        if (gaps < 0) gaps *= -1;

        dto.setCurrent((double) current);
        dto.setPrevious((double) previous);
        dto.setGaps((double) gaps);
        dto.setTotal((double) total);

        return dto;
    }

    @Override
    public List<LineChartGardenDto> getGardenSource(int month, int year) {
        List<LineChartGardenDto> dataSource = new ArrayList<>();
        if (month <= 0 || month > 12)
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Month is invalid, m=" + month);

        for (int i = 1; i <= 12; i++) {
            LineChartGardenDto item = new LineChartGardenDto();
            long garden = fieldRepository.countFieldByMonthAndYear(i, year);
            double yields = cooperationRepository.countRequiredYieldByMonthAndYear(i, year);
            String name = AppConstants.listMonths[i - 1];

            item.setName(name);
            item.setGarden((double) garden);
            item.setYields(yields);

            dataSource.add(item);
        }

        return dataSource;
    }

    @Override
    public List<PieChartDto> getPieChart(int year) {
        List<PieChartDto> dataSource = new ArrayList<>();

        List<CropsNameStatistic> list = fieldRepository.getCropsNameStatistic(year);
        for (CropsNameStatistic c : list){
            PieChartDto item = new PieChartDto();
            item.setName(c.getCropsName());
            item.setValue(c.getSum());
            dataSource.add(item);
        }

        return dataSource;
    }
}
