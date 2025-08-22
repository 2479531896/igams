package com.matridx.igams.web.service.impl;

import com.dingtalk.api.response.OapiCheckinRecordResponse.Data;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.YhqdxxDto;
import com.matridx.igams.web.dao.entities.YhqdxxModel;
import com.matridx.igams.web.dao.post.IYhqdxxDao;
import com.matridx.igams.web.service.svcinterface.IYhqdxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class YhqdxxServiceImpl extends BaseBasicServiceImpl<YhqdxxDto, YhqdxxModel, IYhqdxxDao> implements IYhqdxxService{

	@Autowired
	DingTalkUtil talkUtil;
	/**
	 * 导出文件存放路径
	 */
	@Value("${matridx.file.exportFilePath}")
	private String exportFilePath = null;

	private Logger log = LoggerFactory.getLogger(YhqdxxServiceImpl.class);
	/**
	 * 更新钉钉签到信息
	 * @return
	 */
	@Override
	public boolean updateCheckinRecord(YhqdxxDto yhqdxxDto, User user){
		//查询上一次的签到时间
		YhqdxxDto s_yhqdxxDto = dao.selectLastTime();
		//格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = 0;
		if(s_yhqdxxDto != null && StringUtil.isNotBlank(s_yhqdxxDto.getDqsk())){
			long dqsk = Long.parseLong(s_yhqdxxDto.getDqsk());
			startTime = dqsk+1000;
		}else{
			try {
				Date date = sdf.parse("2019-10-01 00:00:00");
				Calendar cal = Calendar.getInstance();
		    	cal.setTime(date);
		    	startTime = cal.getTimeInMillis();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
		}
		Long endTime = System.currentTimeMillis();
		//获取签到信息
		Map<String, Object> checkinMap = talkUtil.checkinRecord(user.getYhm(), yhqdxxDto.getJgid(), startTime, endTime);
		if(checkinMap != null){
			if(checkinMap.get("errmsg")!= null) {
                return false;
            }
			@SuppressWarnings("unchecked")
			List<Data> checkinList = (List<Data>)checkinMap.get("list");
			List<YhqdxxDto> yhqdxxDtos = new ArrayList<>();
			if(!CollectionUtils.isEmpty(checkinList)){
                for (Data data : checkinList) {
                    YhqdxxDto t_yhqdxxDto = new YhqdxxDto();
                    t_yhqdxxDto.setQdid(StringUtil.generateUUID());
                    t_yhqdxxDto.setXm(data.getName());
                    t_yhqdxxDto.setXxdz(data.getDetailPlace());
                    t_yhqdxxDto.setDz(data.getPlace());
                    t_yhqdxxDto.setDdid(data.getUserId());
                    t_yhqdxxDto.setQdsj(sdf.format(data.getTimestamp()));
                    t_yhqdxxDto.setDqsk(String.valueOf(data.getTimestamp()));
                    t_yhqdxxDto.setJd(data.getLongitude());
                    t_yhqdxxDto.setWd(data.getLatitude());
                    t_yhqdxxDto.setBz(data.getRemark());
                    t_yhqdxxDto.setLrry(yhqdxxDto.getLrry());
                    yhqdxxDtos.add(t_yhqdxxDto);
                }
				//保存记录至数据库
				int result = dao.insertByYhqdxxDtos(yhqdxxDtos);
                return result != 0;
			}
		}
		return true;
	}

	/**
	 * 更新钉钉签到信息并下载
	 * @param yhqdxxDto
	 * @return
	 */
	@Override
	public String saveCheckinRecord(YhqdxxDto yhqdxxDto, User user) {
		// TODO Auto-generated method stub
		String path = null;
		boolean result = updateCheckinRecord(yhqdxxDto,user);
		if(!result) {
            return null;
        }
        List<YhqdxxDto> yhqdxxDtos = dao.selectCheckinInfo(yhqdxxDto);
        if(!CollectionUtils.isEmpty(yhqdxxDtos)){
        	//创建缓存文件
    		String fileName = new Date().getTime() +".xlsx";
        	String currentDay = DateTimeUtil.getFormatDay(new Date());
        	path = exportFilePath + currentDay.substring(0, 4) + "/" + currentDay.substring(0, 6) + "/" + currentDay.substring(0, 8) + "/";
    		File filepath = new File(path);
    		if(!filepath.exists()){
    			filepath.mkdirs();
    		}
    		path += fileName;
        	//下载导出信息
        	HSSFWorkbook wb = new HSSFWorkbook();
        	HSSFSheet sheet = wb.createSheet("table"); //创建table工作薄
        	Object[] titles = {"姓名", "详细地址", "地址", "签到时间", "备注"};
        	HSSFRow row;
        	HSSFCell cell;
        	for(int i = 0; i < yhqdxxDtos.size()+1; i++) {
        		row = sheet.createRow(i);//创建表格行
        		List<String> values = new ArrayList<>();
                if(i > 0){
        			values.add(yhqdxxDtos.get(i-1).getXm());
        			values.add(yhqdxxDtos.get(i-1).getXxdz());
        			values.add(yhqdxxDtos.get(i-1).getDz());
        			values.add(yhqdxxDtos.get(i-1).getQdsj());
        			values.add(yhqdxxDtos.get(i-1).getBz());
        		}
        		for(int j = 0; j < titles.length; j++) {
        			cell = row.createCell(j);//根据表格行创建单元格
        			if(i == 0){
            			cell.setCellValue(String.valueOf(titles[j]));
        			}else{
        				cell.setCellValue(values.get(j));
        			}
        		}
        	}
        	for(int j = 0; j < titles.length; j++) {
        		sheet.setColumnWidth(j, 256*30);//设置列宽
        	}
        	try {
        		FileOutputStream fos = new FileOutputStream(new File(path));
				wb.write(fos);
				wb.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
        }
		return path;
	}
	
}
