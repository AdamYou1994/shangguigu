package com.allen.crowd.service.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.allen.crowd.entity.po.MemberConfirmInfoPO;
import com.allen.crowd.entity.po.MemberLaunchInfoPO;
import com.allen.crowd.entity.po.MemberLaunchInfoPOExample;
import com.allen.crowd.entity.po.ProjectItemPicPO;
import com.allen.crowd.entity.po.ProjectPO;
import com.allen.crowd.entity.po.ReturnPO;
import com.allen.crowd.entity.vo.MemberConfirmInfoVO;
import com.allen.crowd.entity.vo.MemberLauchInfoVO;
import com.allen.crowd.entity.vo.ProjectVO;
import com.allen.crowd.entity.vo.ReturnVO;
import com.allen.crowd.mapper.MemberConfirmInfoPOMapper;
import com.allen.crowd.mapper.MemberLaunchInfoPOMapper;
import com.allen.crowd.mapper.ProjectItemPicPOMapper;
import com.allen.crowd.mapper.ProjectPOMapper;
import com.allen.crowd.mapper.ReturnPOMapper;
import com.allen.crowd.mapper.TagPOMapper;
import com.allen.crowd.mapper.TypePOMapper;
import com.allen.crowd.service.ProjectService;
import com.allen.crowd.util.CrowdUtils;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;
	
	@Autowired
	private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
	
	@Autowired
	private ProjectItemPicPOMapper projectItemPicPOMapper;
	
	@Autowired
	private ProjectPOMapper projectPOMapper;
	
	@Autowired
	private ReturnPOMapper returnPOMapper;
	
	@Autowired
	private TagPOMapper tagPOMapper;
	
	@Autowired
	private TypePOMapper typePOMapper;

	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public void saveProject(ProjectVO projectVO,String memberId) {
		// 1.保存ProjectPO
		ProjectPO projectPO = new ProjectPO();
		BeanUtils.copyProperties(projectVO, projectPO);
		
		projectPO.setMemberid(Integer.parseInt(memberId));
		
		projectPOMapper.insert(projectPO);
		
		// 2.获取保存ProjectPO后得到的自增主键
		// 在ProjectPOMapper.xml文件中insert方法对应的标签中设置useGeneratedKeys="true" keyProperty="id"
		Integer projectId = projectPO.getId();
		
		// 3.保存typeIdList
		List<Integer> typeIdList = projectVO.getTypeIdList();
		if(CrowdUtils.collectionEffectiveCheck(typeIdList)) {
			typePOMapper.insertRelationshipBatch(projectId, typeIdList);
		}
		
		// 4.保存tagIdList
		List<Integer> tagIdList = projectVO.getTagIdList();
		if(CrowdUtils.collectionEffectiveCheck(tagIdList)) {
			tagPOMapper.insertRelationshipBatch(projectId, tagIdList);
		}
		
		// 5.保存detailPicturePathList
		// ①从VO对象中获取detailPicturePathList
		List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
		if(CrowdUtils.collectionEffectiveCheck(detailPicturePathList)) {
			
			// ②创建一个空List集合，用来存储ProjectItemPicPO对象
			List<ProjectItemPicPO> projectItemPicPOList = new ArrayList<>();
			
			// ③遍历detailPicturePathList
			for (String detailPath : detailPicturePathList) {
				
				// ④创建projectItemPicPO对象
				ProjectItemPicPO projectItemPicPO = new ProjectItemPicPO(null, projectId, detailPath);
				
				projectItemPicPOList.add(projectItemPicPO);
			}
			
			// ⑤根据projectItemPicPOList执行批量保存
			projectItemPicPOMapper.insertBatch(projectItemPicPOList);
		}
		
		
		// 6.保存MemberLaunchInfoPO
		MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
		
		if(memberLauchInfoVO != null) {
			
			// ※※※※※※※※※※※补充功能※※※※※※※※※※※
			// 将旧的用户发起人信息删除
			MemberLaunchInfoPOExample example = new MemberLaunchInfoPOExample();
			example.createCriteria().andMemberidEqualTo(Integer.parseInt(memberId));
			memberLaunchInfoPOMapper.deleteByExample(example);
			// ※※※※※※※※※※※※※※※※※※※※※※※※※※
			
			MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
			BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
			
			memberLaunchInfoPO.setMemberid(Integer.parseInt(memberId));
			
			memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);
		}
		
		// 7.根据ReturnVO的List保存ReturnPO
		List<ReturnVO> returnVOList = projectVO.getReturnVOList();
		
		if(CrowdUtils.collectionEffectiveCheck(returnVOList)) {
			
			List<ReturnPO> returnPOList = new ArrayList<>();
			
			for (ReturnVO returnVO : returnVOList) {
				
				ReturnPO returnPO = new ReturnPO();
				BeanUtils.copyProperties(returnVO, returnPO);
				returnPO.setProjectid(projectId);
				returnPOList.add(returnPO);
			}
			returnPOMapper.insertBatch(returnPOList);
			
		}
		
		// 8.保存MemberConfirmInfoPO
		MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
		
		if(memberConfirmInfoVO != null) {
			MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO(null, Integer.parseInt(memberId), memberConfirmInfoVO.getPaynum(), memberConfirmInfoVO.getCardnum());
			memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
			
		}
		
	}
	
}
