package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.dao.entities.LscxszModel;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.ILscxszDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ILscxszService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LscxszServiceImpl extends BaseBasicServiceImpl<LscxszDto, LscxszModel, ILscxszDao> implements ILscxszService{

	@Autowired
	private IXtszService xtszService;;
	  /**
     * 插入查询信息
     */
    @Override
    public boolean insert(LscxszModel lscxszModel){
    	String cxid = StringUtil.generateUUID();
    	lscxszModel.setCxid(cxid);
    	int result = dao.insert(lscxszModel);
    	return result > 0;
    }
    
	/**
	 * 根据查询id查询临时查询信息
	 */
	public LscxszDto selectLscxByCxid(LscxszDto lscxszDto) {
		return dao.selectLscxByCxid(lscxszDto);
	}
	
	/**
	 * 获取临时查询结果
	 */
	public List<HashMap<String, Object>> getQueryResult(LscxszDto lscxszDto){
		return dao.getQueryResult(lscxszDto);
	}

	/**
	 * 执行sql
	 */
	public boolean executeResult(LscxszDto lscxszDto){
		int result = dao.executeResult(lscxszDto);
		return result > 0;
	}

	/**
	 * 定时任务执行相应代码
	 */
	public void executeByParam(Map<String, String> params) {
		try {
			String lscxid = params.get("lscxid");
			if (StringUtil.isBlank(lscxid)) {
				return;
			}
			LscxszDto lscxszDto = new LscxszDto();
			lscxszDto.setCxid(lscxid);
			LscxszDto t_lscxszDto = selectLscxByCxid(lscxszDto);
			if(t_lscxszDto == null){
				return;
			}
			// 处理查询sql
			t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
			String cxdmone = dealStatisticsQuerySql(t_lscxszDto, "dsrq");
			t_lscxszDto.setCxdm(cxdmone);
			String cxdm = dealQuerySql(t_lscxszDto, "dsrq");

			if(cxdm.toLowerCase().contains("matridxins")) {
				cxdm = cxdm.replaceAll("matridxins", "insert into");
			}
			if(cxdm.toLowerCase().contains("matridxup")) {
				cxdm = cxdm.replaceAll("matridxup", "update");
			}

			lscxszDto.setCxdm(cxdm);

			List<HashMap<String, Object>> listResult;
			if(cxdm.contains("insert into") || cxdm.contains("update")) {
				executeResult(lscxszDto);
				return;
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 获取销售查询字段
	 */
	@Override
	public List<Map<String, String>> getFieldList(String cxdm) {
		// TODO Auto-generated method stub
		List<Map<String,String>> fieldList = new ArrayList<>();
		char[] chars = cxdm.toCharArray();
        int count = 0;
        for(char i : chars){
            if (i == '{'){
            	count++;
            }
        }
        for (int i = 0; i < count; i++) {
        	Map<String,String> map = new HashMap<>();
        	int start = cxdm.indexOf("{");
        	int end = cxdm.indexOf("}");
        	if(start != -1 && end!= -1) {
        		String substring = cxdm.substring(start+1, end);
            	String[] split = substring.split(":|：");
            	if(split.length >=3) {
	            	map.put("zdmc", split[0]);
	            	map.put("zdgs", split[1]);
	            	map.put("zdmrz", "");
	            	if(!"null".equals(split[2])) {// 判断是否获取默认时间
						if(split[2].contains("startDate")) {
							if("startDate".equals(split[2])){
								List<String> nearDate = StringUtil.getNearDate(split[1]);
								if(nearDate != null)
									map.put("zdmrz", nearDate.get(0));
							}else{
								map.put("zdmrz", dealMrrq(split[2],split[1]));
							}
						}else if(split[2].contains("endDate")) {
							if("endDate".equals(split[2])){
								List<String> nearDate = StringUtil.getNearDate(split[1]);
								if(nearDate != null)
									map.put("zdmrz", nearDate.get(0));
							}else{
								map.put("zdmrz", dealMrrq(split[2],split[1]));
							}
						}else if("月初".equals(split[2])) {
							String dateStr = new SimpleDateFormat(split[1]).format(new Date()) + "-01";
							map.put("zdmrz", dateStr);
						}else if("当日".equals(split[2])) {
							String dateStr = new SimpleDateFormat(split[1]).format(new Date());
							map.put("zdmrz", dateStr);
						}else if("当月".equals(split[2])) {
							String dateStr = new SimpleDateFormat(split[1]).format(new Date());
							map.put("zdmrz", dateStr);
						}else if("当年".equals(split[2])) {
							String dateStr = new SimpleDateFormat(split[1]).format(new Date());
							map.put("zdmrz", dateStr);
						}else {
							map.put("zdmrz", split[2]);
						}
	            	}
	            	fieldList.add(map);
	            	// 通过字符串判断参数合并
	            	cxdm = cxdm.replace(cxdm.substring(start, end+1), "");
            	}else if(split.length ==2){
	            	map.put("zdmc", split[0]);
	            	map.put("zdgs", split[1]);
	            	map.put("zdmrz", "");
	            	fieldList.add(map);
	            	// 通过字符串判断参数合并
	            	cxdm = cxdm.replace(cxdm.substring(start, end+1), "");
            	}else if(split.length ==1){
	            	map.put("zdmc", split[0]);
	            	map.put("zdgs", "");
	            	map.put("zdmrz", "");
	            	fieldList.add(map);
	            	// 通过字符串判断参数合并
	            	cxdm = cxdm.replace(cxdm.substring(start, end+1), "");
            	}else {
	            	// 通过字符串判断参数合并
	            	cxdm = cxdm.replace(cxdm.substring(start, end+1), "");
            	}
        	}
		}
		return fieldList;
	}


	public String dealMrrq(String split2,String split1){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			if (split2.split("-").length > 1) {
				String[] str = split2.split("-");
				List<String> nearDate = StringUtil.getNearDate(split1);
				if (nearDate != null) {
					Date date = formatter.parse(nearDate.get(0));
					calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(str[1]));
				}
			} else if (split2.split("\\+").length > 1) {
				String[] str = split2.split("\\+");
				List<String> nearDate = StringUtil.getNearDate(split1);
				if (nearDate != null) {
					Date date = formatter.parse(nearDate.get(0));
					calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, Integer.valueOf(str[1]));
				}
			}
		}catch(Exception e){
			e.getMessage();
		}
		return formatter.format(calendar.getTime());
	}

	/**
	 * 查询sql处理
	 */
	@Override
	public String dealQuerySql(LscxszDto t_lscxszDto, String yhid) {
		// TODO Auto-generated method stub
		String cxdm = t_lscxszDto.getCxdm();
//		String result_dm="";
		if(StringUtil.isNotBlank(cxdm)) {
			// 替换查询条件
			if(t_lscxszDto.getCxtjs()!=null && t_lscxszDto.getCxtjs().size() > 0) {
				char[] chars = cxdm.toCharArray();
		        int count = 0;
		        for(char i : chars){
		            if (i == '{'){
		            	count++;
		            }
		        }
				String t_cxdm = cxdm;
				List<String> cxdm_cxtjs=new ArrayList<>();
		        for (int i = 0; i < count; i++) {
					int start = t_cxdm.indexOf("{");
					int end = t_cxdm.indexOf("}");
					boolean sfcf = false;
					if((start != -1 && end != -1)){
						if (!CollectionUtils.isEmpty(cxdm_cxtjs)) {
							for (String t_cstj : cxdm_cxtjs) {
								if ( t_cstj.equals(t_cxdm.substring(start, end + 1)))
									sfcf = true;
							}
						}
						if (!sfcf){
							cxdm_cxtjs.add(t_cxdm.substring(start, end + 1));
						}
						t_cxdm= t_cxdm.substring(end+2,t_cxdm.length());
					}
				}
				for( int j=0;j<cxdm_cxtjs.size();j++){
					if("null".equals(t_lscxszDto.getCxtjs().get(j))) {
						cxdm = cxdm.replace(cxdm_cxtjs.get(j), "");
					}else{
						StringBuilder cxtj= new StringBuilder(t_lscxszDto.getCxtjs().get(j));
						if(t_lscxszDto.getCxtjs().get(j).contains(";") || t_lscxszDto.getCxtjs().get(j).contains("；") ) {
							cxtj = new StringBuilder();
							String t_cxtj=t_lscxszDto.getCxtjs().get(j).replaceAll(";", ",");
							t_cxtj=t_cxtj.replaceAll("；", ",");
							String[] sp_cxtj=t_cxtj.split(",");
							for (String s : sp_cxtj) {
								cxtj.append(",").append("'").append(s).append("'");
							}
							cxtj = new StringBuilder(cxtj.substring(2));
							cxtj = new StringBuilder(cxtj.substring(0, cxtj.length() - 1));
						}
						cxdm = cxdm.replace(cxdm_cxtjs.get(j), cxtj.toString());
					}
				}
			}
			// 符号替换
			if (cxdm.toLowerCase().contains("＞")) {
				cxdm = cxdm.replaceAll("＞", ">");
			}
			if (cxdm.toLowerCase().contains("＜")) {
				cxdm = cxdm.replaceAll("＜", "<");
			}
			// 判断是否需要限制用户权限
			if(cxdm.contains("[[yhid]]")) {
				cxdm = cxdm.replace("[[yhid]]", yhid);
			}
		}
		return cxdm;
	}
	
	/**
	 * 统计sql处理
	 */
	public String dealStatisticsQuerySql(LscxszDto t_lscxszDto, String yhid) {
		// TODO Auto-generated method stub
		String cxdm = t_lscxszDto.getCxdm();
		if(StringUtil.isNotBlank(cxdm)) {
			// 替换查询条件
			char[] chars = cxdm.toCharArray();
	        int count = 0;
	        for(char i : chars){
	            if (i == '{'){
	            	count++;
	            }
	        }
	        for (int i = 0; i < count; i++) {
	        	int start = cxdm.indexOf("{");
	        	int end = cxdm.indexOf("}");
	        	if(start != -1 && end != -1) {
	        		String paramString = cxdm.substring(start, end+1);
	        		if("{当月}".equals(paramString)){
	        			String dateStr = new SimpleDateFormat("yyyy-MM").format(new Date());
	        	        cxdm = cxdm.replace(paramString, dateStr);
		        	}else if(paramString.contains("{当日")){
						if("{当日}".equals(paramString)){
							String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
							cxdm = cxdm.replace(paramString, dateStr);
						}else{
							cxdm = cxdm.replace(paramString, dealMrrq(paramString.substring(1, paramString.length()-1),"YYYY-MM-dd"));
						}

		        	}else if("{当年}".equals(paramString)){
	        			String dateStr = new SimpleDateFormat("yyyy").format(new Date());
	        	        cxdm = cxdm.replace(paramString, dateStr);
		        	}else if(paramString.startsWith("{指定月份:")){
		        		//Calendar c = Calendar.getInstance();
		        		//c.add(Calendar.MONTH, month);
		        		String[] params= paramString.replace("{", "").replace("}", "").split(":");
		        		
		        		Calendar cale = Calendar.getInstance();
		                cale.add(Calendar.MONTH, Integer.parseInt(params[1]));
		                
	        			String dateStr = new SimpleDateFormat("yyyy-MM").format(cale.getTime());
	        	        cxdm = cxdm.replace(paramString, dateStr);
		        	}else if(paramString.startsWith("{指定月初:")){
		        		//Calendar c = Calendar.getInstance();
		        		//c.add(Calendar.MONTH, month);
		        		String[] params= paramString.replace("{", "").replace("}", "").split(":");
		        		
		        		Calendar cale = Calendar.getInstance();
		                cale.add(Calendar.MONTH, Integer.parseInt(params[1]));
		                cale.set(Calendar.DAY_OF_MONTH, 1);
	        			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(cale.getTime());
	        	        cxdm = cxdm.replace(paramString, dateStr);
		        	}else if(paramString.startsWith("{指定月末:")){
		        		//Calendar c = Calendar.getInstance();
		        		//c.add(Calendar.MONTH, month);
		        		String[] params= paramString.replace("{", "").replace("}", "").split(":");
		        		
		        		Calendar cale = Calendar.getInstance();
		                cale.add(Calendar.MONTH, Integer.parseInt(params[1] + 1));
		                cale.set(Calendar.DAY_OF_MONTH, 0);
		                
	        			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(cale.getTime());
	        	        cxdm = cxdm.replace(paramString, dateStr);
					} /*
						 * else { cxdm = cxdm.replace(paramString, ""); }
						 */
	        	}
			}
			// 符号替换
			if (cxdm.toLowerCase().contains("＞")) {
				cxdm = cxdm.replaceAll("＞", ">");
			}
			if (cxdm.toLowerCase().contains("＜")) {
				cxdm = cxdm.replaceAll("＜", "<");
			}
			// 判断是否需要限制用户权限
			if(cxdm.contains("[[yhid]]")) {
				cxdm = cxdm.replace("[[yhid]]", yhid);
			}
		}
		return cxdm;
	}

	@Override
	public List<LscxszDto> getPagedDtoListByLimt(LscxszDto lscxszDto) {
		return dao.getPagedDtoListByLimt(lscxszDto);
	}
	
	/**
	 * 获取精简限制的临时查询信息，不包含sql代码
	 */
	public List<LscxszDto> getPagedSimpDtoListByLimt(LscxszDto lscxszDto){
		return dao.getPagedSimpDtoListByLimt(lscxszDto);
	}

	/**
	 * 获取精简限制的临时查询信息，不包含sql代码，主要是针对统计消息发送，里面不需要对用户权限进行限制
	 */
	public List<LscxszDto> getSimpDtoListByCode(LscxszDto lscxszDto){
		return dao.getSimpDtoListByCode(lscxszDto);
	}
	
	/**
	 * 获取指定统计区分的临时查询结果,主要是针对统计消息发送
	 */
	public List<LscxszDto> getDtoListByCode(LscxszDto lscxszDto){
		return dao.getDtoListByCode(lscxszDto);
	}
	
	/**
	 * 查询是否符合添加条件
	 */
	@Override
	public boolean queryByCxid(LscxszDto lscxszDto) {
		List<LscxszDto> lscxszDtos = dao.queryByCxid(lscxszDto);
		return lscxszDtos.size() <= 0;
	}

	/**
	 * @Description: 根据查询区分获取临时查询表数据
	 * @param lscxszDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.LscxszDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/2/27 15:49
	 */
	@Override
	public List<LscxszDto> selectLscxByQfdm(LscxszDto lscxszDto) {
		return dao.selectLscxByQfdm(lscxszDto);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getResult(LscxszDto lscxszDto) {
		return dao.getResult(lscxszDto);
	}

	/**
	 * 处理华山专用下载数据接口
	 * @param lscxszDto
	 */
	public String dealDownloadHuashanData(LscxszDto lscxszDto){
		String cxdm = lscxszDto.getCxdm();
		XtszDto xtszDto=xtszService.getDtoById("huashan.lscx.selectdata");
		String cxqz=xtszDto.getSzz();//需要添加的查询前缀
		String cxhz =")hstab\n" +
				")";//需要添加的查询后缀
		//处理查询代码，将外层查询代码替换为查询前缀和查询后缀
		if(StringUtil.isNotBlank(cxdm)) {
			int indexstart=cxdm.indexOf("from (");//首个
			if(indexstart==-1){
				indexstart=cxdm.indexOf("FROM (");//首个
			}
			int indexend=cxdm.lastIndexOf(")");//末尾
			//拼接查询前缀与后缀，生成新SQL
			cxdm=cxqz+ cxdm.substring(indexstart+6,indexend)+cxhz;
		}
		return cxdm;
	}
}
