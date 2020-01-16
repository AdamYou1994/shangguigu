package com.allen.crowd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allen.crowd.entity.ResultEntity;
import com.allen.crowd.entity.po.MemberLaunchInfoPO;
import com.allen.crowd.entity.po.MemberPO;
import com.allen.crowd.service.MemberService;
import com.allen.crowd.util.CrowdConstant;
import com.allen.crowd.util.CrowdUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="在数据库中查询会员数据")
@RestController
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/retrieve/loign/acct/count")
	public ResultEntity<Integer> retrieveLoignAcctCount(
			@RequestParam("loginAcct") String loginAcct) {
		
		if(!CrowdUtils.strEffectiveCheck(loginAcct)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGINACCT_INVALID);
		}
		
		try {
			int count = memberService.getLoginAcctCount(loginAcct);
			
			return ResultEntity.successWithData(count);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
	}
	
	@RequestMapping("/save/member/remote")
	ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO){
		try {
			// 执行保存
			memberService.saveMemberPO(memberPO);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successNoData();
	}
	
	@RequestMapping("/retrieve/member/by/login/acct")
	ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct){
		MemberPO memberPO = null;
	    
		try {
			memberPO = memberService.getMemberByLoginAcct(loginAcct);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successWithData(memberPO);
	}
	
	@ApiOperation(value="查询MemberLaunchInfoPO对象",httpMethod = "GET")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "memberId",value = "会员id",required = true)
	})
	@RequestMapping("/retriev/member/lanuch/lnfo/po")
	ResultEntity<MemberLaunchInfoPO> retrieveMemberLaunchInfoPO(@RequestParam("memberId") String memberId){
		MemberLaunchInfoPO memberLaunchInfoPO = memberService.getMemberLaunchInfoPO(memberId);
		
		return ResultEntity.successWithData(memberLaunchInfoPO);
	}
}
