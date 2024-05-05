package com.project.shopApp.controllers;

import com.project.shopApp.components.LocalizationUtils;
import com.project.shopApp.dtos.RefreshTokenDTO;
import com.project.shopApp.dtos.UpdateUserDTO;
import com.project.shopApp.dtos.UserDTO;
import com.project.shopApp.dtos.UserLoginDTO;
import com.project.shopApp.models.Token;
import com.project.shopApp.models.User;
import com.project.shopApp.responses.LoginResponse;
import com.project.shopApp.responses.RegisterResponse;
import com.project.shopApp.responses.UserResponse;
import com.project.shopApp.services.token.ITokenService;
import com.project.shopApp.services.user.IUserService;
import com.project.shopApp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    private final ITokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse>  createUser(
            @Valid  @RequestBody UserDTO userDTO,
            BindingResult result
    ){
        RegisterResponse registerResponse = new RegisterResponse();
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                registerResponse.setMessage(errorMessages.toString());
                return ResponseEntity.badRequest().body(registerResponse);
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
                return ResponseEntity.badRequest().body(registerResponse);
            }
            User user=userService.createUser(userDTO);
            registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY));
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        }catch (Exception e){
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }

    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                               HttpServletRequest request) {
        // Kiểm tra thông tin đăng nhập và sinh token

        try {
            String token = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId()==null ?1: userLoginDTO.getRoleId()
            );
            String userAgent = request.getHeader("User-Agent");
            User user=userService.getUserDetailsFromToken(token);
            Token jwtToken= tokenService.addToken(user, token, isMobileDevice(userAgent));

            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(jwtToken.getToken())
                            .tokenType(jwtToken.getTokenType())
                            .refreshToken(jwtToken.getRefreshToken())
                            .username(user.getUsername())
                            .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                            .id(user.getId())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
        // Trả về token trong response

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO){
        try{
            User userDetail = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
            Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);
            return ResponseEntity.ok(LoginResponse.builder()
                    .message("Refresh token successfully")
                    .token(jwtToken.getToken())
                    .tokenType(jwtToken.getTokenType())
                    .refreshToken(jwtToken.getRefreshToken())
                    .username(userDetail.getUsername())
                    .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .id(userDetail.getId())
                    .build());


        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(
                                    MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }
    private boolean isMobileDevice(String userAgent) {
        // Kiểm tra User-Agent header để xác định thiết bị di động
        // Ví dụ đơn giản:
        return userAgent.toLowerCase().contains("mobile");
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization")
                                                           String authorizationHeader){
        try {
            String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/details/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updatedUserDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            // Ensure that the user making the request matches the user being updated
            if (!Objects.equals(user.getId(), userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            User updatedUser = userService.updateUser(userId, updatedUserDTO);
            return ResponseEntity.ok(UserResponse.fromUser(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
