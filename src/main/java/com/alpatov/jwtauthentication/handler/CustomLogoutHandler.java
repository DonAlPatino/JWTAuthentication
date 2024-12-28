package com.alpatov.jwtauthentication.handler;

import com.alpatov.jwtauthentication.entity.Token;
import com.alpatov.jwtauthentication.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * @author Viacheslav Alpatov
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    /**
     * Обработчик выхода пользователя из системы.
     * Устанавливает флаг "loggedOut" в true для соответствующего токена,
     * если токен найден в хранилище.
     *
     * @param request HttpServletRequest объект, содержащий информацию о запросе.
     * @param response HttpServletResponse объект, содержащий информацию о ответе.
     * @param authentication объект аутентификации.
     */

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);

        Token tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);

        if (tokenEntity != null) {
            tokenEntity.setLoggedOut(true);
            tokenRepository.save(tokenEntity);
        }
    }
}
