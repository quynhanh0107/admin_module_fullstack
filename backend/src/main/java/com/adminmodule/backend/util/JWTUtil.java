package com.adminmodule.backend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JWTUtil {
    // tạo chữ ký bí mật của Server (một chuỗi cố định tối thiểu 32 ký tự)
    private static final String SECRET_KEY = "MotCauFanChantRatHayMaMinhRatThichDoLaCuaMCKAnhLongEmFanAnhAnhChoEmXinKieuAnh";

    //thời gian sống của vé (vd: 24h)
    // hết thời gian, người dùng phải đăng nhập lại
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    // Dùng HMAC-SHA thuật toán để mã hóa các Bytes trong SECRET_KEY
    // dùng như là một argument cho .signwith() ở dưới khi tạo JWT
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // hàm tạo Token dựa trên username
    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username) // gắn tên user vào vé
            .setIssuedAt(new Date()) // Thời gian bắt đầu phát hành Token
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION)) // thời gian hết hạn Token
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact(); // xuất Token
    }

    // đọc tên người dùng từ Token
    public String extractUsername(String token) {
        return Jwts.parserBuilder() // tạo ra bản thiết kế máy soát Token
            .setSigningKey(getSigningKey()) // nạp chìa khóa bảo mật (đã được ký mã hóa) vào bộ nhớ để đối chiếu
            .build() // khởi động máy
            .parseClaimsJws(token) // soát Token: kiểm tra tính toàn vẹn (xem Token có bị thay đổi) và niên hạn sử dụng
            .getBody() // lấy phần thông tin của Token, gồm 3 phần: Header, Payload, Signature
            .getSubject(); // lấy tên chủ thể và trả lại 
    }

    // kiểm tra vé có hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
