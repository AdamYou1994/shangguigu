package com.allen.crowd.service;

import com.allen.crowd.entity.po.MemberLaunchInfoPO;
import com.allen.crowd.entity.po.MemberPO;

public interface MemberService {

	int getLoginAcctCount(String loginAcct);

	void saveMemberPO(MemberPO memberPO);

	MemberPO getMemberByLoginAcct(String loginAcct);

	MemberLaunchInfoPO getMemberLaunchInfoPO(String memberId);

}
