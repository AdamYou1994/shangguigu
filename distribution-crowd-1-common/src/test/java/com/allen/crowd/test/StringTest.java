package com.allen.crowd.test;

import org.junit.Test;

import com.allen.crowd.util.CrowdUtils;

public class StringTest {

	@Test
	public void testSendCode() {
		String appcode = "61f2eaa3c43e42ad82c26fbbe1061311";
		String randomCode = CrowdUtils.randomCode(4);
		String phoneNum = "19112599638";
		CrowdUtils.sendShortMessage(appcode, randomCode, phoneNum);
	}
}
