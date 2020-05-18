package com.hsshy.beam.admin.common.shiro;
import com.hsshy.beam.admin.common.shiro.factory.ShiroFactroy;
import com.hsshy.beam.admin.modular.sys.entity.User;
import com.hsshy.beam.common.utils.ToolUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 认证
 *
 */
@Component
public class UserRealm extends AuthorizingRealm {
    /**
     * 授权(验证权限时调用)
     */

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		IShiro shiroFactory = ShiroFactroy.me();
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();

		List<String> roleNameList = shiroUser.getRoleNames();

		List<String> permsList = shiroFactory.findPermissionsByUserId(shiroUser.getId());
		//用户权限列表
		Set<String> permsSet = new HashSet<>();
		for(String perms : permsList){
			if(ToolUtil.isEmpty(perms)){
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		info.addRoles(roleNameList);
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		IShiro shiroFactory = ShiroFactroy.me();
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
		User user = shiroFactory.user(token.getUsername());

		ShiroUser shiroUser = shiroFactory.shiroUser(user);

		SimpleAuthenticationInfo info = shiroFactory.info(shiroUser,user,getName());
		return info;
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
		shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
		shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
		super.setCredentialsMatcher(shaCredentialsMatcher);
	}
}
