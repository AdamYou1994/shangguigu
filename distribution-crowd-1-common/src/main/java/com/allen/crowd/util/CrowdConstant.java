package com.allen.crowd.util;

import java.util.HashMap;
import java.util.Map;

public class CrowdConstant {

	public static final String ATTR_NAME_MESSAGE = "MESSAGE";
	public static final String ATTR_NAME_LOGIN_ADMIN = "LOGIN-ADMIN";
	public static final String ATTR_NAME_PAGE_INFO = "PAGE-INFO";
	public static final String ATTR_NAME_LOGIN_MEMBER = "LOGIN-MEMBER";
	public static final String ATTR_NAME_INITED_PROJECT = "INITED-PROJECT";
	
	public static final String MESSAGE_LOGIN_FAILED = "登录的账号或密码不正确，请核对后在登陆";
	public static final String MESSAGE_CODE_INVALID = "MD5加密，明文无效！！！";
	public static final String MESSAGE_ACCESS_DENIED = "请登陆后再操作！！！";
	public static final String ATTR_NAME_LOGIN_ACCT_ALREADY_IN_USE = "用户名已被使用！！";
	public static final String MESSAGE_RANDOM_CODE_LENGTH_INVALID = "验证码长度不合适";
	
	public static final Map<String, String> EXCEPTION_MESSAGE_MAP = new HashMap<>();
	
	public static final String MESSAGE_REDIS_KEY_OR_VALUE_INVALID = "待存入redis的key或value无效";
	public static final String MESSAGE_REDIS_KEY_TIME_OUT_INVALID = "抱歉！不接受0或null值，请明确您的意图，是否需要过期时间";
	
	public static final String REDIS_RANDOM_CODE_PREFIX = "RANDOM_CODE_";
	public static final String REDIS_MEMBER_SING_TOKEN_PREFIX = "SIGNED_MEMBER_";
	public static final String REDIS_PROJECT_TEMP_TOKEN_PREFIX = "PROJECT_TEMP_TOKEN_";
	
	public static final String MESSAGE_PHONE_NUM_INVALID = "您的手机号不存在或非法";
	public static final String MESSAGE_LOGINACCT_INVALID = "登陆账号字符串无效";
	public static final String MESSAGE_RANDOM_CODE_INVALID = "验证码无效";
	public static final String MESSAGE_PHONENUM_INVALID = "您的手机号不存在或非法";
	public static final String MESSAGE_RANDOM_CODE_OUT_OF_DATE = "验证码过期";
	public static final String MESSAGE_RANDOM_CODE_NOT_MATCH = "验证码不匹配";
	public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "用户名已被使用！！";
	public static final String MESSAGE_PASSWORD_INVALID = "密码无效";
	public static final String MESSAGE_UPLOAD_FILE_EMPTY = "未检测到长传文件";
	
	static {
		EXCEPTION_MESSAGE_MAP.put("java.lang.ArithmeticException", "数学运算时异常");
		EXCEPTION_MESSAGE_MAP.put("java.lang.RuntimeException", "运行时异常");
		EXCEPTION_MESSAGE_MAP.put("com.allen.crowd.funding.exception.LoginException", "用户登录时异常");
		EXCEPTION_MESSAGE_MAP.put("org.springframework.security.access.AccessDeniedException", "权限不足，拒绝访问");
	}
}
