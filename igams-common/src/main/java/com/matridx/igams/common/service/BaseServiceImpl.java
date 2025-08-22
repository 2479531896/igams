package com.matridx.igams.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.BaseDao;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import com.matridx.springboot.util.base.StringUtil;

/**
 * 通用Service实现<br>
 * 创建时间：2012-5-3<br>
 * 说明：daoBase注入方式
 * 修改人：ZhenFei.Cao
 * 修改时间：2012-08-02
 * @param <T>
 *            model
 * @param <E>
 *            dao
 * 
 */
public class BaseServiceImpl<T, E extends BaseDao<T>> implements BaseService<T> {

	@Autowired
	protected E dao;

	/**
	 * 用于spring注入
	 * 
	 * @param dao
	 */
	public void setDao(E dao) {
		this.dao = dao;
	}
	
	/**
	 * 增加记录
	 * @param t
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(T t){
		int result = dao.insert(t);
		return result > 0;
	}

	/**
	 * 批量插入
	 * @param map
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean batchInsert(List<T> list){
		int result = dao.batchInsert(list);
		return result > 0;
	}

	/**
	 * 修改记录
	 * @param t
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean update(T t){
		int result = dao.update(t);
		return result > 0;
	}
	
	/**
	 *删除记录
	 * @param t
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delete(T t){
		int result = dao.delete(t);
		return result > 0;
	}
	
	/**
	 *删除记录
	 * @param t
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteById(String id){
		int result = dao.deleteById(id);
		return result > 0;
	}

	/**
	 * 批量删除
	 * @param map
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean batchDeleteByMap(Map<String, Object> map){
		int result = dao.batchDeleteByMap(map);
		return result > 0;
	}
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean batchDelete(List<String> list){
		int result = dao.batchDelete(list);
		return result > 0;
	}

	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
	public T getModelById(String id){
		return dao.getModelById(id);
	}

	/**
	 * 查询单条数据
	 * @param t
	 * @return
	 */	
	public T getModel(T t){
		return dao.getModel(t);
	}
	
	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
	public List<T> getPagedList(T t){
		return dao.getPagedList(t);
	}

	/**
	 * 无分页查询
	 * @param t
	 * @return
	 */
	public List<T> getModelList(T t){

		return dao.getModelList(t);
	}

	/**
	 * 统计记录数
	 * @param t
	 * @return
	 */
	public int getCount(T t){
		return dao.getCount(t);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.matridx.igams.common.service.BaseService#getList(java.lang.Object)
	 */
	public List<T> getList(T t) {
		if (t instanceof BaseModel){
			((BaseModel) t).setPageSize(Integer.MAX_VALUE);
			return getPagedList(t);
		}
		return getModelList(t);
	}
	
	/**
	 * 主键IDs生成参数Map
	 * @param ids 逗号分隔的主键IDs
	 * @param mapKey map集合的主键key
	 * @return List<Map<String, String>>
	 */
	protected List<Map<String, String>> genarateMapList(String ids,String mapKey){
		if(StringUtil.isNotBlank(ids) && StringUtil.isNotBlank(mapKey)){
			String[] idArrs = ids.split(GlobalString.SEPARATOR_COMMA);
			List<Map<String,String>> list = new ArrayList<>();
            for (String idArr : idArrs) {
                HashMap<String, String> map = new HashMap<>();
                map.put(mapKey, idArr);
                list.add(map);
            }
			return list;
		}
		return null;
	}
	
	/**
     * 主键IDs生成参数Map
     * @param ids 指定分隔符拼接的IDs列表
     * @param separator 分隔符
     * @param mapVal 其他map值
     * @param mapKeys map集合的主键keys: mapVal对应的mapKey下标为0，ids中mapKey按顺序
     * @return List
     */
	protected List<Map<String, String>> genarateMapList(List<Object> ids,String separator,
			String mapVal,String[] mapKeys) {
	    List<Map<String, String>> list = new ArrayList<>();
	    if(ids != null && !ids.isEmpty() && StringUtil.isNotBlank(separator) 
	    		&& mapKeys != null && mapKeys.length > 0){
	    	for (Object o_id : ids) {
	    		String id = (String)o_id;
	    		if(StringUtil.isNotBlank(id)){
	    			String[] idArrs = id.split(separator);
	    			if(idArrs.length == (mapKeys.length - 1)){
	    				Map<String, String> map = new HashMap<>();
						map.put(mapKeys[0], mapVal);
						for(int i = 0; i < idArrs.length; i++){
							map.put(mapKeys[i + 1], idArrs[i]);
						}
						list.add(map);
	    			}
	    		}
	    	}
	    }
	    return list;
    }
	
	/**
	 * 把审核信息从源List复制到目标List
	 * @param ryList
	 * @param t_ryList
	 * @return
	 */
	protected boolean updateListShxxValue(List<?> targetList,List<?> t_srcList){
		if(targetList==null ||t_srcList==null||targetList.size() != t_srcList.size()|| targetList.isEmpty())
			return false;
		
		int size = targetList.size();
		for(int i=0;i<size;i++){
			BaseBasicModel t_bsModel = (BaseBasicModel)t_srcList.get(i);
			BaseBasicModel bsModel = (BaseBasicModel)targetList.get(i);
			//信息拷贝
			bsModel.setShxx_sqsj(t_bsModel.getShxx_sqsj());
			bsModel.setShxx_gwmc(t_bsModel.getShxx_gwmc());
			bsModel.setShxx_shxxid(t_bsModel.getShxx_shxxid());
			bsModel.setShxx_gcid(t_bsModel.getShxx_gcid());
			bsModel.setShxx_xh(t_bsModel.getShxx_xh());
			bsModel.setShxx_shid(t_bsModel.getShxx_shid());
			bsModel.setShxx_lcxh(t_bsModel.getShxx_lcxh());
			bsModel.setShxx_shsj(t_bsModel.getShxx_shsj());
			bsModel.setShxx_ssyj(t_bsModel.getShxx_ssyj());
			bsModel.setShxx_lrryxm(t_bsModel.getShxx_lrryxm());
			bsModel.setShxx_lrsj(t_bsModel.getShxx_lrsj());
			bsModel.setShxx_shryxm(t_bsModel.getShxx_shryxm());
			bsModel.setShxx_sftg(t_bsModel.getShxx_sftg());
			bsModel.setShxx_shyj(t_bsModel.getShxx_shyj());
		}
		return true;
	}
}
