package com.allen.crowd.handler;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.allen.crowd.api.MemberOperationRemoteService;
import com.allen.crowd.entity.ResultEntity;
import com.allen.crowd.entity.vo.MemberSignSuccessVO;
import com.allen.crowd.entity.vo.MemberVO;
import com.allen.crowd.util.CrowdConstant;

@Controller
public class MemberHandler {


	@Autowired
	private MemberOperationRemoteService memberManagerRemoteService;

	//<mvc:view-controller path="/member/do/login.html" value-name="member-login"/>
	@RequestMapping("/member/do/login.html")
	public String doLogin(MemberVO memberVO, Model model, HttpSession session) {
		
		String loginAcct = memberVO.getLoginacct();
		String userPswd = memberVO.getUserpswd();
		
		// 调用远程方法执行登录操作
		ResultEntity<MemberSignSuccessVO> resultEntity = memberManagerRemoteService.login(loginAcct, userPswd);
		
		// 检查远程方法调用结果
		String result = resultEntity.getResult();
		
		if(ResultEntity.FAILED.equals(result)) {
			
			String message = resultEntity.getMessage();
			
			model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, message);
			
			return "member-login";
		}
		
		// 如果登录成功，则获取MemberSignSuccessVO对象
		MemberSignSuccessVO memberSignSuccessVO = resultEntity.getData();
		
		// 将MemberSignSuccessVO对象存入Session域
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberSignSuccessVO);
		
		return "redirect:/member/to/member/center/page.html";
	}
	
	@RequestMapping("/member/logout.html")
	public String logout(HttpSession session) {
		
		// 1.从现有Session中获取已登录的Member
		MemberSignSuccessVO memberSignSuccessVO = (MemberSignSuccessVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
		
		// 2.如果memberSignSuccessVO为null，则已经退出，不必继续执行
		if(memberSignSuccessVO  == null) {
			return "redirect:/";
		}
		
		// 3.获取token值
		String token = memberSignSuccessVO.getToken();
		
		// 4.调用远程方法删除Redis中存储的token
		ResultEntity<String> resultEntity = memberManagerRemoteService.logout(token);
		
		// 5.如果调用远程方法失败，抛出异常
		String result = resultEntity.getResult();
		
		if(ResultEntity.FAILED.equals(result)) {
			throw new RuntimeException(resultEntity.getMessage());
		}
		
		// 6.释放当前Session
		session.invalidate();
		
		return "redirect:/index.html";
	}
}
