package com.allen.crowd.entity.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignSuccessVO implements Serializable {
	
	private String username;
	private String email;
	private String token;

}