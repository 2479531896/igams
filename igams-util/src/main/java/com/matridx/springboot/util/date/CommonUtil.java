package com.matridx.springboot.util.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * 从提取和文库的输入信息里获取内部编码
	 * @param srcnbbh
	 * @return
	 */
	public static String getNbbmByNbbm(String srcnbbh) {
		//提取/文库内部编码
		String[] bms = srcnbbh.split("--");
		String bmhz = "";
		String nbbh = "";
		// 提取所有后缀，暂时不需要
		if(bms.length > 1) {
			for(int x=1;x<bms.length;x++) {
				String[] t_bh = bms[x].split("-");
				if(t_bh.length <= 1) {
					continue;
				}
				for(int i=1;i<t_bh.length;i++)
					bmhz = bmhz + "-" + t_bh[i];
			}
		}
		String[] spl_nbbh = bms[0].split("-");
		
		//如果拆分后取第一部分长度为11则是正常内部编码，直接取。
		if(spl_nbbh[0].length()==13 || spl_nbbh[0].length() == 11){
			nbbh=spl_nbbh[0];
		}else{
			// 判断第一部分开头是否为NC或PC，如果是则去掉末尾4位，如果不是取第二部份
			if (spl_nbbh[0].length() >= 2) {
				nbbh = bms[0] + bmhz;
				if ("NC".equals(spl_nbbh[0].substring(0, 2)) || "PC".equals(spl_nbbh[0].substring(0, 2))|| "DC".equals(spl_nbbh[0].substring(0, 2)))
				{
					//NC PC 的情况下不再做处理 2025-8-12，因为NCPC不再带相应后缀
				}else if ("SP".equals(spl_nbbh[0].substring(0, 2))) {
					String[] split = nbbh.split("-");
					if (split.length>1&&("DNA".equals(split[split.length - 1])|| "RNA".equals(split[split.length - 1]))) {
						nbbh = nbbh.substring(0, nbbh.length() - 4);
					}else if (split.length>1&&("tNGS".equals(split[split.length - 1]))){
						nbbh = nbbh.substring(0, nbbh.length() - 5);
					}else if (split.length>1&&("TBtNGS".equals(split[split.length - 1]))){
						nbbh = nbbh.substring(0, nbbh.length() - 7);
					}  else if (split.length>1&&("HS".equals(split[split.length - 1]) || "XD".equals(split[split.length - 1]) || "XT".equals(split[split.length - 1]))) {
						nbbh = nbbh.substring(0, nbbh.length() - 3);
					}else if (split.length>2&&"REM".equals(split[split.length - 1])&&"DNA".equals(split[split.length - 2])) {
						nbbh = nbbh.substring(0, nbbh.length() - 8);
					}
				}else {
					if(spl_nbbh.length > 1 && spl_nbbh[0].length() < 7) {
						nbbh = spl_nbbh[0]+"-" + spl_nbbh[1];
					}else {
						nbbh = spl_nbbh[0];
					}
				}
			} else {
				//throw new BusinessException("msg", "内部编号"+srcnbbh+"不符合!");
				log.error("getNbbmByNbbm : 内部编号"+srcnbbh+"不符合!");
			}
		}
		//送检内部编码
		return nbbh;
	}
}
