package com.matridx.igams.production.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjqxDto;
import com.matridx.igams.production.dao.entities.WjqxModel;
import com.matridx.igams.production.dao.post.IWjqxDao;
import com.matridx.igams.production.service.svcinterface.IWjqxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class WjqxServiceImpl extends BaseBasicServiceImpl<WjqxDto, WjqxModel, IWjqxDao> implements IWjqxService{

	@Autowired
	ICommonService commonService;

	/**
	 * 查询已有权限角色
	 */
	@Override
	public List<WjglDto> getSelectedJs(WjglDto wjglDto) {
		return dao.getSelectedJs(wjglDto);
	}
	
	/**
	 * 查询没有学习、查看权限角色
	 */
	@Override
	public List<WjglDto> getUnSelectedJs(WjglDto wjglDto) {
		return dao.getUnSelectedJs(wjglDto);
	}

	/**
	 * 根据文件ID删除权限信息
	 */
	@Override
	public void deleteByWjid(WjglDto wjglDto) {
		int result = dao.deleteByWjid(wjglDto);
    }

	/**
	 * 插入文件权限信息
	 */
	@Override
	public boolean insertByWjid(WjglDto wjglDto) {
		List<String> jsids = wjglDto.getJsids();
		List<String> t_jsids = wjglDto.getT_jsids();
		List<String> d_jsids = wjglDto.getD_jsids();
		if(!CollectionUtils.isEmpty(jsids)) {
			List<WjqxDto> wjqxList = new ArrayList<>();
			for (String jsid : jsids) {
				WjqxDto wjqxDto = new WjqxDto();
				wjqxDto.setQxid(StringUtil.generateUUID());
				wjqxDto.setWjid(wjglDto.getWjid());
				wjqxDto.setJsid(jsid);
				wjqxDto.setQxlx("STUDY");
				wjqxList.add(wjqxDto);
			}
			boolean isSuccesss = dao.insertByWjid(wjqxList);
			if(!isSuccesss)
				return false;
		}
		if(!CollectionUtils.isEmpty(t_jsids)) {
			List<WjqxDto> wjqxList = new ArrayList<>();
			for (String t_jsid : t_jsids) {
				WjqxDto wjqxDto = new WjqxDto();
				wjqxDto.setQxid(StringUtil.generateUUID());
				wjqxDto.setWjid(wjglDto.getWjid());
				wjqxDto.setJsid(t_jsid);
				wjqxDto.setQxlx("VIEW");
				wjqxList.add(wjqxDto);
			}
			boolean isSuccesss = dao.insertByWjid(wjqxList);
			if(!isSuccesss)
				return false;
		}
		if(!CollectionUtils.isEmpty(d_jsids)) {
			List<WjqxDto> wjqxList = new ArrayList<>();
			for (String d_jsid : d_jsids) {
				WjqxDto wjqxDto = new WjqxDto();
				wjqxDto.setQxid(StringUtil.generateUUID());
				wjqxDto.setWjid(wjglDto.getWjid());
				wjqxDto.setJsid(d_jsid);
				wjqxDto.setQxlx("DOWNLOAD");
				wjqxList.add(wjqxDto);
			}
			return dao.insertByWjid(wjqxList);
		}
		
		return true;
	}

	/**
	 * 修改文件权限信息
	 */
	@Override
	public boolean updateByWjid(WjglDto wjglDto) {
		//先删除再添加
		dao.deleteByWjid(wjglDto);
		return insertByWjid(wjglDto);
	}

	/**
	 * 根据文件ID查询有权限用户
	 */
	@Override
	public List<WjglDto> selectXtyhByWjid(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		return dao.selectXtyhByWjid(wjglDto);
	}

	/**
	 * 查询已有学习权限角色
	 */
	@Override
	public List<WjglDto> getStudyJs(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		return dao.getStudyJs(wjglDto);
	}

	/**
	 * 查询已有查看权限角色
	 */
	@Override
	public List<WjglDto> getViewJs(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		return dao.getViewJs(wjglDto);
	}
	
	/**
	 * 查询学习权限角色
	 */
	@Override
	public String SelectStudyJs(WjglDto wjglDto){
		// TODO Auto-generated method stub
		List<WjglDto> Studylist=dao.getStudyJs(wjglDto);
		StringBuilder sb = new StringBuilder(" ");
		if(!CollectionUtils.isEmpty(Studylist)) {
		for(int i =0;i<Studylist.size();i++) {
			if(i!=0) {
				sb.append(",");
			}
			sb.append(Studylist.get(i).getStudyjsmc());
			}
			return new String(sb);
	}
		return null;
	}
	
	/**
	 * 查询查看权限角色
	 */
	@Override
	public String SelectViewJs(WjglDto wjglDto){
		// TODO Auto-generated method stub
		List<WjglDto> viewlist=dao.getViewJs(wjglDto);
		StringBuilder sb = new StringBuilder();
		if(!CollectionUtils.isEmpty(viewlist)) {
		for(int i =0;i<viewlist.size();i++) {
			if(i!=0) {
				sb.append(",");
			}
			sb.append(viewlist.get(i).getViewjsmc());
			}
			return new String(sb);
	}
		return null;
	}

	/**
	 * 根据文件ID获取文件权限信息
	 */
	@Override
	public List<WjqxDto> selectWjqxlist(WjglDto t_wjglDto) {
		// TODO Auto-generated method stub
		return dao.selectWjqxlist(t_wjglDto);
	}

	/**
	 * 新增文件权限
	 */
	@Override
	public boolean insertByWjqxDtos(List<WjqxDto> wjqxDtos) {
		// TODO Auto-generated method stub
		return dao.insertByWjid(wjqxDtos);
	}

	/**
	 * 查询已有下载权限角色
	 */
	@Override
	public List<WjglDto> getDownloadJs(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		return dao.getDownloadJs(wjglDto);
	}

	/**
	 * 查询没有下载权限角色
	 */
	@Override
	public List<WjglDto> getUndownloadJs(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		return dao.getUndownloadJs(wjglDto);
	}

	/**
	 * 查询是否有下载权限
	 */
	@Override
	public boolean isDownload(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		int result = dao.getXzCountByJs(wjglDto);
		return result > 0;
	}

	/**
	 * 获取角色文件权限
	 */
	@Override
	public Map<String, List<WjglDto>> getPermission(WjglDto wjglDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, Object> rolemap = commonService.getRoleList(null, request);
		@SuppressWarnings("unchecked")
		List<Role> roles = (List<Role>) rolemap.get("roles");
		wjglDto.setRoles(roles);
		List<WjglDto> wjglDtos = dao.getPermission(wjglDto);
		List<WjglDto> viewList = new ArrayList<>();
		List<WjglDto> studyList = new ArrayList<>();
		List<WjglDto> t_wjglList = new ArrayList<>();
		List<WjglDto> downloadList = new ArrayList<>();
		List<WjglDto> undownloadList = new ArrayList<>();
		for (WjglDto dto : wjglDtos) {
			//查看权限
			if (dto.getCkflg() == 1) {
				viewList.add(dto);
			}
			//学习权限
			if (dto.getXxflg() == 1) {
				studyList.add(dto);
			}
			//下载权限
			if (dto.getXzflg() == 1) {
				downloadList.add(dto);
			} else {
				undownloadList.add(dto);
			}
			//没有查看和学习的权限
			if (dto.getCkflg() == 0 && dto.getXxflg() == 0) {
				t_wjglList.add(dto);
			}
		}
		Map<String, List<WjglDto>> map = new HashMap<>();
		map.put("viewList", viewList);
		map.put("studyList", studyList);
		map.put("t_wjglList", t_wjglList);
		map.put("downloadList", downloadList);
		map.put("undownloadList", undownloadList);
		return map;
	}

	/**
	 * 获取文件权限人员信息
	 */
	@Override
	public WjglDto getPermissionStr(WjglDto wjglDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, List<WjglDto>> permissionMap = getPermission(wjglDto, request);
		//查询查看权限角色
		List<WjglDto> viewList = permissionMap.get("viewList");
		String viewjsmc = wjglDtosToString(viewList);
		wjglDto.setViewjsmc(viewjsmc);
		//查询学习权限角色
		List<WjglDto> studyList = permissionMap.get("studyList");
		String studyjsmc = wjglDtosToString(studyList);
		wjglDto.setStudyjsmc(studyjsmc);
		//查询下载权限角色
		List<WjglDto> downloadList = permissionMap.get("downloadList");
		String downloadjsmc = wjglDtosToString(downloadList);
		wjglDto.setDownloadjsmc(downloadjsmc);
		return wjglDto;
	}
	
	/**
	 * 文件管理角色拼接成字符串
	 */
	private String wjglDtosToString(List<WjglDto> wjglDtos) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		if(!CollectionUtils.isEmpty(wjglDtos)) {
			for(int i =0;i<wjglDtos.size();i++) {
				if(i!=0) {
					sb.append(",");
				}
				sb.append(wjglDtos.get(i).getJsmc());
			}
			return new String(sb);
		}
		return null;
	}

/**
	 * 查询已有下载权限角色拼接成字符串
	 */
	@Override
	public String SelectDownloadJs(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		List<WjglDto> downloadlist=dao.getDownloadJs(wjglDto);
		StringBuilder sb = new StringBuilder();
		if(!CollectionUtils.isEmpty(downloadlist)) {
			for(int i =0;i<downloadlist.size();i++) {
				if(i!=0) {
					sb.append(",");
				}
				sb.append(downloadlist.get(i).getDownloadjsmc());
			}
			return new String(sb);
		}
		return null;
	}

	@Override
	public List<WjglDto> getPagedRoles(WjglDto wjglDto) {
		return dao.getPagedRoles(wjglDto);
	}
}
