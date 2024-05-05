package com.project.shopApp.services.token;

import com.project.shopApp.models.Token;
import com.project.shopApp.models.User;
import org.springframework.stereotype.Service;


public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    Token refreshToken(String refreshToken, User user) throws Exception;
}
