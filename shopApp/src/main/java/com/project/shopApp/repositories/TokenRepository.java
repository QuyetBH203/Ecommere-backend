package com.project.shopApp.repositories;

import com.project.shopApp.models.Token;
import com.project.shopApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token,Long> {
    List<Token> findByUser(User user);

    Token findByToken(String token);

    Token findByRefreshToken(String refreshToken);
}
