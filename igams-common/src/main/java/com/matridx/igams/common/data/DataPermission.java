package com.matridx.igams.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.matridx.igams.common.dao.DataPermissionModel;
import com.matridx.igams.common.dao.DataPermissionSingleModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import com.matridx.igams.common.dao.entities.ShlbModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.igams.common.service.svcinterface.IWtxxService;
import com.matridx.springboot.util.base.SqlUtil;
import com.matridx.springboot.util.base.StringUtil;

public class DataPermission {
	
	final static Logger log = LoggerFactory.getLogger(DataPermission.class);
	
	/**
	 * 将委托相关参数传入权限对象
	 * @param model
	 * @param action
	 * @return
	 */
	public static void addWtParam(BaseBasicModel model){
		DataPermissionModel dataPermissionModel = model.getDataPermissionModel();
		if(dataPermissionModel!=null){
			dataPermissionModel.setWtZyid(model.getWtZyid());
			dataPermissionModel.setWtWtbh(model.getWtWtbh());
			dataPermissionModel.setSqrxm(model.getSqrxm());
			dataPermissionModel.setSqsjstart(model.getSqsjstart());
			dataPermissionModel.setSqsjend(model.getSqsjend());
			dataPermissionModel.setSftg(model.getShxx_sftg());
		}
	}
	
	/**
	 * 添加一个数据权限控制。
	 * @param model 具体业务的model，不应为空。
	 * @param permissionType com.matridx.igams.common.enums.DataPermissionTypeEnum，不应为空
	 * @param tableAlias 具体表的别名，传null或空串表示无别名
	 * @param columnName 具体的表字段名或字段别名，若有别名应传别名，不应为空
	 * @param auditType 审批类型
	 * @return
	 * @author goofus
	 */
	public static void add(BaseBasicModel model,
			DataPermissionTypeEnum permissionType, String tableAlias,
			String columnName, AuditTypeEnum auditType){
		add(model, permissionType, tableAlias, columnName, auditType, null);
	}

	/**
	 * 添加一个数据权限控制。
	 * @param model 具体业务的model，不应为空。
	 * @param permissionType com.matridx.igams.common.enums.DataPermissionTypeEnum，不应为空
	 * @param tableAlias 具体表的别名，传null或空串表示无别名
	 * @param columnName 具体的表字段名或字段别名，若有别名应传别名，不应为空
	 * @param auditTypes 审批类型
	 * @return
	 * @author goofus
	 */
	public static void add(BaseBasicModel model,
						   DataPermissionTypeEnum permissionType, String tableAlias,
						   String columnName, List<AuditTypeEnum> auditTypes){
		_add(model, permissionType, tableAlias, columnName, auditTypes, null);
	}
	
	/**
	 * 添加一个数据权限控制。
	 * @param model 具体业务的model，不应为空。
	 * @param permissionType com.matridx.igams.common.enums.DataPermissionTypeEnum，不应为空
	 * @param tableAlias 具体表的别名，传null或空串表示无别名
	 * @param columnName 具体的表字段名或字段别名，若有别名应传别名，不应为空
	 * @param auditType 审批类型
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author goofus
	 */
	public static void add(BaseBasicModel model,
			DataPermissionTypeEnum permissionType, String tableAlias,
			String columnName, AuditTypeEnum auditType, SsdwTableEnum ssdw) {
		List<AuditTypeEnum> auditTypes = null;
		if(auditType!=null){
			auditTypes = new ArrayList<>();
			auditTypes.add(auditType);
		}
		_add(model, permissionType, tableAlias, columnName, auditTypes, ssdw);
	}
	
	/**
	 * 添加一个数据权限控制。
	 * @param model 具体业务的model，不应为空。
	 * @param permissionType com.matridx.igams.common.enums.DataPermissionTypeEnum，不应为空
	 * @param tableAlias 具体表的别名，传null或空串表示无别名
	 * @param columnName 具体的表字段名或字段别名，若有别名应传别名，不应为空
	 * @param auditTypes 审批类型List
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author goofus
	 */
	public static void _add(BaseBasicModel model,
			DataPermissionTypeEnum permissionType, String tableAlias,
			String columnName, List<AuditTypeEnum> auditTypes, SsdwTableEnum ssdw) {
		if(null == model || null == permissionType || StringUtil.isBlank(columnName)) return;
		if(StringUtil.isBlank(tableAlias)) tableAlias = "";
		
		DataPermissionModel dataPermissionModel = model.getDataPermissionModel();
		DataPermissionSingleModel permission = new DataPermissionSingleModel();
		permission.setPermissionType(permissionType);
		permission.setTableAlias(tableAlias);
		permission.setColumnName(columnName);
		if(auditTypes!=null){
			permission.getAuditTypes().addAll(auditTypes); 
		}
		permission.setSsdw(ssdw);
		dataPermissionModel.addPermission(permission);
		
	}
	
	/**
	 * 添加一个特殊的多单位的数据权限控制。
	 * （特殊情况用：当columnName与SsdwTaableEnum中的relCol字段不相同时）
	 * @param model 具体业务的model，不应为空。
	 * @param tableAlias 与所属单位关联的表的别名，传null或空串表示无别名
	 * @param columnName tableAlias中关联所属单位的字段名，不应为空
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author 
	 */
	public static void addJsDdw(BaseBasicModel model, String tableAlias,
			String columnName, SsdwTableEnum ssdw) {
		add(model, DataPermissionTypeEnum.PERMISSION_TYPE_DDWQX_JS,
				tableAlias, columnName, null, ssdw);
	}
	
	/**
	 * 添加一个通用的多单位的数据权限控制（根据SsdwTableEnum中配置的值进行关联）。
	 * @param model 具体业务的model，不应为空。
	 * @param tableAlias 与所属单位关联的表的别名，传null或空串表示无别名
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author xinyf
	 */
	public static void addJsDdw(BaseBasicModel model, String tableAlias, SsdwTableEnum ssdw) {
		addJsDdw(model, tableAlias, ssdw.getRelCol(), ssdw);
	}
	
	/**
	 * 添加一个特殊的多单位的数据权限控制。
	 * （特殊情况用：当columnName与SsdwTaableEnum中的relCol字段不相同时）
	 * @param model 具体业务的model，不应为空。
	 * @param tableAlias 与所属单位关联的表的别名，传null或空串表示无别名
	 * @param columnName tableAlias中关联所属单位的字段名，不应为空
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author 
	 */
	public static void addSpDdw(BaseBasicModel model, String tableAlias,
			String columnName, SsdwTableEnum ssdw) {
		add(model, DataPermissionTypeEnum.PERMISSION_TYPE_DDWQX_SP,
				tableAlias, columnName, null, ssdw);
	}
	
	/**
	 * 添加一个通用的多单位的数据权限控制（根据SsdwTableEnum中配置的值进行关联）。
	 * @param model 具体业务的model，不应为空。
	 * @param tableAlias 与所属单位关联的表的别名，传null或空串表示无别名
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author xinyf
	 */
	public static void addSpDdw(BaseBasicModel model, String tableAlias, SsdwTableEnum ssdw) {
		addSpDdw(model, tableAlias, ssdw.getRelCol(), ssdw);
	}
	
	/**
	 * 添加一个通用的多单位的数据权限控制（根据SsdwTableEnum中配置的值进行关联）。
	 * @param model 具体业务的model，不应为空。
	 * @param tableAlias 与所属单位关联的表的别名，传null或空串表示无别名
	 * @param ssdw 所属单位枚举类、多单位情况用
	 * @return
	 * @author xinyf
	 */
	public static void addCurrentUser(BaseBasicModel model, User c_user) {
		DataPermissionModel dataPermissionModel = model.getDataPermissionModel();
		User t_user = copyFromUserModel(c_user);
		dataPermissionModel.setCurrentUser(t_user);
	}
	
	public static String handlePermission(String sql, DataPermissionModel dataPermissionModel){
		
		StringBuffer rawSql = new StringBuffer(sql);
		//第一个where条件
		StringBuilder whereSql = new StringBuilder();
		//第二个where条件
		StringBuilder whereSecondSql = new StringBuilder();
		//数据权限
		List<DataPermissionSingleModel> permissions = dataPermissionModel.getPermissions();
		if (null == permissions || permissions.isEmpty()){
			return rawSql.toString();
		}
		boolean isWtOpe = dataPermissionModel.isWtOpe();//是否是委托的操作
		
		User currentUser = dataPermissionModel.getCurrentUser();
		//非分布式功能块里 因为 dataPermissionModel.setCurrentUser没有设置，则 currentUser会出现为空的情况
		//临时增加当为空的时候，默认为本地服务器
		if(currentUser==null) {
			currentUser = getLoginInfo();
		}
		List<User> users = dataPermissionModel.getUsers();//当前用户们
		//判断是否是委托操作
		if(isWtOpe){
			String wtZyid = dataPermissionModel.getWtZyid();//当前访问的委托资源id
			String wtWtbh = dataPermissionModel.getWtWtbh();//委托编号
			User wtr = null;
			try {
				IWtxxService wtxxService = (IWtxxService)ServiceFactory.getService("wtxxServiceImpl");
				//根据委托资源id、委托编号、受托人获取委托人,以及该委托的该委托角色的资源列表
				wtr = wtxxService.getWtr(wtWtbh, wtZyid, currentUser.getYhid());
			} catch (Exception e) {
				log.error("委托用户获取失败：wtWtbh("+wtWtbh+"),wtZyid("+wtZyid+"),yhm("+currentUser.getYhm()+")", e);
			}
			if(wtr==null){
				whereSql.append(" and 1=2 ");//委托人不存在时，无权限看数据
				whereSecondSql.append(" and 1=2 ");//委托人不存在时，无权限看数据
			}else{
				//委托审核则清空放入委托人
				users.clear();
				users.add(wtr);
			}
		}
		if(users!=null&&users.isEmpty() && currentUser!=null){
			users.add(currentUser);
		}
		/*
		 * 现只考虑单个用户的情况，users暂不删除
		 * user表示当前审核人，如果非委托审核，则即=currentUser，如果委托审核，则是委托人
		 * currentUser表示当前登录用户
		 */
		User user = users.get(0);
		
		boolean isAudit = false;//是否是审核的过滤
		for (DataPermissionSingleModel dp : permissions) {
			String type = dp.getPermissionType().getCode();
			if(DataPermissionTypeEnum.PERMISSION_TYPE_YSP.getCode().equalsIgnoreCase(type)||
					DataPermissionTypeEnum.PERMISSION_TYPE_WSP.getCode().equalsIgnoreCase(type)
					||DataPermissionTypeEnum.PERMISSION_TYPE_YSP_LL.getCode().equalsIgnoreCase(type)){
				isAudit = true;
				break;
			}
		}
		
		//增加菜单权限，不同的菜单根据角色显示不同的内容
		/*String funcodeKey = dataPermissionModel.getFuncodeKey();
		if(StringUtil.isBlank(funcodeKey) && SessionFactory.getHttpRequest()!=null){
			funcodeKey = SessionFactory.getHttpRequest().getParameter("funcodeKey");
			dataPermissionModel.setFuncodeKey(funcodeKey);
		}*/
		//审批岗位是否限制
		//boolean isSpLimit = false;
		String SpColName = "";
		String SpTabName = "";
		
		//处理非审批相关的权限start.
        for (DataPermissionSingleModel single : permissions) {
            String type = single.getPermissionType().getCode();
            String columnName = single.getColumnName();
            String jdbcSql = single.getJdbcSql();
            if (StringUtil.isBlank(type)) {
                if (type.startsWith(DataPermissionTypeEnum.PERMISSION_TYPE_SQL.getCode()) && StringUtil.isBlank(jdbcSql)) {
                    continue;
                } else if (StringUtil.isBlank(columnName)) {
                    continue;
                }
            }
            String tabAlias = single.getTableAlias();

            if (DataPermissionTypeEnum.PERMISSION_TYPE_DDWQX_JS.getCode().equalsIgnoreCase(type)) {//多单位权限
                //如果包含任意不控制单位权限的角色
                if (isLimit(dataPermissionModel, user, 1)) continue;
                whereSql.append(getJSDDWSqlWhere(dataPermissionModel, user, single, tabAlias, columnName, isAudit));
            } else if (DataPermissionTypeEnum.PERMISSION_TYPE_DDWQX_EXTEND.getCode().equalsIgnoreCase(type)) {//多单位权限,前后两个权限不一样的时候
                //如果包含任意不控制单位权限的角色
                if (isLimit(dataPermissionModel, user, 1)) continue;
                whereSecondSql.append(getJSDDWSqlWhere(dataPermissionModel, user, single, tabAlias, columnName, isAudit));
            } else if (DataPermissionTypeEnum.PERMISSION_TYPE_DDWQX_SP.getCode().equalsIgnoreCase(type)) {//审批对多单位权限
                //如果包含任意不控制单位权限的角色
                if (isLimit(dataPermissionModel, user, 1)) continue;
                //判断是否需要进行审批的单位限制
                //isSpLimit = true;
                SpColName = single.getSsdw().getSsdwCol();
                SpTabName = tabAlias;
            } else if (DataPermissionTypeEnum.PERMISSION_TYPE_SQL.getCode().equalsIgnoreCase(type)) {//权限类型，附加到内部的自定义SQL
                if (isLimit(dataPermissionModel, user, 4)) continue;
                whereSql.append(single.getJdbcSql());
                whereSecondSql.append(single.getJdbcSql());
            }

        }
		
		if(whereSql.length() > 0){
			String upperSql = sql.toUpperCase();
			upperSql = StringUtil.replaceAllByRegex(upperSql, " ", "\t|\r|\n");
		
			boolean flag = true;//判断sql中是否有 qxxzcommonsql
			while(true){
				int i_index = sql.indexOf("and 'qxxzcommonsql' = 'qxxzcommonsql'");
				sql = sql.replaceFirst("and 'qxxzcommonsql' = 'qxxzcommonsql'", whereSql.toString());
				if(i_index!=-1){
					rawSql = rawSql.replace(i_index, i_index + 37, whereSql.toString());
					flag = false;
				}else{
					if(flag){
						if(!SqlUtil.whereExists(sql)){
							whereSql.insert(0, " where 1=1 ");
						}
						if(upperSql.lastIndexOf("GROUP") != -1){
							rawSql.insert(upperSql.lastIndexOf("GROUP"), whereSql);
						}else if(upperSql.lastIndexOf("ORDER") != -1){
							rawSql.insert(upperSql.lastIndexOf("ORDER"), whereSql);
						}else{
							rawSql.append(whereSql);
						}
					}
					break;
				}
			}
		}
		
		if(whereSecondSql.length() > 0){
			String upperSql = sql.toUpperCase();
			StringUtil.replaceAllByRegex(upperSql, " ", "\t|\r|\n");
		
			int i_index = sql.indexOf("and 'sexzcommonsql' = 'sexzcommonsql'");
			if(i_index!=-1){
				rawSql = rawSql.replace(i_index, i_index + 37, whereSecondSql.toString());
			}
		}
		//end.
		
		//处理审批相关的权限start.
		StringBuffer spSql = new StringBuffer();
        for (DataPermissionSingleModel single : permissions) {
            String type = single.getPermissionType().getCode();
            String columnName = single.getColumnName();
            if (StringUtil.isBlank(type) || StringUtil.isBlank(columnName)) continue;
            String tabAlias = single.getTableAlias();

            if (DataPermissionTypeEnum.PERMISSION_TYPE_WSP.getCode().equalsIgnoreCase(type)) {//未审批
                if (StringUtil.isBlank(tabAlias)) tabAlias = "inner_table";
                spSql.append("select ").append(tabAlias).append(".*,to_char(shgc.sqsj,'YYYY-MM-DD HH24:MI:SS') shxx_sqsj,shgc.sqr,gw.gwmc shxx_gwmc,shgc.shid shxx_shid,shgc.shyj shxx_shyj from ( ").append(rawSql).append(" ) ").append(tabAlias);
                spSql.append(" join matridx_shgc shgc on shgc.ywid = ");
                spSql.append(tabAlias).append(".").append(columnName);
                spSql.append(" join matridx_shlc shlc ");
                spSql.append(" on shgc.shid=shlc.shid and shgc.xlcxh=shlc.lcxh and shgc.sqsj >= shlc.qysj and (shlc.tysj is null or shlc.tysj >= shgc.sqsj) ");
                spSql.append(" join matridx_xtsh xtsh on xtsh.shid = shgc.shid ");
                spSql.append(" join matridx_spgw gw on gw.gwid = shlc.gwid ");
                //如果进行审批的单位限制  modify 2020-04-09 start
                spSql.append(" left outer join matridx_spgwcy gwcy on gw.gwid = gwcy.gwid ");
                spSql.append(" left outer join matridx_xtjs gwjs on gwjs.jsid = gwcy.jsid and gwjs.scbj!='1' ");
                //如果进行审批的单位限制  add  2019-10-18 start
				/*if(isSpLimit) {
					spSql.append(" and (gw.dwxz = '0' or gw.dwxz is null  or (gw.dwxz = '1' and ").append(SpTabName).append(".").append(SpColName).append(" in (select jgid from matridx_jsdwqx qx where (1=2 ");//不存在角色权限，则不显示
					//过滤用户的角色权限
					if(StringUtil.isNotBlank(user.getDqjs())) {
						spSql.append(" or qx.jsid = '").append(user.getDqjs()).append("' ");
					}
					spSql.append(" ) ) ) ) ");
				}*/
                //如果进行审批的单位限制  add  2019-10-18 end
                //如果进行审批的单位限制  modify 2020-04-09 end
                //关联业务角色的角色信息
                spSql.append(" left join matridx_xtjs xtjs on xtjs.jsid=gw.ywjs and xtjs.scbj!='1' and gw.sfywjs='")
                        .append(YesNotEnum.YES.getCode()).append("' ");
                //关联用户角色取得登录号
                spSql.append(" left join (select distinct yhid,jsid from matridx_yhjs where ");//只查询该用户的角色信息，以减少数据量
                spSql.append(" yhid='").append(user.getYhid()).append("' ");
                spSql.append(" and (1=2 ");//不存在角色权限，则不显示
                //过滤用户的角色权限
                if (StringUtil.isNotBlank(user.getDqjs())) {
                    spSql.append(" or jsid = '").append(user.getDqjs()).append("' ");
                }
                spSql.append(" ) ");
                spSql.append(") yhjs on yhjs.jsid=xtjs.jsid ");
                spSql.append(" where ( 1=2 ");
                //如果进行审批的单位限制  modify 2020-04-09 start
                spSql.append("  or (gw.sfywjs = '1' and xtjs.jsid is not null and yhjs.yhid = '").append(user.getYhid()).append("') ");
                spSql.append("  or (shgc.lx = 'SJZG' and shgc.lxid = '").append(user.getYhid()).append("') ");
                spSql.append("   or (gw.sfywjs = '0' and gwjs.jsid is not null and gwcy.yhid = '").append(user.getYhid()).append("' and gwcy.jsid = '").append(user.getDqjs()).append("' ");
                spSql.append("	  and (gw.dwxz ='0' or (gw.dwxz ='1' and (gwjs.dwxdbj ='0' or (gwjs.dwxdbj='1' and ");
                if (StringUtil.isBlank(SpTabName)) {
                    spSql.append(" 1=1 ");
                } else {
                    spSql.append(" ( ( ");
                    spSql.append(SpTabName).append(".").append(SpColName).append(" IN ( ");
                    spSql.append("				SELECT jgid ");
                    spSql.append("				FROM matridx_jsdwqx qx ");
                    spSql.append("				WHERE 1 = 2 ");
                    spSql.append("					OR qx.jsid = '").append(user.getDqjs()).append("') ");

                    spSql.append(" ) or ( ");
                    spSql.append(SpTabName).append(".").append(SpColName).append(" IN ( ");
                    spSql.append("				SELECT jgid ");
                    spSql.append("				FROM matridx_yhjgqx qx ");
                    spSql.append("				WHERE qx.yhid = '").append(user.getYhid()).append("' ");
                    spSql.append("					and qx.jsid = '").append(user.getDqjs()).append("') ");
                    spSql.append(" ) ) ");
                }
                spSql.append("			)");
                spSql.append(" ) ) ) ");
                spSql.append("	) ) ");
                //或者存在审批岗位成员（dlh为user的，jsid是user的角色）（因存在一对多故用exists）
				/*spSql.append(" or exists (select 1 from matridx_spgwcy spgwcy join matridx_xtjs cyjs on cyjs.jsid = spgwcy.jsid and cyjs.scbj!='1' ")
					.append(" where spgwcy.gwid=gw.gwid and (gw.sfywjs='").append(YesNotEnum.NOT.getCode()).append("' or gw.sfywjs is null) ")
					.append(" and spgwcy.yhid = '").append(user.getYhid()).append("' ")
					.append(" and (1=2 ");//不存在角色权限，则不显示
					//过滤用户的角色权限
					if(StringUtil.isNotBlank(user.getDqjs())) {
						spSql.append(" or cyjs.jsid = '").append(user.getDqjs()).append("' ");
					}
					spSql.append(" ) ");
				spSql.append(" ) ");
				
				//或者审批岗位业务角色审批的角色过滤
				spSql.append(" or (yhjs.yhid='").append(user.getYhid()).append("') ");
				spSql.append(" ) ");*/
                //如果进行审批的单位限制  modify 2020-04-09 end
                //添加审核类别过滤，如果未传递则认为是非法操作（存在关联其它业务相同业务id的审核数据的可能），故添加1=2过滤
                spSql.append(" and ( 1=2 ");
                if (single.getAuditTypes()!=null&&!single.getAuditTypes().isEmpty()) {
                    for (AuditTypeEnum auditType : single.getAuditTypes()) {
                        if (auditType != null) {
                            spSql.append(" or xtsh.shlb='").append(auditType.getCode()).append("' ");
                        }
                    }
                }
                spSql.append(" ) ");
                if (StringUtil.isNotBlank(dataPermissionModel.getShxx_gwmc())) {
                    dataPermissionModel.setShxx_gwmc(dataPermissionModel.getShxx_gwmc().replaceAll("'", "''"));
                    spSql.append(" and gw.gwmc like '%").append(dataPermissionModel.getShxx_gwmc()).append("%'");
                }
                break;//未审批和已审批，只能二存一
            } else if (DataPermissionTypeEnum.PERMISSION_TYPE_YSP.getCode().equalsIgnoreCase(type)
                    || DataPermissionTypeEnum.PERMISSION_TYPE_YSP_LL.getCode().equalsIgnoreCase(type)) {//已审批
                Set<String> fqmcSet = new HashSet<>();
                Set<String> fqbjSet = new HashSet<>();

                IShlbService shlbService = (IShlbService) ServiceFactory.getService("shlbServiceImpl");

                if (single.getAuditTypes()!=null&&!single.getAuditTypes().isEmpty()) {
                    for (AuditTypeEnum auditType : single.getAuditTypes()) {
                        if (auditType != null) {
                            //获取分区名称
                            ShlbModel shlbModel = shlbService.getDtoById(auditType.getCode());
                            fqmcSet.add(shlbModel.getFqlb());
                            fqbjSet.add(shlbModel.getFqlb());
                        }
                    }
                }

                if (StringUtil.isBlank(tabAlias)) tabAlias = "inner_table";
                spSql.append("select ").append(tabAlias).append(".*, ");
                spSql.append(" shxx.shxxid shxx_shxxid,shxx.gcid shxx_gcid,shxx.xh shxx_xh,shxx.shid shxx_shid,shxx.lcxh shxx_lcxh,shxx.sqr, ");
                spSql.append(" to_char(shxx.sqsj, 'yyyy-mm-dd hh24:mi:ss') shxx_sqsj, ");
                spSql.append(" to_char(shxx.shsj, 'yyyy-mm-dd hh24:mi:ss') shxx_shsj, ");
                spSql.append(" shxx.sftg shxx_sftg,shxx.shyj shxx_shyj,shxx.ssyj shxx_ssyj, ");
                spSql.append(" shxx.gwid shxx_gwid,shxx.lrry shxx_lrry,xtyh.zsxm shxx_lrryxm, ");
                spSql.append(" to_char(shxx.lrsj, 'yyyy-mm-dd hh24:mi:ss') shxx_lrsj, ");
                spSql.append(" coalesce(shxx.stshry,shxx.lrry) shxx_shry, ");
                spSql.append(" (case when shxx.stshry is null then xtyh.zsxm else wtxtyh.zsxm || '(委托  ' || stxtyh.zsxm || ')' end) as shxx_shryxm ");
                spSql.append(" from ( ").append(rawSql).append(" ) ").append(tabAlias);
                spSql.append(" join matridx_shxx ");
				/*if(fqmcSet.size()==1){//一个分区时用partition关键字，多个时用fqbj查询条件
					spSql.append(" partition(").append(fqmcSet.iterator().next()).append(") ");//分区信息
				}*/
                spSql.append(" shxx on shxx.ywid = ").append(tabAlias).append(".").append(columnName);
                //关联履历表时，用过程id进行关联
                if (DataPermissionTypeEnum.PERMISSION_TYPE_YSP_LL.getCode().equalsIgnoreCase(type)) {
                    spSql.append(" and shxx.gcid= ").append(tabAlias).append(".gcid ");
                }
                spSql.append(" left outer join matridx_shgc shgc on shgc.ywid = ");
                spSql.append(tabAlias).append(".").append(columnName);
                //关联matridx_spgwcy表来限制gwid,jsid,yhid
                spSql.append(" left join matridx_spgwcy gwcy on gwcy.yhid = shxx.lrry  and gwcy.gwid = shxx.gwid and (gwcy.jsid = shxx.jsid or shxx.jsid is null)"); //and gwcy.jsid = shxx.jsid 暂时不用  可以允许看到其他角色的同一个人的审核信息
                spSql.append(" join matridx_xtyh xtyh on xtyh.yhid=shxx.lrry ");
                spSql.append(" join matridx_xtsh xtsh on xtsh.shid = shxx.shid ");
                spSql.append(" left join matridx_xtyh stxtyh on stxtyh.yhid=shxx.stshry ");
                spSql.append(" left join matridx_xtyh wtxtyh on wtxtyh.yhid=shxx.wtshry ");
                //申请人信息
                if (StringUtil.isNotBlank(dataPermissionModel.getSqrxm())) {
                    spSql.append(" left outer join matridx_xtyh sqryh on sqryh.yhid = shxx.sqr ");
                }
                spSql.append(" where 1=1 ");
                spSql.append(" and shxx.sftg is not null ");//过滤掉提交的记录(管理端新增提交导致在审核记录中可看到提交记录)
                //设置检索条件
                //申请人信息
                if (StringUtil.isNotBlank(dataPermissionModel.getSftg())) {
                    spSql.append(" and shxx.sftg = '").append(StringUtil.changeSqlXSS(dataPermissionModel.getSftg())).append("' ");
                }
                //申请人信息
                if (StringUtil.isNotBlank(dataPermissionModel.getSqrxm())) {
                    spSql.append(" and sqryh.zsxm like '%").append(StringUtil.changeSqlXSS(dataPermissionModel.getSqrxm())).append("%' ");
                }
                //申请时间——开始
                if (StringUtil.isNotBlank(dataPermissionModel.getSqsjstart())) {
                    spSql.append(" and to_char(shxx.sqsj,'YYYY-MM-dd') >= '").append(StringUtil.changeSqlXSS(dataPermissionModel.getSqsjstart())).append("' ");
                }
                //申请时间——结束
                if (StringUtil.isNotBlank(dataPermissionModel.getSqsjend())) {
                    spSql.append(" and to_char(shxx.sqsj,'YYYY-MM-dd') <= '").append(StringUtil.changeSqlXSS(dataPermissionModel.getSqsjend())).append("' ");
                }
                spSql.append(" and (gwcy.gwid in (select gwid from matridx_spgwcy where yhid = '").append(currentUser.getYhid()).append("' ");
                spSql.append(" and (jsid is null ");
                //过滤审批用户的角色权限（审批岗位）
                if (StringUtil.isNotBlank(user.getDqjs())) {
                    spSql.append(" or jsid = '").append(user.getDqjs()).append("' ");
                }
                spSql.append(" )) or shxx.lrry = '").append(currentUser.getYhid()).append("')");

                //分区名称存在多个的情况，则用分区标记进行过滤
				/*if(fqmcSet.size()>1){
					spSql.append(" and ( 1=2 ");
					for (String fqbj: fqbjSet) {
						if(fqbj!=null){
							spSql.append(" or shxx.fqbj='").append(fqbj).append("' ");
						}else{
							spSql.append(" or shxx.fqbj is null ");
						}
					}
				}*/
                //添加审核类别过滤，如果未传递则认为是非法操作（存在关联其它业务相同业务id的审核数据的可能），故添加1=2过滤
                spSql.append(" and ( 1=2 ");
                if (single.getAuditTypes()!=null&&!single.getAuditTypes().isEmpty()) {
                    for (AuditTypeEnum auditType : single.getAuditTypes()) {
                        if (auditType != null) {
                            spSql.append(" or xtsh.shlb='").append(auditType.getCode()).append("' ");
                        }
                    }
                }
                spSql.append(" ) ");
				/*if(fqmcSet.size()>1){
					spSql.append(" ) ");
				}*/
                break;//未审批和已审批，只能二存一
            }

        }
		if(spSql.length() > 0)
			rawSql = spSql;
		//end.
		
		return rawSql.toString();
	}
	
	private static User copyFromUserModel(User user)
	{
		User t_user = new User();

		t_user.setYhid(user.getYhid());
		t_user.setClient_id(user.getClient_id());
		t_user.setYhm(user.getYhm());
		t_user.setZsxm(user.getZsxm());
		t_user.setSfsd(user.getSfsd());
		t_user.setDlsj(user.getDlsj());
		t_user.setCwcs(user.getCwcs());
		t_user.setJsids(user.getJsids());
		t_user.setJsmcs(user.getJsmcs());
		t_user.setDqjs(user.getDqjs());
		t_user.setDqjsdwxdbj(user.getDqjsdwxdbj());
		t_user.setDqjsmc(user.getDqjsmc());
		t_user.setDdid(user.getDdid());
		t_user.setGwmc(user.getGwmc());
		t_user.setJgid(user.getJgid());
		t_user.setScbj(user.getScbj());
		return t_user;
	}
	
	/**
	 * 判断当前用户是否不做相应权限控制
	 * @param dataPermissionModel
	 * @param type 1单位是否控制；2项目类别是否控制;3成果是否控制
	 * @param user 用户们
	 * @return
	 * @author goofus
	 */
	private static boolean isLimit(DataPermissionModel dataPermissionModel, User user, int type) {
		boolean flag = false;
		
		/*
		 * 逻辑变更：
		 * 当前菜单编号对应的角色们，存在任意一个角色权限限制判断结果为否则为否
		 * 注：不同角色赋不同菜单，以实现同一个人在不同功能有不同数据权限
		 */
		String funcodeKey = dataPermissionModel.getWtZyid();
		Map<String, Map<String, Object>> qxxdbjMap = user.getQxxdbjMap();
		if(null != qxxdbjMap && !qxxdbjMap.isEmpty() && qxxdbjMap.containsKey(funcodeKey)){
			Map<String, Object> map = qxxdbjMap.get(funcodeKey);
			//拥有资源重复计数，最小为1
			int tally = Integer.parseInt(String.valueOf(map.get("TALLY")));
			
			if(type == 1){//单位
				int bj = Integer.parseInt(String.valueOf(map.get("DWBJ")));
				if(bj < tally) flag = true;//直接不限
			}
		}
		
		return flag;
	}
	
	/**
	 * 多单位权限控制
	 * @param dataPermissionModel
	 * @param user
	 * @param single
	 * @param tabAlias
	 * @param columnName
	 * @param isAudit
	 * @return
	 */
	private static StringBuffer getJSDDWSqlWhere(DataPermissionModel dataPermissionModel, User user, DataPermissionSingleModel single,String tabAlias,String columnName,boolean isAudit){
		
		StringBuffer whereSql = new StringBuffer();
		Map<String,Object> param = new HashMap<>();
		param.put("user", user);

		ICommonService commonService = (ICommonService)ServiceFactory.getService("commonServiceImpl");
		
		//判断当前角色是否对单位进行限制
		if("1".equals(user.getDqjsdwxdbj())) {
			//取得授权的机构
			List<String> orgIdList = commonService.listOrgByYhid(param);
			
			//List<HashMap> orgList = commonService.getOrgStartWithSup(orgIdList);
			SsdwTableEnum ssdwTb = single.getSsdw();
			if(orgIdList!=null&&!orgIdList.isEmpty() && ssdwTb !=null){
				//如果序号为空，则认为为未拆分表，则直接加条件无需用exist
				if(StringUtil.isBlank(ssdwTb.getXhCol()))
				{
					if(StringUtil.isNotBlank(tabAlias) ){
						String[] tabs = tabAlias.split(",");
						String t_tab = tabs[0];
						
						whereSql.append(" and ").append(t_tab).append(".").append(ssdwTb.getSsdwCol()).append(" in (");
						for(int a=0,al=orgIdList.size(); a<al; a++){
							whereSql.append(" '").append(orgIdList.get(a)).append("' ");
							if((a + 1) < al)
								whereSql.append(",");
						}
						whereSql.append(")");
					}else {
						whereSql.append(" and ").append(ssdwTb.getTable()).append(".").append(ssdwTb.getSsdwCol()).append(" in (");
						for(int a=0,al=orgIdList.size(); a<al; a++){
							whereSql.append(" '").append(orgIdList.get(a)).append("' ");
							if((a + 1) < al)
								whereSql.append(",");
						}
						whereSql.append(")");
					}
				}
				else {
					String yxTable = ssdwTb.getTable();
					String yxAlias = yxTable.trim()+"_";
					String yxAliasP = yxAlias+".";
					whereSql.append(" and exists ( select 1 from ").append(yxTable).append(" ").append(yxAlias).append(" where ");
					if(StringUtil.isNotBlank(tabAlias) && tabAlias.contains(",")){
						String[] tabs = tabAlias.split(",");
						String[] recCols = ssdwTb.getRelCol().split(",");
						String[] columns = columnName.split(",");
						for(int ind = 0 ; ind < tabs.length ; ind ++ ){
							String t_tab = tabs[ind];
							if(StringUtil.isBlank(t_tab))
								continue;
							if(ind > 0)
								whereSql.append(" and ");
							String tabAliasP = t_tab.trim()+".";
							if(columns[ind].contains(":"))
								whereSql.append(yxAliasP).append(recCols[ind].split(":")[0]).append(" = ").append(tabAliasP).append(columns[ind].split(":")[1]);
							else
								whereSql.append(yxAliasP).append(recCols[ind]).append(" = ").append(tabAliasP).append(columns[ind]);
							if(StringUtil.isNotBlank(ssdwTb.getLlhCol())){//配置了履历号，则关联该履历数据
								whereSql.append(" and ").append(yxAliasP).append(ssdwTb.getLlhCol().trim()).append(" = ").append(tabAliasP).append(ssdwTb.getLlhCol());
							}
						}
					}else{
						String tabAliasP = (StringUtil.isNotBlank(tabAlias)?tabAlias.trim()+".":"");
						whereSql.append(yxAliasP).append(ssdwTb.getRelCol()).append(" = ").append(tabAliasP).append(columnName);
						if(StringUtil.isNotBlank(ssdwTb.getLlhCol())){//配置了履历号，则关联该履历数据
							whereSql.append(" and ").append(yxAliasP).append(ssdwTb.getLlhCol().trim()).append(" = ").append(tabAliasP).append(ssdwTb.getLlhCol());
						}
					}
					if(isAudit&&StringUtil.isNotBlank(ssdwTb.getXhCol())){//审核的过滤，则取xlCol为xlVal的单位
						whereSql.append(" and ").append(yxAliasP).append(ssdwTb.getXhCol()).append(" = '").append(ssdwTb.getXhVal()).append("' ");
					}
					whereSql.append(" and (");
					for(int a=0,al=orgIdList.size(); a<al; a++){
						if(a > 0) whereSql.append(" or ");
						whereSql.append(" ").append(yxAlias).append(".").append(ssdwTb.getSsdwCol()).append(" = '")
							.append(orgIdList.get(a)).append("' ");
					}
					whereSql.append(")) ");
				}
			}else{
				whereSql.append(" and 1=2 ");//当前用户未授权任何单位权限
			}
		}
		//当前角色不限制单位
		else {
			whereSql.append(" and 1=1 ");//未限定单位权限，则都可以通过
		}
		
		return whereSql;
	}
	
	/**
	 * 获取用户登录信息，本应采用分布式的情况调用，但因为现有功能只修改了一部分，未修改部分调用会出问题，所以需要重新增加
	 * @return
	 */
	private static User getLoginInfo(){
		//String name = request.getRemoteUser();
		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		Authentication authentication = securityContext.getAuthentication();

		// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
		// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
		// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
		// 一个适配器。
		@SuppressWarnings("unchecked")
		List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();
		
		IgamsGrantedAuthority authority = authorities.get(0);
		
		return authority.getYhxx();
	}
	
}
