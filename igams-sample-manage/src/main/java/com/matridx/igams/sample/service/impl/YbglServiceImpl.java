package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LshkDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.SerialNumTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILshkService;
import com.matridx.igams.common.service.svcinterface.IStatisService;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.entities.YbglModel;
import com.matridx.igams.sample.dao.entities.YblyDto;
import com.matridx.igams.sample.dao.post.IYbglDao;
import com.matridx.igams.sample.dao.post.IYblyDao;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.ISbkxglService;
import com.matridx.igams.sample.service.svcinterface.IYbglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class YbglServiceImpl extends BaseBasicServiceImpl<YbglDto, YbglModel, IYbglDao> implements IYbglService,IStatisService,IFileImport{

	private final Logger logger = LoggerFactory.getLogger(YbglServiceImpl.class);
	@Autowired
	ISbkxglService sbkxglService;
	@Autowired
	ILshkService lshkservice;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IYblyDao yblyDao;
	@Autowired
	ISbglService sbglService;
	
	/**
	 * 根据来源ID获取所有属于该来源的标本列表
	 */
	public List<YbglDto> getDtoByLyid(String lyid){
		YbglDto ybglDto = new YbglDto();
		ybglDto.setLyid(lyid);
		return dao.getDtoList(ybglDto);
	}
	
	@Override
	public List<YbglDto> getPagedDtoList(YbglDto ybglDto){
		List<YbglDto> ybglDtos = dao.getPagedDtoList(ybglDto);
		
		jcsjService.handleCodeToValue(ybglDtos,
				new BasicDataTypeEnum[] {
						BasicDataTypeEnum.SAMPLE_TYPE}, new String[] {
				"yblx:yblxmc"});
		//处理位置信息
		dealListOfPos(ybglDtos);
		
		return ybglDtos;
	}
	
	@Override
	public YbglDto getDtoById(String ybid){
		YbglDto ybglDto = dao.getDtoById(ybid);
		
		dealDtoOfPos(ybglDto);
		
		return ybglDto;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<String> insertYbDto(YbglDto ybglDto){
		List<YbglDto> mxs = ybglDto.getMxs();
		
		if(mxs == null || mxs.isEmpty())
			return null;
		
		List<String> bhs = new ArrayList<>();
		
		//获取标本来源信息
		YblyDto yblyDto = yblyDao.getDtoById(mxs.get(0).getLyid());
		//更新标本来源的状态
		YblyDto t_YblyDto = new YblyDto();
		//设置标本来源状态
		t_YblyDto.setZt(ybglDto.getLyzt());
		//设置来源ID
		t_YblyDto.setLyid(mxs.get(0).getLyid());
		//设置修改人员
		t_YblyDto.setXgry(ybglDto.getLrry());
		
		yblyDao.update(t_YblyDto);

        for (YbglDto t_YbglDto : mxs) {
            //设置标本ID
            String ybid = StringUtil.generateUUID();

            t_YbglDto.setYbid(ybid);
            //设置状态
            t_YbglDto.setZt(StatusEnum.CHECK_PASS.getCode());
            //标本来源类型固定成 0 外部
            t_YbglDto.setYblylx("0");
            //保存原数量
            t_YbglDto.setYsl(t_YbglDto.getSl());
            //获取时间
            t_YbglDto.setHqsj("2018-12");
            //设置录入人员
            t_YbglDto.setLrry(ybglDto.getLrry());
            //产品编号
            t_YbglDto.setCpbh(ybglDto.getCpbh());
            //标本规格
            t_YbglDto.setYbgg(ybglDto.getYbgg());
            //标本批号
            t_YbglDto.setYbph(ybglDto.getYbph());
            //生成内部用流水号
            LshkDto lshkDto = new LshkDto();
            lshkDto.setYbly(yblyDto.getLyd());
            lshkDto.setYblx(yblyDto.getYblx());
            try {
                String ybbh = lshkservice.doMakeSerNum(SerialNumTypeEnum.SAMP_CODE, lshkDto);
                t_YbglDto.setYbbh(ybbh);
                bhs.add(ybbh);
            } catch (Exception e) {
                t_YbglDto.setYbbh("9999");
                // TODO: handle exception
            }

            dao.insert(t_YbglDto);
            //同时更新标本空闲表
            sbkxglService.recountSbkx(t_YbglDto.getHzid());
        }
		return bhs;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean update(YbglModel ybglModel){
		int result = dao.update(ybglModel);
		
		//同时更新标本空闲表
		sbkxglService.recountSbkx(ybglModel.getHzid());
		return result > 0;
	}
	
	/**
	 * 获取列表里的根据位置的数字信息，转换成正方形位置
	 */
	private void dealListOfPos(List<YbglDto> ybgllist){
		if(ybgllist == null || ybgllist.isEmpty()){
			return;
		}
		for (YbglDto ybglDto : ybgllist) {
            dealDtoOfPos(ybglDto);
        }
	}
	
	/**
	 * 获取统计页面信息,用于获取页面初始化的数据，如果有一些 大量耗时间的处理，建议放到 getStatisSample等异步处理中
	 */
	@Override
	public ModelAndView getStatisPage() {
		// TODO Auto-generated method stub
		ModelAndView mv = new ModelAndView("sample/statis/samp_statis");
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_TYPE});
		mv.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		
		return mv;
	}
	
	/**
	 * 获取标本的统计信息
	 */
	public Map<String,Object> getStatisSample(YbglDto ybglDto){
		Map<String,Object> result = new HashMap<>();
		//标本类型统计
		List<Map<String, String>> yblxList= dao.getYblxCount();
		result.put("yb", yblxList);
		//冰箱空闲统计
		List<Map<String, String>> bxkxList= dao.getBxkxCount();
		result.put("bxkx", bxkxList);
		
		//标本使用统计
		Map<String,Object> ybsyList = getStatisSampUse(ybglDto);
		result.put("ybsy", ybsyList);

		
		result.put("ybs", ybsyList);
		
		return result;
	}
	
	/**
	 * 获取标本使用的统计信息
	 */
	public Map<String,Object> getStatisSampUse(YbglDto ybglDto){
		if(StringUtil.isBlank(ybglDto.getTj()) || "yf".equals(ybglDto.getTj())){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM"); 
			Calendar cal = Calendar.getInstance();
			List<String> ids = new ArrayList<>();
			for(int i=0;i<12;i++){
				String yf = format.format(cal.getTime());
				ids.add(yf);
				
				cal.add(Calendar.MONTH, -1);
			}
			ybglDto.setIds(ids);
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy"); 
			Calendar cal = Calendar.getInstance();
			List<String> ids = new ArrayList<>();
			for(int i=0;i<5;i++){
				String nd = format.format(cal.getTime());
				ids.add(nd);
				
				cal.add(Calendar.YEAR, -1);
			}
			ybglDto.setIds(ids);
		}
		
		List<Map<String, String>> ybqkList= dao.getYbsyqkCount(ybglDto);
		List<Map<String, String>> xzList= new ArrayList<>();
		List<Map<String, String>> syList= new ArrayList<>();
		List<String> cateList= new ArrayList<>();
		Map<String,Object> ybsyList = new HashMap<>();
        for (Map<String, String> stringStringMap : ybqkList) {
            if (stringStringMap.get("lx").equals("ybxz")) {
                xzList.add(stringStringMap);
                cateList.add(String.valueOf(stringStringMap.get("sj")));
            } else {
                syList.add(stringStringMap);
            }
        }
		ybsyList.put("xzList", xzList);
		ybsyList.put("syList", syList);
		ybsyList.put("cateList", cateList);

		return ybsyList;
	}
	
	/**
	 * 根据位置的数字信息，转换成正方形位置
	 */
	private void dealDtoOfPos(YbglDto ybglDto){
		if(ybglDto == null || StringUtil.isBlank(ybglDto.getCfs())){
			return;
		}
		int cfs = Integer.parseInt(ybglDto.getCfs());
		double n =Math.sqrt(cfs);
		
		//设置起始代码位置
		int i_start = Integer.parseInt(ybglDto.getQswz());
		//计算当前位置的纵向偏移量
		double z_py = (i_start-1)/n+1;
		//计算当前位置的横向偏移量
		int h_py = (int)((i_start-1)%n+1);
		char c1=(char) (z_py+64);
		
		ybglDto.setQswzdm(String.valueOf(c1)+ h_py);
		
		//设置结束代码位置
		int i_end = Integer.parseInt(ybglDto.getJswz());
		//计算当前位置的纵向偏移量
		z_py = (i_end-1)/n+1;
		//计算当前位置的横向偏移量
		h_py = (int)((i_end-1)%n+1);
		char c2=(char) (z_py+64);
		
		ybglDto.setJswzdm(String.valueOf(c2)+ h_py);
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex,StringBuffer errorMessages) {
		// TODO Auto-generated method stub
		try{
			// TODO Auto-generated method stub
			YbglDto ybglDto = (YbglDto)baseModel;
			ybglDto.setZt(StatusEnum.CHECK_PASS.getCode());
			//新增标本来源信息
			if(!StringUtil.isBlank(ybglDto.getYblylx())){
				ybglDto.setLyid(StringUtil.generateUUID());
				dao.insertYblyByYbglDto(ybglDto);
			}
			//根据盒子号、冰箱号、抽屉号，查询冰箱ID，盒子ID，抽屉ID
			List<SbglDto> sbglList = sbglService.selectSbidsBySbh(ybglDto);
			if(sbglList != null && sbglList.size() == 1){
                for (SbglDto sbglDto : sbglList) {
                    ybglDto.setHzid(sbglDto.getHzid());
                    ybglDto.setChtid(sbglDto.getCtid());
                    ybglDto.setBxid(sbglDto.getBxid());
                }
			}
			//新增标本信息
			String ybid = StringUtil.generateUUID();
			ybglDto.setYbid(ybid);
			
		    JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.SAMPLE_TYPE.getCode());
			jcsjDto.setCsid(ybglDto.getYblx());
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
            String lshqz = ybglDto.getYbbh().substring(0, 8);
			if(StringUtil.isNotBlank(t_jcsjDto.getCskz1())){
				lshqz = lshqz+t_jcsjDto.getCskz1();
			}else{
				lshqz = lshqz+t_jcsjDto.getCsdm();
			}
			lshkservice.updateSerNum(SerialNumTypeEnum.SAMP_CODE.getCode(), lshqz, ybglDto.getYbbh());
			
			dao.insertImpYbglDto(ybglDto);
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return true;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		if(tranTrack.equalsIgnoreCase("YB001")){//标本类型
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.SAMPLE_TYPE.getCode());
			jcsjDto.setCsdm(value);
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的物料类别的编码，单元格值为：").append(value).append("；<br>");
			}
			else{
				recModel.setYblx(t_jcsjDto.getCsid());
				return t_jcsjDto.getCsid();
			}
		}else if(tranTrack.equalsIgnoreCase("YB002")){//标本来源类型
			if("外部".equals(value))
				return "0";
			else
				return null;
		}else if(tranTrack.equalsIgnoreCase("YB003")){//入库类型
			if("正式".equals(value))
				return "00";
			else
				return "01";
		}else if(tranTrack.equalsIgnoreCase("YB004")){//性别
			if("男".equals(value))
				return "1";
			else
				return "2";
		}
		
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}
}
