package com.example.springbootshiro.configuration.shiro.realm;

import com.alibaba.fastjson.JSON;
import com.example.springbootshiro.configuration.shiro.token.JwtToken;
import com.example.springbootshiro.domain.vo.UserDetailVO;
import lombok.extern.slf4j.Slf4j;
import ml.ytooo.redis.single.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Resource
    private JedisClient jedis;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("Shiro权限配置");
        String token = principals.toString();
        UserDetailVO userDetailVO = JSON.parseObject(jedis.getJedis().get(token), UserDetailVO.class);
        Set<String> roles = new HashSet<>();
        roles.add(userDetailVO.getPermission());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        return info;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Shiro认证");
        JwtToken usToken = (JwtToken) token;
        //获取用户的输入的账号.
        String sid = (String) usToken.getCredentials();
        if (StringUtils.isBlank(sid)) {
            return null;
        }
        log.info("sid: " + sid);
        return new SimpleAccount(sid, sid, "userRealm");
    }
}
