package com.allen.crowd.controller;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allen.crowd.api.DataBaseOperationRemoteService;
import com.allen.crowd.api.RedisOperationRemoteService;
import com.allen.crowd.entity.ResultEntity;
import com.allen.crowd.entity.po.MemberLaunchInfoPO;
import com.allen.crowd.entity.po.MemberPO;
import com.allen.crowd.entity.vo.MemberSignSuccessVO;
import com.allen.crowd.entity.vo.MemberVO;
import com.allen.crowd.util.CrowdConstant;
import com.allen.crowd.util.CrowdUtils;

@RestController
public class MemberController {

	@Autowired
	private RedisOperationRemoteService redisOperationRemoteService ;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private DataBaseOperationRemoteService dataBaseOperationRemoteService;

	//spring会根据@Value注解中的表达式读取yml（properties）配置文件给成员变量设置对应的值
	@Value("${crowd.short.message.appCode}")
	private String appcode ;
	
	@RequestMapping("/retrieve/member/launch/info/by/member/token")
	public ResultEntity<MemberLaunchInfoPO> retrieveMemberLaunchInfoByMemberToken(@RequestParam("token") String token) {
		
		// 1.根据token查询memberId
		ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(token);
		
		String memberId = resultEntity.getData();
		
		if(memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_ACCESS_DENIED);
		}
		
		// 2.根据memberId查询MemberLaunchInfoPO
		return dataBaseOperationRemoteService.retrieveMemberLaunchInfoPO(memberId);
	}

	@RequestMapping("/member/manager/send/code")
	public ResultEntity<String> sendCode(@RequestParam("phoneNum") String phoneNum) {

		if(!CrowdUtils.strEffectiveCheck(phoneNum)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PHONE_NUM_INVALID);
		}

		// 1.生成验证码
		int length = 4;
		String randomCode = CrowdUtils.randomCode(length);

		// 2.保存到Redis
		Integer timeoutMinute = 15;

		String normalKey = CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;

		ResultEntity<String> resultEntity = redisOperationRemoteService.saveNormalStringKeyValue(normalKey, randomCode, timeoutMinute);

		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			return resultEntity;
		}

		// 3.发送验证码到用户手机
		//String appcode = "61f2eaa3c43e42ad82c26fbbe1061311";

		try {
			CrowdUtils.sendShortMessage(appcode, randomCode, phoneNum);

			return ResultEntity.successNoData();
		} catch (Exception e) {
			e.printStackTrace();

			return ResultEntity.failed(e.getMessage());
		}

	}

	@RequestMapping("/member/manager/register")
	public ResultEntity<String> register(@RequestBody MemberVO memberVO) {

		// 1.获取验证码数据并进行有效性检测
		String randomCode = memberVO.getRandomCode();

		if(!CrowdUtils.strEffectiveCheck(randomCode)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_INVALID);
		}

		// 2.获取手机号数据并进行有效性检测
		String phoneNum = memberVO.getPhoneNum();

		if(!CrowdUtils.strEffectiveCheck(phoneNum)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PHONENUM_INVALID);
		}

		// 3.拼接Redis存储验证码的KEY
		String randomCodeKey = CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;

		// 4.远程调用redis-provider的方法查询对应验证码
		ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(randomCodeKey);

		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			return resultEntity;
		}

		// 5.检查远程获取的验证码是否存在
		String randomCodeRemote = resultEntity.getData();

		if(!CrowdUtils.strEffectiveCheck(randomCodeRemote)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_OUT_OF_DATE);
		}

		// 6.将“表单验证码”和“Redis验证码”进行比较
		if(!Objects.equals(randomCode, randomCodeRemote)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_NOT_MATCH);
		}

		//删除redis-provider中的key
		ResultEntity<String> resultEntityRemoveByKey = redisOperationRemoteService.removeByKey(randomCode);
		if (ResultEntity.FAILED.equals(resultEntity.getResult())) {
			return resultEntityRemoveByKey;
		}
		
		// 7.检测登录账号是否被占用
		String loginacct = memberVO.getLoginacct();
		ResultEntity<Integer> loignAcctCountResultEntity = dataBaseOperationRemoteService.retrieveLoignAcctCount(loginacct);

		if(ResultEntity.FAILED.equals(loignAcctCountResultEntity.getResult())) {
			return ResultEntity.failed(loignAcctCountResultEntity.getMessage());
		}

		Integer loignAcctCount = loignAcctCountResultEntity.getData();

		if(loignAcctCount > 0) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
		}

		// 8.加密
		String userpswd = memberVO.getUserpswd();

		if(!CrowdUtils.strEffectiveCheck(userpswd)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PASSWORD_INVALID);
		}

		userpswd = passwordEncoder.encode(userpswd);
		memberVO.setUserpswd(userpswd);

		// 9.将VO对象转换为PO对象
		MemberPO memberPO = new MemberPO();
		BeanUtils.copyProperties(memberVO, memberPO);

		// 10.执行保存操作
		ResultEntity<String> saveMemberRemoteResultEntity = dataBaseOperationRemoteService.saveMemberRemote(memberPO);

		return saveMemberRemoteResultEntity;
	}
	
	@RequestMapping("/member/manager/login")
	public ResultEntity<MemberSignSuccessVO> login(
			@RequestParam("loginAcct") String loginAcct, 
			@RequestParam("userPswd") String userPswd) {

		// 1.根据登录账号查询数据库获取MemberPO对象
		ResultEntity<MemberPO> resultEntity = dataBaseOperationRemoteService.retrieveMemberByLoginAcct(loginAcct);
		
		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			return ResultEntity.failed(resultEntity.getMessage());
		}
		
		// 2.获取MemberPO对象
		MemberPO memberPO = resultEntity.getData();
		
		// 3.检查MemberPO对象是否为空
		if(memberPO == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		// 4.获取从数据库查询得到的密码
		String userpswdDatabase = memberPO.getUserpswd();
		
		// 5.比较密码
		boolean matcheResult = passwordEncoder.matches(userPswd, userpswdDatabase);
		
		if(!matcheResult) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		// 6.生成token
		String token = CrowdUtils.generateToken();
		
		// 7.从MemberPO对象获取memberId
		String memberId = memberPO.getId() + "";
		
		// 8.将token和memberId存入Redis
		ResultEntity<String> resultEntitySaveToken = redisOperationRemoteService.saveNormalStringKeyValue(token, memberId, 30);
		
		if(ResultEntity.FAILED.equals(resultEntitySaveToken.getResult())) {
			return ResultEntity.failed(resultEntitySaveToken.getMessage());
		}
		
		// 9.封装MemberSignSuccessVO对象
		MemberSignSuccessVO memberSignSuccessVO = new MemberSignSuccessVO();
		
		BeanUtils.copyProperties(memberPO, memberSignSuccessVO);
		
		memberSignSuccessVO.setToken(token);
		
		// 10.返回结果
		return ResultEntity.successWithData(memberSignSuccessVO);
	}
	
	@RequestMapping("/member/manager/logout")
	public ResultEntity<String> logout(@RequestParam("token") String token) {
		
		return redisOperationRemoteService.removeByKey(token);
	}
}
