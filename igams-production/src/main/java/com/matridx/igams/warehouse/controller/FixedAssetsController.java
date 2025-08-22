package com.matridx.igams.warehouse.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.Fa_ObjectsDto;
import com.matridx.igams.production.dao.entities.FpmxDto;
import com.matridx.igams.production.dao.matridxsql.Fa_ObjectsDao;
import com.matridx.igams.production.service.svcinterface.IFpmxService;
import com.matridx.igams.warehouse.dao.entities.GdzcglDto;
import com.matridx.igams.warehouse.service.svcinterface.IGdzcglService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/asset")
public class FixedAssetsController extends BaseBasicController{
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    @Autowired
    private IGdzcglService gdzcglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    Fa_ObjectsDao fa_objectsDao;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IFpmxService fpmxService;
    /**
     * 固定资产列表
     *
     
     */
    @RequestMapping("/asset/pageListFixedAssets")
    public ModelAndView pageListFixedAssets() {
        ModelAndView mav = new  ModelAndView("warehouse/asset/fixedAssets_list");
        mav.addObject("lblist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSET_CLASS.getCode()));
        mav.addObject("zjfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INCREASE_MODE.getCode()));
        mav.addObject("syzklist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.USE_STATUES.getCode()));
        mav.addObject("zjfflist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEPRECIATION_METHOD.getCode()));
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 固定资产列表
     *
     * @param gdzcglDto
     
     */
    @RequestMapping("/asset/pageGetListFixedAssets")
    @ResponseBody
    public Map<String, Object> pageGetListFixedAssets(GdzcglDto gdzcglDto){
        Map<String, Object> map = new HashMap<>();
        List<GdzcglDto> list = gdzcglService.getPagedDtoList(gdzcglDto);
        map.put("total", gdzcglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 固定资产列表 查看
     
     */
    @RequestMapping("/asset/viewFixedAsset")
    public ModelAndView viewFixedAsset(GdzcglDto gdzcglDto) {
        ModelAndView mav = new  ModelAndView("warehouse/asset/fixedAssets_view");
        GdzcglDto dtoById = gdzcglService.getDtoById(gdzcglDto.getHwid());
        FpmxDto fpmxDto=new FpmxDto();
        fpmxDto.setHtmxid(dtoById.getHtmxid());
        List<FpmxDto> listByHtmxid = fpmxService.getListByHtmxid(fpmxDto);
        mav.addObject("list",listByHtmxid);
        mav.addObject("gdzcglDto", dtoById);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 固定资产列表 资产维护
     
     */
    @RequestMapping("/asset/assetupholdFixedAsset")
    public ModelAndView assetupholdFixedAsset(GdzcglDto gdzcglDto) {
        ModelAndView mav = new  ModelAndView("warehouse/asset/fixedAssets_uphold");
        GdzcglDto dtoById = gdzcglService.getDtoById(gdzcglDto.getHwid());
        Fa_ObjectsDto fa_objectsDto_t = fa_objectsDao.getlMaxIDByiObjectNum("5");
        if(fa_objectsDto_t!=null){
            int i = Integer.parseInt(fa_objectsDto_t.getlMaxID());
            String str="00000"+ (i + 1);
            dtoById.setKpbh(str.substring(str.length()-5));
        }else{
            dtoById.setKpbh("00001");
//            Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
//            fa_objectsDto.setiObjectNum("5");
//            fa_objectsDto.setlMaxID("1");
//            fa_objectsDao.insert(fa_objectsDto);
        }
        mav.addObject("gdzcglDto", dtoById);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("lblist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSET_CLASS.getCode()));
        mav.addObject("zjfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INCREASE_MODE.getCode()));
        mav.addObject("syzklist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.USE_STATUES.getCode()));
        mav.addObject("zjfflist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEPRECIATION_METHOD.getCode()));
        mav.addObject("zjkmlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEPRECIATION_ACCOUNT.getCode()));
        mav.addObject("zczlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSET_GROUP.getCode()));
        mav.addObject("bizlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CURRENCY.getCode()));
        mav.addObject("xmlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode()));
        return mav;
    }

    /**
     * 固定资产列表 资产维护
     *
     * @param gdzcglDto
     
     */
    @RequestMapping("/asset/assetupholdSaveFixedAsset")
    @ResponseBody
    public Map<String, Object> assetupholdSaveFixedAsset(GdzcglDto gdzcglDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        gdzcglDto.setLrry(user.getYhid());
        gdzcglDto.setLrrymc(user.getZsxm());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = gdzcglService.insertDto(gdzcglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 固定资产列表 删除
     *
     * @param gdzcglDto
     
     */
    @RequestMapping("/asset/delFixedAsset")
    @ResponseBody
    public Map<String, Object> delFixedAsset(GdzcglDto gdzcglDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        gdzcglDto.setScry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = gdzcglService.deleteDto(gdzcglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
}
