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
    private FieldService fieldService;
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

    @GetMapping("{supplierId}")
    public ResponseEntity<List<FieldDto>> getAllFieldBySupplierId(@PathVariable("supplierId") Long supplierId){
        return ResponseEntity.ok(fieldService.getAllFieldBySupplierId(supplierId));
    }
}
