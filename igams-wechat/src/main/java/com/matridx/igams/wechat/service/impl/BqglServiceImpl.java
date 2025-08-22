package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.BqglDto;
import com.matridx.igams.wechat.dao.entities.BqglModel;
import com.matridx.igams.wechat.dao.entities.WeChatTagFansModel;
import com.matridx.igams.wechat.dao.entities.WeChatTagModel;
import com.matridx.igams.wechat.dao.entities.WeChatTagUserModel;
import com.matridx.igams.wechat.dao.entities.YhbqDto;
import com.matridx.igams.wechat.dao.post.IBqglDao;
import com.matridx.igams.wechat.service.svcinterface.IBqglService;
import com.matridx.igams.wechat.service.svcinterface.IYhbqService;
import com.matridx.igams.wechat.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BqglServiceImpl extends BaseBasicServiceImpl<BqglDto, BqglModel, IBqglDao> implements IBqglService{

	@Autowired
	IYhbqService yhbqSerivce;
	
	/**
	 * 查询全部标签
	 * @return
	 */
	@Override
	public List<BqglDto> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	/**
	 * 新增标签
	 * @param bqglDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
		//发送微信
		bqglDto = createTag(bqglDto);
		if(bqglDto == null)
			return false;
		//保存到本地
        return insert(bqglDto);
    }
	
	/**
	 * 创建微信用户标签信息
	 * @param bqglDto
	 * @return
	 */
	public BqglDto createTag(BqglDto bqglDto) {
		RestTemplate t_restTemplate = new RestTemplate();
		// TODO Auto-generated method stub
		bqglDto = WeChatUtil.createTag(bqglDto,t_restTemplate);
		return bqglDto;
	}

	/**
	 * 修改标签
	 * @param bqglDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
		//发送微信
		boolean result = updateTag(bqglDto);
		if(!result)
			return false;
		//保存到本地
		result = update(bqglDto);
        return result;
    }

	/**
	 * 编辑微信用户标签信息
	 * @param bqglDto
	 * @return
	 */
	private boolean updateTag(BqglDto bqglDto) {
		RestTemplate t_restTemplate = new RestTemplate();
		// TODO Auto-generated method stub
        return WeChatUtil.updateTag(bqglDto,t_restTemplate);
    }

	/**
	 * 删除标签
	 * @param bqglDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
		//发送微信
		List<String> ids = bqglDto.getIds();
		if(!CollectionUtils.isEmpty(ids)){
            for (String id : ids) {
                bqglDto.setBqid(id);
                boolean result = deleteTag(bqglDto);
                if (!result)
                    return false;
            }
		}
		//保存到本地
        return delete(bqglDto);
    }

	/**
	 * 删除微信用户标签信息
	 * @param bqglDto
	 * @return
	 */
	private boolean deleteTag(BqglDto bqglDto) {
		RestTemplate t_restTemplate = new RestTemplate();
		// TODO Auto-generated method stub
        return WeChatUtil.deleteTag(bqglDto,t_restTemplate);
    }

	/**
	 * 获取已创建标签信息
	 * @param bqglDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean getTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
		RestTemplate t_restTemplate = new RestTemplate();
		//获取标签，循环获取用户，保存
		List<WeChatTagModel> tagList = WeChatUtil.getTag(t_restTemplate);
		if(!CollectionUtils.isEmpty(tagList)){
			List<BqglDto> bqglDtos = new ArrayList<>();
            for (WeChatTagModel weChatTagModel : tagList) {
                //marger into保存到标签管理表
                bqglDto.setBqid(weChatTagModel.getId());
                bqglDto.setBqm(weChatTagModel.getName());
                bqglDtos.add(bqglDto);
                //查询标签下用户信息
                WeChatTagUserModel tagUser = WeChatUtil.getTagUser(bqglDto, t_restTemplate);
                //保存至标签用户表
                WeChatTagFansModel data = tagUser.getData();
                if (data != null) {
                    List<String> wxidList = data.getOpenid();
                    YhbqDto yhbqDto = new YhbqDto();
                    yhbqDto.setBqid(bqglDto.getBqid());
                    yhbqDto.setWxids(wxidList);
                    boolean result = yhbqSerivce.insertByTags(yhbqDto);
                    if (!result)
                        return false;
                }
            }
			dao.deleteByBqglDtos(bqglDtos);
            return dao.insertByBqglDtos(bqglDtos);
		}
		return true;
	}

}
