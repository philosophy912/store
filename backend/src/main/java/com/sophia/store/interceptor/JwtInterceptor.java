package com.sophia.store.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sophia.store.utils.JwtTokenUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获取请求头中的令牌
        String token = request.getHeader("token");
        Map<String, Object> map = new HashMap<>();
        try {
          JwtTokenUtil.verify(token);//验证令牌
            return true;
        }catch (SignatureVerificationException e){
            map.put("msg", "无效签名");
        }catch (AlgorithmMismatchException e){
            map.put("msg", "Token算法不一致");
        }catch (TokenExpiredException e){
            map.put("msg", "token过期");
        }catch (Exception e){
            map.put("msg", "token无效");
        }
        map.put("status", false);//设置状态
        //将map转换成json
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
        return false;
    }
}
