package com.hwx.backeend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//https://www.cnblogs.com/hackyo/p/8004928.html
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据负载生成 token
     */
    private String generateTokenFromClaim(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从 token 中获取负载
     */
    private Claims getClaimsFromToken(String token) {
//        if (isTokenExpired(token)){
//            throw TokenExpireException()
//        }
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 注意这里已经会为过期抛出一个错误
            // 类型为 ExpiredJwtException
            logger.warn("JWT验证失败:{}", e.getMessage());
            throw e;
        }
        return claims;
    }

    /**
     * 从 token 中获取登录用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            throw e;
        }
        return username;
    }

    /**
     * 从 bearerToken 中获取登录用户名
     */
    String getUsernameFromBearerToken(String bearerToken) {
        String username;
        try {
            String token = getToken(bearerToken);
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            throw e;
        }
        return username;
    }

    /**
     * 从 request 中获取登录用户名
     */
    public String getUsernameFromRequest(HttpServletRequest request) {
        String username;
        String token = getToken(request.getHeader("Authorization"));
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            throw e;
        }
        return username;
    }

    /**
     * 这个 token 有效吗?
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 这个 token 过期了吗?
     */
    boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 这个 token 啥时候过期?
     */
    Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成 token
     */
    public String generateTokenFromUserDetail(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateTokenFromClaim(claims);
    }

    /**
     * 骚年,要来刷新 token 🐴 <br>
     *
     * @param oldToken 带tokenHead的token
     */
    String refreshToken(String oldToken) {
        // 首先你得有东西吧
        if (StringUtils.hasText(oldToken)) {
            return null;
        }
        // token校验不通过
        Claims claims = getClaimsFromToken(oldToken);
        if(claims==null){
            return null;
        }
        // 过期就爬
        if (isTokenExpired(oldToken)) {
//            throw TokenExpireException()
            return null;
        }
        // 半小时内无需刷新
        if (this.isTokenFresh(oldToken, 30 * 60)) {
            return oldToken;
        } else {
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateTokenFromClaim(claims);
        }
    }

    /**
     * token 是否新鲜(刚刷新)
     *
     * @param token        原token
     * @param freshTimeout 新鲜的时间,也就是在这个时间内不需要刷新
     */
    private boolean isTokenFresh(String token, int freshTimeout) {
        Claims claims = getClaimsFromToken(token);
        // 签发时间
        Date createdDate= claims.get(CLAIM_KEY_CREATED, Date.class);
        // 当前时间
        Date currentDate = new Date();
        // 无需刷新的时间
        Date refreshTimeoutData = new Date(currentDate.getTime() + (freshTimeout * 1000L));
        // 在创建时间之后
        // 在刷新超时时间之前
        return currentDate.after(createdDate) && currentDate.before(refreshTimeoutData);
    }

    /**
     * 从 bearerToken 获取 token
     */
    public String getToken(String bearerToken) {
        String token;
        if (bearerToken.startsWith("Bearer ")) {
            token=bearerToken.substring(7);
        } else {
            logger.warn("你这个请求头格式不对啊,没有 Bearer 开头");
            return null;
        }
        return token;
    }

}
