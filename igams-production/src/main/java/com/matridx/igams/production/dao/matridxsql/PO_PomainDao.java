
  package com.matridx.igams.production.dao.matridxsql;
  
  import org.apache.ibatis.annotations.Mapper;
  
  import com.matridx.igams.common.dao.BaseBasicDao;
  import com.matridx.igams.production.dao.entities.PO_PomainDto;
  import com.matridx.igams.production.dao.entities.PO_PomainModel;
  import org.apache.ibatis.annotations.Param;

  @Mapper 
  public interface PO_PomainDao extends BaseBasicDao<PO_PomainDto,PO_PomainModel>{
	/**
	 * 获取POID最大值
	 */
	Integer getMax(PO_PomainDao pO_PomainDao);

	/**
	 * 获取u8的流水号
	 * num 截取几位
	 */
	String getContractSerial(@Param("prefix") String prefix,@Param("num") String num);
  }
 