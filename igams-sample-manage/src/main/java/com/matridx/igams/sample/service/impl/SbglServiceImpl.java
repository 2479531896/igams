package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.SbglModel;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.post.ISbglDao;
import com.matridx.igams.sample.dao.post.ISbkxglDao;
import com.matridx.igams.sample.dao.post.IYbglDao;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.ISbkxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SbglServiceImpl extends BaseBasicServiceImpl<SbglDto, SbglModel, ISbglDao> implements ISbglService{
	private final Logger logger = LoggerFactory.getLogger(SbglServiceImpl.class);
	@Autowired
	ISbkxglDao sbkxglDao;
	
	@Autowired
	IYbglDao ybglDao;
	
	@Autowired
	ISbkxglService sbkxglService;
	
	/**
	 * 获取设备清单
	 */
	public List<SbglDto> getDeviceList(SbglDto sbglDto){
		return dao.getDeviceList(sbglDto);
	}
	
	/**
	 * 获取所有设备清单
	 */
	public List<SbglDto> getAllDeviceList(SbglDto sbglDto){
		return dao.getAllDeviceList(sbglDto);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(SbglModel sbglModel){
		String sbid = StringUtil.generateUUID();
		sbglModel.setSbid(sbid);
		int result = dao.insert(sbglModel);
		//新增盒子后，要在空闲管理里增加相应的空闲
		if(result >0 && "hz".equals(sbglModel.getSblx())){
			sbkxglService.recountSbkx(sbid);
		}
		return result > 0;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean update(SbglModel sbglModel){
		int result = dao.update(sbglModel);
		//修改盒子后，要在空闲管理里更新相应的空闲
		if(result >0 && "hz".equals(sbglModel.getSblx())){
			sbkxglService.recountSbkx(sbglModel.getSbid());
		}
		return result > 0;
	}
	
	/**
	 * 根据盒子信息获取位置内容
	 */
	public Map<String,Object> getHzPosition(SbglDto sbglDto){
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(sbglDto.getCfs())){
			SbkxglDto t_sbkxglDto = new SbkxglDto();
			t_sbkxglDto.setSbid(sbglDto.getSbid());
			List<SbkxglDto> kxList = sbkxglDao.getSbkxList(t_sbkxglDto);
			map.put("wzlist", getPositionListAndTab(sbglDto,kxList));
		}
		return map;
	}
	
	/**
	 * 根据标本类型和数量获取包括推荐位置等信息,用于标识哪些是本次修改原本所在的位置
	 */
	public Map<String,Object> getRecommendInfo(SbkxglDto sbkxglDto){
		Map<String,Object> result = new HashMap<>();
		SbkxglDto t_SbkxglDto = getRecommendPos(sbkxglDto);
		result.put("sbkxglDto", t_SbkxglDto);
		if(t_SbkxglDto!=null){
			SbglDto sbglDto = new SbglDto();
			sbglDto.setSblx("ct");
			sbglDto.setFsbid(t_SbkxglDto.getBxid());
			result.put("ctList",dao.getDeviceList(sbglDto));
	
			sbglDto.setSblx("hz");
			sbglDto.setFsbid(t_SbkxglDto.getFsbid());
			result.put("hzList",dao.getDeviceList(sbglDto));
			
			sbglDto.setCfs(t_SbkxglDto.getCfs());
			sbglDto.setSbid(t_SbkxglDto.getSbid());
			
			sbglDto.setYbid(t_SbkxglDto.getYbid());
			sbglDto.setCfqswz(t_SbkxglDto.getYscfqswz());
			sbglDto.setCfjswz(t_SbkxglDto.getYscfjswz());
			result.put("wzlist",getHzPosition(sbglDto).get("wzlist"));
		}
		return result;
	}
	
	/**
	 * 根据标本类型和数量获取一个推荐位置
	 */
	public SbkxglDto getRecommendPos(SbkxglDto sbkxglDto){
		if(StringUtil.isNotBlank(sbkxglDto.getYbid())){
			YbglDto ybglDto = ybglDao.getDtoById(sbkxglDto.getYbid());
			
			int i_sl =Integer.parseInt(sbkxglDto.getSl()) ;
			int i_ybsl = Integer.parseInt(ybglDto.getSl());
			
			//如果为企参盘
			if("1".equals(sbkxglDto.getYblxkzcs())) {
				SbkxglDto t_sbkxglDto = new SbkxglDto();
				t_sbkxglDto.setBxid(ybglDto.getBxid());
				t_sbkxglDto.setFsbid(ybglDto.getChtid());
				t_sbkxglDto.setSbid(ybglDto.getHzid());
				t_sbkxglDto.setYscfqswz("1");
				t_sbkxglDto.setYscfjswz("81");
				t_sbkxglDto.setYbid(ybglDto.getYbid());
				return t_sbkxglDto;
			}
			
			if(i_ybsl >= i_sl){
				SbkxglDto t_sbkxglDto = new SbkxglDto();
				t_sbkxglDto.setBxid(ybglDto.getBxid());
				t_sbkxglDto.setFsbid(ybglDto.getChtid());
				t_sbkxglDto.setSbid(ybglDto.getHzid());
				t_sbkxglDto.setCfs(ybglDto.getCfs());
				t_sbkxglDto.setCfqswz(ybglDto.getQswz());
				t_sbkxglDto.setCfjswz(String.valueOf(Integer.parseInt(ybglDto.getQswz()) + Integer.parseInt(sbkxglDto.getSl()) -1));
				t_sbkxglDto.setYscfqswz(ybglDto.getQswz());
				t_sbkxglDto.setYscfjswz(ybglDto.getJswz());
				t_sbkxglDto.setYbid(ybglDto.getYbid());
				return t_sbkxglDto;
			}else{
				List<SbkxglDto> fitList= sbkxglDao.getFitPos(sbkxglDto);
				SbkxglDto t_sbkxglDto = dealFitPos(fitList,sbkxglDto.getSl());
				t_sbkxglDto.setYscfqswz(ybglDto.getQswz());
				t_sbkxglDto.setYscfjswz(ybglDto.getJswz());
				t_sbkxglDto.setYbid(ybglDto.getYbid());
				return t_sbkxglDto;
			}
		}else{
			//如果为企参盘
			if("1".equals(sbkxglDto.getYblxkzcs())) {
				SbkxglDto qcp_sbkxglDto = new SbkxglDto();
				qcp_sbkxglDto.setSl("81");
				
				List<SbkxglDto> fitList= sbkxglDao.getFitPos(qcp_sbkxglDto);
				return dealFitPosByQcp(fitList,sbkxglDto.getSl());
			}else {
				List<SbkxglDto> fitList= sbkxglDao.getFitPos(sbkxglDto);
				return dealFitPos(fitList,sbkxglDto.getSl());
			}
		}
	}
	
	/**
	 * 根据计算规则获取最合适的位置
	 */
	private SbkxglDto dealFitPos(List<SbkxglDto> fitList,String sl){
		if(fitList == null || fitList.isEmpty())
			return null;
		//循环空闲位置，确认是否合适
        for (SbkxglDto sbkxglDto : fitList) {
            List<SbkxglDto> t_List = new ArrayList<>();
            t_List.add(sbkxglDto);
            int total = Integer.parseInt(sbkxglDto.getCfs());
            //根据空闲位置的开始地点，空闲数 ，返回空闲列表
            List<String> pxlist = changeNumList(t_List);

            double n = Math.sqrt(total);
            //根据总数，判断 当前数量是否可以放在同一行，如果不是同一行则，自动从下一行开始
            int i_start = Integer.parseInt(pxlist.get(0));
            //计算当前位置的偏移量
            double d_py = (i_start - 1) % n + 1;
            int i_sl = Integer.parseInt(sl);
            //如果超过一行数目，则直接采用第一个地方
            if (i_sl > n) {
                sbkxglDto.setCfqswz(pxlist.get(0));
                sbkxglDto.setCfjswz(String.valueOf(i_start + i_sl - 1));
                return sbkxglDto;
            }
            //如果起始位置开始没有换行，则采用当前位置
            else if (d_py + i_sl - 1 <= n) {
                sbkxglDto.setCfqswz(pxlist.get(0));
                sbkxglDto.setCfjswz(String.valueOf(i_start + i_sl - 1));
                return sbkxglDto;
            }
            //如果换行后可以放下，则采用换行后的位置
            else if (Integer.parseInt(sbkxglDto.getKxs()) - (n - d_py + 1) >= i_sl) {
                sbkxglDto.setCfqswz(String.valueOf(Integer.parseInt(pxlist.get(0)) + (int) (n - d_py + 1)));
                sbkxglDto.setCfjswz(String.valueOf(i_start + (int) (n - d_py) + i_sl));
                return sbkxglDto;
            }
        }
		SbkxglDto t_sbkxDto = fitList.get(0);
		t_sbkxDto.setCfqswz(t_sbkxDto.getQswz());
		t_sbkxDto.setCfjswz(Integer.parseInt(t_sbkxDto.getQswz()) + sl);
		return t_sbkxDto;
	}
	
	/**
	 * 根据空闲信息计算企参盘的盒子号
	 */
	private SbkxglDto dealFitPosByQcp(List<SbkxglDto> fitList,String sl){
		if(fitList == null || fitList.isEmpty())
			return null;
		List<SbkxglDto> resultDtos = new ArrayList<>();
		int i_sl  = Integer.parseInt(sl);
		//循环空闲位置，确认是否合适
        for (SbkxglDto sbkxglDto : fitList) {
            if (sbkxglDto != null && Integer.parseInt(sbkxglDto.getKxs()) >= 81) {
                resultDtos.add(sbkxglDto);
                i_sl--;
            }
            if (i_sl <= 0)
                break;
        }
		if(!resultDtos.isEmpty()) {
			SbkxglDto t_sbkxDto = resultDtos.get(0);
			t_sbkxDto.setCfqswz("1");
			t_sbkxDto.setCfjswz("81");
			//先设置盒子号
			List<String> hzs = new ArrayList<>();
			hzs.add(t_sbkxDto.getSbid());
			
			for(int i=1;i<resultDtos.size();i++) {
				//如果为同一个抽屉的盒子,则加到盒子数里
				if(t_sbkxDto.getFsbid().equals(resultDtos.get(i).getFsbid())) {
					hzs.add(resultDtos.get(i).getSbid());
				}
			}
			t_sbkxDto.setHzs(hzs);
			return t_sbkxDto;
		}
		return null;
	}
	
	/**
	 * 根据总数转换成位置信息，并根据空闲信息进行标记
	 */
	public List<JcsjDto> getPositionList(YbglDto ybglDto){
		SbglDto sbglDto = new SbglDto();
		sbglDto.setYbid(ybglDto.getYbid());
		sbglDto.setCfqswz(ybglDto.getQswz());
		sbglDto.setCfjswz(ybglDto.getJswz());
		sbglDto.setCfs(ybglDto.getCfs());
		List<List<JcsjDto>> posList = getPositionListAndTab(sbglDto,null);
		List<JcsjDto> result = new ArrayList<>();
		if (posList != null) {
			for (List<JcsjDto> jcsjDtos : posList) {
				result.addAll(jcsjDtos);
			}
		}
		return result;
	}
	
	/*
	  根据总数转换成位置信息，并根据空闲信息进行标记
	  @param total
	 * @param kxList
	 * @return
	 */
	/*private List<List<JcsjDto>> getPositionList(int total,List<SbkxglDto> kxList){
		SbglDto sbglDto = new SbglDto();
		sbglDto.setCfs(String.valueOf(total));
		return getPositionListAndTab(sbglDto,kxList);
	}*/
	
	/**
	 * 根据总数转换成位置信息，并根据空闲信息进行标记
	 */
	private List<List<JcsjDto>> getPositionListAndTab(SbglDto sbglDto,List<SbkxglDto> kxList){
		double n;
		try{
			int total = Integer.parseInt(sbglDto.getCfs());
			n =Math.sqrt(total);
			//标本原有位置列表
			List<String> t_ybwzlist = new ArrayList<>();
			//如果为标本修改的情况下，要把现有标本的位置明确标识处理
			if(StringUtil.isNotBlank(sbglDto.getYbid())){
				int i_qswz = Integer.parseInt(sbglDto.getCfqswz());
				int i_jswz = Integer.parseInt(sbglDto.getCfjswz());
				for(int i=i_qswz;i<=i_jswz;i++){
					t_ybwzlist.add(String.valueOf(i));
				}
			}
			List<String> t_kxlist= changeNumList(kxList);
			List<List<JcsjDto>> result = new ArrayList<>();
			for(int i=1;i<=n;i++){
				List<JcsjDto> t_row = new ArrayList<>();
				char c2=(char) (i+64);
				for(int j=1;j<=n;j++){
					String csmc = String.valueOf(c2)+j;
					JcsjDto jcsjDto = new JcsjDto();
					jcsjDto.setCsid(String.valueOf((i-1)*(int)n+j));
					jcsjDto.setCsdm(String.valueOf(c2));
					jcsjDto.setCsmc(csmc);
					if(t_kxlist!=null&& !t_kxlist.isEmpty()){
						if(t_kxlist.get(0).equals(jcsjDto.getCsid())){
							t_kxlist.remove(0);
							jcsjDto.setCskz1("kx");
						}
					}
					if(!t_ybwzlist.isEmpty()){
						if(t_ybwzlist.get(0).equals(jcsjDto.getCsid())){
							t_ybwzlist.remove(0);
							//原有占有位置
							jcsjDto.setCskz1("yy");
						}
					}
					t_row.add(jcsjDto);
				}
				result.add(t_row);
			}
			return result;
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据空闲信息转换成空闲格子列表
	 */
	private List<String> changeNumList(List<SbkxglDto> kxList){
		if(kxList!=null){
			List<String> result = new ArrayList<>();
			for (SbkxglDto sbkxDto : kxList) {
				int i_qswz = Integer.parseInt(sbkxDto.getQswz());
				int i_kxs = Integer.parseInt(sbkxDto.getKxs());
				for(int i=0;i<i_kxs;i++){
					result.add(String.valueOf(i_qswz + i));
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 根据设备号查询设备信息
	 */
	@Override
	public List<SbglDto> selectSbglBySbh(String sbh) {
		// TODO Auto-generated method stub
		return dao.selectSbglBySbh(sbh);
	}

	/**
	 * 根据盒子号、冰箱号、抽屉号，查询冰箱ID，盒子ID，抽屉ID
	 */
	@Override
	public List<SbglDto> selectSbidsBySbh(YbglDto ybglDto) {
		// TODO Auto-generated method stub
		return dao.selectSbidsBySbh(ybglDto);
	}
	/*
	 * 获取所有的冰箱
	 * */
	@Override
	public List<SbglDto> getAllBxh(String sblx) {
		SbglDto sbglDto = new SbglDto();
		sbglDto.setSblx(sblx);
		return dao.getAllBxh(sbglDto);
	}
	/*
	 * 通过父设备id获取子设备
	 * */
	@Override
	public List<SbglDto> getSbListByFsbid(SbglDto sbglDto){
		return dao.getSbListByFsbid(sbglDto);
	}


	/*
	 * 获取已存放数最少的一组
	 * */
	@Override
	public SbglDto getDefault() {
		return dao.getDefault();
	}
	/*
	 * 维护已存放数
	 * */
	@Override
	public boolean updateYcfs(SbglDto sbglDto) {
		return dao.updateYcfs(sbglDto);
	}
	/*
	 * 维护已存放数
	 * */
	@Override
	public boolean updateYcfsDtos(List<SbglDto> sbglDtos) {
		return dao.updateYcfsDtos(sbglDtos);
	}

	@Override
	public List<SbglDto> getSbListByJcdw(SbglDto sbglDto) {
		return dao.getSbListByJcdw(sbglDto);
	}

	@Override
	public boolean updateYcfsForCK(SbglDto sbglDto) {
		return dao.updateYcfsForCK(sbglDto);
	}

	@Override
	public boolean updateYcfsForMod(SbglDto sbglDto) {
		return dao.updateYcfsForMod(sbglDto);
	}

	@Override
	public boolean updateForXgsj(SbglDto sbglDto) {
		return dao.updateForXgsj(sbglDto);
	}

	/**
	 * 根据sbis查询设备信息
	 */
	@Override
	public List<SbglDto> getDeviceInfoBySbids(SbglDto sbglDto){
		return dao.getDeviceInfoBySbids(sbglDto);
	}

	@Override
	public boolean updateSb(SbglDto sbglDto) {
		return dao.updateSb(sbglDto);
	}

	@Override
	public boolean updateYcfsForMods(SbglDto sbglDto) {
		return dao.updateYcfsForMods(sbglDto);
	}

	@Override
	public boolean batchUpdateYcfs(List<SbglDto> sbglDtos) {
		return dao.batchUpdateYcfs(sbglDtos);
	}

	@Override
	public boolean updateSbs(List<SbglDto> list) {
		return dao.updateSbs(list);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insetList(List<SbglDto> list) {
		return dao.insetList(list);
	}
}
