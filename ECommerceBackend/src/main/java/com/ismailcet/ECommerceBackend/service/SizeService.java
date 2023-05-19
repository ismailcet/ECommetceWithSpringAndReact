package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.request.CreateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllSizesResponse;
import com.ismailcet.ECommerceBackend.entity.Size;
import com.ismailcet.ECommerceBackend.repository.SizeRepository;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SizeService {

    private final SizeRepository sizeRepository;
    private final JwtFilter jwtFilter;
    public SizeService(SizeRepository sizeRepository, JwtFilter jwtFilter) {
        this.sizeRepository = sizeRepository;
        this.jwtFilter = jwtFilter;
    }

    public ResponseEntity<String> createSize(CreateSizeRequest createSizeRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Size size = sizeRepository.findByName(createSizeRequest.getName());
                if(Objects.isNull(size)){
                    Size newCat = Size.builder()
                            .name(createSizeRequest.getName())
                            .build();
                    sizeRepository.save(newCat);

                    return SystemUtils.getResponseEntity("Size Successfully added ! " ,HttpStatus.CREATED);
                }
                return SystemUtils.getResponseEntity("Size Name already exist", HttpStatus.BAD_REQUEST);
            }
            return SystemUtils.getResponseEntity("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> updateSizeBySizeId(Integer id, UpdateSizeRequest updateSizeRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Size> size =
                        sizeRepository.findById(id);
                if(size.isPresent()){
                    size.get().setName(updateSizeRequest.getName());
                    sizeRepository.save(size.get());
                    return SystemUtils.getResponseEntity("Size Successfully Updated ! ", HttpStatus.OK);
                }
                return SystemUtils.getResponseEntity("Size Id does not exist ! " , HttpStatus.BAD_REQUEST);
            }
            return SystemUtils.getResponseEntity("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> deleteSizeBySizeId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Size> size = sizeRepository.findById(id);
                if(size.isPresent()){
                    sizeRepository.deleteById(id);
                    return SystemUtils.getResponseEntity("Size Successfully Deleted", HttpStatus.OK);
                }
                return SystemUtils.getResponseEntity("Size Id does not exist", HttpStatus.BAD_REQUEST);
            }
            return SystemUtils.getResponseEntity("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<GetAllSizesResponse>> getAllSizes() {
        try{
            if(jwtFilter.isAdmin()){
                List<GetAllSizesResponse> sizes =
                        sizeRepository.findAll().stream()
                                .map(s->new GetAllSizesResponse(s.getId(),s.getName()))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(sizes, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
