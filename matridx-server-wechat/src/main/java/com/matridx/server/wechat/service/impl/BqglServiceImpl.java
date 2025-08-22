package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.BqglDto;
import com.matridx.server.wechat.dao.entities.BqglModel;
import com.matridx.server.wechat.dao.entities.WeChatTagFansModel;
import com.matridx.server.wechat.dao.entities.WeChatTagModel;
import com.matridx.server.wechat.dao.entities.WeChatTagUserModel;
import com.matridx.server.wechat.dao.entities.YhbqDto;
import com.matridx.server.wechat.dao.post.IBqglDao;
import com.matridx.server.wechat.service.svcinterface.IBqglService;
import com.matridx.server.wechat.service.svcinterface.IYhbqService;
import com.matridx.server.wechat.util.WeChatUtils;

@Service
public class BqglServiceImpl extends BaseBasicServiceImpl<BqglDto, BqglModel, IBqglDao> implements IBqglService{

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	IYhbqService yhbqSerivce;
	@Autowired
	RedisUtil redisUtil;
	
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
		if(bqglDto == null) {
            return false;
        }
		//保存到本地
        return insert(bqglDto);
    }
	
	/**
	 * 创建微信用户标签信息
	 * @param bqglDto
	 * @return
	 */
	public BqglDto createTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
		return WeChatUtils.createTag(bqglDto,restTemplate,redisUtil);
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
		if(!result) {
            return false;
        }
		//保存到本地
		return update(bqglDto);
    }

	/**
	 * 编辑微信用户标签信息
	 * @param bqglDto
	 * @return
	 */
	private boolean updateTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
        return WeChatUtils.updateTag(bqglDto,restTemplate,redisUtil);
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
		if(ids != null && ids.size() > 0){
			for (int i = 0; i < ids.size(); i++) {
                bqglDto.setBqid(ids.get(i));
				boolean result = deleteTag(bqglDto);
				if(!result) {
                    return false;
                }
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
		// TODO Auto-generated method stub
        return WeChatUtils.deleteTag(bqglDto,restTemplate,redisUtil);
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
		//获取标签，循环获取用户，保存
		List<WeChatTagModel> tagList = WeChatUtils.getTag(restTemplate, bqglDto,redisUtil);
		if(tagList !=null && tagList.size() > 0){
			List<BqglDto> bqglDtos = new ArrayList<>();
			for (int i = 0; i < tagList.size(); i++) {
				//marger into保存到标签管理表
				BqglDto t_bqglDto = new BqglDto();
				t_bqglDto.setBqid(tagList.get(i).getId());
				t_bqglDto.setBqm(tagList.get(i).getName());
				t_bqglDto.setWbcxid(bqglDto.getWbcxid());
				t_bqglDto.setWbcxdm(bqglDto.getWbcxdm());
				bqglDtos.add(t_bqglDto);
				bqglDto.setBqid(tagList.get(i).getId());
				bqglDto.setBqm(tagList.get(i).getName());
				//查询标签下用户信息
				WeChatTagUserModel tagUser = WeChatUtils.getTagUser(t_bqglDto,restTemplate,redisUtil);
				if(tagUser != null){
					//保存至标签用户表
					WeChatTagFansModel data = tagUser.getData();
					if(data != null){
						List<String> wxidList = data.getOpenid();
						if(wxidList != null && wxidList.size() > 0){
							YhbqDto yhbqDto = new YhbqDto();
							yhbqDto.setBqid(bqglDto.getBqid());
							yhbqDto.setWxids(wxidList);
							yhbqDto.setWbcxid(bqglDto.getWbcxid());
							boolean result = yhbqSerivce.insertByTags(yhbqDto);
							if(!result) {
                                return false;
                            }
						}
					}
				}
			}
			dao.deleteByBqglDtos(bqglDtos);
            return dao.insertByBqglDtos(bqglDtos);
		}
		return true;
	}

	/**
	 * 通过微信公众号查询标签
	 * @param bqglDto
	 * @return
	 */
	@Override
	public List<BqglDto> selectTag(BqglDto bqglDto) {
		// TODO Auto-generated method stub
		return dao.selectTag(bqglDto);
	}

}
