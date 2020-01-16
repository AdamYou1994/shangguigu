package com.allen.crowd.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allen.crowd.entity.ResultEntity;
import com.allen.crowd.entity.vo.ProjectVO;
import com.allen.crowd.service.ProjectService;

@RestController
public class ProjectController {
	@Autowired
	private ProjectService projectService;

	
	@RequestMapping("/project/manager/save/whole/project/{memberId}")
	ResultEntity<String> saveProjectRemote(@RequestBody ProjectVO projectVO,
			@PathVariable("memberId") String memberId){
		try {
			projectService.saveProject(projectVO,memberId);
			return ResultEntity.successNoData();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
	}
}
