package com.adminmodule.backend.config;

import com.adminmodule.backend.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // lấy header từ Token
        String authHeader = request.getHeader("Authorization");
        
        // nếu header bắt đầu bằng Bearer, bỏ phần đó đi và chỉ lấy nội dung
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); 
        
            // đem token cho JWTUtil kiểm tra
            // nếu token hợp lệ, extract username
            if(jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);

                // đánh dấu là đã qua kiểm duyệt
                //tạo đối tượng để quyết định phân quyền, với 3 tham số: 
                // username (Principal): định danh người dùng vừa lấy ở token bên trên để API biết tài khoản nào đang gọi
                // null (Credentials): chứa mật khẩu nhưng ở trên đã xác thực bằng validateToken rồi nên ko cần mk ở bước này nx
                // new ArrayList<>(): danh sách các Quyền và Vai Trò. Hiện tại là danh sách rỗng và chưa được gắn quyền
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                
                // ghi đè đối tượng vừa tạo ở trên vào bộ nhớ của luồng Request hiện tại
                // SecurityContextHolder: lưu trữ mọi thông tin bảo mật. Dùng cơ chế gọi 1 request chạy 1 thread độc lập và có Context riêng
                // .getContext(): Lấy ra vùng nhớ Context của đúng cái Request hiện tại.
                // .setAuthentication(authToken): đặt đối tượng authToken vào context đó.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
