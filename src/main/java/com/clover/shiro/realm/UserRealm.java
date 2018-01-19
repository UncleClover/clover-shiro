package com.clover.shiro.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserRealm extends AuthorizingRealm {
	private String realmName = "userRealm";

	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 通过用户名从数据库获取权限字符串
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 权限
		Set<String> s = new HashSet<String>();
		s.add("look:desk");
		s.add("printer:query");
		authorizationInfo.setStringPermissions(s);

		// 角色
		Set<String> r = new HashSet<String>();
		r.add("client");
		authorizationInfo.setRoles(r);
		return authorizationInfo;
	}

	/**
	 * 验证登录信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = (String) token.getPrincipal();
		String pwd = "123456";
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, pwd, this.getName());
		return simpleAuthenticationInfo;
	}
}
