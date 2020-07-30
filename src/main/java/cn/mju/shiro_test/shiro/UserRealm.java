package cn.mju.shiro_test.shiro;

import cn.mju.shiro_test.domain.Perm;
import cn.mju.shiro_test.domain.Role;
import cn.mju.shiro_test.domain.User;
import cn.mju.shiro_test.mapper.RoleMapper;
import cn.mju.shiro_test.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        User userweb = (User) subject.getPrincipal();
        User dbUser = userService.findUserByUserName(userweb.getUsername());
        Collection<String> roleCollection = new HashSet<String>();
        Collection<String> permCollection = new HashSet<String>();
        if (dbUser != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            Set<Role> roles = dbUser.getRoles();
            for (Role role : roles) {
                roleCollection.add(role.getRoleName());
                Set<Perm> perms = role.getPerms();
                for (Perm perm : perms) {
                    permCollection.add(perm.getPermName());
                }
                info.addStringPermissions(permCollection);
            }
            info.addRoles(roleCollection);
            return info;
        }
        return null;
    }

    /**
     * 执行认证逻辑
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findUserByUserName(token.getUsername());
        if (user == null) {
            return null;
        }
        String realmName = getName();
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, realmName);
    }

    /**
     * 清空缓存方法，在修改用户密码或者退出登录时需手动调用
     * @param principals
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
