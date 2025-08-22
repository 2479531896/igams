package com.matridx.igams.common.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.svcinterface.IJcsjService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.SelectListDto;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.post.ICommonRabbitDao;
import com.matridx.igams.common.dao.post.ISpgwcyDao;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonRabbitService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class CommonRabbitService  extends BaseServiceImpl<SelectListDto, ICommonRabbitDao> implements ICommonRabbitService {
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	
	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	ISpgwcyDao spgwcyDao;
	@Autowired
	IJcsjService jcsjService;

	
	/**
	 * 新增或修改用户信息
	 */
	@Override
	public void insertOrUpdateUser(User user) throws BusinessException {
		// TODO Auto-generated method stub
		int result = dao.insertOrUpdateUser(user);
		if(result == 0)
			throw new BusinessException("msg", "新增或修改用户失败！");
	}

	/**
	 * 删除用户信息
	 */
	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		dao.deleteUser(user);
	}

	/**
	 * 新增或修改角色信息
	 */
	@Override
	public void insertOrUpdateRole(Role role) throws BusinessException {
		// TODO Auto-generated method stub
		int result = dao.insertOrUpdateRole(role);
		if(result == 0)
			throw new BusinessException("msg", "新增或修改角色失败！");
	}

	/**
	 * 删除角色信息
	 */
	@Override
	public void deleteRole(Role role) {
		// TODO Auto-generated method stub
		dao.deleteRole(role);
	}

	@Override
	public boolean updateCkqxByjsid(Role role) {
		return dao.updateCkqxByjsid(role) != 0;
	}

	/**
	 * 用户机构权限新增
	 */
	@Override
	public void insertYhjgqxs(List<User> users) {
		// TODO Auto-generated method stub
		dao.insertYhjgqxList(users);
	}

	/**
	 * 用户机构权限删除
	 */
	@Override
	public void deleteYhjgqx(User user) {
		// TODO Auto-generated method stub
		dao.deleteJgqxxxByYhid(user);
	}

	/**
	 * 用户所属机构新增
	 */
	@Override
	public void insertYhssjg(User user) throws BusinessException {
		// TODO Auto-generated method stub
		int result =dao.insertYhssjg(user);
		if(result == 0) 
			throw new BusinessException("msg", "更新所属用户表失败！");
	}

	/**
	 * 用户所属机构删除
	 */
	@Override
	public void deleteYhssjg(User user) {
		// TODO Auto-generated method stub
		dao.deleteYhssjgByYhid(user);
	}

	/**
	 * 审批岗位成员删除
	 */
	@Override
	public void deleteSpgwcy(User user) {
		// TODO Auto-generated method stub
		spgwcyDao.deleteByYhid(user.getYhid());
	}

	/**
	 * 审批岗位成员新增
	 */
	@Override
	public void insertSpgwcys(List<SpgwcyDto> spgwcyList) {
		// TODO Auto-generated method stub
		spgwcyDao.toSelected(spgwcyList);
	}

	/**
	 * 用户角色新增
	 */
	@Override
	public void insertYhjs(User user) {
		// TODO Auto-generated method stub
		dao.insertYhjs(user);
	}

	/**
	 * 用户角色删除
	 */
	@Override
	public void deleteYhjs(User user) {
		// TODO Auto-generated method stub
		dao.deleteYhjs(user);
	}

	/**
	 * 机构信息新增或修改
	 */
	@Override
	public void insertOrUpdateJgxx(DepartmentDto departmentDto) {
		// TODO Auto-generated method stub
		dao.insertOrUpdateJgxx(departmentDto);
	}

	/**
	 * 机构信息删除
	 */
	@Override
	public void deleteJgxx(DepartmentDto departmentDto) {
		// TODO Auto-generated method stub
		dao.deleteJgxx(departmentDto);
	}

	/**
	 * 新增或修改系统审核信息
	 */
	@Override
	public void insertOrUpdateXtsh(XtshDto xtshDto) throws BusinessException {
		// TODO Auto-generated method stub
		int result = dao.insertOrUpdateXtsh(xtshDto);
		if(result == 0)
			throw new BusinessException("msg", "新增或修改系统审核信息失败！");
	}
	
    /**
     * 新增或修改审批岗位信息
     */
    @Override
    public void insertOrUpdateSpgw(SpgwDto spgwDto) throws BusinessException {
        // TODO Auto-generated method stub
        int result = dao.insertOrUpdateSpgw(spgwDto);
        if(result == 0)
            throw new BusinessException("msg", "新增或修改审批岗位信息失败！");
    }
    
	/**
	 * 删除审批岗位信息
	 */
	@Override
	public void deleteSpgw(SpgwDto spgwDto) {
		// TODO Auto-generated method stub
		dao.deleteSpgw(spgwDto);
	}


	/**
	 * 删除系统审核信息
	 */
	@Override
	public void deleteXtsh(XtshDto xtshDto) {
		// TODO Auto-generated method stub
		dao.deleteXtsh(xtshDto);
	}

	/**
	 * 角色单位权限新增
	 */
	@Override
	public void insertJsdwqx(Role role) {
		// TODO Auto-generated method stub
		dao.insertJsdwqx(role);
	}

	/**
	 * 角色单位权限删除
	 */
	@Override
	public void deleteJsdwqx(Role role) {
		// TODO Auto-generated method stub
		dao.deleteJsdwqx(role);
	}
	
	/**
	 * 根据审核id修改停用时间
	 */
	public void updateTysjByShid(SpgwDto spgwDto) {
		// TODO Auto-generated method stub
		dao.updateTysjByShid(spgwDto);
	}

	/**
	 * 新增审核流程
	 */
	public void insertBySpgwList(List<SpgwDto> spgwList) {
	// TODO Auto-generated method stub
		dao.insertBySpgwList(spgwList);
	}
	
	/**
	 * 新增审核类别
	 */
	public void insertShlb(ShlbDto shlbDto) {
		// TODO Auto-generated method stub
		dao.insertShlb(shlbDto);
	}
	
	/**
	 * 更新审核类别
	 */
	public void updateShlb(ShlbDto shlbDto) {
		// TODO Auto-generated method stub
		dao.updateShlb(shlbDto);
	}
	
	/**
	 * 删除审核类别
	 */
	public void deleteShlb(ShlbDto shlbDto) {
		dao.deleteShlb(shlbDto);
	}
	
	/**
	 * 新增或修改基础数据
	 */
	public void insertOrUpdateJcsj(JcsjDto jcsjDto) throws BusinessException {
		// TODO Auto-generated method stub
        int result = dao.insertOrUpdateJcsj(jcsjDto);
        if(result == 0)
            throw new BusinessException("msg", "新增或修改基础数据信息失败！");
		jcsjService.resetRedisJcsjList(jcsjDto.getJclb());
	}
	
	/**
	 * 删除基础数据
	 */
	public void deleteJcsj(JcsjDto jcsjDto) {
		dao.deleteJcsj(jcsjDto);
	}

	/**
	 * 审批岗位批量删除
	 */
	@Override
	public void batchDelSpgwcy(List<SpgwcyDto> spgwcyList) {
		// TODO Auto-generated method stub
		spgwcyDao.toOptional(spgwcyList);
	}

	/**
	 * 系统用户签名更新同步
	 */
	@Override
	public void userSign(FjcfbDto fjcfbDto) throws BusinessException {
		// TODO Auto-generated method stub
		//更新附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if(fjcfbDtos != null && fjcfbDtos.size() == 1){
			fjcfbDtos.get(0).setWjlj(fjcfbDto.getWjlj());
			fjcfbDtos.get(0).setFwjm(fjcfbDto.getFwjm());
			boolean result = fjcfbService.update(fjcfbDtos.get(0));
			if(!result)
				throw new BusinessException("msg", "修改签名失败！");
		}else if(fjcfbDtos==null||fjcfbDtos.isEmpty()){
			boolean result = fjcfbService.insert(fjcfbDto);
			if(!result)
				throw new BusinessException("msg", "修改签名失败！");
		}
		//根据文件路径下载文件，访问发布服务器
		String wjlj = fjcfbDto.getWjlj();
		String fwjlj = fjcfbDto.getFwjlj();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("wjlj", wjlj);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try{
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
			RestTemplate t_restTemplate = new RestTemplate();
			ResponseEntity<byte[]> response = t_restTemplate.exchange(applicationurl + fjcfbDto.getPrefix() + "/ws/getImportFile", HttpMethod.POST, httpEntity, byte[].class);
			// 校验文件夹目录是否存在，不存在就创建一个目录
			mkDirs(crypt.dCode(fwjlj));
			byte[] result = response.getBody();
			inputStream = new ByteArrayInputStream(result);

			outputStream = new FileOutputStream(filePath);

			int len;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
		} catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try{
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据路径创建文件
	 */
	private boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()){
			return true;
		}
		return file.mkdirs();
	}
	
	/**
	 * 用户所属机构删除
	 */
	public void deleteYhssjgByYhid(User user) {
		dao.deleteYhssjgByYhid(user);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void updateUsers(List<User> users) {
		List<List<User>> partition = ListUtils.partition(users, 100);
		for (List<User> userList : partition) {
			dao.updateUsers(userList);
		}
	}
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void insertUsers(List<User> users) throws BusinessException {
		List<List<User>> partition = ListUtils.partition(users, 100);
		for (List<User> userList : partition) {
			boolean isSuccess = dao.insertUsers(userList);
			if (!isSuccess){
				throw new BusinessException("msg", "批量新增用户失败！");
			}
		}
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void insertYghmcDtos(List<Map<String,String>> maps) {
		List<List<Map<String,String>>> lists = ListUtils.partition(maps, 100);
		for (List<Map<String,String>> list : lists) {
			dao.insertYghmcDtos(list);
		}
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void updateYghmcDtos(List<Map<String,String>> maps) {
		List<List<Map<String,String>>> lists = ListUtils.partition(maps, 100);
		for (List<Map<String,String>> list : lists) {
			dao.updateYghmcDtos(list);
		}
	}

	@Override
	public void updateDqtxByIds(Map<String, Object> map) {
		dao.updateDqtxByIds(map);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void insertYghtxxDtos(List<Map<String,String>> maps) {
		List<List<Map<String,String>>> lists = ListUtils.partition(maps, 100);
		for (List<Map<String,String>> list : lists) {
			dao.insertYghtxxDtos(list);
		}
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void insertYglzxxDtos(List<Map<String,String>> maps) {
		List<List<Map<String,String>>> lists = ListUtils.partition(maps, 100);
		for (List<Map<String,String>> list : lists) {
			dao.insertYglzxxDtos(list);
		}
	}

	@Override
	public void deleteYghtxx(Map<String, Object> map) {
		dao.deleteYghtxx(map);
	}

	@Override
	public void deleteYglzxx(Map<String, Object> map) {
		dao.deleteYglzxx(map);
	}

	@Override
	public void modJgxxList(List<DepartmentDto> departmentDtos) {
		List<List<DepartmentDto>> partition = ListUtils.partition(departmentDtos, 100);
		for (List<DepartmentDto> jgxxs : partition) {
			dao.modJgxxList(jgxxs);
		}
	}

	@Override
	public void updateYhssjg(User user) {
		dao.updateYhssjg(user);
	}

	@Override
	public void insertYhqtxx(User user) {
		dao.insertYhqtxx(user);
	}

	@Override
	public void deleteByWbcxid(DepartmentDto departmentDto) {
		dao.deleteByWbcxid(departmentDto);
	}
}
