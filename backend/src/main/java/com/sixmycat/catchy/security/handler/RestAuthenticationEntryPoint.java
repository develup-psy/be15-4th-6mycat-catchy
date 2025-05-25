package com.sixmycat.catchy.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.exception.CustomJwtException;
import com.sixmycat.catchy.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        CustomJwtException jwtEx = (CustomJwtException) request.getAttribute("jwtException");

        ApiResponse<Void> errorResponse = (jwtEx != null)
                ? ApiResponse.failure(jwtEx.getErrorCode().getCode(), jwtEx.getErrorCode().getMessage())
                : ApiResponse.failure(ErrorCode.UNAUTHORIZED_USER.getCode(), ErrorCode.UNAUTHORIZED_USER.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
