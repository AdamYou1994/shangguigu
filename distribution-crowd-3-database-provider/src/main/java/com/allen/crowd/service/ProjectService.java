package com.allen.crowd.service;

import com.allen.crowd.entity.vo.ProjectVO;

public interface ProjectService {

	void saveProject(ProjectVO projectVO, String memberId);

}
