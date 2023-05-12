package com.ismailcet.ECommerceBackend.JWT;

import com.ismailcet.ECommerceBackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private com.ismailcet.ECommerceBackend.model.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {} " + username);
        userDetail = userRepository.findByEmail(username);
        if(!Objects.isNull(userDetail)){
            return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User Not Found");
        }
    }

    public com.ismailcet.ECommerceBackend.model.User getUserDetail(){
        return userDetail;
    }
}
