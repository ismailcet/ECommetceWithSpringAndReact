package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.request.CreateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllSizesResponse;
import com.ismailcet.ECommerceBackend.entity.Size;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.SizeNotFoundException;
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

    public String createSize(CreateSizeRequest createSizeRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Size size = sizeRepository.findByName(createSizeRequest.getName());
                if(Objects.isNull(size)){
                    Size newCat = Size.builder()
                            .name(createSizeRequest.getName())
                            .build();
                    sizeRepository.save(newCat);

                    return "Size Successfully added ! ";
                }
                throw new SizeNotFoundException("Size Name already exist");
            }
            throw new AuthenticationException("Unauthorized access.");
        }catch (Exception ex){
            throw ex;
        }
    }

    public String updateSizeBySizeId(Integer id, UpdateSizeRequest updateSizeRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Size> size =
                        sizeRepository.findById(id);
                if(size.isPresent()){
                    size.get().setName(updateSizeRequest.getName());
                    sizeRepository.save(size.get());
                    throw new SizeNotFoundException("Size Successfully Updated ! ");
                }
                throw new SizeNotFoundException("Size Id does not exist ! ");
            }
            throw new AuthenticationException("Unauthorized access.");
        }catch (Exception ex){
            throw ex;
        }
    }

    public String deleteSizeBySizeId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Size> size = sizeRepository.findById(id);
                if(size.isPresent()){
                    sizeRepository.deleteById(id);
                    return "Size Successfully Deleted";
                }
                throw new SizeNotFoundException("Size Id does not exist");
            }
            throw new AuthenticationException("Unauthorized access.");
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<GetAllSizesResponse> getAllSizes() {
        try{
            if(jwtFilter.isAdmin()){
                List<GetAllSizesResponse> sizes =
                        sizeRepository.findAll().stream()
                                .map(s->new GetAllSizesResponse(s.getId(),s.getName()))
                                .collect(Collectors.toList());
                return sizes;
            }
            throw new SizeNotFoundException("Size Not Found ! ");
        }catch (Exception ex){
            throw ex;
        }
    }
}
