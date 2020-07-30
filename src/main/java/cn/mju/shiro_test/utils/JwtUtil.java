package cn.mju.shiro_test.utils;

import cn.mju.shiro_test.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt 工具类
 */
public class JwtUtil {

    //过期时间为一周
    public static final Long EXPIRE=1000 * 60 * 60 * 24 * 7L;

    public static final String APPSECRET="WgtqaT1HNTZPZNMDJu3k";

    /**
     * 生成jwt
     * @param
     * @return
     */
    public static String geneJsonWebToken(User user){
        if(user == null || user.getId() == null || user.getUsername() == null
                || user.getEmail() == null){
            return null;
        }
        return Jwts.builder()
                .claim("id",user.getId())
                .claim("username",user.getUsername())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();
    }

    /**
     * 解析jwt
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        try{
           return Jwts.parser().setSigningKey(APPSECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }

    /**
     *
     * 校验token是否过期
     * @param expiprationTime
     * @return
     */
    public static boolean isTokenExpired(Date expiprationTime) {
        return expiprationTime.before(new Date());
    }


}
