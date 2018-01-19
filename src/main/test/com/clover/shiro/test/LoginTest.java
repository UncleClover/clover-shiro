package com.clover.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class LoginTest {

	@Test
	public void test() {
		// 加载配置文件
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		System.out.println(JSON.toJSONString(factory));

		// 2.解析配置文件，并且返回一些SecurityManger实例
		SecurityManager securityManager = factory.getInstance();

		// 3.设置SecurityManager到静态内存区，单例模式
		SecurityUtils.setSecurityManager(securityManager);

		// 安全操作
		Subject subject = SecurityUtils.getSubject();

		// 在应用的当前会话中设置属性
		Session session = subject.getSession();
		session.setAttribute("key", "value");

		// 当前我们的用户是匿名的用户，我们尝试进行登录，
		if (!subject.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken("aihe", "123456");

			// this is all you have to do to support 'remember me' (no config -
			// built in!):
			token.setRememberMe(true);

			// 尝试进行登录用户，如果登录失败了，我们进行一些处理

			try {
				subject.login(token);

				// 当我们获登录用户之后
				System.out.println("User [" + subject.getPrincipal() + "] logged in successfully.");

				System.out.println(JSON.toJSONString(subject));
				// 查看用户是否有指定的角色
				if (subject.hasRole("client")) {
					System.out.println("Look is in your role");
				} else {
					System.out.println(".....");
				}

				// 查看用户是否有某个权限
				if (subject.isPermitted("look:desk")) {
					System.out.println("You can look.  Use it wisely.");
				} else {
					System.out.println("Sorry, you can't look.");
				}

				if (subject.isPermitted("winnebago:drive:eagle5")) {
					System.out.println("You are permitted to 'drive' the 'winnebago' with license plate (id) 'eagle5'.  " + "Here are the keys - have fun!");
				} else {
					System.out.println("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
				}
				System.out.println(JSON.toJSONString(factory));
				// 登出
				subject.logout();
			} catch (UnknownAccountException uae) {
				uae.printStackTrace();
			} catch (IncorrectCredentialsException ice) {
				ice.printStackTrace();
			} catch (LockedAccountException lae) {
				lae.printStackTrace();
			} catch (AuthenticationException ae) {
				ae.printStackTrace();
			}
		}
	}
}
