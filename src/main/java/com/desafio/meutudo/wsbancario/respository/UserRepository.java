package com.desafio.meutudo.wsbancario.respository;

import com.desafio.meutudo.wsbancario.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
