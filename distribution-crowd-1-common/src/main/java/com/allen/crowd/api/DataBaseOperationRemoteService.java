package com.allen.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.allen.crowd.entity.ResultEntity;
import com.allen.crowd.entity.po.MemberLaunchInfoPO;
import com.allen.crowd.entity.po.MemberPO;
import com.allen.crowd.entity.vo.ProjectVO;

@FeignClient("database-provider")
public interface DataBaseOperationRemoteService {
	
	@RequestMapping("/retrieve/loign/acct/count")
	ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct);

	@RequestMapping("/save/member/remote")
	ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);

	@RequestMapping("/retrieve/member/by/login/acct")
	ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct);

	@RequestMapping("/project/manager/save/whole/project/{memberId}")
	ResultEntity<String> saveProjectRemote(@RequestBody ProjectVO projectVO,
			@PathVariable("memberId") String memberId);
	
	@RequestMapping("/retriev/member/lanuch/lnfo/po")
	ResultEntity<MemberLaunchInfoPO> retrieveMemberLaunchInfoPO(@RequestParam("memberId") String memberId);
}