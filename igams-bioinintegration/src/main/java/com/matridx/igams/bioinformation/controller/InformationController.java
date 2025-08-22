package com.matridx.igams.bioinformation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.bioinformation.service.svcinterface.*;
import com.matridx.igams.bioinformation.util.DockerUtil;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDcszService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.util.ExcelUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/information")
public class InformationController extends BaseController {
	@Autowired
	private IOtherService otherService;
	@Autowired
	private IWkcxService wkcxService;
	@Autowired
	private IWkcxbbService wkcxbbService;
	@Autowired
	private IMngsmxjgService mngsmxjgService;
	@Autowired
	private IMngszjg2Service mngszjg2Service;
	@Autowired
	private INyfxjgService nyfxjgService;
	@Autowired
	private IDlfxjgService dlfxjgService;
	@Autowired
	private IXgwxService xgwxService ;
	@Autowired
	private IZsxxService zsxxService ;
	@Autowired
	private IBioLczzznglService lczzznglService ;
	@Autowired
	private IBioWzglService wzglService ;
	@Autowired
	private ICnvjgService cnvjgService;
	@Autowired
	private DockerUtil dockerUtil ;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IWklcznService wklcznService;
	@Autowired
	IWkysmxService wkysmxService;
	@Autowired
	private IWknyfxService wknyfxService;
	@Autowired
	private IWkdlfxService wkdlfxService;
	@Autowired
	private IBioDlyzzsService bioDlyzzsService;
	@Autowired
	private IDcszService dcszService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IGrszService grszService;
	@Autowired
	private IMngsFileParsingService mngsFileParsingService;

	private final Logger log = LoggerFactory.getLogger(InformationController.class);

	Lock lock=new ReentrantLock();
	/**
	 * CNV图片展示
	 */
	@RequestMapping(value="/file/pagedataCnvPictureShow")
	@ResponseBody
	public String pagedataCnvPictureShow(CnvjgDto cnvjgDto,HttpServletResponse response){
		String date = cnvjgDto.getXpm().substring(0,6);
		String path = "/20"+date.substring(0,2)+"/"+date.substring(2,4)+"/"+date.substring(4,6)+"/"+cnvjgDto.getXpm();
		String wjm=cnvjgDto.getWkbh()+"."+cnvjgDto.getRst()+".png";
		MinioClient minioClient;
		try {
			minioClient = new MinioClient("https://minio.matridx.com", "ituser", "Matridx@2022");
			InputStream inputStream = minioClient.getObject("oncoplot", path + "/graphData/" + wjm);
			response.setContentType("image/png");
			byte[] buffer = new byte[4096];
			BufferedInputStream bis = null;
			OutputStream os = null; //输出流
			try {
				os = response.getOutputStream();
				bis = new BufferedInputStream(inputStream);
				int n;
				while ((n = bis.read(buffer, 0, 4096)) > -1) {
					os.write(buffer, 0, n);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图片/视频展示
	 */
	@RequestMapping(value="/file/pagedataFileShow")
	@ResponseBody
	public String pictureShow(FjcfbDto fjcfbDto, HttpServletResponse response){
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		String wjlj = t_fjcfbDto.getWjlj();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		String fileName=crypt.dCode(t_fjcfbDto.getFwjm());
		//File downloadFile = new File(filePath);
		String[] array = fileName.split("[.]");
		String fileType = array[array.length-1].toLowerCase();
		//设置文件ContentType类型
		if("jpg,jepg,gif,png".contains(fileType)){//图片类型
			response.setContentType("image/"+fileType);
			byte[] buffer = new byte[1024];
			BufferedInputStream bis = null;
			InputStream iStream;
			OutputStream os = null; //输出流
			try {
				iStream = new FileInputStream(filePath);
				os = response.getOutputStream();
				bis = new BufferedInputStream(iStream);
				int i = bis.read(buffer);
				while(i != -1){
					os.write(buffer);
					os.flush();
					i = bis.read(buffer);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * review按钮页面信息
	 */
	@RequestMapping("/review/pagedataCs")
	@ResponseBody
	public Map<String, Object> cs() {
		Map<String, Object> map = new HashMap<>();
		try {
			dockerUtil.createContainers("/code/density_plot_Hindex.R","/code/host_index.txt","/matridx",StringUtil.generateUUID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 文库详情
	 */
	@RequestMapping("/review/pagedataLibraryInfo")
	@ResponseBody
	public Map<String, Object> libraryInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(wkcxDto.getXpid()) && StringUtil.isNotBlank(wkcxDto.getTaxid())){
			List<WkcxDto> libraryInfo = wkcxService.libraryInfo(wkcxDto);
			map.put("status", "success");
			map.put("libraryInfo", libraryInfo);
		}else{
			map.put("status", "false");
			map.put("message", "获取数据失败!");
		}
		return map;
	}

	/**
	 * 树详情
	 */
	@RequestMapping("/review/pagedataTreeInfo")
	@ResponseBody
	public Map<String, Object> treeInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(wkcxDto.getWkcxid()) && StringUtil.isNotBlank(wkcxDto.getJgid())){
			WkcxDto dto = wkcxService.getDtoNew(wkcxDto);
			Mngszjg2Dto mngszjg2Dto = new Mngszjg2Dto();
			mngszjg2Dto.setWkcxid(wkcxDto.getWkcxid());
			mngszjg2Dto.setJgid(wkcxDto.getJgid());
			Mngszjg2Dto jsonDto = mngszjg2Service.getDto(mngszjg2Dto);
			MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
			mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
			mngsmxjgDto.setWkbh(dto.getWkbh());
			mngsmxjgDto.setBbh(dto.getBbh());
			mngsmxjgDto.setWkcxid(wkcxDto.getWkcxid());
			List<MngsmxjgDto>mngsmxjgDtoList=mngsmxjgService.getSampleTreeList(mngsmxjgDto);
//			MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
//			mngsmxjgDto.setJgid(dto.getJgid());
//			mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
//			List<MngsmxjgDto> listInfo = mngsmxjgService.getDtoList(mngsmxjgDto);
//			map.put("listInfo", listInfo);


//			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioWzglDto.class, "wzid");
//			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
//			listFilters[0] = filter;
//			BioWzglDto wzglDto  = new BioWzglDto();
//			wzglDto.setFl("1");
//			List<BioWzglDto> dtoList_h = wzglService.getDtoList(wzglDto);
//			wzglDto.setFl("0");
//			List<BioWzglDto> dtoList_fh = wzglService.getDtoList(wzglDto);
//			map.put("dtoList_fh", JSONObject.toJSONString(dtoList_fh,listFilters));
//			map.put("dtoList_h", JSONObject.toJSONString(dtoList_h,listFilters));
			map.put("jsonDto", jsonDto);
			map.put("flList",mngsmxjgDtoList);
			map.put("wkcxDto",dto);
			map.put("status", "success");

		}else{
			map.put("status", "false");
			map.put("message", "获取数据失败!");
		}
		return map;
	}

	/**
	 * Filter
	 */
	@RequestMapping("/review/pagedataReviewFilterInfo")
	@ResponseBody
	public Map<String, Object> reviewFilterInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(wkcxDto.getWkcxid())){
			WkcxDto dto = wkcxService.getDtoNew(wkcxDto);
			if (null == dto ){
				map.put("status", "false");
				map.put("message", "获取数据失败!");
				return map;
			}
			if (StringUtil.isNotBlank(dto.getJgid())){
				MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
				mngsmxjgDto.setJgid(dto.getJgid());
				mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
				wkcxDto.setLibccon(dto.getLibccon());
				wkcxDto.setWkbh(dto.getWkbh());
				wkcxDto.setXpid(dto.getXpid());
				List<MngsmxjgDto> listInfo;
				try {
					listInfo = mngsmxjgService.getReviewFilterInfo(mngsmxjgDto,wkcxDto);
				} catch (BusinessException e) {
					map.put("status", "false");
					map.put("message", e.getMsg());
					return map;
				}
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(MngsmxjgDto.class, "dl", "children", "xl", "num", "taxid", "wkbh", "readscount","readsaccum","mxjgid", "gzd", "aijg", "qz","qzpw", "cov", "wzlx"
						, "fldj", "xm", "aijg", "zwmm", "rpq", "abd", "grc", "wzfl", "ftaxid", "jgid", "fz", "xjlb", "bdlb", "fl", "sfgz", "scid", "zbx","index","dep","sfdc","gid",
						"fwzlx","fxm","fzwmm","fabd","freadsaccum","staxid","fmxjgid","fqz","fqzpw","fgzd","fwzfl","fbdlb");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("listInfo", JSON.toJSONString(listInfo,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
		}else{
			map.put("status", "false");
			map.put("message", "未获取到查询id!");
		}
		return map;
	}


	/**
	 * 提交或者发送报告后判断用接口
	 */
	@RequestMapping("/review/pagedataGetListByNbbm")
	@ResponseBody
	public Map<String, Object> GetListByNbbm(WkcxDto wkcxDto){
		Map<String, Object> map = new HashMap<>();
		List<WkcxDto> list=wkcxService.getByNbbmToList(wkcxDto);
		map.put("status", "success");
		map.put("list", list);
		return map;
	}

	@RequestMapping("/review/pagedataFlushSampleInfo")
	@ResponseBody
	public Map<String, Object> reviewInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		WkcxDto dto = wkcxService.getDtoNew(wkcxDto);
		OtherDto sjxxDto = new OtherDto();
		if (StringUtil.isNotBlank(dto.getSjid())){
			OtherDto otherDto = new OtherDto();
			otherDto.setSjid(dto.getSjid());
			sjxxDto = otherService.getSjxxDto(otherDto);
			if(null != sjxxDto){
				if(StringUtil.isNotBlank(sjxxDto.getBz()))
					sjxxDto.setBz(sjxxDto.getBz()+";");
				sjxxDto.setBz(sjxxDto.getBz()+(dto.getComment()==null?"":dto.getComment()));
				RestTemplate restTemplate=new RestTemplate();
				String reString = restTemplate.getForObject("https://medlab.matridx.com/ws/getsjxxpage?ybbh="+sjxxDto.getYbbh()+"&pageSize=15&pageNumber=1&pageStart=0&sortName=sjid&sortOrder=desc&sortLastName=sjid&sortLastOrder=asc&sjdw=&_=1661327479554", String.class);
				JSONObject jsonObject = JSONObject.parseObject(reString);
				if (null != jsonObject.get("total")){
					sjxxDto.setTotal(jsonObject.get("total").toString());
				}
			}
		}
		map.put("status", "success");
		map.put("sample_info", sjxxDto);
		return map;
	}

	/**
	 * review按钮页面信息
	 */
	@RequestMapping("/review/pagedataReviewInfo")
	@ResponseBody
	public Map<String, Object> reviewInfo(WkcxDto wkcxDto,String zyid) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(wkcxDto.getWkcxid())){
			WkcxDto dto = wkcxService.getDtoNew(wkcxDto);

			if (null == dto ){
				map.put("status", "false");
				map.put("message", "获取数据失败!");
				return map;
			}
			OtherDto sjxxDto = new OtherDto();
			String yblxdm="";
			if (StringUtil.isNotBlank(dto.getSjid())){
				OtherDto otherDto = new OtherDto();
				otherDto.setSjid(dto.getSjid());
				sjxxDto = otherService.getSjxxDto(otherDto);
				yblxdm=sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length()-1);
				if(StringUtil.isNotBlank(sjxxDto.getBz()))
					sjxxDto.setBz(sjxxDto.getBz()+";");
				sjxxDto.setBz(sjxxDto.getBz()+(dto.getComment()==null?"":dto.getComment()));
				RestTemplate restTemplate=new RestTemplate();
				String reString = restTemplate.getForObject("https://medlab.matridx.com/ws/getsjxxpage?ybbh="+sjxxDto.getYbbh()+"&pageSize=15&pageNumber=1&pageStart=0&sortName=sjid&sortOrder=desc&sortLastName=sjid&sortLastOrder=asc&sjdw=&_=1661327479554", String.class);
				JSONObject jsonObject = JSONObject.parseObject(reString);
				if (null != jsonObject.get("total")){
					sjxxDto.setTotal(jsonObject.get("total").toString());
				}
			}
 			User user=getLoginInfo();
            GrszDto grszDto=new GrszDto();
            grszDto.setYhid(user.getYhid());
            grszDto.setSzlbs(new String[]{PersonalSettingEnum.BIO_AUDIT.getCode(),PersonalSettingEnum.BIO_INSPECT.getCode()});
            Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
            GrszDto grszDto_sh = grszDtoMap.get(PersonalSettingEnum.BIO_AUDIT.getCode());
            if(grszDto_sh!=null){
                if(StringUtil.isNotBlank(grszDto_sh.getSzz())){
                    dto.setShryid(grszDto_sh.getSzz());
                }
            }
            GrszDto grszDto_jy = grszDtoMap.get(PersonalSettingEnum.BIO_INSPECT.getCode());
            if(grszDto_jy!=null){
                if(StringUtil.isNotBlank(grszDto_jy.getSzz())){
                    dto.setJyryid(grszDto_jy.getSzz());
                }
            }

			if (StringUtil.isNotBlank(dto.getJgid())){
				MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
				mngsmxjgDto.setJgid(dto.getJgid());
				mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
				wkcxDto.setLibccon(dto.getLibccon());
				wkcxDto.setWkbh(dto.getWkbh());
				wkcxDto.setXpid(dto.getXpid());
				if("01".equals(wkcxDto.getZt())){
					mngsmxjgDto.setNoai("4");
				}
				mngsmxjgDto.setYblx(StringUtil.isBlank(yblxdm)?"Other":yblxdm);
				List<MngsmxjgDto> reviewInfo = mngsmxjgService.getInfo(mngsmxjgDto);

				List<MngsmxjgDto> listInfo = mngsmxjgService.getReviewInfo(reviewInfo);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(MngsmxjgDto.class, "dl", "children", "xl", "num", "taxid", "wkbh", "readscount","readsaccum","mxjgid", "gzd", "aijg", "qz","qzpw", "cov", "wzlx"
						, "fldj", "xm", "aijg", "zwmm", "rpq", "abd", "grc", "wzfl", "ftaxid", "jgid", "fz", "xjlb", "bdlb", "fl", "sfgz", "scid", "zbx","index","dep","sfdc","gid","wzdl",
						"fwzlx","fxm","fzwmm","fabd","freadsaccum","staxid","fmxjgid","fqz","fqzpw","fgzd","fwzfl","fbdlb","wzzs","wzzs","jtxlxx","sfys","bg","fx");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;

				map.put("listInfo", JSON.toJSONString(listInfo,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				List<MngsmxjgDto> highConcernList = new ArrayList<>();
				List<String> highConcern = new ArrayList<>();
				List<MngsmxjgDto> suspectedList = new ArrayList<>();
				List<String> suspected = new ArrayList<>();
				WkysmxDto wkysmxDto =new WkysmxDto();
				wkysmxDto.setWkcxid(wkcxDto.getWkcxid());
				List<WkysmxDto> wkysmxDtoList=wkysmxService.getList(wkysmxDto);

				for(MngsmxjgDto dto_t:reviewInfo){
					if("1".equals(dto_t.getGzd())||"2".equals(dto_t.getGzd())){
						for(MngsmxjgDto dto_x:reviewInfo){
							if(dto_t.getFtaxid().equals(dto_x.getTaxid())){
								dto_t.setFmxjgid(dto_x.getMxjgid());
								dto_t.setFwzlx(dto_x.getWzlx());
								dto_t.setFxm(dto_x.getXm());
								dto_t.setFzwmm(dto_x.getZwmm());
								dto_t.setFabd(dto_x.getAbd());
								dto_t.setFreadsaccum(dto_x.getReadsaccum());
								dto_t.setFqz(dto_x.getQz());
								dto_t.setFqzpw(dto_x.getQzpw());
								dto_t.setFgzd(dto_x.getGzd());
								dto_t.setFwzfl(dto_x.getWzfl());
								dto_t.setFbdlb(dto_x.getBdlb());
								break;
							}
						}
						if(!highConcern.contains(dto_t.getTaxid())){
							highConcern.add(dto_t.getTaxid());
							highConcernList.add(dto_t);
						}
					}else if("4".equals(dto_t.getSfys())){

						for(MngsmxjgDto dto_x:reviewInfo){
							if(dto_t.getFtaxid().equals(dto_x.getTaxid())){
								if("Bacteria".equals(dto_x.getWzdl())){
									dto_t.setYsindex("1");
								}else if("Fungi".equals(dto_x.getWzdl())){
									dto_t.setYsindex("2");
								}else if("Viruses".equals(dto_x.getWzdl())){
									dto_t.setYsindex("3");
								}else if("Parasite".equals(dto_x.getWzdl())){
									dto_t.setYsindex("4");
								}
								dto_t.setFmxjgid(dto_x.getMxjgid());
								dto_t.setFwzlx(dto_x.getWzlx());
								dto_t.setFxm(dto_x.getXm());
								dto_t.setFzwmm(dto_x.getZwmm());
								dto_t.setFabd(dto_x.getAbd());
								dto_t.setFreadsaccum(dto_x.getReadsaccum());
								dto_t.setFqz(dto_x.getQz());
								dto_t.setFqzpw(dto_x.getQzpw());
								dto_t.setFgzd(dto_x.getGzd());
								dto_t.setFwzfl(dto_x.getWzfl());
								dto_t.setFbdlb(dto_x.getBdlb());
								break;
							}
						}
						if("01".equals(wkcxDto.getZt())&&!("1".equals(dto.getSfcxsh()))){
							if(!suspected.contains(dto_t.getTaxid())){
								suspected.add(dto_t.getTaxid());
								suspectedList.add(dto_t);
							}
						}else{
							if(!suspected.contains(dto_t.getTaxid())&&wkysmxDtoList.stream().filter(item->dto_t.getMxjgid().equals(item.getMxjgid())).findFirst().orElse(null)!=null){
								suspected.add(dto_t.getTaxid());
								suspectedList.add(dto_t);
							}
						}

					}
				}
				map.put("highConcernList", highConcernList);
				if(suspectedList.size()>0){
						suspectedList=suspectedList.stream().sorted((e1,e2)->Integer.parseInt(e2.getYsindex())- Integer.parseInt(e1.getYsindex()))
															.sorted((e1,e2)->Integer.parseInt(e2.getFreadsaccum())- Integer.parseInt(e1.getFreadsaccum()))
															.sorted((e1,e2)->Integer.parseInt(e2.getReadsaccum())- Integer.parseInt(e1.getReadsaccum()))
														.collect(Collectors.toList());
				}

				map.put("suspectedList", suspectedList);

				MngsmxjgDto t_mngsmxjgDto=new MngsmxjgDto();
				t_mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
				t_mngsmxjgDto.setXm("spike.");
				t_mngsmxjgDto.setJgid(dto.getJgid());
				List<MngsmxjgDto> dtoList = mngsmxjgService.getDtoList(t_mngsmxjgDto);
				if(dtoList!=null&&dtoList.size()>0){
					for(MngsmxjgDto dto_t:dtoList){
						if(StringUtil.isNotBlank(dto_t.getJtxlxx())){
							JSONObject jsonObject=JSONObject.parseObject(dto_t.getJtxlxx());
							dto_t.setBarcode(jsonObject.getString("barcode_all_match"));
						}
					}
				}
				map.put("spikeinRPM",dtoList);
			}
			MngsmxjgDto t_mngsmxjgDto=new MngsmxjgDto();
			t_mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
			t_mngsmxjgDto.setJgid(dto.getJgid());
			t_mngsmxjgDto.setWkcxid(wkcxDto.getWkcxid());
			List<MngsmxjgDto> mngsmxjgDtos = mngsmxjgService.getHighConcernList(t_mngsmxjgDto);
			NyfxjgDto nyfxjgDto=new NyfxjgDto();
			nyfxjgDto.setBbh(dto.getBbh());
			nyfxjgDto.setWkcxid(wkcxDto.getWkcxid());
			List<NyfxjgDto> nyfxjgDtos = nyfxjgService.getDtos(nyfxjgDto);
			WknyfxDto wknyfxDto=new WknyfxDto();
			wknyfxDto.setWkcxid(wkcxDto.getWkcxid());
			List<WknyfxDto> nydtoList = wknyfxService.getDtoList(wknyfxDto);
			List<String> mcs=new ArrayList<>();
			for(NyfxjgDto nyfxjgDto_t:nyfxjgDtos){
				if(StringUtil.isNotBlank(nyfxjgDto_t.getGenefamily())){
					mcs.add(nyfxjgDto_t.getGenefamily());
				}
			}
			if(mcs.size()>0){
				ZsxxDto zsxxDto = new ZsxxDto();
				zsxxDto.setMcs(wkcxDto.getZsxxIds());
				List<ZsxxDto> dtoList = zsxxService.getDtoList(zsxxDto);
				if(dtoList!=null&&dtoList.size()>0){
					for(NyfxjgDto nyfxjgDto_t:nyfxjgDtos){
						for(ZsxxDto zsxxDto_t:dtoList){
							if(nyfxjgDto_t.getGenefamily().equals(zsxxDto_t.getMc())){
								nyfxjgDto_t.setTaxid(zsxxDto_t.getTaxids());
								nyfxjgDto_t.setGlsrst(zsxxDto_t.getGlsrst());
							}
						}
					}
				}
			}
			for(NyfxjgDto nyfxjgDto_t:nyfxjgDtos){
				boolean flag=false;
				if("01".equals(wkcxDto.getZt())){
					if("parC".equals(nyfxjgDto_t.getGenefamily())||"gyrA".equals(nyfxjgDto_t.getGenefamily())){
						if(StringUtil.isNotBlank(nyfxjgDto_t.getCkwztaxid())){
							String[] split = nyfxjgDto_t.getCkwztaxid().split(";");
							for(String s:split){
								if(s.equals(nyfxjgDto_t.getTaxid())){
									if(mngsmxjgDtos!=null&&mngsmxjgDtos.size()>0){
										for(MngsmxjgDto mngsmxjgDto:mngsmxjgDtos){
											if(!"Mycoplasma".equals(mngsmxjgDto.getWzfl())&&!"Mycobacteria".equals(mngsmxjgDto.getWzfl())&&!"Chlamydia".equals(mngsmxjgDto.getWzfl())){
												if(StringUtil.isNotBlank(mngsmxjgDto.getFtaxid())){
													if(mngsmxjgDto.getFtaxid().equals(nyfxjgDto_t.getTaxid())){
														flag=true;
														break;
													}
												}

												if(StringUtil.isNotBlank(mngsmxjgDto.getWzlx())){
													if(mngsmxjgDto.getWzlx().equals(nyfxjgDto_t.getGlsrst())){
														flag=true;
														break;
													}
												}

												if("G+".equals(mngsmxjgDto.getWzlx())){
													if("G+".equals(nyfxjgDto_t.getGlsrst())||"both".equals(nyfxjgDto_t.getGlsrst())){
														flag=true;
														break;
													}

												}else if("G-".equals(mngsmxjgDto.getWzlx())){
													if("G-".equals(nyfxjgDto_t.getGlsrst())||"both".equals(nyfxjgDto_t.getGlsrst())){
														flag=true;
														break;
													}
												}if("both".equals(mngsmxjgDto.getWzlx())){
													if("both".equals(nyfxjgDto_t.getGlsrst())||"G+".equals(nyfxjgDto_t.getGlsrst())||"G-".equals(nyfxjgDto_t.getGlsrst())){
														flag=true;
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}else{
						if(mngsmxjgDtos!=null&&mngsmxjgDtos.size()>0){
							for(MngsmxjgDto mngsmxjgDto:mngsmxjgDtos){
								if(!"Mycoplasma".equals(mngsmxjgDto.getWzfl())&&!"Mycobacteria".equals(mngsmxjgDto.getWzfl())&&!"Chlamydia".equals(mngsmxjgDto.getWzfl())){
									if(StringUtil.isNotBlank(mngsmxjgDto.getFtaxid())){
										if(mngsmxjgDto.getFtaxid().equals(nyfxjgDto_t.getTaxid())){
											flag=true;
											break;
										}
									}

									if(StringUtil.isNotBlank(mngsmxjgDto.getWzlx())){
										if(mngsmxjgDto.getWzlx().equals(nyfxjgDto_t.getGlsrst())){
											flag=true;
											break;
										}
									}
									if("G+".equals(mngsmxjgDto.getWzlx())){
										if("G+".equals(nyfxjgDto_t.getGlsrst())||"both".equals(nyfxjgDto_t.getGlsrst())){
											flag=true;
											break;
										}

									}else if("G-".equals(mngsmxjgDto.getWzlx())){
										if("G-".equals(nyfxjgDto_t.getGlsrst())||"both".equals(nyfxjgDto_t.getGlsrst())){
											flag=true;
											break;
										}
									}if("both".equals(mngsmxjgDto.getWzlx())){
										if("both".equals(nyfxjgDto_t.getGlsrst())||"G+".equals(nyfxjgDto_t.getGlsrst())||"G-".equals(nyfxjgDto_t.getGlsrst())){
											flag=true;
											break;
										}
									}
								}
							}
						}
					}
				}else{
					for(WknyfxDto wknyfxDto_t:nydtoList){
						if(wknyfxDto_t.getNyjgid().equals(nyfxjgDto_t.getNyjgid())){
							flag=true;
							break;
						}
					}
				}

				if(flag){
					nyfxjgDto_t.setSfbg("1");
				}else{
					nyfxjgDto_t.setSfbg("0");
				}
			}
			map.put("nyfxjgDtos", nyfxjgDtos);

			DlfxjgDto dlfxjgDto=new  DlfxjgDto();
			dlfxjgDto.setBbh(dto.getBbh());
			dlfxjgDto.setWkcxid(wkcxDto.getWkcxid());
			List<DlfxjgDto> dlfxjgDtos = dlfxjgService.getDtoList(dlfxjgDto);
			WkdlfxDto wkdlfxDto=new WkdlfxDto();
			wkdlfxDto.setWkcxid(wkcxDto.getWkcxid());
			List<WkdlfxDto> dtoList = wkdlfxService.getDtoList(wkdlfxDto);
			List<String> vfids=new ArrayList<>();
			for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
				if(StringUtil.isNotBlank(dlfxjgDto_t.getVfid())){
					vfids.add(dlfxjgDto_t.getVfid());
				}
			}
			if(vfids.size()>0){
				BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
				bioDlyzzsDto.setIds(vfids);
				List<BioDlyzzsDto> bioDlyzzsDtos = bioDlyzzsService.getDtoList(bioDlyzzsDto);
				if(bioDlyzzsDtos!=null&&bioDlyzzsDtos.size()>0){
					for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
						for(BioDlyzzsDto dlyzzsDto:bioDlyzzsDtos){
							if(dlfxjgDto_t.getVfid().equals(dlyzzsDto.getVfid())){
								dlfxjgDto_t.setTaxid(dlyzzsDto.getTaxid());
							}
						}
					}
				}
			}
			for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
				boolean flag=false;
				if("01".equals(wkcxDto.getZt())){
					if(mngsmxjgDtos!=null&&mngsmxjgDtos.size()>0){
						for(MngsmxjgDto mngsmxjgDto:mngsmxjgDtos){
							if(mngsmxjgDto.getTaxid().equals(dlfxjgDto_t.getTaxid())){
								flag=true;
								break;
							}
						}
					}
				}else{
					for(WkdlfxDto wkdlfxDto_t:dtoList){
						if(wkdlfxDto_t.getDlfxid().equals(dlfxjgDto_t.getDlfxid())){
							flag=true;
							break;
						}
					}
				}

				if(flag){
					dlfxjgDto_t.setSfbg("1");
				}else{
					dlfxjgDto_t.setSfbg("0");
				}
			}
			map.put("dlfxjgDtos", dlfxjgDtos);
			CnvjgDto parmCnvjgDto=new CnvjgDto();
			parmCnvjgDto.setXpid(dto.getXpid());
			parmCnvjgDto.setWkcxid(dto.getWkcxid());
			CnvjgDto cnvjgDto=cnvjgService.getDtoByXpidAndWkcxId(parmCnvjgDto);
			if(cnvjgDto!=null){
				dto.setCnvjgid(cnvjgDto.getCnvjgid());
			}
			map.put("wkcxDto", dto);
			map.put("sample_info", sjxxDto);
			map.put("status", "success");
		}else{
			map.put("status", "false");
			map.put("message", "未获取到查询id!");
		}
		map.put("audit","false");
		map.put("send","false");
		map.put("classifyExport","false");
		map.put("resistentExport","false");
		map.put("virulenceExport","false");
		map.put("resultExport","false");
		map.put("aiExport","false");
		map.put("modelExport","false");
		User user = getLoginInfo();
		OtherDto otherDto = otherService.getXtyhByYhid(user.getYhid());
		if(otherDto!=null){
			OtherDto otherDto_t=new OtherDto();
			otherDto_t.setJsid(otherDto.getJsid());
			otherDto_t.setZyid(zyid);
			List<OtherDto> jszyczb = otherService.getJszyczb(otherDto_t);
			for(OtherDto dto:jszyczb){
				if("audit".equals(dto.getCzdm())){
					map.put("audit","true");
				}else if("send".equals(dto.getCzdm())){
					map.put("send","true");
				}else if("classifyExport".equals(dto.getCzdm())){
					map.put("classifyExport","true");
				}else if("resistentExport".equals(dto.getCzdm())){
					map.put("resistentExport","true");
				}else if("virulenceExport".equals(dto.getCzdm())){
					map.put("virulenceExport","true");
				}else if("resultExport".equals(dto.getCzdm())){
					map.put("resultExport","true");
				}else if("aiExport".equals(dto.getCzdm())){
					map.put("aiExport","true");
				}else if("modelExport".equals(dto.getCzdm())){
					map.put("modelExport","true");
				}
			}
		}
		return map;
	}

	/**
	 * previewInfo预览信息
	 */
	@RequestMapping("/review/pagedataPreviewInfo")
	@ResponseBody
	public Map<String, Object> previewInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		String zt=wkcxDto.getZt();
		//判断当前的审核状态
		if("01".equals(zt)&&(!"1".equals(wkcxDto.getSfcxsh())))
		{//审核状态为01并且未重新审核则说明未审核
			//获取审核人和检验人列表
			Map<String, Object> userInfo = otherService.getReviewUserInfo(new OtherDto());
			map.put("userInfo", userInfo);
			//相关文献
			//获取前端传过来的相关文献的taxids并查询出来给前端
			XgwxDto xgwxDto = new XgwxDto();
			if (StringUtil.isNotBlank(wkcxDto.getYblx())){
				xgwxDto.setYblx(wkcxDto.getYblx());
			}
			if (null != wkcxDto.getXgwxTaxids() && wkcxDto.getXgwxTaxids().size()>0){
				xgwxDto.setIds(wkcxDto.getXgwxTaxids());
				List<XgwxDto> dtoList = xgwxService.getDtoList(xgwxDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(XgwxDto.class, "bt", "qk", "taxid", "wxid", "yblx", "zz");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("XgwxList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//注释信息
			//获取前端传过来的注释信息的ids并查询出来给前端
			if (null != wkcxDto.getZsxxIds() && wkcxDto.getZsxxIds().size()>0){
				ZsxxDto zsxxDto = new ZsxxDto();
				zsxxDto.setMcs(wkcxDto.getZsxxIds());
				List<ZsxxDto> dtoList = zsxxService.getDtoList(zsxxDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ZsxxDto.class, "glsrst", "knwzly", "mc", "zs", "zsid");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("zsxxList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//毒力因子注释
			if (null != wkcxDto.getDlfxids() && wkcxDto.getDlfxids().size()>0){
				//获取前端传过来的毒力因子的ids并查询出来给前端
				DlfxjgDto dlfxjgDto = new DlfxjgDto();
				dlfxjgDto.setIds(wkcxDto.getDlfxids());
				List<DlfxjgDto> dtoList = dlfxjgService.getDtoList(dlfxjgDto);
				List<BioDlyzzsDto> list =  new ArrayList<>();
				//遍历查出来的毒力因子数据，将每个dto的内容字段（nr）解析出来然后拿里面vfid去毒力因子注释表获取注释
				if(dtoList!=null&&dtoList.size()>0){
					for(DlfxjgDto dto:dtoList){
						if(StringUtil.isNotBlank(dto.getNr())){
							JSONObject jsonObject=JSONObject.parseObject(dto.getNr());
							String dlyz = jsonObject.getString("dlyz");
							if(StringUtil.isNotBlank(dlyz)){
								String vfid="";
								String[] split = dlyz.split("\\(");
								if(split.length>1){
									String[] split_t = split[1].split("\\)");
									vfid=split_t[0];
								}
								if(StringUtil.isNotBlank(vfid)){
									BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
									bioDlyzzsDto.setVfid(vfid);
									BioDlyzzsDto bioDlyzzsDto_t = bioDlyzzsService.getDto(bioDlyzzsDto);
									if(bioDlyzzsDto_t!=null){
										bioDlyzzsDto_t.setDlfxid(dto.getDlfxid());
										list.add(bioDlyzzsDto_t);
									}
								}
							}
						}
					}
				}
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioDlyzzsDto.class, "comment", "dlfxid", "dljyid", "name", "taxid", "vfcategory", "vfid", "wzzwm");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("dlyzzsList", JSON.toJSONString(list,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//微生物解释
			//获取前端传过来的微生物的ids并从物种管理表中查询出来给前端
			if (null != wkcxDto.getWswTaxids() && wkcxDto.getWswTaxids().size()>0){
				BioWzglDto wzglDto = new BioWzglDto();
				wzglDto.setIds(wkcxDto.getWswTaxids());
				List<BioWzglDto> dtoList = wzglService.getDtoList(wzglDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioWzglDto.class, "bdlb", "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("wswList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//获取临床指南表所有数据
			BioLczzznglDto lczzznglDto = new BioLczzznglDto();
			List<BioLczzznglDto> lcznDtoList = lczzznglService.getDtoList(lczzznglDto);
			//获取当前文库的临床指南
			List<String> wswTaxids = wkcxDto.getWswTaxids();
			List<BioLczzznglDto> lczzznglDtos = new ArrayList<>();
			OtherDto otherDto =new OtherDto();
			otherDto.setSjid(wkcxDto.getSjid());
			OtherDto sjxxDto=otherService.getSjxxDto(otherDto);
			//遍历所有临床指南的那个List
			for(BioLczzznglDto lczzznglDto_t:lcznDtoList){
				//判断关联物种字段是否为空
				if(StringUtil.isNotBlank(lczzznglDto_t.getGlwz())){

					//将关联物种json解析并循环
					JSONArray jsonArray = JSON.parseArray(lczzznglDto_t.getGlwz());
					for(int i=0;i<jsonArray.size();i++){
						boolean flag=false;
						//循环当前文库的临床指南信息，找到在关联物种中存在的物种的临床指南放进一个新的临床指南list
						for(String taxid:wswTaxids){
							if(StringUtil.isNotBlank(taxid)){
								if(taxid.equals(jsonArray.getString(i))){
									lczzznglDtos.add(lczzznglDto_t);
									flag=true;
									break;
								}
							}
						}
						if(flag){
							break;
						}
					}
				}
			}
			List<BioLczzznglDto> dtos = new ArrayList<>();
			//将得到的那个新的临床指南list遍历，将样本类型为空或者与文库样本类型一致的传给前端
			if (null != wkcxDto.getWswTaxids() && wkcxDto.getWswTaxids().size()>0) {
				for (BioLczzznglDto lczzznglDto_t : lczzznglDtos) {
					if(StringUtil.isNotBlank(lczzznglDto_t.getNlz())){
						if(StringUtil.isNotBlank(sjxxDto.getNl().trim())){
							int nl=0;
							if("岁".equals(sjxxDto.getNldw())){
								Pattern pattern = Pattern.compile("[0-9]*");
								if(pattern.matcher(sjxxDto.getNl().trim()).matches()){
									nl=Integer.parseInt(sjxxDto.getNl().trim());
								}
							}
							String nlStr="1";
							if(nl>=14){
								nlStr="2";
							}
							if(!lczzznglDto_t.getNlz().equals(nlStr)){
								continue;
							}
						}
					}
					if(StringUtil.isNotBlank(lczzznglDto_t.getXb())){
						if(StringUtil.isNotBlank(sjxxDto.getXb())){
							String xbStr=sjxxDto.getXb();
							if(lczzznglDto_t.getXb().equals("2")){
								if(!xbStr.equals(lczzznglDto_t.getXb())){
									continue;
								}
							}
						}
					}
					if ( wkcxDto.getYblx().equals(lczzznglDto_t.getYblx())) {
						//StringUtil.isBlank(lczzznglDto_t.getYblx()) ||
						dtos.add(lczzznglDto_t);
					}
//					if(StringUtil.isBlank(lczzznglDto_t.getYblx())){
//						dtos.add(lczzznglDto_t);
//					}
				}
			}
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioLczzznglDto.class, "ly", "mc", "url", "name", "yygs", "znid");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("lcznList", JSON.toJSONString(dtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			//疑似物种解释
			//获取前端传过来的疑似物种的taxids并从物种管理表中查询出来给前端
			if (null != wkcxDto.getYsTaxids() && wkcxDto.getYsTaxids().size()>0){
				BioWzglDto wzglDto = new BioWzglDto();
				wzglDto.setIds(wkcxDto.getYsTaxids());
				List<BioWzglDto> dtoList = wzglService.getDtoList(wzglDto);
				filter = new SimplePropertyPreFilter(BioWzglDto.class, "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
				listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("ysList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			WkcxDto wkcxDto_t=new WkcxDto();
			wkcxDto_t.setWkcxid(wkcxDto.getWkcxid());
			wkcxDto_t.setBbh(wkcxDto.getShbbbh());
			WkcxDto dtoById = wkcxService.getDtoNew(wkcxDto_t);
			//获取个人设置里所设置的常用审核人以及常用检验人，前端默认选中，便于操作
            User user=getLoginInfo();
            GrszDto grszDto=new GrszDto();
            grszDto.setYhid(user.getYhid());
            grszDto.setSzlbs(new String[]{PersonalSettingEnum.BIO_AUDIT.getCode(),PersonalSettingEnum.BIO_INSPECT.getCode()});
            Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
            GrszDto grszDto_sh = grszDtoMap.get(PersonalSettingEnum.BIO_AUDIT.getCode());
            if(grszDto_sh!=null){
                if(StringUtil.isNotBlank(grszDto_sh.getSzz())){
                    dtoById.setShryid(grszDto_sh.getSzz());
                }
            }
            GrszDto grszDto_jy = grszDtoMap.get(PersonalSettingEnum.BIO_INSPECT.getCode());
            if(grszDto_jy!=null){
                if(StringUtil.isNotBlank(grszDto_jy.getSzz())){
                    dtoById.setJyryid(grszDto_jy.getSzz());
                }
            }

			map.put("wkcxDto", dtoById);
			//传递标记区分是否是合并操作：MERGE是合并，VIEW则不是
			map.put("pageFlag", "VIEW");
		}else{//如果是已审核
			wkcxDto.setWkbh(wkcxDto.getNbbm());
			List<String> zts=new ArrayList<>();
			zts.add("02");
			zts.add("03");
			wkcxDto.setZts(zts);
			List<WkcxDto> wkcxDtos = wkcxService.getDtoList(wkcxDto);

			if(wkcxDtos.size()>1&&!"01".equals(zt)){
				//存在多个文库，则自动显示合并页面
				String wklx="";
				for(WkcxDto dto:wkcxDtos){
					if(wkcxDto.getWkcxid().equals(dto.getWkcxid())){
						wklx=dto.getWklx();
					}
				}
				//将不同于前端所传文库类型的list传给前端
				List<WkcxDto> list=new ArrayList<>();
				for(WkcxDto dto:wkcxDtos){
					if(StringUtil.isNotBlank(dto.getWklx())){
						if(!wklx.equals(dto.getWklx())){
							list.add(dto);
						}
					}
				}
				map.put("wkcxDtos", list);
				//标记，说明是合并
				map.put("pageFlag", "MERGE");
			}else{
				//说明只有此一个文库，那则继续按照单个的预览操作进行（已审核流程）
				//获取审核人和检验人列表
				Map<String, Object> userInfo = otherService.getReviewUserInfo(new OtherDto());
				map.put("userInfo", userInfo);
				//相关文献
				//获取前端传过来的相关文献的taxids并查询出来给前端
				XgwxDto xgwxDto = new XgwxDto();
				if (StringUtil.isNotBlank(wkcxDto.getYblx())){
					xgwxDto.setYblx(wkcxDto.getYblx());
				}
				if (null != wkcxDto.getXgwxTaxids() && wkcxDto.getXgwxTaxids().size()>0){
					xgwxDto.setIds(wkcxDto.getXgwxTaxids());
					List<XgwxDto> dtoList = xgwxService.getDtoList(xgwxDto);
					SimplePropertyPreFilter filter = new SimplePropertyPreFilter(XgwxDto.class, "bt", "qk", "taxid", "wxid", "yblx", "zz");
					SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
					listFilters[0] = filter;
					map.put("XgwxList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				}
				//注释信息
				//获取前端传过来的注释信息的ids并查询出来给前端
				if (null != wkcxDto.getZsxxIds() && wkcxDto.getZsxxIds().size()>0){
					ZsxxDto zsxxDto = new ZsxxDto();
					zsxxDto.setMcs(wkcxDto.getZsxxIds());
					List<ZsxxDto> dtoList = zsxxService.getDtoList(zsxxDto);
					SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ZsxxDto.class, "glsrst", "knwzly", "mc", "zs", "zsid");
					SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
					listFilters[0] = filter;
					map.put("zsxxList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				}
				//微生物解释
				//获取前端传过来的微生物的ids并从物种管理表中查询出来给前端
				if (null != wkcxDto.getWswTaxids() && wkcxDto.getWswTaxids().size()>0){
					BioWzglDto wzglDto = new BioWzglDto();
					wzglDto.setIds(wkcxDto.getWswTaxids());
					List<BioWzglDto> dtoList = wzglService.getDtoList(wzglDto);
					SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioWzglDto.class, "bdlb", "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
					SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
					listFilters[0] = filter;
					map.put("wswList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				}
				//通过文库测序ID获取文库临床指南表（此表保存的是审核时保存的临床指南）
				WklcznDto wklcznDto=new WklcznDto();
				wklcznDto.setWkcxid(wkcxDto.getWkcxid());
				List<WklcznDto> dtoList = wklcznService.getDtoList(wklcznDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioLczzznglDto.class, "ly", "mc", "url", "name", "yygs", "znid");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("lcznList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				//判断版本信息是否存在，存在则取其录入时间并截取作为mngs明细结果的分表后缀
				if (null != wkcxDto.getYsTaxids() && wkcxDto.getYsTaxids().size()>0){
					BioWzglDto wzglDto = new BioWzglDto();
					wzglDto.setIds(wkcxDto.getYsTaxids());
					List<BioWzglDto> wzglDtos = wzglService.getDtoList(wzglDto);
					filter = new SimplePropertyPreFilter(BioWzglDto.class, "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
					listFilters = new SimplePropertyPreFilter[1];
					listFilters[0] = filter;
					map.put("ysList", JSON.toJSONString(wzglDtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				}
				WkcxDto wkcxDto_t=new WkcxDto();
				wkcxDto_t.setWkcxid(wkcxDto.getWkcxid());
				wkcxDto_t.setBbh(wkcxDto.getShbbbh());
				WkcxDto dtoById = wkcxService.getDtoNew(wkcxDto_t);
				map.put("wkcxDto", dtoById);
				//标记，说明是单个操作
				map.put("pageFlag", "VIEW");
				//毒力因子注释
				if (null != wkcxDto.getDlfxids() && wkcxDto.getDlfxids().size()>0){
					//获取前端传过来的毒力因子的ids并查询出来给前端
					DlfxjgDto dlfxjgDto = new DlfxjgDto();
					dlfxjgDto.setIds(wkcxDto.getDlfxids());
					List<DlfxjgDto> dlfxjgDtos = dlfxjgService.getDtoList(dlfxjgDto);
					List<BioDlyzzsDto> list =  new ArrayList<>();
					//遍历查出来的毒力因子数据，将每个dto的内容字段（nr）解析出来然后拿里面vfid去毒力因子注释表获取注释
					if(dlfxjgDtos!=null&&dlfxjgDtos.size()>0){
						for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
							if(StringUtil.isNotBlank(dlfxjgDto_t.getNr())){
								JSONObject jsonObject=JSONObject.parseObject(dlfxjgDto_t.getNr());
								String dlyz = jsonObject.getString("dlyz");
								if(StringUtil.isNotBlank(dlyz)){
									String vfid="";
									String[] strings = dlyz.split("\\(");
									if(strings.length>1){
										String[] split_t = strings[1].split("\\)");
										vfid=split_t[0];
									}
									if(StringUtil.isNotBlank(vfid)){
										BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
										bioDlyzzsDto.setVfid(vfid);
										BioDlyzzsDto bioDlyzzsDto_t = bioDlyzzsService.getDto(bioDlyzzsDto);
										if(bioDlyzzsDto_t!=null){
											bioDlyzzsDto_t.setDlfxid(dlfxjgDto_t.getDlfxid());
											list.add(bioDlyzzsDto_t);
										}
									}
								}
							}
						}
					}
					filter = new SimplePropertyPreFilter(BioDlyzzsDto.class, "comment", "dlfxid", "dljyid", "name", "taxid", "vfcategory", "vfid", "wzzwm");
					listFilters = new SimplePropertyPreFilter[1];
					listFilters[0] = filter;
					map.put("dlyzzsList", JSON.toJSONString(list,listFilters, SerializerFeature.DisableCircularReferenceDetect));
				}
			}
		}
		return map;
	}


	/**
	 * 合并
	 */
	@RequestMapping("/review/pagedataMergeInfo")
	@ResponseBody
	public Map<String, Object> mergeInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		//获取审核人和检验人列表
		Map<String, Object> userInfo = otherService.getReviewUserInfo(new OtherDto());
		map.put("userInfo", userInfo);
		//判断是否有合并文库测序ID,如果有则说明选中了一条进行合并，则走合并操作
		//如果没有则说明一条都没选则走单个的操作
		if(StringUtil.isNotBlank(wkcxDto.getHbwkcxid()))
		{//说明选中了一条进行合并，走合并操作
			List<String> ids=new ArrayList<>();
			ids.add(wkcxDto.getWkcxid());
			ids.add(wkcxDto.getHbwkcxid());
			wkcxDto.setIds(ids);
			List<XgwxDto> xgwxDtos = new ArrayList<>();//存放总的文献数据
			List<ZsxxDto> zsxxDtos = new ArrayList<>();//存放总的注释数据
			List<BioWzglDto> wswList = new ArrayList<>();//存放总的微生物数据
			List<WkysmxDto> ysList = new ArrayList<>();//存放总的疑似数据
			List<WklcznDto> lczzznglDtos = new ArrayList<>();//存放总的临床诊断数据
			List<String> znids=new ArrayList<>();
			List<String> taxids=new ArrayList<>();
			//查出两个文库的文库信息
			List<WkcxDto> listByIds = wkcxService.getListByIds(wkcxDto);
			//遍历这两个文库信息
			for(WkcxDto wkcxDto_t:listByIds){
				String xgwxids="";
				//获取文库的版本信息
				WkcxbbDto wkcxbbDto = new WkcxbbDto();
				wkcxbbDto.setBbh(wkcxDto_t.getShbbbh());
				wkcxbbDto.setWkcxid(wkcxDto_t.getWkcxid());
				WkcxbbDto dto = wkcxbbService.getDto(wkcxbbDto);

				//获取耐药分析结果数据
				WknyfxDto wknyfxDto = new WknyfxDto();
				wknyfxDto.setWkcxid(wkcxDto_t.getWkcxid());
				List<WknyfxDto> dtoList = wknyfxService.getDtoList(wknyfxDto);
				if(dtoList!=null&&dtoList.size()>0){
					List<String> mcs=new ArrayList<>();
					//遍历耐药分析结果数据，将其中的内容字段的json解析出来，将其中的耐药家族组装成一个list
					for(WknyfxDto dto_t:dtoList){
						NyfxjgDto nyfxjgDto_t= JSON.parseObject(dto_t.getNr(),NyfxjgDto.class);
						mcs.add(nyfxjgDto_t.getNyjzu());
					}
					//将组装成的耐药家族list带入注释信息表查出对应的注释信息并添加到总的注释信息List
					ZsxxDto zsxxDto=new ZsxxDto();
					zsxxDto.setMcs(mcs);
					List<ZsxxDto> dtoList_zsxx = zsxxService.getDtoList(zsxxDto);
					zsxxDtos.addAll(dtoList_zsxx);
				}
				//获取文库的微生物数据以及疑似数据
				BioWzglDto wzglDto = new BioWzglDto();
				wzglDto.setWkcxid(wkcxDto_t.getWkcxid());
				wzglDto.setBbh(wkcxDto_t.getShbbbh());
				//为了防止内部编码里刚好相等，比如MDL001-HSXXXX,所以后面加-
				if(!"1".equals(wkcxDto_t.getQxhbbg())){
					if(wkcxDto_t.getWkbh().contains("-RNA-")){
						wzglDto.setRnaflag("1");
					}
					if(wkcxDto_t.getWkbh().contains("-DNA-")||wkcxDto_t.getWkbh().contains("-Onco-")||wkcxDto_t.getWkbh().contains("-XD-")
							||wkcxDto_t.getWkbh().contains("-HS-")){
						wzglDto.setDnaflag("1");
					}
					if(wkcxDto_t.getWkbh().contains("-DR-")){
						OtherDto otherDto = new OtherDto();
						OtherDto sjxxDto = new OtherDto();
						otherDto.setSjid(wkcxDto_t.getSjid());
						sjxxDto = otherService.getSjxxDto(otherDto);
						if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
							if(sjxxDto.getNbbm().contains("01D")){
								wzglDto.setDnaflag("1");
							}
							if(sjxxDto.getNbbm().contains("01R")){
								wzglDto.setRnaflag("1");
							}
						}
					}

					if(wkcxDto_t.getWkbh().contains("-tNGS")||wkcxDto_t.getWkbh().contains("-TBtNGS-")){
						if(wkcxDto_t.getWkbh().contains("MDL001")){
							wzglDto.setRnaflag("1");
						}else{
							wzglDto.setDnaflag("1");
	   					}
					}
				}
				if (dto!=null){//版本信息不为空则正常获取mngs明细结果分表后缀
					wzglDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
				}
				//获取当前文库的所有物种信息
				List<BioWzglDto> wzList = wzglService.getWzList(wzglDto);
				if(wzList!=null&&wzList.size()>0){
					//遍历物种信息
					for(BioWzglDto dto_t:wzList){
						//将其中关注度为1或者2的物种放进相关文献的IDs,用于下面查询对应的相关文献
						 if("1".equals(dto_t.getGzd())||"2".equals(dto_t.getGzd())){
							 if(StringUtil.isBlank(xgwxids)){
								 xgwxids+=dto_t.getWzid();
							 }else{
								 xgwxids+=","+dto_t.getWzid();
							 }
							 //去除重复的物种信息
							 if(!wswList.stream().filter(item->item.getWzid().equals(dto_t.getWzid())).findFirst().isPresent()){
								 wswList.add(dto_t);
							 }

						}
					}
				}
				//通过文库测序ID获取文库临床指南表（此表保存的是审核时保存的临床指南）
				WklcznDto wklcznDto=new WklcznDto();
				wklcznDto.setWkcxid(wkcxDto_t.getWkcxid());
				List<WklcznDto> wklcznDtos = wklcznService.getDtoList(wklcznDto);
				for(WklcznDto wklcznDto_t:wklcznDtos){
					//去重重复数据
					if(!znids.contains(wklcznDto_t.getZnid())){
						znids.add(wklcznDto_t.getZnid());
						lczzznglDtos.add(wklcznDto_t);
					}
				}
				if (dto!=null){
					//通过文库测序ID获取文库疑似明细表（此表保存的是审核时保存的疑似的物种信息）
					WkysmxDto wkysmxDto=new WkysmxDto();
					wkysmxDto.setWkcxid(wkcxDto_t.getWkcxid());
					wkysmxDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
					List<WkysmxDto> wkysmxDtos = wkysmxService.getDtoList(wkysmxDto);
					for(WkysmxDto wkysmxDto_t:wkysmxDtos){
						//去重重复数据
						if(!taxids.contains(wkysmxDto_t.getWzid())){
							taxids.add(wkysmxDto_t.getWzid());
							if(!ysList.contains(wkysmxDto_t)){
								ysList.add(wkysmxDto_t);

							}
							//将疑似物种的物种ID也添加到相关文献ids，用于下面查询对应的相关文献
							if(StringUtil.isBlank(xgwxids)){
								xgwxids+=wkysmxDto_t.getWzid();
							}else{
								xgwxids+=","+wkysmxDto_t.getWzid();
							}
						}

					}
				}
				//将集合微生物以及疑似的物种ID的相关文献ids带入表中查出相关文献
				if(StringUtil.isNotBlank(xgwxids)){
					XgwxDto xgwxDto=new XgwxDto();
					xgwxDto.setIds(xgwxids);
					List<XgwxDto> xgwxList = xgwxService.getDtoList(xgwxDto);
					for(XgwxDto xgwxDto1:xgwxList){
						//去除重复数据
						if(!xgwxDtos.stream().filter(item->item.getWxid().equals(xgwxDto1.getWxid())).findFirst().isPresent()){
							xgwxDtos.add(xgwxDto1);
						}
					}
				}
			}
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioLczzznglDto.class, "ly", "mc", "url", "name", "yygs", "znid");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("lcznList", JSON.toJSONString(lczzznglDtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			String hbwkcxid=wkcxDto.getHbwkcxid();
			//更新文库测序表数据
			WkcxDto dto=new WkcxDto();
			String bbh;
			//判断所选择的质控类型与被合并的文库类型是否相等，相等则以被合并的那条文库为主文库，并更新文库测序表信息
			if(wkcxDto.getWklx().equals(wkcxDto.getZklx())){
				String wkcxid=wkcxDto.getWkcxid();
				dto.setWkcxid(wkcxDto.getHbwkcxid());
				dto.setHbwkcxid(wkcxid);
				bbh=wkcxDto.getBbh();
			}else{
				dto.setWkcxid(wkcxDto.getWkcxid());
				dto.setHbwkcxid(wkcxDto.getHbwkcxid());
				bbh=wkcxDto.getShbbbh();
			}
			dto.setZklx(wkcxDto.getZklx());
			wkcxService.updateDto(dto);
			WkcxDto wkcxDto_t=new WkcxDto();
			wkcxDto_t.setWkcxid(dto.getWkcxid());
			wkcxDto_t.setBbh(bbh);
			//将最新的主文库测序信息重新查一遍给前端
			WkcxDto dtoById = wkcxService.getDtoNew(wkcxDto_t);
			map.put("wkcxDto", dtoById);
			filter = new SimplePropertyPreFilter(XgwxDto.class, "bt", "qk", "taxid", "wxid", "yblx", "zz");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("XgwxList", JSON.toJSONString(xgwxDtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			filter = new SimplePropertyPreFilter(ZsxxDto.class, "glsrst", "knwzly", "mc", "zs", "zsid");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("zsxxList", JSON.toJSONString(zsxxDtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			filter = new SimplePropertyPreFilter(BioWzglDto.class, "bdlb", "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("wswList", JSON.toJSONString(wswList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			filter = new SimplePropertyPreFilter(BioWzglDto.class, "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("ysList", JSON.toJSONString(ysList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			List<MngsmxjgDto> ysGather =new ArrayList<>();//疑似
			List<MngsmxjgDto> zjGather =new ArrayList<>();//真菌
			List<MngsmxjgDto> xjGather =new ArrayList<>();//细菌
			List<MngsmxjgDto> fzgjGather =new ArrayList<>();//分枝杆菌
			List<MngsmxjgDto> zytGather =new ArrayList<>();//支原体
			List<MngsmxjgDto> dnaGather =new ArrayList<>();//DNA
			List<MngsmxjgDto> rnaGather =new ArrayList<>();//RNA
			List<MngsmxjgDto> jscGather =new ArrayList<>();//寄生虫
			if (StringUtil.isNotBlank(wkcxDto.getJgid())){
				MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
				mngsmxjgDto.setJgid(wkcxDto.getJgid());
				mngsmxjgDto.setLrsj(wkcxDto.getLrsj().substring(0,4)+wkcxDto.getLrsj().substring(5,7));
				mngsmxjgDto.setAijg("1");
				mngsmxjgDto.setAijg1("2");
				mngsmxjgDto.setAijg2("4");
				WkcxDto t_wkcxDto=new WkcxDto();
				t_wkcxDto.setWkcxid(wkcxDto.getHbwkcxid());
				//获取被合并的那个文库的文库测序信息
				WkcxDto hbwkcx = wkcxService.getDtoNew(t_wkcxDto);
				//判断是否是强行合并，不是则增加物种DNA和RNA的显示限制，反之则不加
				if(!"1".equals(hbwkcx.getQxhbbg())){
					if(hbwkcx.getWkbh().contains("-RNA-")){
						mngsmxjgDto.setRnaflag("1");
					}
					if(hbwkcx.getWkbh().contains("-DNA-")||hbwkcx.getWkbh().contains("-Onco-")||hbwkcx.getWkbh().contains("-XD-")
							||hbwkcx.getWkbh().contains("-HS-")){
						mngsmxjgDto.setDnaflag("1");
					}
					if(hbwkcx.getWkbh().contains("-DR-")){
						OtherDto otherDto = new OtherDto();
						OtherDto sjxxDto = new OtherDto();
						otherDto.setSjid(hbwkcx.getSjid());
						sjxxDto = otherService.getSjxxDto(otherDto);
						if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
							if(sjxxDto.getNbbm().contains("01D")){
								mngsmxjgDto.setDnaflag("1");
							}
							if(sjxxDto.getNbbm().contains("01R")){
								mngsmxjgDto.setRnaflag("1");
							}
						}
					}
					if(hbwkcx.getWkbh().contains("-tNGS")||hbwkcx.getWkbh().contains("-TBtNGS-")){
						if(hbwkcx.getWkbh().contains("MDL001")){
							mngsmxjgDto.setRnaflag("1");
						}else{
							mngsmxjgDto.setDnaflag("1");
	   					}
					}
				}
				//将关注度为1或者2，以及疑似的数据全部物种取出
				List<MngsmxjgDto> mngsmxjgDtos = mngsmxjgService.getMngsInfo(mngsmxjgDto);
				List<MngsmxjgDto> list=new ArrayList<>();
				List<String> xgwxTaxids=wkcxDto.getXgwxTaxids();
				if(xgwxTaxids!=null&&xgwxTaxids.size()>0){
					for(MngsmxjgDto mngsmxjgDto_t:mngsmxjgDtos){
						boolean flag=true;
						for(String taxid:xgwxTaxids){
							if(taxid.equals(mngsmxjgDto_t.getTaxid())){
								flag=false;
								break;
							}
						}
						if(flag){
							list.add(mngsmxjgDto_t);
						}
					}
				}else{
					list.addAll(mngsmxjgDtos);
				}
				//获取被合并的那个文库的文库疑似明细数据
				WkysmxDto wkysmxDto=new WkysmxDto();
				wkysmxDto.setWkcxid(wkcxDto.getHbwkcxid());
				List<WkysmxDto> wkysmxDtoList=wkysmxService.getList(wkysmxDto);
				//遍历全部物种list,按照不同的物种分类，将不同的物种放进不同的List
				for(MngsmxjgDto mngsmxjgDto_t:list){
					if("4".equals(mngsmxjgDto_t.getSfys())&&wkysmxDtoList.stream().filter(item->mngsmxjgDto_t.getMxjgid().equals(item.getMxjgid())).findFirst().orElse(null)!=null){
						ysGather.add(mngsmxjgDto_t);
					}else if("Fungi".equals(mngsmxjgDto_t.getWzfl())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						zjGather.add(mngsmxjgDto_t);
					}else if("Bacteria".equals(mngsmxjgDto_t.getWzfl())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						xjGather.add(mngsmxjgDto_t);
					}else if("Mycobacteria".equals(mngsmxjgDto_t.getWzfl())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						fzgjGather.add(mngsmxjgDto_t);
					}else if("Mycoplasma".equals(mngsmxjgDto_t.getWzfl())||"Rickettsia".equals(mngsmxjgDto_t.getWzfl())||"Chlamydia".equals(mngsmxjgDto_t.getWzfl())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						zytGather.add(mngsmxjgDto_t);
					}else if("Viruses".equals(mngsmxjgDto_t.getWzfl())&&"DNA".equals(mngsmxjgDto_t.getBdlb())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						dnaGather.add(mngsmxjgDto_t);
					}else if("Viruses".equals(mngsmxjgDto_t.getWzfl())&&"RNA".equals(mngsmxjgDto_t.getBdlb())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						rnaGather.add(mngsmxjgDto_t);
					}else if("parasites".equals(mngsmxjgDto_t.getWzfl())&&!"4".equals(mngsmxjgDto_t.getSfys())){
						jscGather.add(mngsmxjgDto_t);
					}
				}

			}
			filter = new SimplePropertyPreFilter(BioWzglDto.class, "bdlb", "fl","fid", "fldj", "freadsaccum", "ftaxid", "fwzlx", "fzwmm", "grc", "gzd", "jgid", "mxjgid", "qz", "mxjgid", "qzpw", "readsaccum", "scid", "sfys", "taxid", "wkbh", "wkcxid", "wzdl", "wzfl", "wzlx", "xh", "xm", "xpid", "zbx", "zwmm");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("ysGather", JSON.toJSONString(ysGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("zjGather", JSON.toJSONString(zjGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("xjGather", JSON.toJSONString(xjGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("fzgjGather", JSON.toJSONString(fzgjGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("zytGather", JSON.toJSONString(zytGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("dnaGather", JSON.toJSONString(dnaGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("rnaGather", JSON.toJSONString(rnaGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			map.put("jscGather", JSON.toJSONString(jscGather,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			//通过文库测序ID去文库耐药分析表查出审核时所保存的耐药分析数据
			WknyfxDto wknyfxDto=new WknyfxDto();
			wknyfxDto.setWkcxid(hbwkcxid);
			List<WknyfxDto> wknyfxDtos = wknyfxService.getDtoList(wknyfxDto);
			filter = new SimplePropertyPreFilter(BioWzglDto.class, "bbh", "gene","genefamily", "mechanism", "nyjgid", "nyjy", "sfbg", "species", "tagrget", "wkcxid", "xgry");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("nyfxjgDtos", JSON.toJSONString(wknyfxDtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			List<BioDlyzzsDto> list =  new ArrayList<>();
			//将前端传过来的ids(原本的文库)的毒力因子注释和被合并的文库的毒力因子注释合并传给前端
			if (null != wkcxDto.getDlfxids() && wkcxDto.getDlfxids().size()>0){
				DlfxjgDto dlfxjgDto = new DlfxjgDto();
				dlfxjgDto.setIds(wkcxDto.getDlfxids());
				List<DlfxjgDto> dlfxjgDtos = dlfxjgService.getDtoList(dlfxjgDto);
				//遍历查出来的毒力因子数据，将每个dto的内容字段（nr）解析出来然后拿里面vfid去毒力因子注释表获取注释
				if(dlfxjgDtos!=null&&dlfxjgDtos.size()>0){
					for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
						if(StringUtil.isNotBlank(dlfxjgDto_t.getNr())){
							JSONObject jsonObject=JSONObject.parseObject(dlfxjgDto_t.getNr());
							String dlyz = jsonObject.getString("dlyz");
							if(StringUtil.isNotBlank(dlyz)){
								String vfid="";
								String[] strings = dlyz.split("\\(");
								if(strings.length>1){
									String[] split_t = strings[1].split("\\)");
									vfid=split_t[0];
								}
								if(StringUtil.isNotBlank(vfid)){
									BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
									bioDlyzzsDto.setVfid(vfid);
									BioDlyzzsDto bioDlyzzsDto_t = bioDlyzzsService.getDto(bioDlyzzsDto);
									if(bioDlyzzsDto_t!=null){
										bioDlyzzsDto_t.setDlfxid(dlfxjgDto_t.getDlfxid());
										list.add(bioDlyzzsDto_t);
									}
								}
							}
						}
					}
				}
			}
			//获取被合并文库审核时所保存的毒力因子信息
			WkdlfxDto wkdlfxDto=new WkdlfxDto();
			wkdlfxDto.setWkcxid(wkcxDto.getHbwkcxid());
			List<WkdlfxDto> dtoList = wkdlfxService.getDtoList(wkdlfxDto);
			filter = new SimplePropertyPreFilter(BioWzglDto.class, "category", "dlfxid","gene", "reads", "sfbg", "species", "virulencefactor", "wkcxid");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("dlfxjgDtos", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			if(dtoList!=null&&dtoList.size()>0){
				//遍历查出来的毒力因子数据，将每个dto的内容字段（nr）解析出来然后拿里面vfid去毒力因子注释表获取注释
				for(WkdlfxDto wkdlfxDto_t:dtoList){
					if(StringUtil.isNotBlank(wkdlfxDto_t.getNr())){
						JSONObject jsonObject=JSONObject.parseObject(wkdlfxDto_t.getNr());
						String dlyz = jsonObject.getString("dlyz");
						if(StringUtil.isNotBlank(dlyz)){
							String vfid="";
							String[] split = dlyz.split("\\(");
							if(split.length>1){
								String[] split_t = split[1].split("\\)");
								vfid=split_t[0];
							}
							if(StringUtil.isNotBlank(vfid)){
								BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
								bioDlyzzsDto.setVfid(vfid);
								BioDlyzzsDto bioDlyzzsDto_t = bioDlyzzsService.getDto(bioDlyzzsDto);
								if(bioDlyzzsDto_t!=null){
									bioDlyzzsDto_t.setDlfxid(wkdlfxDto_t.getDlfxid());
									list.add(bioDlyzzsDto_t);
								}
							}
						}
					}
				}
			}
			filter = new SimplePropertyPreFilter(BioDlyzzsDto.class, "comment", "dlfxid", "dljyid", "name", "taxid", "vfcategory", "vfid", "wzzwm");
			listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("dlyzzsList", JSON.toJSONString(list,listFilters, SerializerFeature.DisableCircularReferenceDetect));
		}else{//说明一条都没选，还是走的单个文库的操作
			//相关文献
			//获取前端传过来的相关文献的taxids并查询出来给前端
			XgwxDto xgwxDto = new XgwxDto();
			if (StringUtil.isNotBlank(wkcxDto.getYblx())){
				xgwxDto.setYblx(wkcxDto.getYblx());
			}
			if (null != wkcxDto.getXgwxTaxids() && wkcxDto.getXgwxTaxids().size()>0){

				xgwxDto.setIds(wkcxDto.getXgwxTaxids());

				List<XgwxDto> dtoList = xgwxService.getDtoList(xgwxDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(XgwxDto.class, "bt", "qk", "taxid", "wxid", "yblx", "zz");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("XgwxList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//注释信息
			//获取前端传过来的注释信息的ids并查询出来给前端
			if (null != wkcxDto.getZsxxIds() && wkcxDto.getZsxxIds().size()>0){
				ZsxxDto zsxxDto = new ZsxxDto();
				zsxxDto.setMcs(wkcxDto.getZsxxIds());
				List<ZsxxDto> dtoList = zsxxService.getDtoList(zsxxDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ZsxxDto.class, "glsrst", "knwzly", "mc", "zs", "zsid");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("zsxxList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//微生物解释
			//获取前端传过来的微生物的ids并从物种管理表中查询出来给前端
			if (null != wkcxDto.getWswTaxids() && wkcxDto.getWswTaxids().size()>0){
				BioWzglDto wzglDto = new BioWzglDto();
				wzglDto.setIds(wkcxDto.getWswTaxids());
				List<BioWzglDto> dtoList = wzglService.getDtoList(wzglDto);
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ZsxxDto.class, "glsrst", "knwzly", "mc", "zs", "zsid");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("wswList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			//通过文库测序ID获取文库临床指南表（此表保存的是审核时保存的临床指南）
			WklcznDto wklcznDto=new WklcznDto();
			wklcznDto.setWkcxid(wkcxDto.getWkcxid());
			List<WklcznDto> dtoList = wklcznService.getDtoList(wklcznDto);
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BioLczzznglDto.class, "ly", "mc", "url", "name", "yygs", "znid");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("lcznList", JSON.toJSONString(dtoList,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			if (null != wkcxDto.getYsTaxids() && wkcxDto.getYsTaxids().size()>0){
				BioWzglDto wzglDto = new BioWzglDto();
				wzglDto.setIds(wkcxDto.getYsTaxids());
				List<BioWzglDto> wzglDtos = wzglService.getDtoList(wzglDto);
				filter = new SimplePropertyPreFilter(BioWzglDto.class, "fid", "fldj", "wzdl", "wzfl", "wzid", "wzlx", "wzywm", "wzzs", "wzzwm", "zbx");
				listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("ysList", JSON.toJSONString(wzglDtos,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
			WkcxDto wkcxDto_t=new WkcxDto();
			wkcxDto_t.setWkcxid(wkcxDto.getWkcxid());
			wkcxDto_t.setBbh(wkcxDto.getShbbbh());
			WkcxDto dtoById = wkcxService.getDtoNew(wkcxDto_t);
			map.put("wkcxDto", dtoById);
			//标记，说明是单个操作
			map.put("pageFlag", "VIEW");
			//毒力因子注释
			if (null != wkcxDto.getDlfxids() && wkcxDto.getDlfxids().size()>0){
				//获取前端传过来的毒力因子的ids并查询出来给前端
				DlfxjgDto dlfxjgDto = new DlfxjgDto();
				dlfxjgDto.setIds(wkcxDto.getDlfxids());
				List<DlfxjgDto> dlfxjgDtos = dlfxjgService.getDtoList(dlfxjgDto);
				List<BioDlyzzsDto> list =  new ArrayList<>();
				//遍历查出来的毒力因子数据，将每个dto的内容字段（nr）解析出来然后拿里面vfid去毒力因子注释表获取注释
				if(dlfxjgDtos!=null&&dlfxjgDtos.size()>0){
					for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
						if(StringUtil.isNotBlank(dlfxjgDto_t.getNr())){
							JSONObject jsonObject=JSONObject.parseObject(dlfxjgDto_t.getNr());
							String dlyz = jsonObject.getString("dlyz");
							if(StringUtil.isNotBlank(dlyz)){
								String vfid="";
								String[] strings = dlyz.split("\\(");
								if(strings.length>1){
									String[] split_t = strings[1].split("\\)");
									vfid=split_t[0];
								}
								if(StringUtil.isNotBlank(vfid)){
									BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
									bioDlyzzsDto.setVfid(vfid);
									BioDlyzzsDto bioDlyzzsDto_t = bioDlyzzsService.getDto(bioDlyzzsDto);
									if(bioDlyzzsDto_t!=null){
										bioDlyzzsDto_t.setDlfxid(dlfxjgDto_t.getDlfxid());
										list.add(bioDlyzzsDto_t);
									}
								}
							}
						}
					}
				}
				filter = new SimplePropertyPreFilter(BioDlyzzsDto.class, "comment", "dlfxid", "dljyid", "name", "taxid", "vfcategory", "vfid", "wzzwm");
				listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("dlyzzsList", JSON.toJSONString(list,listFilters, SerializerFeature.DisableCircularReferenceDetect));
			}
		}

		return map;
	}

	/**
	 * 强行合并
	 */
	@RequestMapping("/review/pagedataForceMerge")
	@ResponseBody
	public Map<String, Object> forceMerge(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = wkcxService.updateDto(wkcxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"合并成功！":"合并失败！");
		return map;
	}


	/**
	 * 提交
	 */
	@RequestMapping("/review/pagedataSubmit")
	@ResponseBody
	public Map<String, Object> submit(WkcxDto wkcxDto) {
		//加锁，防止同时审核同一条数据的情况
		lock.lock();
		Map<String, Object> map = new HashMap<>();
		try {

			User user = getLoginInfo();
			//判断是否未选择审核人员以及检验人员
			if (StringUtil.isBlank(wkcxDto.getShryid()) || StringUtil.isBlank(wkcxDto.getJyryid())) {
				map.put("status", "err");
				map.put("message", "请选择审核人员以及检验人员！");
				return map;
			}
			wkcxDto.setSffsbg("0");
			wkcxDto.setSfxybg("1");
			wkcxDto.setZt("02");
			WkcxDto cxDto = new WkcxDto();
			cxDto.setZt("02");
			cxDto.setWkcxid(wkcxDto.getWkcxid());
			WkcxDto wkcxDto1 = wkcxService.getDto(cxDto);
			List<MngsmxjgDto> list = JSON.parseArray(wkcxDto.getMngsmx_json(), MngsmxjgDto.class);
			//判断是否已经被别人审核过
			if (wkcxDto1 == null) {
				mngsmxjgService.reviewAudit(list, wkcxDto, user);
				map.put("status", "success");
				map.put("message", "提交成功！");
			} else {
				map.put("status", "err");
				map.put("message", "当前文库已经被其他人员审核");
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		} finally {
			lock.unlock();
		}
		return map;
	}

	/**
	 * 发送报告
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/review/pagedataSendReport")
	@ResponseBody
	public Map<String, Object> sendReport(WkcxDto wkcxDto) {
		JSONObject jsonObject=new JSONObject();//用于组装报告json数据
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		List<String> ids = wkcxDto.getIds();//获取前端所传的文库测序IDs,从而判断是否是合并文库发送报告
		WkcxDto cxDto=new WkcxDto();
		cxDto.setZt("03");
		cxDto.setWkcxid(wkcxDto.getWkcxid());
		//加锁，防止出现同时发送报告的情况
		lock.lock();
		try{
			WkcxDto wkcxDto1=wkcxService.getDto(cxDto);
			cxDto.setZt("01");
			WkcxDto wkcxDto2=wkcxService.getDto(cxDto);
			if(wkcxDto2!=null){
				map.put("status","err");
				map.put("message","当前文库未审核请审核后再发送报告!");
			}
			else if(wkcxDto1!=null&&!(ids.size()>1)){
				map.put("status","err");
				map.put("message","当前文库已经被其他人员发送报告!");
			}
			else{
				String wklx="";
				MngsmxjgDto mngsmxjgDto =new MngsmxjgDto();
				//如果为合并文库报告发送，则设置报告类型为'C'
				if(ids.size()>1){
					wklx="C";
				}else{
					//单个文库报告发送
					//判断是否是强行合并报告，是的话设置报告类型为'C'
					if("1".equals(wkcxDto.getQxhbbg())){
						wklx="C";

					}else{
						//不是的话则根据文库类型去判断是DNA还是RNA
						if(wkcxDto.getWkbh().contains("-RNA-")){
							wklx="R";
							//用于物种RNA的限制
							mngsmxjgDto.setRnaflag("1");
						}
						if(wkcxDto.getWkbh().contains("-DNA-")||wkcxDto.getWkbh().contains("-Onco-")||wkcxDto.getWkbh().contains("-XD-")
								||wkcxDto.getWkbh().contains("-HS-")){
							wklx="D";
							//用于物种DNA的限制
							mngsmxjgDto.setDnaflag("1");
						}
						WkcxDto wkcxDtosj=wkcxService.getDto(wkcxDto);
						if(wkcxDto.getWkbh().contains("-DR-")){
							OtherDto otherDto = new OtherDto();
							OtherDto sjxxDto = new OtherDto();
							otherDto.setSjid(wkcxDtosj.getSjid());
							sjxxDto = otherService.getSjxxDto(otherDto);
							if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
								if(sjxxDto.getNbbm().contains("01D")){
									mngsmxjgDto.setDnaflag("1");
									wklx="D";
								}
								if(sjxxDto.getNbbm().contains("01R")){
									mngsmxjgDto.setRnaflag("1");
									wklx="R";
								}
							}
						}else if(wkcxDto.getWkbh().contains("-tNGS")||wkcxDto.getWkbh().contains("-TBtNGS")){
							if(wkcxDto.getWkbh().contains("MDL001")){
								mngsmxjgDto.setRnaflag("1");
								wklx="R";
							}else{
								mngsmxjgDto.setDnaflag("1");
								wklx="D";
		   					}
						}
					}
				}

				WkcxbbDto wkcxbbDto= new WkcxbbDto();
				wkcxbbDto.setTaxid("9606");//人的taxid
				wkcxbbDto.setYblx(wkcxDto.getYblx());
				wkcxbbDto.setJgid(wkcxDto.getJgid());
				wkcxbbDto=wkcxbbService.getDtoByJgid(wkcxbbDto);
				List<MngsmxjgDto> pathogen =new ArrayList<>();
				List<String> pathogenTaxids =new ArrayList<>();
				List<MngsmxjgDto> possible =new ArrayList<>();
				List<String> possibleTaxids =new ArrayList<>();
				List<MngsmxjgDto> background =new ArrayList<>();
				List<String> backgroundTaxids =new ArrayList<>();

				mngsmxjgDto.setBbh(wkcxDto.getShbbbh());
				mngsmxjgDto.setLibrary_type(wkcxDto.getLibrary_type());
				mngsmxjgDto.setWkcxid(wkcxDto.getWkcxid());
				mngsmxjgDto.setWswTaxids(wkcxDto.getZdTaxids());
				mngsmxjgDto.setYblx(wkcxDto.getYblx());
				mngsmxjgDto.setWkbh(wkcxDto.getWkbh());

				//获取文库的测序版本信息，为了得到明细分表的后缀
				wkcxbbDto.setBbh(wkcxDto.getShbbbh());
				wkcxbbDto.setWkcxid(wkcxDto.getWkcxid());
				WkcxbbDto dto = wkcxbbService.getDto(wkcxbbDto);
				if (dto!=null){
					mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
				}
				//查询重点关注微生物信息
				if(wkcxDto.getZdTaxids()!=null&&wkcxDto.getZdTaxids().size()>0){
					List<MngsmxjgDto> pathogenList =mngsmxjgService.getListbyTaxids(mngsmxjgDto);
					if(pathogenList!=null&&pathogenList.size()>0){
						for(MngsmxjgDto dto_t:pathogenList){
							pathogenTaxids.add(dto_t.getTaxid());
							if("Viruses".equals(dto_t.getSp_type())){
								dto_t.setSpecies_category(dto_t.getBdlb());
							}
							if("pathogenic".equals(dto_t.getZbx())){
								dto_t.setHighlight("true");
							}
							//格式化Last_update
							if(StringUtil.isNotBlank(dto_t.getLast_update())){
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = null;
								try {
									date = simpleDateFormat.parse(dto_t.getLast_update());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								long ts = 0;
								if (date != null) {
									ts = date.getTime();
								}
								String res = String.valueOf(ts/1000);  // 转化为字符串
								dto_t.setLast_update(res);
							}
						}
						//放进统一的list
						pathogen.addAll(pathogenList);
					}
				}
				//查询关注微生物信息
				mngsmxjgDto.setWswTaxids(wkcxDto.getPossibleTaxids());
				if(wkcxDto.getPossibleTaxids()!=null&&wkcxDto.getPossibleTaxids().size()>0){
					List<MngsmxjgDto> possibleList =mngsmxjgService.getListbyTaxids(mngsmxjgDto);
					if(possibleList!=null&&possibleList.size()>0){
						for(MngsmxjgDto dto_t:possibleList){
							possibleTaxids.add(dto_t.getTaxid());
							if("Viruses".equals(dto_t.getSp_type())){
								dto_t.setSpecies_category(dto_t.getBdlb());
							}
							if("pathogenic".equals(dto_t.getZbx())){
								dto_t.setHighlight("true");
							}
							//格式化Last_update
							if(StringUtil.isNotBlank(dto_t.getLast_update())){
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = null;
								try {
									date = simpleDateFormat.parse(dto_t.getLast_update());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								long ts = 0;
								if (date != null) {
									ts = date.getTime();
								}
								String res = String.valueOf(ts/1000);  // 转化为字符串
								dto_t.setLast_update(res);
							}
						}
						//放进统一的list
						possible.addAll(possibleList);
					}
				}
				//查询疑似微生物信息
				mngsmxjgDto.setWswTaxids(wkcxDto.getBackgroundTaxids());
				if(wkcxDto.getBackgroundTaxids()!=null&&wkcxDto.getBackgroundTaxids().size()>0){
					List<MngsmxjgDto> backgroundList =mngsmxjgService.getListbyTaxids(mngsmxjgDto);
					if(backgroundList!=null&&backgroundList.size()>0){
						for(MngsmxjgDto dto_t:backgroundList){
							backgroundTaxids.add(dto_t.getTaxid());
							if("Viruses".equals(dto_t.getSp_type())){
								dto_t.setSpecies_category(dto_t.getBdlb());
							}
							//格式化Last_update
							if(StringUtil.isNotBlank(dto_t.getLast_update())){
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = null;
								try {
									date = simpleDateFormat.parse(dto_t.getLast_update());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								long ts = 0;
								if (date != null) {
									ts = date.getTime();
								}
								String res = String.valueOf(ts/1000);  // 转化为字符串
								dto_t.setLast_update(res);
							}
						}
						//放进统一的list
						background.addAll(backgroundList);
					}
				}

				if(ids.size()>1){//当为合并文库发送报告
					//获取被合并的文库测序信息
					WkcxDto hbwkcx=new WkcxDto();
					hbwkcx.setWkcxid(wkcxDto.getHbwkcxid());
					//hbwkcx.setBbh(wkcxDto.getHbbbh());
					WkcxDto dtoNew = wkcxService.getDtoNew(hbwkcx);
					mngsmxjgDto =new MngsmxjgDto();
					mngsmxjgDto.setBbh(dtoNew.getShbbbh());
					mngsmxjgDto.setLibrary_type(wkcxDto.getLibrary_type());
					mngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
					mngsmxjgDto.setWswTaxids(wkcxDto.getZdTaxids());
					mngsmxjgDto.setYblx(dtoNew.getYblx());
					mngsmxjgDto.setWkbh(dtoNew.getWkbh());
					if(!"1".equals(dtoNew.getQxhbbg())){
						if(dtoNew.getWkbh().contains("-RNA-")){
							mngsmxjgDto.setRnaflag("1");
							mngsmxjgDto.setDnaflag("");
						}
						if(dtoNew.getWkbh().contains("-DNA-")||dtoNew.getWkbh().contains("-Onco-")||dtoNew.getWkbh().contains("-XD-")
								||dtoNew.getWkbh().contains("-HS-")){
							mngsmxjgDto.setDnaflag("1");
							mngsmxjgDto.setRnaflag("");
						}
						if(dtoNew.getWkbh().contains("-DR-")){
							OtherDto otherDto = new OtherDto();
							OtherDto sjxxDto = new OtherDto();
							otherDto.setSjid(dtoNew.getSjid());
							sjxxDto = otherService.getSjxxDto(otherDto);
							if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
								if(sjxxDto.getNbbm().contains("01D")){
									mngsmxjgDto.setDnaflag("1");
									mngsmxjgDto.setRnaflag("");
								}
								if(sjxxDto.getNbbm().contains("01R")){
									mngsmxjgDto.setRnaflag("1");
									mngsmxjgDto.setDnaflag("");
								}
							}
						}
						if(dtoNew.getWkbh().contains("-tNGS")||dtoNew.getWkbh().contains("-TBtNGS")){
							if(dtoNew.getWkbh().contains("MDL001")){
								mngsmxjgDto.setRnaflag("1");
							}else {
								mngsmxjgDto.setDnaflag("1");
							}
						}
					}
					//获取被合并的文库测序版本信息，获取被合并文库明细分表的后缀
					wkcxbbDto.setBbh(dtoNew.getShbbbh());
					wkcxbbDto.setWkcxid(dtoNew.getWkcxid());
					WkcxbbDto wkcxbbDto_t = wkcxbbService.getDto(wkcxbbDto);

					if (wkcxbbDto_t!=null){
						if (dto != null) {
							mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
						}
					}
					//查询被合并文库重点关注微生物信息
					if(wkcxDto.getZdTaxids()!=null&&wkcxDto.getZdTaxids().size()>0){
						List<MngsmxjgDto> pathogenList =mngsmxjgService.getListbyTaxids(mngsmxjgDto);
						if(pathogenList!=null&&pathogenList.size()>0){
							for(MngsmxjgDto dto_t:pathogenList){
								if("Viruses".equals(dto_t.getSp_type())){
									dto_t.setSpecies_category(dto_t.getBdlb());
								}
								if("pathogenic".equals(dto_t.getZbx())){
									dto_t.setHighlight("true");
								}
								//格式化Last_update
								if(StringUtil.isNotBlank(dto_t.getLast_update())){
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date date = null;
									try {
										date = simpleDateFormat.parse(dto_t.getLast_update());
									} catch (ParseException e) {
										e.printStackTrace();
									}
									long ts = 0;
									if (date != null) {
										ts = date.getTime();
									}
									String res = String.valueOf(ts/1000);  // 转化为字符串
									dto_t.setLast_update(res);
								}
								//去除重复数据
								if(!pathogenTaxids.contains(dto_t.getTaxid())){
									pathogenTaxids.add(dto_t.getTaxid());
									pathogen.add(dto_t);
								}
							}
						}
					}
					//查询被合并文库关注微生物信息
					mngsmxjgDto.setWswTaxids(wkcxDto.getPossibleTaxids());
					if(wkcxDto.getPossibleTaxids()!=null&&wkcxDto.getPossibleTaxids().size()>0){
						List<MngsmxjgDto> possibleList =mngsmxjgService.getListbyTaxids(mngsmxjgDto);
						if(possibleList!=null&&possibleList.size()>0){
							for(MngsmxjgDto dto_t:possibleList){
								if("Viruses".equals(dto_t.getSp_type())){
									dto_t.setSpecies_category(dto_t.getBdlb());
								}
								if("pathogenic".equals(dto_t.getZbx())){
									dto_t.setHighlight("true");
								}
								//格式化Last_update
								if(StringUtil.isNotBlank(dto_t.getLast_update())){
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date date = null;
									try {
										date = simpleDateFormat.parse(dto_t.getLast_update());
									} catch (ParseException e) {
										e.printStackTrace();
									}
									long ts = 0;
									if (date != null) {
										ts = date.getTime();
									}
									String res = String.valueOf(ts/1000);  // 转化为字符串
									dto_t.setLast_update(res);
								}
								//去除重复数据
								if(!possibleTaxids.contains(dto_t.getTaxid())){
									possibleTaxids.add(dto_t.getTaxid());
									possible.add(dto_t);
								}
							}
						}
					}
					//查询被合并文库疑似微生物信息
					mngsmxjgDto.setWswTaxids(wkcxDto.getBackgroundTaxids());
					if(wkcxDto.getBackgroundTaxids()!=null&&wkcxDto.getBackgroundTaxids().size()>0){

						List<MngsmxjgDto> backgroundList =mngsmxjgService.getListbyTaxids(mngsmxjgDto);
						if(backgroundList!=null&&backgroundList.size()>0){
							for(MngsmxjgDto dto_t:backgroundList){
								if("Viruses".equals(dto_t.getSp_type())){
									dto_t.setSpecies_category(dto_t.getBdlb());
								}
								//格式化Last_update
								if(StringUtil.isNotBlank(dto_t.getLast_update())){
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date date = null;
									try {
										date = simpleDateFormat.parse(dto_t.getLast_update());
									} catch (ParseException e) {
										e.printStackTrace();
									}
									long ts = 0;
									if (date != null) {
										ts = date.getTime();
									}
									String res = String.valueOf(ts/1000);  // 转化为字符串
									dto_t.setLast_update(res);
								}
								//去除重复数据
								if(!backgroundTaxids.contains(dto_t.getTaxid())){
									backgroundTaxids.add(dto_t.getTaxid());
									background.add(dto_t);
								}
							}
						}
					}
				}
				//查询相关文献数据
				List<XgwxDto> xgwxDtos =new ArrayList<>();
				if(wkcxDto.getTaxids()!=null&&wkcxDto.getTaxids().size()>0){
					XgwxDto xgwxDto=new XgwxDto();
					xgwxDto.setIds(wkcxDto.getTaxids());
					xgwxDtos =xgwxService.getDtoListByIds(xgwxDto);
				}
				//查询耐药分析结果数据
				List<NyfxjgDto> drug_resistance_stat=new ArrayList<>();
				if(wkcxDto.getNyfx()!=null&&wkcxDto.getNyfx().size()>0){
					NyfxjgDto nyfxjgDto=new NyfxjgDto();
					nyfxjgDto.setIds(wkcxDto.getNyfx());
					drug_resistance_stat=nyfxjgService.getDtoListByIds(nyfxjgDto);
				}
				//查询临床指南数据
				List<BioLczzznglDto> lczzznglDtos =new ArrayList<>();
				if(wkcxDto.getZnids()!=null&&wkcxDto.getZnids().size()>0){
					BioLczzznglDto lczzznglDto=new BioLczzznglDto();
					lczzznglDto.setIds(wkcxDto.getZnids());
					lczzznglDtos = lczzznglService.getDtoListByIds(lczzznglDto);
				}
				if(lczzznglDtos!=null&&lczzznglDtos.size()>0){
					for(BioLczzznglDto dto_t:lczzznglDtos){
						//格式化Last_update
						if(StringUtil.isNotBlank(dto_t.getLast_update())){
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = null;
							try {
								date = simpleDateFormat.parse(dto_t.getLast_update());
							} catch (ParseException e) {
								e.printStackTrace();
							}
							long ts = 0;
							if (date != null) {
								ts = date.getTime();
							}
							String res = String.valueOf(ts/1000);  // 转化为字符串
							dto_t.setLast_update(res);
						}
					}
				}
				//查询注释信息数据
				List<ZsxxDto> dtoList =new ArrayList<>();
				if(wkcxDto.getZsxxIds()!=null&&wkcxDto.getZsxxIds().size()>0){
					dtoList = zsxxService.getDtoListByIds(wkcxDto.getZsxxIds(),wkcxDto.getWkcxid());
				}
				//查询毒力因子注释数据
				List<BioDlyzzsDto> list=new ArrayList<>();
				if(wkcxDto.getDlzsids()!=null&&wkcxDto.getDlzsids().size()>0){
					//获取前端传过来的毒力因子的ids查询出来
					DlfxjgDto dlfxjgDto=new DlfxjgDto();
					dlfxjgDto.setIds(wkcxDto.getDlzsids());
					List<DlfxjgDto> dlfxjgDtos = dlfxjgService.getDtoList(dlfxjgDto);
					//遍历查出来的毒力因子数据，将每个dto的内容字段（nr）解析出来然后拿里面vfid去毒力因子注释表获取注释
					if(dlfxjgDtos!=null&&dlfxjgDtos.size()>0){
						for(DlfxjgDto dlfxjgDto_t:dlfxjgDtos){
							if(StringUtil.isNotBlank(dlfxjgDto_t.getNr())){
								JSONObject nr=JSONObject.parseObject(dlfxjgDto_t.getNr());
								String dlyz = nr.getString("dlyz");
								if(StringUtil.isNotBlank(dlyz)){
									String vfid="";
									String[] split = dlyz.split("\\(");
									if(split.length>1){
										String[] split_t = split[1].split("\\)");
										vfid=split_t[0];
									}
									if(StringUtil.isNotBlank(vfid)){
										BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
										bioDlyzzsDto.setVfid(vfid);
										BioDlyzzsDto bioDlyzzsDto_t = bioDlyzzsService.getDto(bioDlyzzsDto);
										if(bioDlyzzsDto_t!=null){
											bioDlyzzsDto_t.setGene(nr.getString("dljy"));
											bioDlyzzsDto_t.setVf_name(bioDlyzzsDto_t.getName());
											bioDlyzzsDto_t.setVf_category(bioDlyzzsDto_t.getVfcategory());
											bioDlyzzsDto_t.setSpecies_name(bioDlyzzsDto_t.getWzzwm());
											list.add(bioDlyzzsDto_t);
										}
									}
								}
							}
						}
					}
				}
				//组装报告数据
				jsonObject.put("ybbh",wkcxDto.getYbbh());
				jsonObject.put("detection_type",wklx);
				if(ids.size()>1){
					//如果是合并文库，则将两个文库编号拼接
					StringBuilder wkbh= new StringBuilder();
					List<WkcxDto> listByIds = wkcxService.getListByIds(wkcxDto);
					for(WkcxDto dto_t:listByIds){
						wkbh.append("+").append(dto_t.getWkbh());
					}
					wkbh= new StringBuilder(wkbh.substring(1));
					jsonObject.put("sample_id", wkbh.toString());
				}else{
					jsonObject.put("sample_id",wkcxDto.getWkbh());
				}
				jsonObject.put("q20",wkcxDto.getCleanq20());
				jsonObject.put("q30",wkcxDto.getCleanq30());
				jsonObject.put("gc",wkcxDto.getCleangc());
				jsonObject.put("total_reads",wkcxDto.getTotalreads());
				jsonObject.put("raw_reads",wkcxDto.getRawreads());
				jsonObject.put("homo_percentage",wkcxDto.getHomo());
				jsonObject.put("microbial_reads",wkcxDto.getViruses());
				jsonObject.put("host_index",wkcxDto.getRyzs());
				jsonObject.put("host_percentile",wkcxDto.getRypw());
				jsonObject.put("host_position",wkcxbbDto.getHost_position());
				jsonObject.put("spike_rpm",wkcxDto.getSpikeinrpm());
				jsonObject.put("audit_comment",wkcxDto.getShbz());
				jsonObject.put("status","success");
				JSONArray array= JSONArray.parseArray(JSON.toJSONString(pathogen));
				jsonObject.put("pathogen",array);
				array= JSONArray.parseArray(JSON.toJSONString(possible));
				jsonObject.put("possible",array);
				array= JSONArray.parseArray(JSON.toJSONString(background));
				jsonObject.put("background",array);
				array= JSONArray.parseArray(JSON.toJSONString(drug_resistance_stat));
				jsonObject.put("drug_resistance_stat",array);
				array= JSONArray.parseArray(JSON.toJSONString(xgwxDtos));
				jsonObject.put("papers",array);
				jsonObject.put("jyry",wkcxDto.getJyry());
				jsonObject.put("shry",wkcxDto.getShry());
				array= JSONArray.parseArray(JSON.toJSONString(lczzznglDtos));
				jsonObject.put("guidelines",array);
				array= JSONArray.parseArray(JSON.toJSONString(dtoList));
				jsonObject.put("drug_resist_gene",array);
				array= JSONArray.parseArray(JSON.toJSONString(list));
				jsonObject.put("virulence_factor_stat",array);
				RestTemplate restTemplate = new RestTemplate();
				jsonObject.put("mngsFlag","Mngs_flag");
				if("0".equals(wkcxDto.getSfqry())){
					jsonObject.put("auto_rem_off","false");
				}
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_SEQUENCING_IMG.getCode());
				fjcfbDto.setYwid(wkcxDto.getWkcxid());
				FjcfbDto fjcfbDto_t = fjcfbService.getDto(fjcfbDto);
				if(fjcfbDto_t==null){
					map.put("status","fail");
					map.put("message","未获取到图片信息！");
				}else{
					DBEncrypt dbEncrypt=new DBEncrypt();
					String filePath=dbEncrypt.dCode(fjcfbDto_t.getWjlj());
					FileSystemResource resource = new FileSystemResource(new File(filePath));
					paramMap.add("file", resource);
					paramMap.add("sample_result", JSON.toJSONString(jsonObject));
					String url=applicationurl+"/ws/pathogen/receiveInspectionGenerateReportSec?userid="+user.getYhm();
					map=  restTemplate.postForObject(url, paramMap, Map.class);
					String status = null;
					if (map != null) {
						status = (String)map.get("status");
					}
					if("success".equals(status)){
						//成功，如果是合并，则更新两个文库的状态，不是则更新那一个文库的状态
						WkcxDto wkcxDto_t=new WkcxDto();
						wkcxDto_t.setZt("03");
						wkcxDto_t.setWkcxid(wkcxDto.getBtwkcxid());
						wkcxService.updateDto(wkcxDto_t);
						if(ids.size()>1){
							wkcxDto_t.setZt("03");
							wkcxDto_t.setWkcxid(wkcxDto.getHbwkcxid());
							wkcxService.updateDto(wkcxDto_t);
						}

					}
				}
			}
		}catch (Exception e){
			map.put("status","fail");
			map.put("message",e.getMessage());
		}finally {
			//解锁
			lock.unlock();
		}
		return map;
	}

	/**
	 * 撤回审核
	 */
	@RequestMapping("/review/pagedataRevertInfo")
	@ResponseBody
	public Map<String, Object> pagedataRevertInfo(WkcxDto wkcxDto) {
		Map<String, Object> map = new HashMap<>();
		wkcxDto.setZt("01");
		wkcxDto.setQxhbbg("0");
		wkcxDto.setSfcxsh("1");
		boolean isSuccess = wkcxService.updateDto(wkcxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"撤回成功！":"撤回失败！");
		return map;
	}


	/**
	 * Mngs数据导出（耐药，毒力，导出，AI）
	 * dcszModel.ywid  必须  如PROJECT
	 * dcszModel.sfbc  是否保存个人设置	Y:保存
	 * dcszModel.fileType  0：excel  1：cvs
	 * choseList  已选字段
	 */
	@RequestMapping(value="/export/pagedataMngsExport")
	@ResponseBody
	public Map<String,Object> pagedataMngsExport(DcszDto dcszDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			ProcessModel pModel = dcszService.mngsExport(dcszDto,request);
			map.put("result", true);
			map.put("totalCount",pModel.getCurrentCount());
			map.put("wjid",pModel.getWjid());
		} catch (BusinessException e) {
			//logException(e);
			map.put("result", false);
			map.put("msg", e.getMsgId());
		} catch (Exception e) {
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 模型结果导出
	 */
	@RequestMapping(value="/export/pagedataModalExport", method = RequestMethod.GET)
	@ResponseBody
	public void pagedataModalExport(DcszDto dcszDto,HttpServletResponse response){
		String fileName=dcszDto.getHzxm()+"-"+dcszDto.getYblxmc();
		// 第 1 个 Sheet 页
		List<List<Object>> sheet1 = new ArrayList<>();
		List<Object> sheet1Head = new ArrayList<>();
		sheet1Head.add("SampleID");
		sheet1Head.add("RawReads");
		sheet1Head.add("CleanReads");
		sheet1Head.add("Q30");
		sheet1Head.add("HostRatio");
		sheet1.add(sheet1Head);
		WkcxDto wkcxDto=new WkcxDto();
		wkcxDto.setWkcxid(dcszDto.getIds().get(0));
		List<WkcxDto> list = wkcxService.getListForExp(wkcxDto);
		for(WkcxDto dto:list){
			if(StringUtil.isNotBlank(dto.getQc())){
				JSONObject object = JSONObject.parseObject(dto.getQc());
				dto.setCleanreads(object.get("cleanreads").toString());
			}
		}
		WkcxbbDto wkcxbbDto=new WkcxbbDto();
		wkcxbbDto.setBbh(dcszDto.getIds().get(1));
		wkcxbbDto.setWkcxid(dcszDto.getIds().get(0));
		WkcxbbDto dto = wkcxbbService.getDto(wkcxbbDto);
		if (dto!=null){
			MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
			mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
			mngsmxjgDto.setJgid(dto.getJgid());
			mngsmxjgDto.setTaxid("9606");
			MngsmxjgDto mngsmxjgDto_t = mngsmxjgService.getDto(mngsmxjgDto);
			if(mngsmxjgDto_t!=null){
				BigDecimal readsaccum=new BigDecimal(mngsmxjgDto_t.getReadsaccum());
				BigDecimal bigDecimal=new BigDecimal(100);
				if(StringUtil.isNotBlank(dto.getTotalreads())){
					BigDecimal totalReads=new BigDecimal(dto.getTotalreads());
					for(WkcxDto wkcxDto_t:list){
						wkcxDto_t.setHostratio((readsaccum.multiply(bigDecimal)).divide(totalReads, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) +"%");
					}
				}
			}
		}
		for(WkcxDto wkcxDto_t:list){
			List<Object> row = new ArrayList<>();
			row.add(wkcxDto_t.getYbbh());
			row.add(wkcxDto_t.getRawreads());
			row.add(wkcxDto_t.getCleanreads());
			row.add(wkcxDto_t.getCleanq30());
			row.add(wkcxDto_t.getHostratio());
			sheet1.add(row);
		}
		// 第 2 个 Sheet 页
		List<List<Object>> sheet2 = new ArrayList<>();
		List<Object> sheet2Head = new ArrayList<>();
		sheet2Head.add("TaxID");
		sheet2Head.add("Name");
		sheet2Head.add("ChineseName");
		sheet2Head.add("Rank");
		sheet2Head.add("ReadsAccum");
		sheet2Head.add("RPM");
		sheet2Head.add("Ratio(%)");
		sheet2Head.add("Coverage(%)");
		sheet2Head.add("GenusName");
		sheet2Head.add("GenusReadsAccum");
		sheet2Head.add("Abundance");
		sheet2Head.add("Comment");
		sheet2.add(sheet2Head);
		MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
		List<MngsmxjgDto> bylist =new ArrayList<>();
		List<MngsmxjgDto> newList =new ArrayList<>();
		if (dto!=null){
			mngsmxjgDto.setLrsj(dto.getLrsj().substring(0,4)+dto.getLrsj().substring(5,7));
			mngsmxjgDto.setJgid(dto.getJgid());
			String px="Bacteria,Fungi,Viruses,Parasite";
			String[] split = px.split(",");
			bylist = mngsmxjgService.getExportList(mngsmxjgDto);
			for(MngsmxjgDto mngsmxjgDto_t:bylist){
				if (StringUtil.isNotBlank(mngsmxjgDto_t.getFgdxx())){
					JSONObject jsonObject = JSONObject.parseObject(mngsmxjgDto_t.getFgdxx());
					BigDecimal b2 = new BigDecimal("10000");
					BigDecimal value = new BigDecimal(jsonObject.get("coverage").toString()).multiply(b2).setScale(2, RoundingMode.HALF_UP);
					if (0 ==value.compareTo(new BigDecimal(0))){
						mngsmxjgDto_t.setCov("NA");
					}else {
						if (value.compareTo(new BigDecimal(1)) < 0){
							mngsmxjgDto_t.setCov("<0.01%");
						}else{
							mngsmxjgDto_t.setCov(value.divide(new BigDecimal(100),2,RoundingMode.HALF_UP)+"%");
						}
					}

				}
				BigDecimal readsaccum=new BigDecimal(mngsmxjgDto_t.getReadsaccum());
				BigDecimal bigDecimal=new BigDecimal(100);
				if(StringUtil.isNotBlank(dto.getTotalreads())){
					BigDecimal totalReads=new BigDecimal(dto.getTotalreads());
					mngsmxjgDto_t.setRatio(String.valueOf((readsaccum.multiply(bigDecimal)).divide(totalReads,2,RoundingMode.HALF_UP))+"%");
				}
			}
			for(String s:split){
				for(MngsmxjgDto mngsmxjgDto_t:bylist){
					if(s.equals(mngsmxjgDto_t.getWzfl())){
						newList.add(mngsmxjgDto_t);
					}
				}
			}
			for(MngsmxjgDto mngsmxjgDto_t:bylist){
				if(!px.contains(mngsmxjgDto_t.getWzfl())){
					newList.add(mngsmxjgDto_t);
				}
			}
		}
		for(MngsmxjgDto mngsmxjgDto_t:bylist){
			List<Object> row = new ArrayList<>();
			row.add(mngsmxjgDto_t.getTaxid());
			row.add(mngsmxjgDto_t.getXm());
			row.add(mngsmxjgDto_t.getZwmm());
			row.add(mngsmxjgDto_t.getFldj());
			row.add(mngsmxjgDto_t.getReadsaccum());
			row.add(mngsmxjgDto_t.getRpq());
			row.add(mngsmxjgDto_t.getRatio());
			row.add(mngsmxjgDto_t.getCov());
			row.add(mngsmxjgDto_t.getFxm());
			row.add(mngsmxjgDto_t.getFreadsaccum());
			row.add(mngsmxjgDto_t.getAbd());
			row.add(mngsmxjgDto_t.getWzzs());
			sheet2.add(row);
		}
		// 第 3 个 Sheet 页
		List<List<Object>> sheet3 = new ArrayList<>();
		List<Object> sheet3Head = new ArrayList<>();
		sheet3Head.add("检出耐药基因");
		sheet3Head.add("基因分型");
		sheet3Head.add("序列数");
		sheet3Head.add("主要机制");
		sheet3Head.add("耐药类型");
		sheet3Head.add("主要来源物种");
		sheet3.add(sheet3Head);
		NyfxjgDto nyfxjgDto=new NyfxjgDto();
		nyfxjgDto.setWkcxid(dcszDto.getIds().get(0));
		nyfxjgDto.setBbh(dcszDto.getIds().get(1));
		List<NyfxjgDto> nyfxjgDtos = nyfxjgService.getExportList(nyfxjgDto);
		if(nyfxjgDtos!=null&&nyfxjgDtos.size()>0){
			List<String> mcs=new ArrayList<>();
			for(NyfxjgDto nyfxjgDto_t:nyfxjgDtos){
				if(StringUtil.isNotBlank(nyfxjgDto_t.getNr())){
					NyfxjgDto nyfxjgDto1= JSON.parseObject(nyfxjgDto_t.getNr(),NyfxjgDto.class);
					if(StringUtil.isNotBlank(nyfxjgDto1.getNyjzu())){
						mcs.add(nyfxjgDto1.getNyjzu());
					}
				}
			}
			if(mcs.size()>0){
				ZsxxDto zsxxDto = new ZsxxDto();
				zsxxDto.setMcs(mcs);
				List<ZsxxDto> dtoList = zsxxService.getDtoList(zsxxDto);
				if(dtoList!=null&&dtoList.size()>0){
					for(NyfxjgDto nyfxjgDto_t:nyfxjgDtos){
						NyfxjgDto nyfxjgDto1= JSON.parseObject(nyfxjgDto_t.getNr(),NyfxjgDto.class);
						for(ZsxxDto zsxxDto_t:dtoList){
							if(zsxxDto_t.getMc().equals(nyfxjgDto1.getNyjzu())){
								nyfxjgDto_t.setKnwzly(zsxxDto_t.getKnwzly());
							}
						}
					}
				}
			}
			for(NyfxjgDto nyfxjgDto_t:nyfxjgDtos){
				if(StringUtil.isNotBlank(nyfxjgDto_t.getNr())){
					NyfxjgDto nyfxjgDto1= JSON.parseObject(nyfxjgDto_t.getNr(),NyfxjgDto.class);
					List<Object> row = new ArrayList<>();
					row.add(nyfxjgDto1.getNyjzu());
					row.add(nyfxjgDto1.getTopjy());
					String topjydjcxx = nyfxjgDto1.getTopjydjcxx();
					if(StringUtil.isNotBlank(topjydjcxx)){
						StringBuilder str= new StringBuilder();
						String[] split = topjydjcxx.split(";");
						for(String s:split){
							String[] split1 = s.split(":");
							str.append(";").append(split1[1]);
						}
						if(StringUtil.isNotBlank(str.toString())){
							str = new StringBuilder(str.substring(1));
						}
						row.add(str.toString());
					}else{
						row.add("");
					}
					row.add(nyfxjgDto1.getNyjz());
					row.add(nyfxjgDto1.getNylb());
					row.add(nyfxjgDto_t.getKnwzly());
					sheet3.add(row);
				}
			}
		}
		// 将两个 Sheet 页添加到集合中
		Map<String, List<List<Object>>> sheets = new LinkedHashMap<>();
		sheets.put("样本信息", sheet1);
		sheets.put("病原信息", sheet2);
		sheets.put("耐药基因", sheet3);
		// 导出数据
		ExcelUtils.exportManySheet(response, fileName, sheets);
	}

	/**
	 * 操作Blast
	 */
	@RequestMapping("/information/pagedataOperationBlast")
	@ResponseBody
	public Map<String, Object> pagedataOperationBlast(WkcxDto wkcxDto,HttpServletResponse response) {
		return mngsFileParsingService.getBlast(wkcxDto,response);
	}


}
