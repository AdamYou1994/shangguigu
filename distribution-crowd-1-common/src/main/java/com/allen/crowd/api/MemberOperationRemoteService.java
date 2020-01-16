package com.allen.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.allen.crowd.entity.ResultEntity;
import com.allen.crowd.entity.po.MemberLaunchInfoPO;
import com.allen.crowd.entity.vo.MemberSignSuccessVO;
import com.allen.crowd.entity.vo.MemberVO;

@FeignClient("member-manager")
public interface MemberOperationRemoteService {
	@RequestMapping("/retrieve/member/launch/info/by/member/token")
	public ResultEntity<MemberLaunchInfoPO> retrieveMemberLaunchInfoByMemberToken(@RequestParam("token") String token);
	
	@RequestMapping("/member/manager/logout")
	public ResultEntity<String> logout(@RequestParam("token") String token);
	
	@RequestMapping("/member/manager/login")
	public ResultEntity<MemberSignSuccessVO> login(
			@RequestParam("loginAcct") String loginAcct, 
			@RequestParam("userPswd") String userPswd) ;
	
	@RequestMapping("/member/manager/register")
	public ResultEntity<String> register(@RequestBody MemberVO memberVO);
	
	@RequestMapping("/member/manager/send/code")
	public ResultEntity<String> sendCode(@RequestParam("phoneNum") String phoneNum);
}