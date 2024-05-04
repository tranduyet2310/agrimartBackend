package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.FieldDto;
import com.example.agriecommerce.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/field")
public class FieldController {
    private final FieldService fieldService;
    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping("{supplierId}")
    public ResponseEntity<FieldDto> createNewCrops(@PathVariable("supplierId") Long supplierId,
                                                   @RequestBody FieldDto fieldDto){
        FieldDto result = fieldService.createNewCrops(supplierId, fieldDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("{fieldId}")
    public ResponseEntity<FieldDto> updateCrops(@PathVariable("fieldId") Long fieldId,
                                                   @RequestBody FieldDto fieldDto){
        return ResponseEntity.ok(fieldService.updateCropsField(fieldId, fieldDto));
    }

    @PatchMapping("{fieldId}/complete")
    public ResponseEntity<FieldDto> completeCrops(@PathVariable("fieldId") Long fieldId){
        return ResponseEntity.ok(fieldService.completeCrops(fieldId));
    }

    @PatchMapping("{fieldId}")
    public ResponseEntity<FieldDto> updateYield(@PathVariable("fieldId") Long fieldId,
                                                @RequestBody FieldDto fieldDto){
        return ResponseEntity.ok(fieldService.updateYield(fieldId, fieldDto));
    }

    @GetMapping("{fieldId}/detail")
    public ResponseEntity<FieldDto> getFieldById(@PathVariable("fieldId") Long fieldId){
        return ResponseEntity.ok(fieldService.getFieldById(fieldId));
    }

    @GetMapping("{supplierId}")
    public ResponseEntity<List<FieldDto>> getAllFieldBySupplierId(@PathVariable("supplierId") Long supplierId){
        return ResponseEntity.ok(fieldService.getAllFieldBySupplierId(supplierId));
    }
}
