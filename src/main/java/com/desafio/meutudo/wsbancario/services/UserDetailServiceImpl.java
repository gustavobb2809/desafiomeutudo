package com.desafio.meutudo.wsbancario.services;

import com.desafio.meutudo.wsbancario.models.User;
import com.desafio.meutudo.wsbancario.respository.UserRepository;
import com.desafio.meutudo.wsbancario.security.data.UserDetailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("unauthenticated  user: " + userEntity.get().getEmail());
        }

        return new UserDetailData(userEntity);
    }
}
