package com.matridx.igams.common;

import org.springframework.beans.factory.annotation.Value;

public class GlobalString {
	
	/**
	 * 是否可编辑，对比参考类型DLH
	 */
	public static final String EDITABLE_CONTRAST_DLH = "DLH";
	/**
	 * 是否可编辑，对比参考类型RYID
	 */
	public static final String EDITABLE_CONTRAST_RYID = "RYID";
	/**
	 * 分隔符，英文分号
	 */
	public static final String SEPARATOR_SEMI = ";";
	
	/**
	 * 分隔符，英文逗号
	 */
	public static final String SEPARATOR_COMMA = ",";
	
	/**
	 * 分隔符，英文冒号
	 */
	public static final String SEPARATOR_COLON = ":";
	
	/**
	 * 分隔符，特殊符号1
	 */
	public static final String SEPARATOR_SIGN1 = "!@";
	
	 /**
	  * 换行符
	  */
	public static final String NEW_LINE = "<br/>";
	
	/**
	 * 分隔符，路径
	 */
	public static final String SEPARATOR_PATH = "/";
	
	/**
	 * 分隔符，连字符
	 */
	public static final String SEPARATOR_HYPHEN = "-";
	
	/**
	 * 分隔符，双连字符
	 */
	public static final String SEPARATOR_TWO_HYPHEN = SEPARATOR_HYPHEN+SEPARATOR_HYPHEN;
	
	/**
	 * 分隔符，下划线
	 */
	public static final String SEPARATOR_UNLINE = "_";
	
	/**
	 * 确定多少条记录后再一起提交
	 */
	public static final int commitNum = 10;
	
	/**
	 * 压缩包扩展名
	 */
	public static final String EXTENSION_NAME = ".zip";
	
	/**
	 * word扩展名
	 */
	public static final String EXTENSION_WORD_SUFFIX = ".doc";
	
	/**
	 * 项目报告文件压缩包名称
	 */
	public static final String COMPRESS_PAG_NAME_PROJECT = "projectReportFile";
	
	/**
	 * 通知报告文件压缩包名称
	 */
	public static final String COMPRESS_PAG_NAME_NOTICE = "noticeReportFile";
	
	/**
	 * 重置密码的默认密码
	 */
	@Value("${matridx.reset.pd}")
	public static final String MATRIDX_RESET_PD = null;
	
	/**
	 * 数据库备份服务器的IP
	 */
	public static final String DBBACK_IP = "matridx.dbback.ip";
	
	/**
	 * 数据库备份服务器的联系人手机
	 */
	public static final String DBBACK_PHONE = "matridx.dbback.phone";
	
	/**
	 * 功能显示
	 */
	public static final String MATRIDX_GNAN_SFXS_YES = "1";
	
	/**
	 * 功能不显示
	 */
	public static final String MATRIDX_GNAN_SFXS_NO = "0";
	
	/**
	 * 审核步骤一
	 */
	public static final String AUDIT_STEP_ONE = "1";
	
	/**
	 * 审核状态待审核
	 */
	public static final String AUDIT_SHZT_DSH = "dsh";
	
	/**
	 * 审核状态已审核
	 */
	public static final String AUDIT_SHZT_YSH = "ysh";
	
	/**
	 * 删除标记-0
	 */
	public static final String SCBJ_ZERO = "0";
	
	/**
	 * 删除标记-1
	 */
	public static final String SCBJ_ONE = "1";
	
	/**
	 * id常量字符串
	 */
	public static final String ID = "id";
	
	/**
	 * 结果常量字符串
	 */
	public static final String RESULT = "result";
	
	/**
	 * service常量字符串
	 */
	public static final String SERVICE = "service";
	
	/**
	 * cancelAudited常量字符串
	 */
	public static final String CANCEL_AUDITED = "cancelAudited";
	
	/**
	 * 全部数据
	 */
	public static final String ALL = "all";
	
	/**
	 * 系统用户
	 */
	public static final String USER_XTYH = "2";
	
	/**
	 * 科研人员
	 */
	public static final String USER_KYRY = "1";
	
	
	/**
	 * 密码标记
	 */
	public static final String USER_MMBJ = "1";
	
	/**
	 * 所属单位的父子单位分隔符
	 */
	public static final String SEPARATOR_SSDW = SEPARATOR_TWO_HYPHEN;
	
	/**
	 * 审核流程选择配置：0.弹出流程选择，1.一个流程不弹出，多个则弹出
	 */
	public static final String AUDIT_CONFIRM_TYPE = "matridx.audit.confirmType";
	
	/**
	 * 审核钉钉提醒
	 */
	public static final String AUDIT_DINGTALK_FLG = "matridx.audit.dingtalk";
	
	/**
	 * 审核流程选择配置：弹出流程选择
	 */
	public static final String AUDIT_CONFIRM_CHOOSE = "0";
	
	/**
	 * 审核流程选择配置：视情况而定，一个流程不弹出，多个弹出
	 */
	public static final String AUDIT_CONFIRM_JUDGE = "1";
	
	
	/**
	 * 管理员角色admin
	 */
	public static String ROLE_ADMIN = "admin";
	
	/**
	 * 默认编码UTF-8
	 */
	public static String UTF_8 = "utf-8";
	
	/**
	 * 附件存放路径前缀
	 */
	@Value("${matridx.file.pathPrefix}")
	public static final String FILE_STOREPATH_PREFIX = "";
	
	/**
	 * 导出文件存放路径
	 */
	@Value("${matridx.file.exportFilePath}")
	public static final String EXPORT_FILE_PATH = null;

	/**
	 * 文件生成输出临时目录
	 */
	@Value("${matridx.file.path}")
	public static final String MATRIDX_FILE_PATH = null;
	
	/**
	 * 左括号
	 * 
	 * */
	public final static String LEFT_PARENTHESIS = "(";
	
	/**
	 * 右括号
	 */
	public final static String RIGHT_PARENTHESIS = ")";
	
	/**
	 * 扩展参数值: 一
	 */
	public static final String EXTEND_ONE = "1";
	
	
	/**
	 * 角色切换配置： 0不支持切换，1支持切换
	 */
	@Value("$(matridx.role.switch)")
	public static final String ROLE_SWITCH = null;
	
	/**
	 * 角色切换配置boolean：true支持切换，false不支持切换
	 */
	@Value("$(matridx.role.switch)")
	public static final boolean IS_ROLE_SWITCH = false;
	
	/**
	 * session中保存当前切换角色操作权限列表名称的key
	 */
	public static final String SSK_DQJS_CZQX = "dqjsCzqxList";
	
	/**
	 * session中保存菜单数据的key
	 */
	public static final String SSK_MENU_LIST = "topMenuList";
	
	/**
	 * session中保存手机端菜单数据的key
	 */
	public static final String SSK_MOBILE_MENULIST = "topMobileMenuList";
	
	/**
	 * session中保存某路径的操作代码数据的key前缀
	 */
	public static final String SSK_PATH = "_path-";
	
	/**
	 * session中保存某路径的导航信息数据的key前缀
	 */
	public static final String SSK_PATH_DH = "_path_dh-";
	
	/**
	 * 因切换角色时，session中需要清除的缓存数据的相应key
	 * 注：添加请在后面追加，不用修改已有数据！！！
	 * 	添加key同时请加注释！！！
	 */
	public static final String[] SS_SWITCH_KEYS = new String[]{
		SSK_DQJS_CZQX,
		SSK_MENU_LIST,
		SSK_MOBILE_MENULIST,
	};
	
	/**
	 * 因切换角色时，session中需要清除的缓存数据的相应key前缀
	 * 注：添加请在后面追加，不用修改已有数据！！！
	 * 	添加key同时请加注释！！！
	 */
	public static final String[] SS_SWITCH_PRE_KEYS = new String[]{
		SSK_PATH,
		SSK_PATH_DH,
	};
	
	/**
	 * 分区值常量字符串
	 */
	public static final String PARTITION_VAL = "partitionVal";
	
	/**
	 * 其它分区名常量字符串
	 */
	public static final String OTHER_PARTTION = "OTHER";
	
	/**
	 * 文件任务期限
	 */
	public static final String DOCUMENT_TASKDEADLINE = "document.task.deadline";
	
	/**
	 * 公众号上传素材信息
	 */
	public static final String WECAHT_UPLOAD_MATERIAL = "wechat.upload.material";
	
	/**
	 * 公众号上传素材删除
	 */
	public static final String WECAHT_UPLOAD_MATERIAL_DEL = "wechat.upload.material.del";
	
	/**
	 * 公众号发送信息内容
	 */
	public static final String WECAHT_SEND_MESSAGE = "wechat.send.message";
	
	/**
	 * 公众号结果提示
	 */
	public static final String WECAHT_RESULT_MESSAGE = "wechat.result.message";
	
	/**
	 * 送检小程序版本号
	 */
	public static final String MATRIDX_WECAHT_VERSION = "matridx.wechat.version";
	
	/**
	 * 钉钉小程序版本号
	 */
	public static final String MATRIDX_DINGTALK_VERSION = "matridx.dingtalk.version";
	
	/**
	 * 提取列表内部编码添加后缀权限,0:不加 1:根据检测项目，先加RNA
	 */
	public static final String MATRIDX_EXTRACT_NBBMEXTED = "matridx.extract.nbbmextend";
	
	/**
	 * 支付结果查询标记 1 查询线程执行 0 查询线程不存在
	 */
	public static final String PAY_RESULT_FLAG = "pay.result.flag";

	/**
	 * 送检报告发送完成消息是否发送控制
	 */
	public static final String MATRIDX_SENDREPORT_FINISHMESSAGE = "matridx.sendreport.finishmessage";
	/**
	 * 其他入库引用领料单路径前缀
	 */
	public static final String URL_PREFIX_RECEIVEMATERIEL = "urlprefix.receiveMateriel";

	/*
		设备发送命令最大错误次数
	 */
	public static final String CHANNEL_ERROR_MAX_COUNT = "channel.error.max.count";


}
