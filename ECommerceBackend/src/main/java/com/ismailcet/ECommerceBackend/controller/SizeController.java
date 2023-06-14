package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.request.CreateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllSizesResponse;
import com.ismailcet.ECommerceBackend.service.SizeService;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
public class SizeController {

    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @PostMapping()
    public ResponseEntity<String> createSize(@RequestBody CreateSizeRequest createSizeRequest){
            return new ResponseEntity<>(
                    sizeService.createSize(createSizeRequest),
                    HttpStatus.CREATED
            );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSizeBySizeId(@PathVariable("id") Integer id, @RequestBody UpdateSizeRequest updateSizeRequest){
            return ResponseEntity.ok(sizeService.updateSizeBySizeId(id, updateSizeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSizeBySizeId(@PathVariable("id") Integer id){
        sizeService.deleteSizeBySizeId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<GetAllSizesResponse>> getAllSizes(){
            return ResponseEntity.ok(sizeService.getAllSizes());
    }
}
