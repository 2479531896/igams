package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.VendorDto;
import com.matridx.igams.production.dao.entities.VendorModel;
  
@Mapper
public interface VendorDao extends BaseBasicDao<VendorDto,VendorModel>{
	/**
	 * 判断去重
	 */
	VendorDto queryByCVenCode(VendorDto vendorDto);
	
	/**
	 * 查询供应商编码最大值
	 */
	Integer countMax(VendorDto vendorDto);
	
	/**
	 * 判断去重(供应商名称，供应商简称)
	 */
	List<VendorDto> queryByMcAndJc(VendorDto vendorDto);
	/**
	 * @description 停用供应商
	 */
    boolean disableOrEnableVendor(VendorDto vendorDto);
}
 