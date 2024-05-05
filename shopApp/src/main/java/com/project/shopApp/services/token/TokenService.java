package com.project.shopApp.services.token;

import com.project.shopApp.components.JwtTokenUtils;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.exceptions.ExpiredTokenException;
import com.project.shopApp.models.Token;
import com.project.shopApp.models.User;
import com.project.shopApp.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{
    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepository tokenRepository;

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @Transactional
    public Token addToken(User user, String token, boolean isMobileDevice) {
        List<Token> userTokens=tokenRepository.findByUser(user);
        int tokenCounts=userTokens.size();
        if(tokenCounts>=MAX_TOKENS){
            //Kiem tra xem trong danh sach userTokens co ton tai
            //it nhat mot token khong phai la mobile hay khong

            boolean hasNonMobileToken=!userTokens.stream().allMatch(Token::isMobile);
            Token tokenDelete;
            if(hasNonMobileToken){
                tokenDelete=userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            }else{
                tokenDelete=userTokens.get(0);
            }
            tokenRepository.delete(tokenDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        //Tao mot token moi cho nguoi dung

        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;

    }

    @Override
    @Transactional
    public Token refreshToken(String refreshToken, User user) throws Exception {
        Token existingToken=tokenRepository.findByRefreshToken(refreshToken);
        if(existingToken==null){
            throw new DataNotFoundException("Refresh token does not exit");
        }
        if(existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())){
            tokenRepository.delete(existingToken);
            throw new ExpiredTokenException("Refresh token is expired");
        }
        String token= jwtTokenUtils.generateToken(user);
        LocalDateTime expirationDateTime=LocalDateTime.now().plusSeconds(expiration);
        existingToken.setExpirationDate(expirationDateTime);
        existingToken.setToken(token);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }
}



