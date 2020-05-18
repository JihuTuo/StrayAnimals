package com.shiep.jwt.config;

import com.shiep.jwt.pojo.UserInfo;
import com.shiep.jwt.util.JwtConstans;
import com.shiep.jwt.util.ObjectUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT的token，区分大小写
 */
@ConfigurationProperties(prefix = "shiep.jwt") // 读取配置文件中的信息，以shiep开头 下的jwt
@Component
@Data
public class JwtConfig {

    private String secret;   // 加密密钥
    private long expire;    // token有效时间
    private String header;  // header名称
    private String cookieName;  // 获取CookieName

    /**
     * 生成token
     *
     * @param
     * @return
     */
    public String createToken(UserInfo userInfo) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);//过期时间

        return Jwts.builder()
                .claim(JwtConstans.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstans.JWT_KEY_USER_NAME, userInfo.getUsername())
                .claim(JwtConstans.JWT_KEY_USER_PHOTO, userInfo.getPhoto())
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(nowDate) // 签发时间
                .setExpiration(expireDate)  // 过期时间
                .signWith(SignatureAlgorithm.HS512, secret) // 签名算法以及密匙
                .compact();
    }

    /**
     * 获取保存在token中的用户信息内容
     */
    public UserInfo getUserInfoFromToken(String token) {
        Claims claims = getTokenClaim(token);  // 取得对象
        // 这里的 ObjectUtils是自己封装的，用来获取到相应类型的额数据，因为claims.get方法返回对象是Object
        if (claims == null) {
            return null;
        }
        System.out.println(claims.get(JwtConstans.JWT_KEY_ID));
        System.out.println(claims.get(JwtConstans.JWT_KEY_USER_NAME));
        System.out.println(claims.get(JwtConstans.JWT_KEY_USER_PHOTO));
        return new UserInfo(
                ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID)),
                ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_USER_NAME)));
    }


    /**
     * 获取token中注册信息
     *
     * @param token
     * @return
     */
    public Claims getTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }


    /**
     * 验证token是否过期失效
     *
     * @param expirationTime
     * @return
     */
    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }

    /**
     * 获取token失效时间
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }

    /**
     * 获取保存在token中的内容
     * 获取用户名从token中
     */
    public String getUsernameFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }

    // --------------------- getter & setter ---------------------

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
