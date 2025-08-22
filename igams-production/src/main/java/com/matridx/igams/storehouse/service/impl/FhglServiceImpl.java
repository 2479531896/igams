package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.DispatchListDto;
import com.matridx.igams.production.dao.entities.VoucherHistoryDto;
import com.matridx.igams.production.dao.matridxsql.DispatchListDao;
import com.matridx.igams.production.dao.matridxsql.VoucherHistoryDao;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.DkjejlDto;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.FhglModel;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.XsglDto;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.post.IFhglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkglService;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import com.matridx.igams.storehouse.service.svcinterface.IDhxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDkjejlService;
import com.matridx.igams.storehouse.service.svcinterface.IFhglService;
import com.matridx.igams.storehouse.service.svcinterface.IFhmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IKhglService;
import com.matridx.igams.storehouse.service.svcinterface.IXsglService;
import com.matridx.igams.storehouse.service.svcinterface.IXsmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FhglServiceImpl extends BaseBasicServiceImpl<FhglDto, FhglModel, IFhglDao> implements IFhglService, IAuditService {
	@Autowired
	VoucherHistoryDao voucherHistoryDao;
	@Autowired
    IHwxxService hwxxService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ICkglService ckglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;

    @Autowired
    ICommonService commonService;
    @Autowired
    ICkmxService ckmxService;
    @Autowired
    DispatchListDao dispatchListDao;
    @Autowired
    IFhmxService fhmxService;
    @Autowired
	IRdRecordService rdRecordService;
	@Autowired
    IKhglService khglService;
	@Autowired
	IDhxxService dhxxService;
	@Autowired
	ICkhwxxService ckhwxxService;
	@Autowired
    IXsmxService xsmxService;
	@Autowired
    IDdxxglService ddxxglService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IXsglService xsglService;
    @Autowired
    IDkjejlService dkjejlService;
    @Value("${matridx.rabbit.systemreceiveflg:}")
    private String systemreceiveflg;
    @Value("${sqlserver.matridxds.flag:}")
    private String matridxdsflag;
    private final Logger log = LoggerFactory.getLogger(FhglServiceImpl.class);
    /**
     * 获取单条数据
     */
    public FhglDto getDtoByid(FhglDto fhglDto){
        return dao.getDtoByid(fhglDto);
    }

    /**
	 * 获取可退货列表 
	 * @param fhglDto
	 * @return
	 */
	@Override
	public List<FhglDto> getPagedDtoKThList(FhglDto fhglDto) {
		return dao.getPagedDtoKThList(fhglDto);
	}
	
	@Override
    public String getFhglFhdhh() {
        String yearLast = new SimpleDateFormat("yyyyMMdd", Locale.CHINESE).format(new Date().getTime());
        String prefix = "FH-" + yearLast+"-";
        // 查询流水号
        String serial = dao.getFhdhSerial(prefix);
        return prefix + serial;
    }
    @Override
	public boolean isFhdhRepeatU8(FhglDto fhglDto) {
    	DispatchListDto dispatchListDto = new DispatchListDto();
		dispatchListDto.setcDLCode(fhglDto.getFhdh());
		DispatchListDto dispatchListDto_t = dispatchListDao.getDtoByDh(dispatchListDto);
        return dispatchListDto_t == null;
    }

    @Override
    public List<FhglDto> isFhdhRepeat(FhglDto fhglDto) {
        return dao.getDtoListByFhdh(fhglDto);
    }

    /**
   	 * 生成单号
   	 * @param
   	 * @return
   	 */
	@Override
	public String GenerateNumber() {
		String generateNumber;
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("01");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			String serial = "0000000000"+ serialnum;
			serial = serial.substring(serial.length()-10);
			//判断单号是否存在
			int ccode_new=0;
			DispatchListDto dispatchListDto = new DispatchListDto();
			dispatchListDto.setcDLCode(serial);
			DispatchListDto dispatchListDto_t = dispatchListDao.getDtoByDh(dispatchListDto);			
			if(dispatchListDto_t!=null) {
				List<DispatchListDto> dlList = dispatchListDao.getcCode(dispatchListDto_t);
				ccode_new = Integer.parseInt(dispatchListDto_t.getcDLCode()) + 1;
				for (DispatchListDto dispatchListDto_c : dlList) {
					if(ccode_new-Integer.parseInt(dispatchListDto_c.getcDLCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}				
			}
			if(ccode_new!=0) {
				generateNumber = ("000000000"+ccode_new).substring(serial.length()-10,serial.length());
		    }else {
		    	generateNumber=serial;
		    }    		
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			generateNumber = "0000000001";
			voucherHistoryDao.insert(voucherHistoryDto);
		}
		
		return generateNumber;
	}
	
	/**
   	 * 发货保存
   	 * @param fhglDto
   	 * @return
   	 */
	@Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean shipFhAddSave(FhglDto fhglDto) throws BusinessException {
		fhglDto.setFhid(StringUtil.generateUUID());
	    Boolean success;
        KhglDto khglDto = new KhglDto();
        khglDto.setKhdm(fhglDto.getKhdm());
        khglDto = khglService.getKhglInfoByKhdm(khglDto);
        if (null == khglDto){
            throw new BusinessException("msg","客户信息存在异常！");
        }
        fhglDto.setKh(khglDto.getKhid());
        List<FhmxDto> fhmxDtos = JSON.parseArray(fhglDto.getXsmx_json(), FhmxDto.class);
        //同过hwid 查找hwxx表数据
        HwxxDto hwxxDto = new HwxxDto();
        List<String> hwids= new ArrayList<>();
        for (FhmxDto fhmx:fhmxDtos) {
            fhmx.setLrry(fhglDto.getLrry());
            hwids.add(fhmx.getHwid());
        }
        hwxxDto.setIds(hwids);
        List<HwxxDto> hwxxDtos = hwxxService.getCkInfo(hwxxDto);
        JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setJclb("DELIVERY_TYPE");
        jcsjDto.setCsdm("XSCK");
        jcsjDto = jcsjService.getDto(jcsjDto);
        if(jcsjDto==null) {
        	throw new BusinessException("msg","未找到出库类型销售出库！");
        }
        List<XsmxDto> xsmxDtos = new ArrayList<>();
        for (FhmxDto fhmxDto : fhmxDtos) {
            XsmxDto xsmxDto = new XsmxDto();
            xsmxDto.setXsmxid(fhmxDto.getXsmxid());
            xsmxDto.setYfhsl(fhmxDto.getFhsl());
            xsmxDto.setFhbj("1");
            xsmxDtos.add(xsmxDto);
        }
        //存出库数据
        List<CkglDto> ckglDtos = new ArrayList<>();
//        存出库明细表数据
        List<CkmxDto> ckmxDtos = new ArrayList<>();
//        存仓库货物信息数据
//        List<CkhwxxDto> ckhwxxs = new ArrayList<>();
        //存货物信息数据
        List<HwxxDto> hwxxs = new ArrayList<>();
        //存ckid
        StringBuilder ids = new StringBuilder();
//        生成出库单 生成规则: CK-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "CK" + "-" + date + "-";
        String serial = ckglService.generateDjh(prefix);
        int serial_int = Integer.parseInt(serial)-1;
        List<CkhwxxDto> ckhwxxDtoList = new ArrayList<>();
        for (HwxxDto hwxxInfo:hwxxDtos) {
            CkglDto ckglDto = new CkglDto();
            ckglDto.setCkid(StringUtil.generateUUID()); //出库id
            ids.append(",").append(ckglDto.getCkid());
            ckglDto.setBm(fhglDto.getXsbm()); //部门
            ckglDto.setFlr(fhglDto.getJsr()); //经手人
            ckglDto.setCk(hwxxInfo.getCkid()); //出库仓库
            serial_int = serial_int +1;
            String ckdh="00"+ serial_int;
            ckglDto.setCkdh(prefix+ckdh.substring(ckdh.length()-3)); //出库单号
            ckglDto.setCklb(jcsjDto.getCsid()); //出库类别
            ckglDto.setZt("80"); //状态
            ckglDto.setLrry(fhglDto.getLrry()); //录入人员
            ckglDto.setCkrq(fhglDto.getDjrq()); //出库日期
            ckglDto.setFhid(fhglDto.getFhid());//发货id
            ckglDtos.add(ckglDto);
            
            for (FhmxDto fhmx : fhmxDtos) {
				if(fhmx.getCk().equals(hwxxInfo.getCkid())) {
//					组装出库明细数据
					CkmxDto ckmx = new CkmxDto();
					ckmx.setCkid(ckglDto.getCkid()); //出库id
					ckmx.setCkmxid(StringUtil.generateUUID()); //生成明细id
                    fhmx.setCkid(ckglDto.getCkid());
					fhmx.setCkmxid(ckmx.getCkmxid()); //发货明细存入关联id
					fhmx.setFhid(fhglDto.getFhid()); //存入发货id
					fhmx.setFhmxid(StringUtil.generateUUID()); //生成明细id
					ckmx.setHwid(fhmx.getHwid()); //货物id
					ckmx.setCksl(fhmx.getFhsl()); //出库数量
					ckmx.setLrry(fhglDto.getLrry()); //录入人员
					ckmxDtos.add(ckmx);
					
					//组装货物信息数据
					HwxxDto hwxx = new HwxxDto();
					hwxx.setHwid(fhmx.getHwid()); //货物id
					hwxx.setYds(fhmx.getFhsl()); //发货数量
                    hwxx.setYdsbj("1");
					hwxx.setXgry(fhglDto.getLrry()); //修改人员
					hwxxs.add(hwxx);

                    CkhwxxDto ckhwxxDto = new CkhwxxDto();
                    ckhwxxDto.setCkid(fhmx.getCk());
                    ckhwxxDto.setWlid(fhmx.getWlid());
                    ckhwxxDto.setYdsbj("1");
                    ckhwxxDto.setYds(fhmx.getFhsl());
                    ckhwxxDtoList.add(ckhwxxDto);
				}           	
			}
        }
        success = ckglService.insertCkgls(ckglDtos);
        if (!success){
            throw new BusinessException("msg","添加出库数据出错！");
        }

        success = ckmxService.insertckmxs(ckmxDtos);
        if (!success){
            throw new BusinessException("msg","添加出库明细数据出错！");
        }
        if(!CollectionUtils.isEmpty(ckhwxxDtoList)){
            success = ckhwxxService.updateCkhwxxYds(ckhwxxDtoList);
            if (!success){
                throw new BusinessException("msg","修改ckhwxx预定数出错！");
            }
        }
        //新增发货单
        fhglDto.setScbj("0");
        fhglDto.setSfbj("1");
        fhglDto.setZt("00");
        ids = new StringBuilder(ids.substring(1));
        fhglDto.setCkid(ids.toString());
        success = dao.insert(fhglDto)!=0;
        if (!success){
            throw new BusinessException("msg","添加发货数据出错！");
        }
        
        //保存发货明细
        success = fhmxService.insertList(fhmxDtos);
        if(!success)
        	throw new BusinessException("msg","发货明细新增失败！");
        
        //更新库存
        success = hwxxService.updateHwxxDtos(hwxxs);
        if (!success){
            throw new BusinessException("msg","货物库存数修改失败！");
        }
        success = xsmxService.updateXsmxList(xsmxDtos);
        if (!success){
            throw new BusinessException("msg","销售明细表修改！");
        }
		return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean shipFhModSave(FhglDto fhglDto) throws BusinessException {
        Boolean success;
        KhglDto khglDto = new KhglDto();
        khglDto.setKhdm(fhglDto.getKhdm());
        khglDto = khglService.getKhglInfoByKhdm(khglDto);
        if (null == khglDto){
            throw new BusinessException("msg","客户信息存在异常！");
        }
        fhglDto.setKh(khglDto.getKhid());
        List<FhmxDto> fhmxDtos = JSON.parseArray(fhglDto.getXsmx_json(), FhmxDto.class);
        //同过hwid 查找hwxx表数据
        HwxxDto hwxxDto = new HwxxDto();

        //存货物信息数据
        List<HwxxDto> hwxxs = new ArrayList<>();
        List<FhmxDto> fhmxDtoList = new ArrayList<>();

        if (StringUtil.isNotBlank(fhglDto.getFhid())){
            //查询出之前的发货数量
            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setFhid(fhglDto.getFhid());
            fhmxDtoList = fhmxService.getDtoAllByFhid(fhmxDto);
            //删除发货明细
            success = fhmxService.delete(fhmxDto);
            if(!success)
                throw new BusinessException("msg","发货明细更新失败！");
            //处理出库管理表数据
            FhglDto dtoByid = dao.getDtoByid(fhglDto);
            if (StringUtil.isNotBlank(dtoByid.getCkid())){
                CkglDto ckglDto = new CkglDto();
                ckglDto.setIds(dtoByid.getCkid());
                ckglDto.setScry(fhglDto.getXgry());
                ckglDto.setScbj("1");
                success = ckglService.delete(ckglDto);
                if(!success)
                    throw new BusinessException("msg","出库信息更新失败！");
                CkmxDto ckmxDto = new CkmxDto();
                ckmxDto.setIds(dtoByid.getCkid());
                ckmxDto.setScry(fhglDto.getXgry());
                ckmxDto.setScbj("1");
                success = ckmxService.deleteByCkid(ckmxDto);
                if(!success)
                    throw new BusinessException("msg","出库明细信息更新失败！");
            }

        }
        List<XsmxDto> xsmxDtos = new ArrayList<>();
        List<String> hwids_befor= new ArrayList<>();
        for (FhmxDto fhmxDto : fhmxDtoList) {
            hwids_befor.add(fhmxDto.getHwid());
            XsmxDto xsmxDto = new XsmxDto();
            xsmxDto.setXsmxid(fhmxDto.getXsmxid());
            xsmxDto.setYfhsl(fhmxDto.getFhsl());
            xsmxDto.setFhbj("0");
            xsmxDtos.add(xsmxDto);
        }
        hwxxDto.setIds(hwids_befor);
        List<HwxxDto> hwxxByids = hwxxService.getHwxxByids(hwxxDto);
        List<CkhwxxDto> ckhwxxDtoList = new ArrayList<>();
        for (FhmxDto fhmxDto : fhmxDtoList) {
            for (HwxxDto hwxxInfo:hwxxByids) {
                if (fhmxDto.getHwid().equals(hwxxInfo.getHwid())){
                    HwxxDto hwxx = new HwxxDto();
                    hwxx.setHwid(fhmxDto.getHwid()); //货物id
                    hwxx.setYds(fhmxDto.getFhsl()); //发货数量
                    hwxx.setYdsbj("0");
                    hwxx.setXgry(fhglDto.getXgry()); //修改人员
                    hwxxs.add(hwxx);

                    CkhwxxDto ckhwxxDto = new CkhwxxDto();
                    ckhwxxDto.setCkid(fhmxDto.getCk());
                    ckhwxxDto.setWlid(fhmxDto.getWlid());
                    ckhwxxDto.setYdsbj("0");
                    ckhwxxDto.setYds(fhmxDto.getFhsl());
                    ckhwxxDtoList.add(ckhwxxDto);
                }
            }
        }
        //更新库存
        success = hwxxService.updateHwxxDtos(hwxxs);
        if (!success){
            throw new BusinessException("msg","货物库存数修改失败！");
        }
        success = xsmxService.updateXsmxList(xsmxDtos);
        if (!success){
            throw new BusinessException("msg","销售明细表修改失败！");
        }
        if(!CollectionUtils.isEmpty(ckhwxxDtoList)){
            success = ckhwxxService.updateCkhwxxYds(ckhwxxDtoList);
            if (!success){
                throw new BusinessException("msg","ckhwxx修改失败！");
            }
        }
        hwxxs = new ArrayList<>();
        xsmxDtos = new ArrayList<>();
        List<String> hwids= new ArrayList<>();
        for (FhmxDto fhmx:fhmxDtos) {
            fhmx.setLrry(fhglDto.getXgry());
            hwids.add(fhmx.getHwid());
        }
        hwxxDto.setIds(hwids);
        List<HwxxDto> hwxxDtos = hwxxService.getCkInfo(hwxxDto);
        //存ckid
        StringBuilder ids = new StringBuilder();
//        生成出库单 生成规则: CK-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "CK" + "-" + date + "-";
        String serial = ckglService.generateDjh(prefix);
        int serial_int = Integer.parseInt(serial)-1;
        JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setJclb("DELIVERY_TYPE");
        jcsjDto.setCsdm("XSCK");
        jcsjDto = jcsjService.getDto(jcsjDto);
        if(jcsjDto==null) {
            throw new BusinessException("msg","未找到出库类型销售出库！");
        }
        //存出库数据
        List<CkglDto> ckglDtos = new ArrayList<>();
        List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
//        存出库明细表数据
        List<CkmxDto> ckmxDtos = new ArrayList<>();
        for (FhmxDto fhmx : fhmxDtos) {
            XsmxDto xsmxDto = new XsmxDto();
            xsmxDto.setXsmxid(fhmx.getXsmxid());
            xsmxDto.setYfhsl(fhmx.getFhsl());
            xsmxDto.setFhbj("1");
            xsmxDtos.add(xsmxDto);
        }
        for (HwxxDto hwxxInfo:hwxxDtos) {
            CkglDto ckglDto = new CkglDto();
            ckglDto.setCkid(StringUtil.generateUUID()); //出库id
            ids.append(",").append(ckglDto.getCkid());
            ckglDto.setBm(fhglDto.getXsbm()); //部门
            ckglDto.setFlr(fhglDto.getJsr()); //经手人
            ckglDto.setCk(hwxxInfo.getCkid()); //出库仓库
            serial_int = serial_int +1;
            String ckdh="00"+ serial_int;
            ckglDto.setCkdh(prefix+ckdh.substring(ckdh.length()-3)); //出库单号
            ckglDto.setCklb(jcsjDto.getCsid()); //出库类别
            ckglDto.setZt("80"); //状态
            ckglDto.setLrry(fhglDto.getXgry()); //录入人员
            ckglDto.setCkrq(fhglDto.getDjrq()); //出库日期
            ckglDto.setFhid(fhglDto.getFhid()); //出库日期
            ckglDtos.add(ckglDto);
            for (FhmxDto fhmx : fhmxDtos) {

                if(fhmx.getCk().equals(hwxxInfo.getCkid())) {
                    CkmxDto ckmx = new CkmxDto();
                    ckmx.setCkid(ckglDto.getCkid()); //出库id
                    ckmx.setCkmxid(StringUtil.generateUUID()); //生成明细id
                    ckmx.setHwid(fhmx.getHwid()); //货物id
                    ckmx.setCksl(fhmx.getFhsl()); //出库数量
                    ckmx.setLrry(fhglDto.getXgry()); //录入人员
                    ckmxDtos.add(ckmx);

                    fhmx.setCkid(ckglDto.getCkid());
                    fhmx.setCkmxid(ckmx.getCkmxid()); //发货明细存入关联id
                    fhmx.setFhid(fhglDto.getFhid()); //存入发货id
                    fhmx.setFhmxid(StringUtil.generateUUID()); //生成明细id
                    //组装货物信息数据
                    HwxxDto hwxx = new HwxxDto();
                    hwxx.setHwid(fhmx.getHwid()); //货物id
                    hwxx.setYds(fhmx.getFhsl()); //发货数量
                    hwxx.setYdsbj("1");
                    hwxx.setXgry(fhglDto.getXgry()); //修改人员
                    hwxxs.add(hwxx);

                    CkhwxxDto ckhwxxDto = new CkhwxxDto();
                    ckhwxxDto.setCkid(fhmx.getCk());
                    ckhwxxDto.setWlid(fhmx.getWlid());
                    ckhwxxDto.setYdsbj("1");
                    ckhwxxDto.setYds(fhmx.getFhsl());
                    ckhwxxDtos.add(ckhwxxDto);
                }
            }
        }
        ids = new StringBuilder(ids.substring(1));
        fhglDto.setCkid(ids.toString());
        dao.update(fhglDto);
        success = ckglService.insertCkgls(ckglDtos);
        if (!success){
            throw new BusinessException("msg","添加出库数据出错！");
        }
        success = ckmxService.insertckmxs(ckmxDtos);
        if (!success){
            throw new BusinessException("msg","添加出库明细数据出错！");
        }
        //保存发货明细
        success = fhmxService.insertList(fhmxDtos);
        if(!success)
            throw new BusinessException("msg","发货明细更新失败！");
        //更新库存
        success = hwxxService.updateHwxxDtos(hwxxs);
        if (!success){
            throw new BusinessException("msg","货物库存数修改失败！");
        }
        success = xsmxService.updateXsmxList(xsmxDtos);
        if (!success){
            throw new BusinessException("msg","销售明细表修改！");
        }
        if(!CollectionUtils.isEmpty(ckhwxxDtos)){
            success = ckhwxxService.updateCkhwxxYds(ckhwxxDtos);
            if (!success){
                throw new BusinessException("msg","ckhwxx修改失败！");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void shipFhDelSave(FhglDto fhglDto) throws BusinessException {
        Boolean success = false;
        List<FhmxDto> fhmxDtoList = new ArrayList<>();
        List<String> xsids = new ArrayList<>();
        for (String id : fhglDto.getIds()) {
            if (StringUtil.isNotBlank(id)){
                //查询出之前的发货数量
                FhmxDto fhmxDto = new FhmxDto();
                fhmxDto.setFhid(id);
                fhmxDtoList = fhmxService.getDtoAllByFhid(fhmxDto);
                //删除发货明细
                fhmxDto.setScry(fhglDto.getScry());
                fhmxDto.setScbj("1");
                success = fhmxService.update(fhmxDto);
                if(!success)
                    throw new BusinessException("msg","发货明细更新失败！");
                //处理出库管理表数据
                FhglDto fhglDto1 = new FhglDto();
                fhglDto1.setFhid(id);
                FhglDto dtoByid = dao.getDtoByid(fhglDto1);
                if (!xsids.contains(dtoByid.getDdh())){
                    xsids.add(dtoByid.getDdh());
                }
                if (StringUtil.isNotBlank(dtoByid.getCkid())){
                    CkglDto ckglDto = new CkglDto();
                    ckglDto.setIds(dtoByid.getCkid());
                    ckglDto.setScry(fhglDto.getScry());
                    ckglDto.setScbj("1");
                    success = ckglService.delete(ckglDto);
                    if(!success)
                        throw new BusinessException("msg","出库信息更新失败！");
                    CkmxDto ckmxDto = new CkmxDto();
                    ckmxDto.setIds(dtoByid.getCkid());
                    ckmxDto.setScry(fhglDto.getScry());
                    ckmxDto.setScbj("1");
                    success = ckmxService.deleteByCkid(ckmxDto);
                    if(!success)
                        throw new BusinessException("msg","出库明细信息更新失败！");
                }
                fhglDto1.setScbj("1");
                fhglDto1.setScry(fhglDto.getScry());
                success = dao.update(fhglDto1)!=0;
                if(!success)
                    throw new BusinessException("msg","删除发货信息失败！");

            }
            List<XsmxDto> xsmxDtos = new ArrayList<>();
            List<String> hwids_befor= new ArrayList<>();
            for (FhmxDto fhmxDto : fhmxDtoList) {
                hwids_befor.add(fhmxDto.getHwid());
                XsmxDto xsmxDto = new XsmxDto();
                xsmxDto.setXsmxid(fhmxDto.getXsmxid());
                xsmxDto.setYfhsl(fhmxDto.getFhsl());
                xsmxDto.setFhbj("0");
                xsmxDtos.add(xsmxDto);
            }
            //同过hwid 查找hwxx表数据
            HwxxDto hwxxDto = new HwxxDto();
            List<HwxxDto> hwxxs = new ArrayList<>();
            hwxxDto.setIds(hwids_befor);
            List<HwxxDto> hwxxByids = hwxxService.getHwxxByids(hwxxDto);
            // 修改仓库货物信息库存量
            List<CkhwxxDto> ckhwxxs = new ArrayList<>();
            for (FhmxDto fhmxDto : fhmxDtoList) {
                for (HwxxDto hwxxInfo:hwxxByids) {
                    if (fhmxDto.getHwid().equals(hwxxInfo.getHwid())){
                        HwxxDto hwxx = new HwxxDto();
                        hwxx.setHwid(fhmxDto.getHwid()); //货物id
                        if (StatusEnum.CHECK_PASS.getCode().equals(fhmxDto.getZt())){
                            hwxx.setAddkcl(fhmxDto.getFhsl());
                            CkhwxxDto ckhwxxDto = new CkhwxxDto();
                            ckhwxxDto.setWlid(fhmxDto.getWlid());
                            ckhwxxDto.setCkqxlx(hwxxInfo.getCkqxlx());
                            ckhwxxDto.setCkid(hwxxInfo.getCkid());
                            ckhwxxDto.setKclbj("1"); //库存数标记
                            ckhwxxDto.setKcl(fhmxDto.getFhsl());
                            ckhwxxs.add(ckhwxxDto);
                        }else {
                            hwxx.setYds(fhmxDto.getFhsl()); //发货数量
                            hwxx.setYdsbj("0");
                        }
                        hwxx.setXgry(fhglDto.getScry()); //修改人员
                        hwxxs.add(hwxx);
                    }
                }
            }
            //更新库存
            success = hwxxService.updateHwxxDtos(hwxxs);
            if (!success){
                throw new BusinessException("msg","货物库存数修改失败！");
            }
            if (!CollectionUtils.isEmpty(ckhwxxs)){
                success = ckhwxxService.updateCkhwxxYds(ckhwxxs);
                if (!success){
                    throw new BusinessException("msg","仓库货物库存数修改失败！");
                }
            }
            success = xsmxService.updateXsmxList(xsmxDtos);
            if (!success){
                throw new BusinessException("msg","销售明细表修改失败！");
            }
        }
        DkjejlDto dkjejlDto_del = new DkjejlDto();
        dkjejlDto_del.setScry(fhglDto.getScry());
        dkjejlDto_del.setYwids(fhglDto.getIds());
        dkjejlService.deleteByYwids(dkjejlDto_del);
        if (!CollectionUtils.isEmpty(xsids)){
            XsmxDto xsmxDto = new XsmxDto();
            xsmxDto.setIds(xsids);
            List<XsmxDto> xsmxDtos = xsmxService.getAllDkjeGroupXs(xsmxDto);
            if (!CollectionUtils.isEmpty(xsmxDtos)){
                xsmxService.updateList(xsmxDtos);
                List<XsglDto> xsglDtos = xsmxDtos.stream().collect(Collectors.groupingBy(XsmxDto::getXsid
                            //分组求和
                                , Collectors.mapping(e -> new BigDecimal(String.valueOf(e.getDkje())), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                            //转为list
                        .entrySet().stream().map(e -> new XsglDto(e.getKey(), String.valueOf(e.getValue()),fhglDto.getScry())).collect(Collectors.toList());
                xsglService.updateYsks(xsglDtos);
            }
            FhglDto fhglDto_xs = new FhglDto();
            fhglDto_xs.setIds(xsids);
            List<FhglDto> fhglDtos = dao.getFhztByXsids(fhglDto_xs);
            xsglService.updateFhzt(fhglDtos);
        }
    }

    @Override
    public List<FhglDto> getPagedDtoFhxxList(FhglDto fhglDto) {
        List<FhglDto> pagedDtoList = dao.getPagedDtoList(fhglDto);
        try {
            shgcService.addShgcxxByYwid(pagedDtoList, AuditTypeEnum.AUDIT_SHIPPING.getCode(), "zt", "fhid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
//            shgcService.addShgcxxByYwid(pagedDtoList, AuditTypeEnum.AUDIT_SHIPPING.getCode(), "zt", "fhid",
//                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return pagedDtoList;
    }

    //    /**
//     * 根据具体元素删除
//     */
    public List<String>  delByTargetByValue(List<String> stringList,String value){
        List<String> temps = new ArrayList<>();
        for (String s : stringList) {
            if (!value.equals(s)) {
                temps.add(s);
            }
        }
        return temps;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean thSave(FhglDto fhglDto) throws BusinessException {
        boolean success;
//        新增发货单
        if(StringUtil.isBlank(fhglDto.getFhid())) {
            fhglDto.setFhid(StringUtil.generateUUID());
        }
        fhglDto.setGlfhid(fhglDto.getFhdhs());
        fhglDto.setScbj("0");
        fhglDto.setSfbj("0");
        List<FhmxDto> fhmxDtos = JSON.parseArray(fhglDto.getThmx_json(), FhmxDto.class);
        List<FhmxDto> fhmxDtoList = new ArrayList<>();
        HwxxDto hwxxDto = new HwxxDto();
        List<String> hwids= new ArrayList<>();
        hwxxDto.setIds(hwids);
        List<String> strings = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fhmxDtos)){
            for (FhmxDto fhmxDto : fhmxDtos) {
                FhmxDto fhmxDto1 = new FhmxDto();
                fhmxDto1.setFhmxid(fhmxDto.getGlfhmxid());
                fhmxDto1.setThsl(fhmxDto.getThsl());
                hwids.add(fhmxDto.getHwid());
                fhmxDtoList.add(fhmxDto1);
                fhmxDto.setFhmxid(StringUtil.generateUUID());
                fhmxDto.setFhid(fhglDto.getFhid());
                fhmxDto.setThsl(null);
                fhmxDto.setLrry(fhglDto.getLrry());
                strings.add(fhmxDto.getCk());
            }
        }else{
            throw new BusinessException("msg","发货明细信息为空！");
        }

        //仓库去重
        List<String> temps = new ArrayList<>();
        do{
            temps.add(strings.get(strings.size()-1));
            strings = delByTargetByValue(strings,strings.get(strings.size()-1));
        } while (!CollectionUtils.isEmpty(strings));
        List<DhxxDto> dhxxDtos = new ArrayList<>();
        for (String ckid:temps) {
            //同过ids 查找hwxx表数据
            DhxxDto dhxxDto = new DhxxDto();
            dhxxDto.setDhdh(dhxxService.generateDhdh());
            dhxxDto.setDhid(StringUtil.generateUUID());
            dhxxDto.setLrry(fhglDto.getLrry());
            dhxxDto.setZt("00");
            Map<String, List<JcsjDto>> jcsjList =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_TYPE,BasicDataTypeEnum.ARRIVAL_CATEGORY,BasicDataTypeEnum.INBOUND_TYPE});
            for (JcsjDto jcsjDto:jcsjList.get(BasicDataTypeEnum.INBOUND_TYPE.getCode())) {
                if ("00".equals(jcsjDto.getCsdm())){
                    dhxxDto.setRklb(jcsjDto.getCsid());
                }
            }
            for (JcsjDto jcsjDto:jcsjList.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode())) {
                if ("00".equals(jcsjDto.getCsdm())){
                    dhxxDto.setCglx(jcsjDto.getCsid());
                }
            }
            for (JcsjDto jcsjDto:jcsjList.get(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode())) {
                if ("TH".equals(jcsjDto.getCsdm())){
                    dhxxDto.setDhlx(jcsjDto.getCsid());
                }
            }
            dhxxDto.setCkid(ckid);
            dhxxDto.setDhrq(fhglDto.getDjrq());
            dhxxDto.setBm(fhglDto.getXsbm());
            //新增货物信息’
            success = dhxxService.insert(dhxxDto);
            dhxxDtos.add(dhxxDto);
            if(!success)
                throw new BusinessException("msg","到货信息更新失败！");
        }
        List<HwxxDto> hwxxDtoList = hwxxService.getHwxxByids(hwxxDto);
        List<HwxxDto> hwxxDtos = new ArrayList<>();
        for (HwxxDto hw:hwxxDtoList) {
            for (FhmxDto fhmxDto : fhmxDtos) {
                if (fhmxDto.getHwid().equals(hw.getHwid())){
                    HwxxDto hwxxDto1 = new HwxxDto();
                    hwxxDto1.setHwid(StringUtil.generateUUID());
                    hwxxDto1.setLrry(fhglDto.getLrry());
                    hwxxDto1.setWlid(hw.getWlid());
                    for (DhxxDto dhxxDto : dhxxDtos) {
                        if (dhxxDto.getCkid().equals(fhmxDto.getCk())){
                            hwxxDto1.setDhid(dhxxDto.getDhid());
                        }
                    }
                    hwxxDto1.setDhsl(fhmxDto.getFhsl());
                    hwxxDto1.setZsh(hw.getZsh());
                    hwxxDto1.setScph(hw.getScph());
                    hwxxDto1.setScrq(hw.getScrq());
                    hwxxDto1.setYxq(hw.getYxq());
                    hwxxDto1.setZt("00");
                    hwxxDto1.setCkid(fhmxDto.getCk());
                    hwxxDto1.setCskw(fhmxDto.getHw());
                    if (StringUtil.isNotBlank(hw.getLbbj())){
                        hwxxDto1.setLbbj(hw.getLbbj());
                    }
                    if (StringUtil.isBlank(hwxxDto1.getDhid())){
                        throw new BusinessException("msg","到货信息更新失败！");
                    }
                    hwxxDtos.add(hwxxDto1);
                    fhmxDto.setHwid(hwxxDto1.getHwid());
                }
            }
        }

        //发货单操作U8数据
 //       Map<String, Object> map = rdRecordService.thsave(fhglDto,fhmxDtos,fhmxDtoList);
//        @SuppressWarnings("unchecked")
//        List<FhmxDto> fhmxDtos_s = (List<FhmxDto>) map.get("fhmxDtos");
//        @SuppressWarnings("unchecked")
//        List<CkglDto> ckgls = (List<CkglDto>) map.get("ckglDtos");
//        FhglDto fhgl = (FhglDto) map.get("fhglDto");
//        fhgl.setXgry(fhglDto.getLrry());
//        success = update(fhgl);
//        if(!success) {
//            throw new BusinessException("更新发货关联id失败！");
//        }
//        success = ckglService.updateCkgls(ckgls);
//        if(!success) {
//            throw new BusinessException("更新出库关联id失败！");
//        }
//        success = fhmxService.updateList(fhmxDtos_s);
//        if(!success) {
//            throw new BusinessException("更新发货明细关联id失败！");
//        }
//
//        success = ckmxService.updateList(fhmxDtos);
//        if(!success) {
//            throw new BusinessException("更新发货明细关联id失败！");
//        }
        //新增退货信息
        success = dao.insert(fhglDto)!=0;
        if (!success){
            throw new BusinessException("msg","添加退货数据出错！");
        }
        //新增货物信息’
        success = hwxxService.insertHwxxList(hwxxDtos);
        if(!success)
            throw new BusinessException("msg","货物信息更新失败！");
//        保存退货明细
        success = fhmxService.insertList(fhmxDtos);
        if(!success)
            throw new BusinessException("msg","退货明细新增失败！");
        //        更新退货明细
        success = fhmxService.updateList(fhmxDtoList);
        if(!success)
            throw new BusinessException("msg","退货数量更改失败！");
        return true;
    }

    @Override
    public List<FhglDto> getPagedAuditDevice(FhglDto fhglDto) {
        // 获取人员ID和履历号
        List<FhglDto> fhglDtos= dao.getPagedAuditDevice(fhglDto);
        if (CollectionUtils.isEmpty(fhglDtos))
            return fhglDtos;
        List<FhglDto> fhglDtoList = dao.getAuditListDevice(fhglDtos);
        commonService.setSqrxm(fhglDtoList);
        return fhglDtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean expressMaintenanceFh(FhglDto fhglDto) {
	    try {
	        boolean isSuccess=update(fhglDto);
            if (!isSuccess)
                throw new BusinessException("msg","快递维护失败！");
//            String token = talkUtil.getToken();
            // 发送钉钉消息
            JcsjDto jcsjDto = new JcsjDto();
            jcsjDto.setJclb("DINGMESSAGETYPE");
            jcsjDto.setCsdm("EXPRESS_MAINTENANCE");
            List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
            String title = xxglService.getMsg("ICOMM_KD0001");
            String message = xxglService.getMsg("ICOMM_KD0002", fhglDto.getFhdh(),fhglDto.getWlxx());
            // 内网访问
            String internalbtn =applicationurl + urlPrefix
                    + "/ws/storehouse/ship/viewShip?fhid=" + fhglDto.getFhid();
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("详细");
            btnJsonList.setActionUrl(internalbtn);
            btnJsonLists.add(btnJsonList);
            if (!CollectionUtils.isEmpty(ddxxglDtos)) {
                for (DdxxglDto ddxxglDto : ddxxglDtos) {
                    if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                        talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), title, StringUtil.replaceMsg(message,
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                                btnJsonLists, "1");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        boolean isSuccess = true;
        FhglDto fhglDto = (FhglDto) baseModel;
        fhglDto.setXgry(operator.getYhid());
        if (StringUtil.isBlank(fhglDto.getDdbj())){
            isSuccess = shipFhModSave(fhglDto);
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_SH00084 = xxglService.getMsg("ICOMM_SH00084");
        for (ShgcDto shgcDto : shgcList) {
            FhglDto fhglDto = new FhglDto();
            fhglDto.setFhid(shgcDto.getYwid());
            fhglDto.setXgry(operator.getYhid());
            FhglDto fhglDto1 = dao.getDtoByid(fhglDto);
            shgcDto.setSqbm(fhglDto1.getSsjg());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(), fhglDto1.getSsjg());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                fhglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00085", operator.getZsxm(), shgcDto.getShlbmc(),
                                            fhglDto1.getFhdh(), fhglDto1.getJsrmc(), fhglDto1.getXsbmmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                FhglDto fhglDto_Seldk = new FhglDto();
                fhglDto_Seldk.setDdh(fhglDto1.getDdh());
                fhglDto_Seldk.setFhid(fhglDto1.getFhid());
                //获取要插入的到款信息
                List<FhglDto> fhglDtos = dao.getFhAutoDkjl(fhglDto_Seldk);
                List<DkjejlDto> dkjejlListAdd=new ArrayList<>();
                String lrsj = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss");
                for (FhglDto fhglDto_add : fhglDtos){
                    //如果销售到款金额>发货到款金额
                    if ("1".equals(fhglDto_add.getDkjlbj())){
                        DkjejlDto dkjejlDto=new DkjejlDto();
                        dkjejlDto.setDkjlid(StringUtil.generateUUID());
                        dkjejlDto.setYwid(fhglDto_add.getFhid());
                        dkjejlDto.setFywid(fhglDto_add.getXsmxid());
                        dkjejlDto.setDky(fhglDto1.getDjrq());
                        dkjejlDto.setDkje(fhglDto_add.getDkje());
                        dkjejlDto.setLrsj(lrsj);
                        dkjejlDto.setYwlx("1");
                        dkjejlDto.setLb("2");
                        dkjejlListAdd.add(dkjejlDto);
                    }
                }
                if (!CollectionUtils.isEmpty(dkjejlListAdd)){
                    dkjejlService.insertList(dkjejlListAdd);
                }
                fhglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                fhglDto.setShry(operator.getYhid());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.format(date);
                fhglDto.setShsj(simpleDateFormat.format(date));
                fhglDto1.setZsxm(operator.getZsxm());
                fhglDto1.setXgry(fhglDto.getXgry());
                boolean success;
                if(!"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
                    fhglDto1.setU8fhdh(GenerateNumber());
                    if (!rdRecordService.determine_Entry(fhglDto.getShsj())){
                        throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
                    }
                    Map<String, Object> map = rdRecordService.addU8DataByDeliver(fhglDto1);
                    @SuppressWarnings("unchecked")
                    List<FhmxDto> fhmxDtos_s = (List<FhmxDto>) map.get("fhmxDtos");
                    @SuppressWarnings("unchecked")
                    List<CkglDto> ckgls = (List<CkglDto>) map.get("ckglDtos");
                    FhglDto fhgl = (FhglDto) map.get("fhglDto");
                    fhgl.setXgry(fhglDto.getXgry());
                    fhgl.setU8fhdh(fhglDto1.getU8fhdh());
                    success = update(fhgl);
                    if(!success) {
                        throw new BusinessException("msg","更新发货关联id失败！");
                    }
                    success = ckglService.updateCkgls(ckgls);
                    if(!success) {
                        throw new BusinessException("msg","更新出库关联id失败！");
                    }
                    success = fhmxService.updateList(fhmxDtos_s);
                    if(!success) {
                        throw new BusinessException("msg","更新发货明细关联id失败！");
                    }
                }


                HwxxDto hwxxDto = new HwxxDto();
                //        存仓库货物信息数据
                List<CkhwxxDto> ckhwxxs = new ArrayList<>();
                //存货物信息数据
                List<HwxxDto> hwxxs = new ArrayList<>();
                List<FhmxDto> fhmxDtoList = new ArrayList<>();

                if (StringUtil.isNotBlank(fhglDto.getFhid())) {
                    //查询出之前的发货数量
                    FhmxDto fhmxDto = new FhmxDto();
                    fhmxDto.setFhid(fhglDto.getFhid());
                    fhmxDtoList = fhmxService.getDtoAllByFhid(fhmxDto);
                }
                List<String> hwids_befor= new ArrayList<>();
                for (FhmxDto fhmxDto : fhmxDtoList) {
                    hwids_befor.add(fhmxDto.getHwid());
                }
                hwxxDto.setIds(hwids_befor);
                List<HwxxDto> hwxxByids = hwxxService.getHwxxByids(hwxxDto);
                for (FhmxDto fhmxDto : fhmxDtoList) {
                    for (HwxxDto hwxxInfo:hwxxByids) {
                        if (fhmxDto.getHwid().equals(hwxxInfo.getHwid())){
                            HwxxDto hwxx = new HwxxDto();
                            hwxx.setHwid(fhmxDto.getHwid()); //货物id
                            hwxx.setYds(fhmxDto.getFhsl()); //发货数量
                            hwxx.setYdsbj("0");
                            hwxx.setModkcl(fhmxDto.getFhsl());
                            hwxx.setXgry(fhglDto.getXgry()); //修改人员
                            hwxxs.add(hwxx);
                            CkhwxxDto ckhwxxDto = new CkhwxxDto();
                            ckhwxxDto.setWlid(fhmxDto.getWlid());
                            ckhwxxDto.setCkid(hwxxInfo.getCkid());
                            ckhwxxDto.setYdsbj("0"); //预定数标记
                            ckhwxxDto.setYds(fhmxDto.getFhsl());
                            ckhwxxDto.setKclbj("0"); //预定数标记
                            ckhwxxDto.setKcl(fhmxDto.getFhsl());
                            ckhwxxs.add(ckhwxxDto);
                        }
                    }
                }
                //更新库存
                success = hwxxService.updateHwxxDtos(hwxxs);
                if (!success){
                    throw new BusinessException("msg","货物库存数修改失败！");
                }
                success = ckhwxxService.updateCkhwxxYds(ckhwxxs);
                if (!success){
                    throw new BusinessException("msg","库存数修改失败！");
                }
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00084,
                                    operator.getZsxm(), shgcDto.getShlbmc(), fhglDto1.getFhdh(), fhglDto1.getJsrmc(), fhglDto1.getXsbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }

            }else {
                fhglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                String ICOMM_SH00083 = xxglService.getMsg("ICOMM_SH00083");
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                //获取下一步审核人用户名
                                //内网访问
                                @SuppressWarnings("deprecation")
                                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/sendoutgoods/sendoutgoods&type=1&ywzd=fhid&ywid=" + fhglDto1.getFhid() + "&auditType=" + shgcDto.getAuditType() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
                                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                btnJsonList.setTitle("小程序");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
                                talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00083, operator.getZsxm(), shgcDto.getShlbmc(),
                                        fhglDto1.getFhdh(), fhglDto1.getJsrmc(), fhglDto1.getXsbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                            }
                        }
                    }catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,fhglDto.getFhdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(fhglDto);
            if (StatusEnum.CHECK_PASS.getCode().equals(fhglDto.getZt())){
                List<String> ids = new ArrayList<>();
                ids.add(fhglDto1.getDdh());
                FhglDto fhglDto_xs = new FhglDto();
                fhglDto_xs.setIds(ids);
                List<FhglDto> fhglDtos = dao.getFhztByXsids(fhglDto_xs);
                xsglService.updateFhzt(fhglDtos);
            }
        }

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String fhid = shgcDto.getYwid();
                FhglDto fhglDto = new FhglDto();
                fhglDto.setXgry(operator.getYhid());
                fhglDto.setFhid(fhid);
                fhglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                shipFhModSave(fhglDto);
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String fhid = shgcDto.getYwid();
                FhglDto fhglDto = new FhglDto();
                fhglDto.setXgry(operator.getYhid());
                fhglDto.setFhid(fhid);
                fhglDto.setZt(StatusEnum.CHECK_NO.getCode());
                boolean success = dao.update(fhglDto) != 0;
                if (!success)
                    throw new BusinessException("msg","更新主表信息失败！");
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        FhglDto fhglDto = new FhglDto();
        fhglDto.setIds(ids);
        List<FhglDto> dtoList = dao.getDtoList(fhglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FhglDto dto:dtoList){
                list.add(dto.getFhid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public boolean updateWlxx(FhglDto fhglDto) {
        return dao.updateWlxx(fhglDto);
    }

    public int getCountForSearchExp(FhglDto fhglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(fhglDto);
    }

    public List<FhglDto> getListForSearchExp(Map<String, Object> params) {
        FhglDto fhglDto = (FhglDto) params.get("entryData");
        queryJoinFlagExport(params, fhglDto);
        return dao.getListForSearchExp(fhglDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<FhglDto> getListForSelectExp(Map<String, Object> params) {
        FhglDto fhglDto = (FhglDto) params.get("entryData");
        queryJoinFlagExport(params, fhglDto);
        return dao.getListForSelectExp(fhglDto);
    }

    private void queryJoinFlagExport(Map<String, Object> params, FhglDto fhglDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        fhglDto.setSqlParam(sqlcs);
    }
}
