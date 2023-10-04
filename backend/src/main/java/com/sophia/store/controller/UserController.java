/**
 * @author lizhe
 * @date 2020/11/20 14:54
 **/
package com.sophia.store.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sophia.store.entity.vo.Response;
import com.sophia.store.entity.vo.RolesVo;
import com.sophia.store.entity.vo.TokenVo;
import com.sophia.store.entity.vo.UserVo;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/store/user")
@Slf4j
public class UserController {

    private static final String USERNAME = "username";
    private static final String ID = "id";


    // 登陆接口，假登陆，只是为了生成token
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestBody UserVo userVo) {
        Response response = new Response();
        log.debug("user[{}] try to login", userVo.getUsername());
        response.setMessage("登陆成功");
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, userVo.getUsername());
        map.put(ID, String.valueOf(userVo.getId() == null ? 1 : userVo.getId()));
        String token = JwtTokenUtil.getToken(map);
        log.info("token is {}", token);
        response.setData(new TokenVo(token));
        return response;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Response info(@RequestParam String token) {
        Response response = new Response();
        try {
            log.debug("token is{}", token);
            DecodedJWT decodedJWT = JwtTokenUtil.verify(token);
            RolesVo rolesVo = new RolesVo();
            rolesVo.setRoles(Collections.singletonList("admin"));
            rolesVo.setIntroduction("I am a super administrator");
            rolesVo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            rolesVo.setName(decodedJWT.getClaims().get(USERNAME).asString());
            response.setData(rolesVo);
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("token not ok");
        }
        return response;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Response logout(@RequestHeader("X-Token") String token) {
        Response response = new Response();
        DecodedJWT decodedJWT = JwtTokenUtil.verify(token);
        String username = decodedJWT.getClaims().get(USERNAME).asString();
        if (username != null) {
            // 移出session中的登录标记
            response.setMessage("注销成功");
            response.setData("success");
        }else{
            response.setCode(Constant.NOK);
            response.setMessage("注销失败");
            response.setData("fail");
        }
        return response;
    }

}
