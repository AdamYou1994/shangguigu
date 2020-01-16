package com.allen.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 相当于：<mvc:view-controller path="/to/login/page.html" view-name="member-login" />

		String urlPath = "/index.html";
		String viewName = "portal-page";

		registry.addViewController(urlPath).setViewName(viewName);
		// 跳转到登录页面
		urlPath = "/member/to/login/page.html";
		viewName = "member-login";
		
		registry.addViewController(urlPath).setViewName(viewName);

		// 跳转到个人中心页面
		urlPath = "/member/to/member/center/page.html";
		viewName = "member-center";

		registry.addViewController(urlPath).setViewName(viewName);

		// 跳转到同意协议页面
		urlPath = "/project/to/agree/page";
		viewName = "project-1-start";

		registry.addViewController(urlPath).setViewName(viewName);

	}

}
