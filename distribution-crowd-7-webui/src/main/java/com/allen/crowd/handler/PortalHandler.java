package com.allen.crowd.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalHandler {

	@RequestMapping("/")
	public String showPortalPage() {
		
		// 加载真实数据，到页面上显示
		
		return "portal-page";
	}
}
