package com.sophia.store.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {

    private static final String TOKEN = "lizhe";

    //生成token
    public static String getToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        // 7天过期
        instance.add(Calendar.DATE, 7);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(TOKEN));
    }

    // 验证token并返回token中的payload
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }


}


















