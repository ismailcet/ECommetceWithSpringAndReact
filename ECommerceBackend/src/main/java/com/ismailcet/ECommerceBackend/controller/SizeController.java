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
        try{
            return sizeService.createSize(createSizeRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSizeBySizeId(@PathVariable("id") Integer id, @RequestBody UpdateSizeRequest updateSizeRequest){
        try{
            return sizeService.updateSizeBySizeId(id, updateSizeRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSizeBySizeId(@PathVariable("id") Integer id){
        try{
            return sizeService.deleteSizeBySizeId(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public ResponseEntity<List<GetAllSizesResponse>> getAllSizes(){
        try {
            return sizeService.getAllSizes();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
