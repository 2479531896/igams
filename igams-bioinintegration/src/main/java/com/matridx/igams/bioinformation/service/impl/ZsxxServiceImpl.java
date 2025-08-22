package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.bioinformation.dao.entities.ZsxxDto;
import com.matridx.igams.bioinformation.dao.entities.ZsxxModel;
import com.matridx.igams.bioinformation.dao.post.IZsxxDao;
import com.matridx.igams.bioinformation.service.svcinterface.IWkcxService;
import com.matridx.igams.bioinformation.service.svcinterface.IZsxxService;
import com.matridx.igams.bioinformation.util.JDBCUtils;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ZsxxServiceImpl extends BaseBasicServiceImpl<ZsxxDto, ZsxxModel, IZsxxDao> implements IZsxxService {

	@Autowired
	private IWkcxService wkcxService;
	/**
	 * 根据ids查询list
	 */
	public List<ZsxxDto> getDtoListByIds(List<String> ids,String wkcxid){
		ZsxxDto zsxxDto=new ZsxxDto();
		zsxxDto.setIds(ids);
		List<ZsxxDto> dtoList = dao.getDtoList(zsxxDto);
		List<ZsxxDto> list = new ArrayList<>();
		WkcxDto wkcxDto=new WkcxDto();
		wkcxDto.setWkcxid(wkcxid);
		WkcxDto wkcxDto_t = wkcxService.getDto(wkcxDto);
		String xgsj="";
		if(wkcxDto_t!=null){
			if(StringUtil.isNotBlank(wkcxDto_t.getXgsj())){
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = null;
				try {
					date = simpleDateFormat.parse(wkcxDto_t.getXgsj());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				long ts = 0;
				if (date != null) {
					ts = date.getTime();
				}
				xgsj = String.valueOf(ts/1000);  // 转化为字符串
			}
		}
		if(dtoList!=null&&dtoList.size()>0){
			for(ZsxxDto dto:dtoList){
				dto.setGram_stain("");
				dto.setComment(dto.getZs());
				dto.setName(dto.getMc());
				dto.setLast_update(xgsj);
				dto.setOrigin_species(dto.getTaxids());
				dto.setId(dto.getZsid());
				list.add(dto);
			}
		}
		return list;
	}

	/**
	 * 插入
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(ZsxxDto zsxxDto){
		zsxxDto.setZsid(StringUtil.generateUUID());
		int result = dao.insert(zsxxDto);
		return result != 0;
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDto(ZsxxDto zsxxDto){
		int result = dao.update(zsxxDto);
		return result != 0;
	}

	/**
	 * 删除
	 */
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteDto(ZsxxDto zsxxDto){
		int result = dao.delete(zsxxDto);
		return result != 0;
	}

	/**
	 * 更新注释信息表
	 */
	public void updateZsxx() {
		JDBCUtils jdbcUtils = new JDBCUtils();
		List<ZsxxDto> list = jdbcUtils.queryZsxxData();
		List<ZsxxDto> newList= new ArrayList<>();
		if( list != null && list.size()>0 ){
			if(list.size()>100){
				for(int a=1;a<=list.size();a++){
					newList.add(list.get(a-1));
					if(a!=0&&a%100==0){
						dao.updateList(newList);
						newList.clear();
					}
				}
				if(newList.size()>0){
					dao.updateList(newList);
				}
			}else{
				dao.updateList(list);
			}
		}
	}
}
