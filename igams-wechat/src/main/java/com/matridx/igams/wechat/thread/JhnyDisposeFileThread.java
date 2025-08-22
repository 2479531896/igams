package com.matridx.igams.wechat.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.NyysxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.WzysxxDto;
import com.matridx.igams.wechat.service.svcinterface.INyysxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.IWzysxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JhnyDisposeFileThread extends Thread{

	private Logger log = LoggerFactory.getLogger(JhnyDisposeFileThread.class);
	private String filePath;
	private String fileName;
	private String jclx;
	private ISjxxWsService sjxxWsService;
	private ISjnyxService sjnyxService;
	private ISjwzxxService sjwzxxService;
	private IFjcfbService fjcfbService;
	private IWzysxxService wzysxxService;

	private INyysxxService nyysxxService;

	public JhnyDisposeFileThread(String filePath,String fileName,ISjxxWsService sjxxWsService,String jclx,ISjwzxxService sjwzxxService,ISjnyxService sjnyxService,IFjcfbService fjcfbService,IWzysxxService wzysxxService,INyysxxService nyysxxService) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.sjxxWsService = sjxxWsService;
		this.jclx = jclx;
		this.sjnyxService = sjnyxService;
		this.sjwzxxService = sjwzxxService;
		this.fjcfbService = fjcfbService;
		this.wzysxxService = wzysxxService;
		this.nyysxxService = nyysxxService;
	}

	@Override
	public void run() {
		try {
			String realPath = ZipUtil.unZipFile(filePath,filePath +"/"+ fileName);
			log.error("realPath:"+realPath);
			String path="/results";
			List<Map<String, String>> resultMap = getJsonInfo(realPath, path, "/result.json");
			List<Map<String, String>> pickMap = getJsonInfo(realPath, path, "/pick.json");
			List<Map<String, String>> resultNewMap = getSjxxInfo(resultMap);
			List<Map<String, String>> pickNewMap = getSjxxInfo(pickMap);
			List<SjwzxxDto> sjwzxxDtos = new ArrayList<>();
			List<WzysxxDto> wzysxxDtos = new ArrayList<>();
			List<FjcfbDto>  fjcfbDtoList= new ArrayList<>();
			List<String> ids =new ArrayList<>();
			long timestampInSeconds = System.currentTimeMillis() / 1000;
			getSjwzxxList(resultMap, resultNewMap, sjwzxxDtos,wzysxxDtos, fjcfbDtoList, realPath+path,ids,String.valueOf(timestampInSeconds));
			List<SjnyxDto> sjnyxDtos = new ArrayList<>();
			List<NyysxxDto> nyysxxDtos = new ArrayList<>();
			getSjnyxxList(pickMap, pickNewMap, sjnyxDtos,nyysxxDtos,String.valueOf(timestampInSeconds));
			if (!CollectionUtils.isEmpty(sjwzxxDtos)){
				SjwzxxDto sjwzxxDto=new SjwzxxDto();
				sjwzxxDto.setIds(ids);
				sjwzxxDto.setJclx(jclx);
				sjwzxxService.deleteBySjidsAndJclx(sjwzxxDto);
				sjwzxxService.insertBysjwzxxDtos(sjwzxxDtos);
			}
			if(!CollectionUtils.isEmpty(wzysxxDtos)){
				wzysxxService.insertDtoList(wzysxxDtos);
			}
			if (!CollectionUtils.isEmpty(sjnyxDtos)){
				SjnyxDto sjnyxDto=new SjnyxDto();
				sjnyxDto.setJclx(jclx);
				sjnyxDto.setIds(ids);
				sjnyxService.deleteBySjidsAndJclx(sjnyxDto);
				sjnyxService.insertBysjnyxDtos(sjnyxDtos);
			}
			if(!CollectionUtils.isEmpty(nyysxxDtos)){
				nyysxxService.insertDtoList(nyysxxDtos);
			}
			if (!CollectionUtils.isEmpty(fjcfbDtoList)){
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setYwids(ids);
				fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
				fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);//批量删除原有附件
				fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_INFO_TEMEPLATE.getCode());
				fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);//批量删除原有附件
				fjcfbService.batchInsertFjcfb(fjcfbDtoList);
			}
		} catch (Exception e) {
			log.error("结核耐药线程出错:"+e.toString());
		}
	}

	private void getSjnyxxList(List<Map<String, String>> pickMap, List<Map<String, String>> pickNewMap, List<SjnyxDto> sjnyxDtos,List<NyysxxDto> nyysxxDtos,String llh ) {
		for (Map<String, String> map : pickNewMap) {
			SjnyxDto sjnyxDto = new SjnyxDto();
			NyysxxDto nyysxxDto =new NyysxxDto();
			sjnyxDto.setSjid(map.get("sjid"));
			sjnyxDto.setXh(String.valueOf(map.get("xh")));
			sjnyxDto.setJclx(jclx);
			nyysxxDto.setSjid(map.get("sjid"));
			nyysxxDto.setXh(String.valueOf(map.get("xh")));
			nyysxxDto.setJclx(jclx);
			nyysxxDto.setLlh(llh);
			String id = map.get("id");
			for (int i = pickMap.size()-1; i >= 0 ; i--) {
				if (id.equals(pickMap.get(i).get("ID"))){
					sjnyxDto.setSjnyxid(StringUtil.generateUUID());
					sjnyxDto.setJson(JSONObject.toJSONString(pickMap.get(i)));
					nyysxxDto.setSjnyxid(sjnyxDto.getSjnyxid());
					nyysxxDto.setJson(JSONObject.toJSONString(pickMap.get(i)));
//					sjnyxDto.setJy(String.valueOf(stringMap.get("02.突变基因")));
//					sjnyxDto.setJl(String.valueOf(stringMap.get("03.突变结果")));
//					sjnyxDto.setNyjyid(String.valueOf(stringMap.get("04.耐药性")));
//					sjnyxDto.setDs(String.valueOf(stringMap.get("05.突变深度")));
//					sjnyxDto.setXls(String.valueOf(stringMap.get("06.突变频率")));
					pickMap.remove(i);
					break;
				}
			}
			sjnyxDtos.add(sjnyxDto);
			nyysxxDtos.add(nyysxxDto);
		}
	}

	private void getSjwzxxList(List<Map<String, String>> resultMap, List<Map<String, String>> resultNewMap, List<SjwzxxDto> sjwzxxDtos,List<WzysxxDto> wzysxxDtos,  List<FjcfbDto> fjcfbDtos, String filePath,List<String> ids,String llh) {
		for (Map<String, String> map : resultNewMap) {
			SjwzxxDto sjwzxxDto = new SjwzxxDto();
			WzysxxDto wzysxxDto = new WzysxxDto();
			sjwzxxDto.setSjwzid(StringUtil.generateUUID());
			sjwzxxDto.setSjid(map.get("sjid"));
			sjwzxxDto.setWzid(map.get("taxid"));
			sjwzxxDto.setWzywm(map.get("wzywm"));
			sjwzxxDto.setWzzwm(map.get("wzzwm"));
			sjwzxxDto.setWzfl(map.get("wzfl"));
			sjwzxxDto.setJglx("possible");
			sjwzxxDto.setXpxx(map.get("id"));
			sjwzxxDto.setXh(String.valueOf(map.get("xh")));
			sjwzxxDto.setJclx(jclx);
			wzysxxDto.setSjwzid(sjwzxxDto.getSjwzid());
			wzysxxDto.setSjid(map.get("sjid"));
			wzysxxDto.setWzid(map.get("taxid"));
			wzysxxDto.setWzywm(map.get("wzywm"));
			wzysxxDto.setWzzwm(map.get("wzzwm"));
			wzysxxDto.setWzfl(map.get("wzfl"));
			wzysxxDto.setJglx("possible");
			wzysxxDto.setXpxx(map.get("id"));
			wzysxxDto.setXh(String.valueOf(map.get("xh")));
			wzysxxDto.setJclx(jclx);
			wzysxxDto.setLlh(llh);
			String id = map.get("id");
			for (int i = resultMap.size()-1; i >= 0 ; i--) {
				if (id.equals(resultMap.get(i).get("ID"))){
					resultMap.get(i).put("refANI",map.get("refani")!=null?map.get("refani"):"");
					resultMap.get(i).put("reads",map.get("reads")!=null?map.get("reads"):"");
					sjwzxxDto.setJson(JSONObject.toJSONString(resultMap.get(i)));
					wzysxxDto.setJson(JSONObject.toJSONString(resultMap.get(i)));
//					sjwzxxDto.setSdds(String.valueOf(stringMap.get("CleanReads")));
//					sjwzxxDto.setDqs(String.valueOf(stringMap.get("MappingReads")));
//					sjwzxxDto.setJyzfgd(String.valueOf(stringMap.get("coverage")));
//					sjwzxxDto.setSfd(String.valueOf(stringMap.get("depth")));
//					sjwzxxDto.setXdfd(String.valueOf(stringMap.get("refANI")));
//					sjwzxxDto.setZcd(String.valueOf(stringMap.get("覆盖90%以上的靶标")));
//					sjwzxxDto.setHslx(String.valueOf(stringMap.get("spoligotyping43")));
					break;
				}
			}
			if(!map.get("nbbm").startsWith("NC") && !map.get("nbbm").startsWith("PC")) {//过滤掉NC和PC开头
				sjwzxxDtos.add(sjwzxxDto);
			}
			wzysxxDtos.add(wzysxxDto);
			if(!ids.toString().contains(map.get("sjid"))){
				ids.add(map.get("sjid"));
				DBEncrypt bpe = new DBEncrypt();
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(map.get("sjid"));
				File file = new File(filePath + "/"+map.get("id")+".coverage_depth.png");
				if (!file.exists()) {
					continue;
				}
				fjcfbDto.setWjlj(bpe.eCode(filePath + "/"+map.get("id")+".coverage_depth.png"));
				fjcfbDto.setXh(String.valueOf(map.get("xh")));
				fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
				fjcfbDto.setWjm(map.get("id")+".coverage_depth.png");
				fjcfbDto.setZhbj("0");
				fjcfbDto.setFjid(StringUtil.generateUUID());
				fjcfbDtos.add(fjcfbDto);
				//另外一个图片
				FjcfbDto t_fjcfbDto = new FjcfbDto();
				t_fjcfbDto.setYwid(map.get("sjid"));
				File t_file = new File(filePath + "/"+map.get("id")+".png");
				if (!t_file.exists()) {
					continue;
				}
				t_fjcfbDto.setXh(String.valueOf(map.get("xh")));
				t_fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_INFO_TEMEPLATE.getCode());
				t_fjcfbDto.setWjlj(bpe.eCode(filePath + "/"+map.get("id")+".png"));
				t_fjcfbDto.setWjm(map.get("id")+".png");
				t_fjcfbDto.setZhbj("0");
				t_fjcfbDto.setFjid(StringUtil.generateUUID());
				fjcfbDtos.add(t_fjcfbDto);
			}else{
				ids.add(map.get("sjid"));
			}
		}
	}

	private List<Map<String, String>> getSjxxInfo(List<Map<String, String>> resultMap) {
		List<Map<String, String>> mapInfo = new ArrayList<>();
		List<Map<String, String>> wzmap = new ArrayList<>();
		if (!CollectionUtils.isEmpty(resultMap)){
			for (Map<String, String> map : resultMap) {
				try {
					String id = map.get("ID");
					if (StringUtil.isBlank(id)){
						continue;
					}
					String[] ids = id.split("-");
					if(ids.length<=1) {
						continue;
					}
					if (ids[1].length()>6){
						map.put("nbbm",ids[1]);
					}else{
						if (!ids[1].startsWith("NC") && !ids[1].startsWith("PC")){
							map.put("nbbm",ids[1]+"-"+ids[2]);
						}else{
							//NC PC 处理
							String nbbm="";
							//如果为NC或PC，截掉第一位，倒数第一第二位
							if(ids.length>3){
								for(int i=0;i<ids.length;i++){
									if(i!=0){
										if("TBtNGS".equals(ids[i])){
											break;
										}
										//后期NC的后缀不再存在 2025-07-30
										if("TA".equals(ids[i])){
											nbbm=nbbm+"-"+ids[i];
											break;
										}
										nbbm=nbbm+"-"+ids[i];
									}
								}
							}
							map.put("nbbm",StringUtil.isNotBlank(nbbm)?nbbm.substring(1):"");
						}
					}
					List<Map<String,String>> tblist=new ArrayList<>();
					boolean resultwz=false;//用于判断读取result文件时，判断TB和NTM是否都为空的list，如果为false，放一个taxid为null的map进去
					if(map.get("TB")!=null){
						JSONArray jsonArray=JSONArray.parseArray( JSON.toJSONString(map.get("TB")));
						tblist=(List<Map<String,String>>)JSONArray.parse(jsonArray.toJSONString(), Feature.OrderedField);
						if(!CollectionUtils.isEmpty(tblist)){
							resultwz=true;
							for(int i=0;i<tblist.size();i++){
	                            Map<String, String> t_map = new HashMap<>(map);
								t_map.put("taxid",tblist.get(i).get("taxid"));
								t_map.put("refANI",tblist.get(i).get("ani"));//为了适应原数据回显，往map里再次组装refANI
								t_map.put("reads",tblist.get(i).get("reads"));//为了适应原数据回显，往map里再次组装reads
								wzmap.add(t_map);
							}
						}
					}
					if(map.get("NTM")!=null){
						JSONArray jsonArray=JSONArray.parseArray( JSON.toJSONString(map.get("NTM")));
						tblist=(List<Map<String,String>>)JSONArray.parse(jsonArray.toJSONString(), Feature.OrderedField);
						if(!CollectionUtils.isEmpty(tblist)){
							resultwz=true;
							for(int i=0;i<tblist.size();i++){
	                            Map<String, String> t_map = new HashMap<>(map);
								t_map.put("taxid",tblist.get(i).get("taxid"));
								t_map.put("refANI",tblist.get(i).get("ani"));//为了适应原数据回显，往map里再次组装refANI
								t_map.put("reads",tblist.get(i).get("reads"));//为了适应原数据回显，往map里再次组装reads
								wzmap.add(t_map);
							}
						}
					}
					if(map.get("TB")==null && map.get("NTM")==null) {//处理读取pick文件时，这里用TB和NTM都为null来判断。
						map.put("taxid",null);
						map.put("refANI",null);//为了适应原数据回显，往map里再次组装refANI
						map.put("reads",null);
						wzmap.add(map);
						continue;
					}
					if(!resultwz){
						map.put("taxid",null);
						map.put("refANI",null);//为了适应原数据回显，往map里再次组装refANI
						map.put("reads",null);
						wzmap.add(map);
					}
				}catch(Exception e) {
					log.error("getSjxxInfo 循环Map出错 ID:" + map.get("ID") + " 信息：" + e.getMessage());
				}
			}
			mapInfo = sjxxWsService.getSjxxInfo(wzmap);
		}
		return mapInfo;
	}

	private List<Map<String, String>> getJsonInfo(String realPath, String path,String name) throws IOException {
		Path resultPath = Paths.get(realPath+path+name);
		byte[] data = Files.readAllBytes(resultPath);
		String result = new String(data, "utf-8");
		List<Map<String,String>> maplist=(List<Map<String, String>>) JSONArray.parse( result, Feature.OrderedField);
		List<Map<String,String>> newMapList=new ArrayList<>();
		for(Map<String,String> map:maplist){
			if(map.get("ID")!=null&&(!map.get("ID").endsWith("YZ"))){
				newMapList.add(map);
			}
		}
		return newMapList;
	}
}
