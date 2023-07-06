package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.request.CreateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllSizesResponse;
import com.ismailcet.ECommerceBackend.entity.Size;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.SizeNotFoundException;
import com.ismailcet.ECommerceBackend.repository.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SizeServiceTest {

    private SizeRepository sizeRepository;
    private JwtFilter jwtFilter;
    private SizeService sizeService;

    @BeforeEach
    public void setUp(){
        sizeRepository = mock(SizeRepository.class);
        jwtFilter = mock(JwtFilter.class);

        sizeService = new SizeService(sizeRepository, jwtFilter);
    }

    @Test
    public void testCreateSize_whenUserRoleIsAdminAndSizeNameIsNotExist_shouldSuccessfullyCreatedAndReturnString(){
        CreateSizeRequest given =
                new CreateSizeRequest("test-size");

        Size size = Size.builder()
                .name("test-size")
                .build();

        String excepted = "Size Successfully added ! ";
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findByName(given.getName())).thenReturn(null);
        when(sizeRepository.save(any(Size.class))).thenReturn(size);

        String actual =
                sizeService.createSize(given);

        assertEquals(excepted, actual);

        verify(sizeRepository).findByName(any(String.class));
        verify(sizeRepository).save(any(Size.class));
    }
    @Test
    public void testCreateSize_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        CreateSizeRequest given =
                new CreateSizeRequest("test-size");
        AuthenticationException excepted =
                new AuthenticationException("Unauthorized access.");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> sizeService.createSize(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testCreateSize_whenUserRoleIsAdminAndSizeNameIsExist_shouldThrowsSizeNotFoundException(){
        CreateSizeRequest given =
                new CreateSizeRequest("test-size");
        Size size = Size.builder()
                .name("test-size")
                .build();
        SizeNotFoundException excepted = new SizeNotFoundException("Size Name already exist");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findByName(any(String.class))).thenReturn(size);

        SizeNotFoundException actual = assertThrows(SizeNotFoundException.class,
                ()->sizeService.createSize(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findByName(any(String.class));
    }
    @Test
    public void testUpdateSizeBySizeId_whenUserRoleIsAdminAndSizeIdIsExist_shouldReturnString(){
        Integer sizeId = 1;

        UpdateSizeRequest given =
                new UpdateSizeRequest("test-size-upd");

        Size size = Size.builder()
                .id(1)
                .name("test-size")
                .build();
        Size updated = Size.builder()
                .id(1)
                .name("test-size-upd")
                .build();

        String excepted =
                "Size Successfully Updated ! ";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(any(Integer.class))).thenReturn(Optional.of(size));
        when(sizeRepository.save(any(Size.class))).thenReturn(updated);

        String actual =
                sizeService.updateSizeBySizeId(sizeId, given);

        assertEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
        verify(sizeRepository).save(any(Size.class));
    }
    @Test
    public void testUpdateSizeBySizeId_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        Integer sizeId = 1;
        UpdateSizeRequest given =
                new UpdateSizeRequest("test-size");
        AuthenticationException excepted =
                new AuthenticationException("Unauthorized access.");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> sizeService.updateSizeBySizeId(sizeId,given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testUpdateSizeBySizeId_whenUserRoleIsAdminAndSizeIdIsNotExist_shouldThrowSizeNotFoundException(){
        Integer sizeId = 1;

        UpdateSizeRequest given =
                new UpdateSizeRequest("test-size-upd");

        Size size = Size.builder()
                .id(2)
                .name("test-size")
                .build();

        SizeNotFoundException excepted =
                new SizeNotFoundException("Size Id does not exist ! ");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        SizeNotFoundException actual = assertThrows(SizeNotFoundException.class,
                ()-> sizeService.updateSizeBySizeId(sizeId, given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
    }
    @Test
    public void testDeleteSizeBySizeId_whenUserRoleIsAdminAndSizeIdIsExist_shouldDeleteSuccessfullyAndReturnString(){
        Integer sizeId = 1;

        Size size = Size.builder()
                .id(1)
                .name("test-size")
                .build();

        String excepted =
                "Size Successfully Deleted";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(any(Integer.class))).thenReturn(Optional.of(size));
        doNothing().when(sizeRepository).deleteById(any(Integer.class));

        String actual =
                sizeService.deleteSizeBySizeId(sizeId);

        assertEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
        verify(sizeRepository).deleteById(any(Integer.class));
    }
    @Test
    public void testDeleteSizeBySizeId_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        Integer sizeId = 1;

        AuthenticationException excepted =
                new AuthenticationException("Unauthorized access.");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> sizeService.deleteSizeBySizeId(sizeId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testDeleteSizeBySizeId_whenUserRoleIsAdminAndSizeIdIsNotExist_shouldThrowSizeNotFoundException(){
        Integer sizeId = 1;

        SizeNotFoundException excepted =
                new SizeNotFoundException("Size Id does not exist");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        SizeNotFoundException actual = assertThrows(SizeNotFoundException.class,
                ()-> sizeService.deleteSizeBySizeId(sizeId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
    }
    @Test
    public void testGetAllSizes_whenUserRoleIsAdmin_shouldReturnListTypeOfGetAlSizesResponse(){
        Size size1 = Size.builder()
                .id(1)
                .name("test-size")
                .build();
        Size size2 = Size.builder()
                .id(2)
                .name("test-size")
                .build();

        List<Size> sizes =
                Arrays.asList(size1, size2);

        GetAllSizesResponse s1 =
                new GetAllSizesResponse(1, "test-size");

        GetAllSizesResponse s2 =
                new GetAllSizesResponse(2, "test-size");

        List<GetAllSizesResponse> excepted = Arrays.asList(s1, s2);

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findAll()).thenReturn(sizes);


        List<GetAllSizesResponse> actual =
                sizeService.getAllSizes();

        assertEquals(excepted.size(), actual.size());
        assertEquals(excepted.get(0),actual.get(0));

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findAll();

    }
    @Test
    public void testGetAllSizes_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        AuthenticationException excepted =
                new AuthenticationException("Unauthorized access.");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> sizeService.getAllSizes());

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }

}