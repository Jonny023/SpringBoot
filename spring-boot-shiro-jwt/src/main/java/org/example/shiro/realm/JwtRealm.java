package org.example.shiro.realm;

import cn.hutool.jwt.JWTPayload;
import cn.hutool.system.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.domain.entity.User;
import org.example.domain.vo.UserVO;
import org.example.service.UserService;
import org.example.shiro.token.JwtToken;
import org.example.shiro.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        // 这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return 认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String jwt = (String) token.getPrincipal();
        if (jwt == null) {
            throw new NullPointerException("token不允许为空");
        }
        // 判断
        JWTPayload jwtPayload = JwtUtil.parseToken(jwt);

        // 下面是验证这个user是否是真实存在的
        String username = (String) jwtPayload.getClaim("username");

        User user = userService.findUserByCache(username);
        if (user == null) {
            throw new RuntimeException("user not found");
        }

        UserVO userVO = UserVO.builder().id(user.getId()).username(username).build();

        log.info("在使用token登录: {}", username);
        // 这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名
        return new SimpleAuthenticationInfo(userVO, jwt, "JwtRealm");
    }

}