package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.dingtalkhrm_1_0.models.QueryDismissionStaffIdListHeaders;
import com.aliyun.dingtalkhrm_1_0.models.QueryDismissionStaffIdListRequest;
import com.aliyun.dingtalkhrm_1_0.models.QueryDismissionStaffIdListResponse;
import com.aliyun.dingtalkhrm_1_0.models.QueryHrmEmployeeDismissionInfoHeaders;
import com.aliyun.dingtalkhrm_1_0.models.QueryHrmEmployeeDismissionInfoRequest;
import com.aliyun.dingtalkhrm_1_0.models.QueryHrmEmployeeDismissionInfoResponse;
import com.aliyun.dingtalkhrm_1_0.models.QueryHrmEmployeeDismissionInfoResponseBody;
import com.aliyun.dingtalkrobot_1_0.Client;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOResponse;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskHeaders;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskRequest;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileResponse;
import com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders;
import com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest;
import com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsResponse;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastHeaders;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastRequest;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastResponse;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastResponseBody;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceHeaders;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceRequest;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceRequest.StartProcessInstanceRequestApprovers;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByVoiceRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByVoiceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetattcolumnsRequest;
import com.dingtalk.api.request.OapiAttendanceGetcolumnvalRequest;
import com.dingtalk.api.request.OapiAttendanceGetleavetimebynamesRequest;
import com.dingtalk.api.request.OapiAttendanceGetsimplegroupsRequest;
import com.dingtalk.api.request.OapiAttendanceGroupMemberListRequest;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiAttendanceShiftListRequest;
import com.dingtalk.api.request.OapiAttendanceShiftQueryRequest;
import com.dingtalk.api.request.OapiAttendanceVacationQuotaListRequest;
import com.dingtalk.api.request.OapiAttendanceVacationQuotaUpdateRequest;
import com.dingtalk.api.request.OapiAttendanceVacationTypeListRequest;
import com.dingtalk.api.request.OapiCallBackDeleteCallBackRequest;
import com.dingtalk.api.request.OapiCallBackGetCallBackRequest;
import com.dingtalk.api.request.OapiCallBackRegisterCallBackRequest;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiCheckinRecordRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.dingtalk.api.request.OapiProcessGetByNameRequest;
import com.dingtalk.api.request.OapiProcessInstanceTerminateRequest;
import com.dingtalk.api.request.OapiProcessInstanceTerminateRequest.TerminateProcessInstanceRequestV2;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.request.OapiProcessinstanceCspaceInfoRequest;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQuerydimissionRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQueryonjobRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeV2ListRequest;
import com.dingtalk.api.request.OapiUserListRequest;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2DepartmentListparentbydeptRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubidRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiAttendanceGetattcolumnsResponse;
import com.dingtalk.api.response.OapiAttendanceGetattcolumnsResponse.ColumnForTopVo;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse;
import com.dingtalk.api.response.OapiAttendanceGetleavetimebynamesResponse;
import com.dingtalk.api.response.OapiAttendanceGetsimplegroupsResponse;
import com.dingtalk.api.response.OapiAttendanceGroupMemberListResponse;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceShiftListResponse;
import com.dingtalk.api.response.OapiAttendanceShiftQueryResponse;
import com.dingtalk.api.response.OapiAttendanceVacationQuotaListResponse;
import com.dingtalk.api.response.OapiAttendanceVacationQuotaUpdateResponse;
import com.dingtalk.api.response.OapiAttendanceVacationTypeListResponse;
import com.dingtalk.api.response.OapiCallBackDeleteCallBackResponse;
import com.dingtalk.api.response.OapiCallBackGetCallBackResponse;
import com.dingtalk.api.response.OapiCallBackRegisterCallBackResponse;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiCheckinRecordResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiProcessGetByNameResponse;
import com.dingtalk.api.response.OapiProcessInstanceTerminateResponse;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.dingtalk.api.response.OapiProcessinstanceCspaceInfoResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerydimissionResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerydimissionResponse.Paginator;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQueryonjobResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeV2ListResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeV2ListResponse.EmpRosterFieldVo;
import com.dingtalk.api.response.OapiUserListResponse;
import com.dingtalk.api.response.OapiUserListResponse.Userlist;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse.DeptGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListparentbydeptResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse.DeptBaseResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubidResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.dingtalk.api.response.OapiV2UserListResponse.ListUserResponse;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.email.EmailUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import static com.aliyun.teautil.Common.empty;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;


@Configuration
public class DingTalkUtil {

	@Autowired
	RedisUtil redisUtil;

	private final Logger logger = LoggerFactory.getLogger(DingTalkUtil.class);

	/**
	 * 钉钉获取企业内部应用的access_token的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/orgapp/obtain-orgapp-token
	 */
	public final String URL_GET_TOKKEN = "https://oapi.dingtalk.com/gettoken";

	/**
	 * 钉钉获取单个审批实例详情的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/orgapp/obtains-the-details-of-a-single-approval-instance
	 */
	public final String URL_GET_SHR = "https://oapi.dingtalk.com/topapi/processinstance/get";
	/**
	 * 获取指定用户的所有父部门列表
	 * https://open.dingtalk.com/document/orgapp/obtains-the-details-of-a-single-approval-instance
	 */
	public final String URL_GET_PARENT_LIST = "https://oapi.dingtalk.com/topapi/v2/department/listparentbyuser";

	/**
	 * 钉钉获取部门列表的URL【1.0版接口】
	 * https://open.dingtalk.com/document/orgapp/obtain-the-department-list
	 */
	public final String URL_GET_DEPART_LIST = "https://oapi.dingtalk.com/department/list";

	/**
	 * 钉钉获取部门列表的URL【2.0版接口】
	 * https://open.dingtalk.com/document/orgapp/obtain-the-department-list-v2
	 */
	public final String URL_GET_DEPART_LIST_V2 = "https://oapi.dingtalk.com/topapi/v2/department/listsub";

	/**
	 * 钉钉获取指定部门的所有父部门列表的URL【2.0版接口】
	 * https://open.dingtalk.com/document/orgapp/query-the-list-of-all-parent-departments-of-a-department
	 */
	public static final String FDEPARTMENT_LIST = "https://oapi.dingtalk.com/topapi/v2/department/listparentbydept";
	/**
	 * 钉钉获取子部门ID列表的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/orgapp/obtain-a-sub-department-id-list-v2
	 */
	public static final String CDEPARTMENT_LIST = "https://oapi.dingtalk.com/topapi/v2/department/listsubid";

	/**
	 * 钉钉查询用户详情的URL【1.0版接口】
	 * https://open.dingtalk.com/document/orgapp/queries-user-details
	 */
	public final String URL_USER_GET = "https://oapi.dingtalk.com/user/get";

	/**
	 * 钉钉查询用户详情的URL【2.0版接口】
	 * https://open.dingtalk.com/document/orgapp/queries-the-complete-information-of-a-department-user
	 */
	public final String URL_USER_GET_V2 = "https://oapi.dingtalk.com/topapi/v2/user/get";

	/**
	 * 钉钉获取部门用户详情的URL【1.0版接口】
	 */
	public final String URL_USER_LIST = "https://oapi.dingtalk.com/user/list";

	/**
	 * 钉钉获取部门用户详情的URL【2.0版接口】
	 * https://open.dingtalk.com/document/orgapp/queries-the-complete-information-of-a-department-user
	 */
	public final String URL_USER_LIST_V2 = "https://oapi.dingtalk.com/topapi/v2/user/list";

	/**
	 * 钉钉发送普通消息的URL【1.0版接口】
	 * https://open.dingtalk.com/document/orgapp/send-normal-messages
	 */
	public final String URL_SEND_MESS = "https://oapi.dingtalk.com/message/send_to_conversation";

	/**
	 * 钉钉发送工作通知的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/orgapp/asynchronous-sending-of-enterprise-session-messages
	 */
	public final String URL_SEND_WORKMESS = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

	/**
	 * 钉钉发送消息到企业群的URL【1.0版接口】
	 * https://open.dingtalk.com/document/orgapp/send-group-messages
	 * 存在新版本
	 * 1、发送群助手消息：接口地址 https://oapi.dingtalk.com/topapi/im/chat/scencegroup/message/send_v2
	 * 文档地址：https://open.dingtalk.com/document/orgapp/send-group-helper-message
	 * 2、机器人发送群聊消息
	 * 文档地址：https://open.dingtalk.com/document/orgapp/the-robot-sends-a-group-message
	 */
	public final String URL_SEND_GROUPMESS = "https://oapi.dingtalk.com/chat/send";

	/**
	 * 钉钉群消息发送@消息的URL【未找到开发文档】
	 */
	public final String URL_SEND_GROUPMESSAT = "https://oapi.dingtalk.com/robot/send";

	/**
	 * 钉钉上传媒体文件的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/orgapp/upload-media-files
	 */
	public final String URL_SEND_FILE = "https://oapi.dingtalk.com/media/upload";

	/**
	 * 钉钉获取部门用户签到记录的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/orgapp/get-check-in-data
	 */
	public final String URL_CHECKIN_RECORD = "https://oapi.dingtalk.com/checkin/record";

	/**
	 * 钉钉获取打卡详情的URL【已为最新版本2023.02.28】
	 * https://open.dingtalk.com/document/isvapp/attendance-clock-in-record-is-open
	 */
	public final String URL_CHECKIN_LISTRECORD = "https://oapi.dingtalk.com/attendance/listRecord";

	/**
	 * 钉钉获取模板code的URL【1.0版接口】
	 * https://open.dingtalk.com/document/isvapp/obtains-the-template-code-based-on-the-template-name
	 * 存在新版本
	 * 文档地址：https://open.dingtalk.com/document/orgapp/obtain-the-template-code
	 */
	public final String URL_GET_PROCESSCODE = "https://oapi.dingtalk.com/topapi/process/get_by_name";

	/**
	 * 钉钉发起审批实例的URL【1.0版接口】
	 * https://open.dingtalk.com/document/isvapp/initiate-approval
	 * 存在新版本
	 * 文档地址：https://open.dingtalk.com/document/orgapp/create-an-approval-instance
	 */
	public final String URL_CREATE_INSTANCE = "https://oapi.dingtalk.com/topapi/processinstance/create";

	/**
	 * 钉钉获取审批钉盘空间信息的URL【1.0版接口】
	 * 存在新版本
	 * 文档地址：https://open.dingtalk.com/document/orgapp/obtains-the-information-about-approval-nail-disk
	 */
	public final String URL_GET_SPACEID = "https://oapi.dingtalk.com/topapi/processinstance/cspace/info";

	/**
	 * 钉钉删除事件回调接口的URL【历史文档】
	 * https://open.dingtalk.com/document/orgapp/delete-an-event-callback-interface
	 */
	public final String URL_DELETE_CALLBACK = "https://oapi.dingtalk.com/call_back/delete_call_back";

	/**
	 * 钉钉查询订阅事件的URL【历史文档】
	 * https://open.dingtalk.com/document/orgapp/the-query-event-callback-interface
	 */
	public final String URL_GET_CALLBACK = "https://oapi.dingtalk.com/call_back/get_call_back";

	/**
	 * 钉钉注册回调事件的URL【历史文档】
	 * https://open.dingtalk.com/document/orgapp/registers-event-callback-interfaces
	 */
	public final String URL_REGISTER_CALLBACK = "https://oapi.dingtalk.com/call_back/register_call_back";

	/**
	 * 钉钉撤销审批实例的URL【1.0版接口】
	 * 存在新版本
	 * 文档地址：https://open.dingtalk.com/document/orgapp/revoke-an-approval-instance
	 */
	public final String URL_DINGTALK_CANCEL = "https://oapi.dingtalk.com/topapi/process/instance/terminate";

	/**
	 * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
	 */
	public static final String CHECK_URL = "check_url";

	/**
	 * 审批任务回调，更多回调类型 参考 https://ding-doc.dingtalk.com/doc#/serverapi2/skn8ld
	 */
	public static final String BPMS_TASK_CHANGE = "bpms_task_change";

	/**
	 * 审批实例回调
	 */
	public static final String BPMS_INSTANCE_CHANGE = "bpms_instance_change";

	/**
	 * 相应钉钉回调时的值
	 */
	public static final String CALLBACK_RESPONSE_SUCCESS = "success";

	/**
	 * 钉钉获取员工离职信息的URL【1.0版接口】
	 * https://open.dingtalk.com/document/orgapp/obtain-multiple-employee-demission-information
	 * 存在新版本
	 * 文档地址：https://open.dingtalk.com/document/orgapp/obtain-resignation-information-of-employees-new-version
	 */
	public static final String SEPARATING_EMPLOYEE_INFO = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/listdimission";

	/**
	 * 钉钉获取离职员工列表的URL【1.0版接口】
	 * https://open.dingtalk.com/document/orgapp/intelligent-personnel-query-company-turnover-list
	 * 存在新版本
	 * 文档地址：https://open.dingtalk.com/document/orgapp/obtain-the-list-of-employees-who-have-left
	 */
	public static final String SEPARATING_EMPLOYEE_LIST = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/querydimission";

	/**
	 * 钉钉获取员工花名册字段信息的URL【2.0版接口】
	 * https://open.dingtalk.com/document/orgapp/intelligent-personnel-obtain-employee-roster-information
	 */
	public static final String ROSTER_INFO = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/v2/list";

	/**
	 * 钉钉获取部门详情的URL【2.0版接口】
	 * https://open.dingtalk.com/document/orgapp/query-department-details0-v2
	 */
	public static final String DEPARTMENT_DEAILS = "https://oapi.dingtalk.com/topapi/v2/department/get";
	/**
	 *	调用本接口，获取企业智能考勤报表中的列信息。
	 * 	通过获取列信息中的ID值，可以根据列的ID查询考勤智能报表中该列的统计数据，
	 */
	public static final String ATTENDANCE_ATTCOLUMNS = "https://oapi.dingtalk.com/topapi/attendance/getattcolumns";
	/**
	 *	调用本接口，获取钉钉智能考勤报表的列值数据
	 */
	public static final String ATTENDANCE_ATTCOLUMNVAl = "https://oapi.dingtalk.com/topapi/attendance/getcolumnval";
	/**
	 *	调用本接口，获取钉钉智能考勤假期报表的列值数据
	 */
	public static final String ATTENDANCE_LEAVEATTCOLUMNVAl = "https://oapi.dingtalk.com/topapi/attendance/getleavetimebynames";
	/**
	 * 获取钉钉智能考勤报表的假期数据
	 */
	public static final String ATTENDANCE_LEAVETIMEBYNAMES = "https://oapi.dingtalk.com/topapi/attendance/getleavetimebynames";
	/**
	 * 批量获取考勤组信息
	 */
	public static final String ATTENDANCE_SIMPLEGROUPS = "https://oapi.dingtalk.com/topapi/attendance/getsimplegroups";
	/**
	 * 获取参与考勤人员
	 */
	public static final String ATTENDANCE_MEMBER_LIST = "https://oapi.dingtalk.com/topapi/attendance/group/member/list";
	/***
	 * 获取在职员工
	 */
	private static  final String EMPLOYEE_QUERYONJOB = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/queryonjob";
	/**
	 * 批量更新钉钉假期总额
	 */
	public static final String VACATION_QUOTA_UPDATE = "https://oapi.dingtalk.com/topapi/attendance/vacation/quota/update";
	@Value("${matridx.dingtalk.appkey:}")
	private String APP_KEY;
	@Value("${matridx.dingtalk.appsecret:}")
	private String APP_SECRET;
	@Value("${matridx.dingtalk.aeskey:}")
	private String AES_KEY;
	@Value("${matridx.dingtalk.callbackurl:}")
	private String CALLBACK_URL;
	@Value("${matridx.dingtalk.token:}")
	private String TOKEN;
	@Value("${matridx.aliyunVoice.accessKeyId:}")
	private final String accessKeyId = null;
	@Value("${matridx.aliyunVoice.accessSecret:}")
	private final String accessSecret = null;
	@Value("${matridx.dingtalk.miniappkey:}")
	private String DING_APP_KEY;
	@Value("${matridx.dingtalk.miniappsecret:}")
	private String DING_APP_SECRET;
	@Value("${matridx.dingtalk.cropid:}")
	private String DING_CRORP_ID;
	@Value("${matridx.dingtalk.agentid:}")
	private String DING_AGENT_ID;
	//	@Value("${matridx.dingtalk.robotAppkey:}")
//	private String robotAppkey;
//	@Value("${matridx.dingtalk.robotAppsecret:}")
//	private String  robotAppsecret;
//	@Value("${matridx.dingtalk.robotCode:}")
//	private String robotCode;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Value("${matridx.dingtalk.userId:}")
	private String userId;
	@Value("${matridx.dingtalk.spaceId:}")
	private String spaceId;
	@Autowired
	private IGrszService grszService;

	public String getRobotAccessToken() throws ApiException {
		DBEncrypt db = new DBEncrypt();
		DingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
		OapiGettokenRequest request = new OapiGettokenRequest();
		request.setAppkey(db.dCode(DING_APP_KEY));
		request.setAppsecret(db.dCode(DING_APP_SECRET));
		request.setHttpMethod("GET");
		OapiGettokenResponse response = client.execute(request);

		return response.getAccessToken();
	}

	public Client createClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new Client(config);
	}

	public com.aliyun.dingtalktodo_1_0.Client createTodoClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalktodo_1_0.Client(config);
	}

	/**
	 * @Description: 去重发送消息
	 * @param userList
	 * @param title
	 * @param msgcontent
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/6/20 11:36
	 */
	public boolean sendWorkMessageByUserList(List<User> userList, String title, String msgcontent) {
		if(userList!=null && !userList.isEmpty()){
			List<User> users = userList.stream().collect(
					collectingAndThen(
							toCollection(() -> new TreeSet<>(comparing(User::getYhid))),ArrayList::new)
			);
			for (User user:users){
				if(StringUtil.isNotBlank(user.getYhm()) && StringUtil.isNotBlank(user.getYhid())){
					sendWorkMessage(user.getYhm(),user.getYhid(),title,msgcontent);
				}
			}
		}
		return true;
	}
	/**
	 * @Description: 去重发送消息
	 * @param spgwcyList
	 * @param title
	 * @param msgcontent
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/6/20 11:36
	 */
	public boolean sendWorkMessageBySpgwcyList(String ywid,List<SpgwcyDto> spgwcyList, String title, String msgcontent) {
		if(spgwcyList!=null && !spgwcyList.isEmpty()){
			List<SpgwcyDto> spgwcyDtos = spgwcyList.stream().collect(
					collectingAndThen(
							toCollection(() -> new TreeSet<>(comparing(SpgwcyDto::getYhid))),ArrayList::new)
			);
			for (SpgwcyDto spgwcyDto:spgwcyDtos){
				boolean messageFlag = true;
				if(StringUtil.isNotBlank(spgwcyDto.getYhid()) && StringUtil.isNotBlank(ywid)){
					Object object = redisUtil.hget("Grsz:"+spgwcyDto.getYhid(),ywid);
					if(object!=null){
						messageFlag = "1".equals(String.valueOf(object));
					}else{
						GrszDto grszDto = new GrszDto();
						grszDto.setYhid(spgwcyDto.getYhid());
						grszDto.setGlxx(ywid);
						GrszDto dto = grszService.getDtoByYhidAndGlxx(grszDto);
						if(dto!=null){
							messageFlag = "1".equals(dto.getSzz());
							redisUtil.hset("Grsz:"+spgwcyDto.getYhid(),ywid,dto.getSzz(),-1);
						}else{
							Object objectMr = redisUtil.hget("Grsz:MR",ywid);
							if(objectMr!=null){
								messageFlag = "1".equals(String.valueOf(objectMr));
								redisUtil.hset("Grsz:"+spgwcyDto.getYhid(),ywid,String.valueOf(objectMr),-1);
							}else {
								GrszDto grszDtoMr = new GrszDto();
								grszDtoMr.setYhid("MR");
								grszDtoMr.setGlxx(ywid);
								GrszDto dtoMr = grszService.getDtoByYhidAndGlxx(grszDtoMr);
								if(dtoMr!=null){
									messageFlag = "1".equals(dtoMr.getSzz());
									redisUtil.hset("Grsz:MR",ywid,dtoMr.getSzz(),-1);
									redisUtil.hset("Grsz:"+spgwcyDto.getYhid(),ywid,dtoMr.getSzz(),-1);
								}else{
									redisUtil.hset("Grsz:MR",ywid,"1",-1);
									redisUtil.hset("Grsz:"+spgwcyDto.getYhid(),ywid,"1",-1);
								}
							}
						}
					}
				}
				if(messageFlag && StringUtil.isNotBlank(spgwcyDto.getYhm()) && StringUtil.isNotBlank(spgwcyDto.getYhid())){
					sendWorkMessage(spgwcyDto.getYhm(),spgwcyDto.getYhid(),title,msgcontent);
				}
			}
		}
		return true;
	}
	public boolean sendWorkMessage(String yhm, String userId, String title, String msgcontent) {
		String msgKey = "sampleMarkdown";
		String msgParam = "{\n" +
				"        'title': '" + title + "',\n" +
				"        'text': '" + msgcontent + "',\n" +
				"    }";
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("yhm",yhm);
		map.put("msgKey",msgKey);
		map.put("msgParam",msgParam);
		amqpTempl.convertAndSend("send.message.dispose.exchange","Send.message.dispose", com.alibaba.fastjson.JSONObject.toJSONString(map));
		return true;
	}

	public boolean sendWorkDyxxMessage(String ywid,String yhid,String yhm, String userId, String title, String msgcontent) {
		boolean messageFlag = true;
		if(StringUtil.isNotBlank(yhid) && StringUtil.isNotBlank(ywid)){
			Object object = redisUtil.hget("Grsz:"+yhid,ywid);
			if(object!=null){
				messageFlag = "1".equals(String.valueOf(object));
			}else{
				GrszDto grszDto = new GrszDto();
				grszDto.setYhid(yhid);
				grszDto.setGlxx(ywid);
				GrszDto dto = grszService.getDtoByYhidAndGlxx(grszDto);
				if(dto!=null){
					messageFlag = "1".equals(dto.getSzz());
					redisUtil.hset("Grsz:"+yhid,ywid,dto.getSzz(),-1);
				}else {
					Object objectMr = redisUtil.hget("Grsz:MR",ywid);
					if(objectMr!=null){
						messageFlag = "1".equals(String.valueOf(objectMr));
						redisUtil.hset("Grsz:"+yhid,ywid,String.valueOf(objectMr),-1);
					}else{
						GrszDto grszDtoMr = new GrszDto();
						grszDtoMr.setYhid("MR");
						grszDtoMr.setGlxx(ywid);
						GrszDto dtoMr = grszService.getDtoByYhidAndGlxx(grszDtoMr);
						if(dtoMr!=null){
							messageFlag = "1".equals(dtoMr.getSzz());
							redisUtil.hset("Grsz:MR",ywid,dtoMr.getSzz(),-1);
							redisUtil.hset("Grsz:"+yhid,ywid,dtoMr.getSzz(),-1);
						}else{
							redisUtil.hset("Grsz:MR",ywid,"1",-1);
							redisUtil.hset("Grsz:"+yhid,ywid,"1",-1);
						}
					}
				}
			}
		}
		if(messageFlag){
			sendWorkMessage(yhm, userId, title, msgcontent);
		}
		return true;
	}


	public void paramCardMessageThread(Map<String, Object> params) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				paramCardMessage(params);
			}
		};
		thread.start();
	}

	public boolean paramCardMessage(Map<String, Object> params) {
		try {
			if (StringUtil.isBlank(String.valueOf(params.get("userId"))))
				return false;
			if (StringUtil.isBlank(String.valueOf(params.get("msgcontent"))))
				return false;
			if (StringUtil.isBlank(String.valueOf(params.get("title"))))
				return false;
			if (params.get("btnJsonList") == null)
				return false;
			String userId = String.valueOf(params.get("userId"));
			String msgcontent = String.valueOf(params.get("msgcontent"));
			String title = String.valueOf(params.get("title"));
			@SuppressWarnings("unchecked")
			List<BtnJsonList> btnJsonList = (List<BtnJsonList>) params.get("btnJsonList");
			DBEncrypt db = new DBEncrypt();
			Client client = createClient();
			BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
			batchSendOTOHeaders.xAcsDingtalkAccessToken = getRobotAccessToken();
			BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest()
					.setRobotCode(db.dCode(DING_APP_KEY))
					.setUserIds(Collections.singletonList(userId))
					.setMsgKey("sampleActionCard1")
					.setMsgParam("{\n" +
							"        'title': '" + title + "',\n" +
							"        'text': '" + msgcontent + "',\n" +
							"        'actionTitle': '" + btnJsonList.get(0).getTitle() + "',\n" +
							"        'actionURL': '" + btnJsonList.get(0).getActionUrl() + "',\n" +
							"    }");
			BatchSendOTOResponse batchSendOTOResponse = client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
			logger.error("paramCardMessage Robot+Body:" + JSON.toJSONString(batchSendOTOResponse.getBody()) + "  userid:" + params.get("userId"));
		} catch (TeaException err) {
			logger.error("paramCardMessage TeaException:" + err.getMessage());
			if (!empty(err.code) && !empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("paramCardMessage Robot+code:" + err.code);
				logger.error("paramCardMessage Robot+message:" + err.message);
			}
		} catch (Exception _err) {
			logger.error("paramCardMessage Exception:" + _err.getMessage());
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!empty(err.code) && !empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("paramCardMessage Robot+code2:" + err.code);
				logger.error("paramCardMessage Robot+message2:" + err.message);
			}
		}
		return true;
	}

	public boolean sendActionCardMessage( String webHook, String title, String msgcontent, List<BtnJsonList> btnJsonList) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(webHook);
			OapiRobotSendRequest request = new OapiRobotSendRequest();
			request.setMsgtype("actionCard");
			OapiRobotSendRequest.Actioncard actioncard = new OapiRobotSendRequest.Actioncard();
			actioncard.setTitle(title);
			actioncard.setText(msgcontent);
			actioncard.setBtnOrientation("0");
			List<OapiRobotSendRequest.Btns> list = new ArrayList<>();
			OapiRobotSendRequest.Btns btns = new OapiRobotSendRequest.Btns();
			btns.setTitle(btnJsonList.get(0).getTitle());
			btns.setActionURL(btnJsonList.get(0).getActionUrl());
			list.add(btns);
			actioncard.setBtns(list);
			request.setActionCard(actioncard);
			OapiRobotSendResponse response = client.execute(request);
			logger.error("response:"+response);
		} catch (ApiException e) {
			logger.error("sendActionCardMessage Robot+message:" + e.getMessage());
		}catch (Exception e) {
			logger.error("sendActionCardMessage Robot+message2:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 根据配置文件中的参数，获取钉钉的token
	 * 获取钉钉的token,正常情况下access_token有效期为7200秒。
	 * 为防止多次取钉钉token，所以每个方法调用的时候，先调用这个共通方法一次获取token，然后循环调用发送消息
	 * appKey matridx.dingtalk.appkey
	 * appSecret matridx.dingtalk.appsecret
	 * @return token
	 * @throws RuntimeException
	 */
	public String getToken(String wbcxid) {
		Map<String, String> wbcxInfo = getDingTalkInfo(wbcxid);
		return getToken(wbcxInfo.get("appkey"), wbcxInfo.get("appsecret"));
	}

	/**
	 * 根据传入的参数，获取钉钉的token
	 * 获取钉钉的token,正常情况下access_token有效期为7200秒。
	 * 为防止多次取钉钉token，所以每个方法调用的时候，先调用这个共通方法一次获取token，然后循环调用发送消息
	 * @param appKeyId appid
	 * @param appSecret appsecret
	 * @return token
	 */
	public String getToken(String appKeyId,String appSecret) {
		try {
			if (StringUtil.isBlank(appKeyId)){
				return null;
			}
			DingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
			OapiGettokenRequest request = new OapiGettokenRequest();
			DBEncrypt db = new DBEncrypt();
			request.setAppkey(db.dCode(appKeyId));
			request.setAppsecret(db.dCode(appSecret));
			request.setHttpMethod("GET");
			OapiGettokenResponse response = client.execute(request);
			String accessToken = response.getAccessToken();
			logger.error("重新获取token:"+accessToken);
			return accessToken;
		} catch (ApiException e) {
			logger.error("getAccessToken failed ApiException", e);
//			throw new RuntimeException();
		}
		return null;
	}

	public Map<String, String> getDingTalkInfo(String wbcxid){
		Map<String, String> resultMap = new HashMap<>();
		if(StringUtil.isBlank(wbcxid))
			return getDefaultInfo();
		Object info = redisUtil.hget("WbcxInfo", wbcxid);
		if (info != null){
			//若redis数据库中存在外部程序信息，则设置appkey,apppsecret,agentid,corpid,appid
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(info.toString());
			resultMap.put("appkey",jsonObject.getString("appid"));
			resultMap.put("appsecret",jsonObject.getString("secret"));
			resultMap.put("cropid",jsonObject.getString("cropid"));
			resultMap.put("agentid",jsonObject.getString("agentid"));
			resultMap.put("miniappid",jsonObject.getString("miniappid"));
			resultMap.put("wbcxid",jsonObject.getString("wbcxid"));
			//accessToken = getToken(jsonObject.getString("appid"),jsonObject.getString("secret"));
		}else{
			resultMap = getDefaultInfo();
			//accessToken = getToken(robotAppkey,robotAppsecret);
		}
		return resultMap;
	}

	/**
	 * 根据appid 获取外部程序的钉钉信息
	 * @param appid
	 * @return
	 */
	public Map<String, String> getDingTalkInfoByAppid(String appid){
		if(StringUtil.isBlank(appid))
			return  getDefaultInfo();
		Map<String, String> resultMap = new HashMap<>();
		Map<Object,Object> map = redisUtil.values("WbcxInfo");
		boolean isFind = false;
        for (Object obj : map.keySet()) {
            if (obj != null && StringUtil.isNotBlank(obj.toString())) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(map.get(obj).toString());
                if (appid.equals(jsonObject.getString("appid"))) {
                    isFind = true;
                    resultMap.put("appkey", jsonObject.getString("appid"));
                    resultMap.put("appsecret", jsonObject.getString("secret"));
                    resultMap.put("cropid", jsonObject.getString("cropid"));
                    resultMap.put("agentid", jsonObject.getString("agentid"));
                    resultMap.put("miniappid", jsonObject.getString("miniappid"));
                    resultMap.put("wbcxid", jsonObject.getString("wbcxid"));
                    break;
                }
            }
        }
        if(!isFind)
			return getDefaultInfo();
		return resultMap;
	}

	private Map<String, String> getDefaultInfo()
	{
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("appkey",APP_KEY);
		resultMap.put("appsecret",DING_APP_SECRET);
		resultMap.put("cropid",DING_CRORP_ID);
		resultMap.put("agentid",DING_AGENT_ID);
		resultMap.put("miniappid",DING_APP_KEY);
		resultMap.put("wbcxid","");
		return resultMap;
	}

	/**
	 * 根据配置文件中的参数，获取钉钉的token
	 * 获取钉钉的token,正常情况下access_token有效期为7200秒。
	 * 为防止多次取钉钉token，所以每个方法调用的时候，先调用这个共通方法一次获取token，然后循环调用发送消息
	 * appKey matridx.dingtalk.miniappkey
	 * appSecret matridx.dingtalk.miniappsecret
	 * @return token
	 * @throws RuntimeException
	 */
//	private String getDINGToken() {
//		return getToken(DING_APP_KEY, DING_APP_SECRET);
//	}

	/**
	 * 钉钉获取部门用户详情的URL【1.0版接口】【废弃 2023.03.01】
	 * @param accessToken dd.token
	 * @param departmentId 部门ID
	 * @return List<Userlist> 用户列表(userid和name)
	 */
	public List<Userlist> getAllUsers(String accessToken, long departmentId) {
		try {
			if (StringUtil.isBlank(accessToken))
				return null;
			DingTalkClient client = new DefaultDingTalkClient(URL_USER_LIST);
			OapiUserListRequest request = new OapiUserListRequest();

			request.setHttpMethod("GET");
			request.setDepartmentId(departmentId);
			OapiUserListResponse response = client.execute(request, accessToken);
			List<Userlist> userlists = response.getUserlist();

			return userlists;
		} catch (Exception e) {
			logger.error("getAllUsers failed", e);
//			throw new RuntimeException();
		}
		return null;
	}

	/**
	 * 钉钉获取部门用户详情的URL【2.0版接口】
	 * @param appkey
	 * @param appsecret
	 * @param departmentId 部门ID
	 * @return List<ListUserResponse> 用户列表(userid和name)
	 */
	public List<ListUserResponse> getAllUsersV2ByKeyAndSecret(String appkey,String appsecret, long departmentId) {
		String token = getToken(appkey, appsecret);
		return getAllUsersV2(token, departmentId);
	}
	/**
	 * 钉钉获取部门用户详情的URL【2.0版接口】
	 * @param yhm dd.token
	 * @param departmentId 部门ID
	 * @return List<ListUserResponse> 用户列表(userid和name)
	 */
	public List<ListUserResponse> getAllUsersV2ByYhm(String yhm, long departmentId) {
		String token = getDingTokenByUserId(yhm);
		return getAllUsersV2(token, departmentId);
	}
	/**
	 * 钉钉获取部门用户详情的URL【2.0版接口】
	 * @param accessToken dd.token
	 * @param departmentId 部门ID
	 * @return List<ListUserResponse> 用户列表(userid和name)
	 */
	public List<ListUserResponse> getAllUsersV2(String accessToken, long departmentId) {
		try {
			if (StringUtil.isBlank(accessToken)){
				return null;
			}
			List<ListUserResponse> userlists = new ArrayList<>();
			boolean hasMore = true;
			Long cursor = 0L;
			while (hasMore){
				DingTalkClient client = new DefaultDingTalkClient(URL_USER_LIST_V2);
				OapiV2UserListRequest request = new OapiV2UserListRequest();
				request.setDeptId(departmentId);
				request.setCursor(cursor);
				request.setSize(10L);
				request.setOrderField("modify_desc");
				request.setContainAccessLimit(false);
				request.setLanguage("zh_CN");
				OapiV2UserListResponse response = client.execute(request, accessToken);
				List<ListUserResponse> list = response.getResult().getList();
				userlists.addAll(list);
				hasMore = response.getResult().getHasMore();
				cursor = response.getResult().getNextCursor();
			}
			return userlists;
		} catch (Exception e) {
			logger.error("getAllUsersV2 failed", e);
		}
		return null;
	}

	/**
	 * 钉钉获取子部门ID列表的URL【已为最新版本2023.02.28】
	 * @param accessToken dd.token
	 * @param departmentId 部门ID
	 * @return List<Long> 部门ID列表
	 */
	public List<Long> getNextDepartments(String accessToken, long departmentId) {
		try {
			if (StringUtil.isBlank(accessToken)){
				return null;
			}
			DingTalkClient client = new DefaultDingTalkClient(CDEPARTMENT_LIST);
			OapiV2DepartmentListsubidRequest req = new OapiV2DepartmentListsubidRequest();
			req.setDeptId(departmentId);
			OapiV2DepartmentListsubidResponse rsp = null;
			try {
				rsp = client.execute(req, accessToken);
			} catch (ApiException e) {
				e.printStackTrace();
			}
			List<Long> list = rsp.getResult().getDeptIdList();

			return list;
		} catch (Exception e) {
			logger.error("getNextDepartments failed", e);
		}
		return null;
	}
	/**
	 * 钉钉获取子部门ID列表的URL【已为最新版本2023.02.28】
	 * @param appkey appkey
	 * @param departmentId 部门ID
	 * @return List<Long> 部门ID列表
	 */
	public List<Long> getNextDepartmentsByKeyAndSecret(String appkey,String appsecret, long departmentId) {
		String token = getToken(appkey, appsecret);
		return getNextDepartments(token, departmentId);
	}
	/**
	 * 钉钉获取子部门ID列表的URL【已为最新版本2023.02.28】
	 * @param yhm yhm
	 * @param departmentId 部门ID
	 * @return List<Long> 部门ID列表
	 */
	public List<Long> getNextDepartmentsByYhm(String yhm, long departmentId) {
		String token = getDingTokenByUserId(yhm);
		return getNextDepartments(token, departmentId);
	}

	/**
	 * 钉钉获取部门列表的URL【1.0版接口】【废弃 2023.03.01】
	 * @param accessToken
	 * @Description:根据部门ID获取用户信息(userid和name)
	 */
	public List<Department> getAllDepartments(String accessToken) {
		try {
			if (StringUtil.isBlank(accessToken)){
				return null;
			}
			DingTalkClient client = new DefaultDingTalkClient(URL_GET_DEPART_LIST);
			OapiDepartmentListRequest request = new OapiDepartmentListRequest();
			request.setHttpMethod("GET");
			request.setId("1");
			OapiDepartmentListResponse response = client.execute(request, accessToken);
			List<Department> deplists = response.getDepartment();
			return deplists;
		} catch (Exception e) {
			logger.error("getAllDepartments failed", e);
		}
		return null;
	}

	/**
	 * 钉钉获取部门列表的URL【2.0版接口】
	 * @param appkey
	 * @param appsecret
	 * @Description:根据部门ID获取用户信息(userid和name)
	 */
	public List<DeptBaseResponse> getAllDepartmentsV2ByKeyAndSecret(String appkey,String appsecret){
		String token = getToken(appkey,appsecret);
		return getAllDepartmentsV2(token);
	}
	/**
	 * 钉钉获取部门列表的URL【2.0版接口】
	 * @param yhm
	 * @Description:根据部门ID获取用户信息(userid和name)
	 */
	public List<DeptBaseResponse> getAllDepartmentsV2ByYhm(String yhm){
		String token = getDingTokenByUserId(yhm);
		return getAllDepartmentsV2(token);
	}
	/**
	 * 钉钉获取部门列表的URL【2.0版接口】
	 * @param accessToken
	 * @Description:根据部门ID获取用户信息(userid和name)
	 */
	public List<DeptBaseResponse> getAllDepartmentsV2(List<DeptBaseResponse> allDepartments,String accessToken,DingTalkClient client,OapiV2DepartmentListsubRequest request,OapiV2DepartmentListsubResponse response,Long deptId) {
		try {
			if (StringUtil.isBlank(accessToken)){
				return null;
			}
			request.setDeptId(deptId);
			request.setLanguage("zh_CN");
			response = client.execute(request, accessToken);
			List<DeptBaseResponse> deplists = response.getResult();
			if (deplists!=null&&!deplists.isEmpty()){
				for (DeptBaseResponse deptBaseResponse : deplists) {
					if (deptBaseResponse.getDeptId()!=null && !deptBaseResponse.getDeptId().equals(deptId)){
						allDepartments.add(deptBaseResponse);
						getAllDepartmentsV2(allDepartments,accessToken,client,request, null,deptBaseResponse.getDeptId());
					}
				}
			}
			return allDepartments;
		} catch (Exception e) {
			logger.error("getAllDepartmentsV2 failed", e);
		}
		return null;
	}
	/**
	 * 钉钉获取部门列表的URL【2.0版接口】
	 * @param accessToken
	 * @Description:递归获取所有部门
	 */
	public List<DeptBaseResponse> getAllDepartmentsV2(String accessToken) {
		List<DeptBaseResponse> allDepartments = new ArrayList<>();
		DingTalkClient client = new DefaultDingTalkClient(URL_GET_DEPART_LIST_V2);
		OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
		OapiV2DepartmentListsubResponse response = new OapiV2DepartmentListsubResponse();
		//获取一级部门
		allDepartments = getAllDepartmentsV2(allDepartments,accessToken,client,request,response, 1L);
		return allDepartments;
	}

	/**
	 * 钉钉发送工作通知OLD
	 * @param userId     接收者的用户userid列表
	 * @param msgcontent 消息内容
	 * @Description:发送消息
	 * @Method:sendDDMessage
	 * @Authod:zhang_cq
	 * @Date:2018/3/19 下午4:55
	 */
	public boolean sendWorkMessageOld(String token, String userId, String title, String msgcontent) {
		return sendWorkMessage(token, userId, title, msgcontent, null);
	}

	/**
	 * 钉钉发送工作通知NewThread
	 * @Description:发送消息,新线程
	 * @Method:sendWorkMessageThread
	 * @Authod:yaojiawei
	 * @Date:2022/7/21 下午5:17
	 */
	public void sendWorkMessageThread(Map<String, String> params) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				paramWorkMessage(params);
			}
		};
		thread.start();
	}

	/**
	 * 钉钉发送工作通知【整合方法】
	 * @param agentId 发送消息时使用的微应用的AgentID
	 * @param userIds 接收者的userid列表，最大用户列表长度100。
	 * @param deptIds 接收者的部门id列表，最大列表长度20。接收者是部门ID时，包括子部门下的所有用户。
	 * @param msgType 工作通知类型：text（文本）、image（图片）、voice（语音）、file（文件）、link（链接）、oa（OA）、markdown（markdown）、action_card（消息卡片）
	 * @param token  钉钉token
	 * @param msg 消息内容：OapiMessageCorpconversationAsyncsendV2Request.Text.(Image、Voice、File、Link、OA、Markdown、ActionCard)
	 * @return
	 */
	public boolean sendDingTalkWorkMessage(String agentId, String userIds, String deptIds, String msgType, String token, OapiMessageCorpconversationAsyncsendV2Request.Msg msg) {
		try {
			DBEncrypt db = new DBEncrypt();
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_WORKMESS);
			OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
			req.setAgentId(Long.parseLong(db.dCode(agentId)));//发送消息时使用的微应用的AgentID。企业内部应用可在开发者后台的应用详情页面查看。
			req.setToAllUser(false); //是否发送给企业全部用户。当设置为false时必须指定userid_list或dept_id_list其中一个参数的值。
			//接收者的userid列表，最大用户列表长度100。示例：user123,user456
			if (StringUtil.isNotBlank(userIds)){
				req.setUseridList(userIds);
			}
			//接收者的部门id列表，最大列表长度20。示例：123,345
			if (StringUtil.isNotBlank(deptIds)){
				req.setDeptIdList(deptIds);
			}
			msg.setMsgtype(msgType);
			// 文本消息类型为：text;
			if ("text".equals(msgType) && msg.getText() != null){
				//消息类型为文本消息
				//text.content 消息内容，建议500字符以内。
				if (StringUtil.isBlank(msg.getText().getContent())){
					return false;
				}
			}
			// 图片消息类型为：image;
			else if ("image".equals(msgType) && msg.getImage() != null){
				//消息类型为图片消息
				//image.mediaId 媒体文件mediaid，建议宽600像素 x 400像素，宽高比3 : 2。企业内部应用，调用上传媒体文件接口获取mediaId参数值。
				if (StringUtil.isBlank(msg.getImage().getMediaId())){
					return false;
				}
			}
			// 语音消息类型为：voice;
			else if ("voice".equals(msgType) && msg.getVoice() != null){
				//消息类型为语音消息
				//voice.mediaId 媒体文件ID。企业内部应用，调用上传媒体文件接口获取mediaId参数值。
				//voice.duration 正整数，小于60，表示音频时长。
				if (StringUtil.isBlank(msg.getVoice().getMediaId()) || StringUtil.isBlank(msg.getVoice().getDuration())){
					return false;
				}
			}
			// 文件消息类型为：file;
			else if ("file".equals(msgType) && msg.getFile() != null){
				//消息类型为文件消息
				//file.mediaId 媒体文件ID，引用的媒体文件最大10MB。企业内部应用，调用上传媒体文件接口获取mediaId参数值。
				if (StringUtil.isBlank(msg.getFile().getMediaId())){
					return false;
				}
			}
			// 链接消息类型为：link;
			else if ("link".equals(msgType) && msg.getLink() != null){
				//消息类型为链接消息
				//link.messageUrl 消息点击链接地址，当发送消息为小程序时支持小程序跳转链接。企业内部应用参考消息链接说明。
				//link.picUrl 图片URL。企业内部应用，调用上传媒体文件接口获取。
				//link.title 消息标题，建议100字符以内。
				//link.content 消息描述，建议500字符以内。
				if (StringUtil.isBlank(msg.getLink().getMessageUrl()) && StringUtil.isBlank(msg.getLink().getPicUrl()) && StringUtil.isBlank(msg.getLink().getTitle()) && StringUtil.isBlank(msg.getLink().getText())){
					return false;
				}
			}
			// OA消息类型为：oa;
			else if ("oa".equals(msgType) && msg.getOa() != null){
				//消息类型为OA消息
				//oa.messageUrl 消息点击链接地址，当发送消息为小程序时支持小程序跳转链接。企业内部应用参考消息链接说明。
				if (StringUtil.isBlank(msg.getOa().getMessageUrl())){
					return false;
				}
				//oa.pcMessageUrl PC端点击消息时跳转到的地址。非必填
				//oa.head 消息头部内容。
				if (msg.getOa().getHead() != null){
					//oa.head 消息头部内容。
					//head.text 消息的头部标题。企业内部应用:如果是发送工作通知消息，该参数会被替换为当前应用名称;如果是发送消息到企业群或者发送普通消息，该参数有效，长度限制为最多10个字符
					if (StringUtil.isBlank(msg.getOa().getHead().getText())){
						return false;
					}
					//head.bgcolor 消息头部的背景颜色。长度限制为8个英文字符，其中前2为表示透明度，后6位表示颜色值。不要添加0x。
					if (StringUtil.isBlank(msg.getOa().getHead().getBgcolor())){
						msg.getOa().getHead().setBgcolor("2381E9");
					}
					//head.text 消息的头部标题。企业内部应用:如果是发送工作通知消息，该参数会被替换为当前应用名称;如果是发送消息到企业群或者发送普通消息，该参数有效，长度限制为最多10个字符。
					if (StringUtil.isBlank(msg.getOa().getHead().getText())){
						return false;
					}
				} else {
					return false;
				}
				//oa.statusBar 消息状态栏，只支持接收者的userid列表，userid最多不能超过5个人。非必填
				//statusBar.statusValue 状态栏文案。
				//statusBar.statusColor 状态栏背景色，默认为黑色，推荐0xFF加六位颜色值。
				//oa.body 消息体。
				if (msg.getOa().getBody() != null){
					//body.title 消息体的标题，建议50个字符以内。
					//body.form 消息体的表单，最多显示6个，超过会被隐藏。非必填
					//	form.key 消息体的关键字。
					//	form.value 消息体的关键字对应的值。
					//body.rich 单行富文本信息。非必填
					//	rich.num 单行富文本信息的数目。
					//	rich.unit 单行富文本信息的单位。
					//body.content 消息体的内容，最多显示3行。非必填
					//body.image 消息体中的图片，支持图片资源@mediaId。建议宽600像素 x 400像素，宽高比3 : 2。企业内部应用，调用上传媒体文件接口获取。非必填
					//body.fileCount 自定义的附件数目。此数字仅供显示，钉钉不作验证。非必填
					//body.author 自定义的作者名字。非必填
				} else {
					return false;
				}
			}
			// markdown消息类型为：markdown
			else if ("markdown".equals(msgType) && msg.getMarkdown() != null){
				//消息类型为markdown消息
				//markdown.text 首屏会话透出的展示内容。
				//markdown.title markdown格式的消息，最大不超过5000字符。
				if (StringUtil.isBlank(msg.getMarkdown().getTitle()) && StringUtil.isBlank(msg.getMarkdown().getText())){
					return false;
				}
			}
			// 消息卡片消息类型为：action_card
			else if ("action_card".equals(msgType) && msg.getActionCard() != null) {
				//消息类型为消息卡片消息
				//actionCard.markdown 消息内容，支持markdown，语法参考标准markdown语法。建议1000个字符以内。
				if (StringUtil.isBlank(msg.getActionCard().getMarkdown())){
					return false;
				}
				//actionCard.title 透出到会话列表和通知的文案。
				if (StringUtil.isBlank(msg.getActionCard().getTitle())){
					return false;
				}
				//actionCard.singleTitle 非必填。使用整体跳转ActionCard样式时的标题。必须与single_url同时设置，最长20个字符。如果是整体跳转的ActionCard样式，则single_title和single_url必须设置。
				//actionCard.singleUrl 非必填。消息点击链接地址，当发送消息为小程序时支持小程序跳转链接，最长500个字符。
				//actionCard.btnOrientation 非必填。使用独立跳转ActionCard样式时的按钮排列方式：0-按钮竖直排列，1-按钮横向排列。必须与btn_json_list同时设置。
				//actionCard.btnJsonList 非必填。使用独立跳转ActionCard样式时的按钮列表；必须与btn_orientation同时设置，且长度不超过1000字符。如果是独立跳转的ActionCard样式，则btn_json_list和btn_orientation必须设置。
				//	btnJsonList.title 使用独立跳转ActionCard样式时的按钮的标题，最长20个字符。
				//	btnJsonList.actionURL 使用独立跳转ActionCard样式时的跳转链接，最长700个字符。
			}
			// 非以上类型消息
			else {
				return false;
			}
			req.setMsg(msg);
			OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, token);
			Long errorCode = rsp.getErrcode();
			if (errorCode > 0) {
				logger.error(rsp.getErrmsg());
				return false;
			}
		} catch (ApiException e) {
			logger.error("sendDingTalkWorkMessage ApiException:" + e.getErrMsg());
		}catch (Exception e) {
			logger.error("sendDingTalkWorkMessage Exception:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 钉钉发送工作通知
	 * @Description:发送消息
	 * @Method:paramWorkMessage
	 * @Authod:yaojiawei
	 * @Date:2022/7/21 下午3:46
	 */
	public boolean paramWorkMessage(Map<String, String> params) {
		try {
			if (StringUtil.isBlank(params.get("token")))
				return false;
			if (StringUtil.isBlank(params.get("userId")))
				return false;
			if (StringUtil.isBlank(params.get("title")))
				return false;
			if (StringUtil.isBlank(params.get("msgcontent")))
				return false;
			logger.error("sendWorkMessage userId=" + params.get("userId") + " title=" + params.get("title") + " msgcontent=" + params.get("msgcontent"));
			DBEncrypt db = new DBEncrypt();
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_WORKMESS);
			OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
			req.setAgentId(Long.parseLong(db.dCode(DING_AGENT_ID)));
			req.setUseridList(params.get("userId"));
			req.setToAllUser(false); // 是否发送给企业全部用户

			OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
			/*msg.setMsgtype("text");
			msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
			msg.getText().setContent(msgcontent);
			req.setMsg(msg);*/
			msg.setMsgtype("oa");
			OapiMessageCorpconversationAsyncsendV2Request.OA oa = new OapiMessageCorpconversationAsyncsendV2Request.OA();
			OapiMessageCorpconversationAsyncsendV2Request.Head head = new OapiMessageCorpconversationAsyncsendV2Request.Head();
			head.setBgcolor("2381E9");
			oa.setHead(head);
			oa.setMessageUrl(params.get("urlString"));
			OapiMessageCorpconversationAsyncsendV2Request.Body body = new OapiMessageCorpconversationAsyncsendV2Request.Body();
			body.setTitle(params.get("title"));
			body.setContent(params.get("msgcontent"));
			oa.setBody(body);
			msg.setOa(oa);

			req.setMsg(msg);

			OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, params.get("token"));
			Long errorCode = rsp.getErrcode();
			if (errorCode > 0) {
				logger.error(rsp.getErrmsg());
				return false;
			}
		} catch (ApiException e) {
			logger.error("paramWorkMessage ApiException:" + e.getErrMsg());
		}catch (Exception e) {
			logger.error("paramWorkMessage Exception:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 钉钉发送工作通知
	 * @param userId     接收者的用户userid列表
	 * @param msgcontent 消息内容
	 * @Description:发送消息
	 * @Method:sendDDMessage
	 * @Authod:zhang_cq
	 * @Date:2018/3/19 下午4:55
	 */
	public boolean sendWorkMessage(String token, String userId, String title, String msgcontent, String urlString) {

		try {
			if (StringUtil.isBlank(token))
				return false;
			logger.error("sendWorkMessage userId=" + userId + " title=" + title + " msgcontent=" + msgcontent);
			
			//String robotAppId = null;//数据库miniappid=>小程序appid
            /*User user = redisUtil.hugetDto("Users", userId);
            if(user != null) {
            	if(StringUtil.isBlank(user.getDdid()) ||"1".equals(user.getSfsd())||"1".equals(user.getScbj())) {
            		logger.error("因没有用户相应的ddid或者用户已经锁定，此消息暂不发送! " + user.getYhid() + " sd=" + user.getSfsd() + " sc=" + user.getScbj());
	            	return true;
            	}
            }*/
			DBEncrypt db = new DBEncrypt();
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_WORKMESS);
			OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
			req.setAgentId(Long.parseLong(db.dCode(DING_AGENT_ID)));
			req.setUseridList(userId);
			req.setToAllUser(false); // 是否发送给企业全部用户

			OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
			/*msg.setMsgtype("text");
			msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
			msg.getText().setContent(msgcontent);
			req.setMsg(msg);*/
			msg.setMsgtype("oa");
			OapiMessageCorpconversationAsyncsendV2Request.OA oa = new OapiMessageCorpconversationAsyncsendV2Request.OA();
			OapiMessageCorpconversationAsyncsendV2Request.Head head = new OapiMessageCorpconversationAsyncsendV2Request.Head();
			head.setBgcolor("2381E9");
			oa.setHead(head);
			oa.setMessageUrl(urlString);
			OapiMessageCorpconversationAsyncsendV2Request.Body body = new OapiMessageCorpconversationAsyncsendV2Request.Body();
			body.setTitle(title);
			body.setContent(msgcontent);
			oa.setBody(body);
			msg.setOa(oa);

			req.setMsg(msg);

			OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, token);
			Long errorCode = rsp.getErrcode();
			if (errorCode > 0) {
				logger.error(rsp.getErrmsg());
				return false;
			}
		} catch (ApiException e) {
			logger.error("sendWorkMessage ApiException:" + e.getErrMsg());
		}catch (Exception e) {
			logger.error("sendWorkMessage Exception:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 钉钉发送工作通知NewThread
	 * @param userId     接收者的用户userid列表
	 * @param msgcontent 消息内容
	 * @Description:发送消息,新线程
	 * @Method:sendDDMessage
	 * @Authod:zhang_cq
	 * @Date:2018/3/19 下午4:55
	 */
	public void sendWorkMessageThread(String token, String userId, String title, String msgcontent) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				sendWorkMessage(token, userId, title, msgcontent);
			}
		};
		thread.start();
	}

	/**
	 * 钉钉发送卡片消息 rabbit调用【2.0版本】
	 * @param yhm         用户名
	 * @param userId      接收微信对象的userid
	 * @param title       信息标题
	 * @param msgcontent  消息内容
	 * @param btnJsonList 按钮清单 ，需要定义名称和地址 {"title": "一个按钮","action_url": "https://www.taobao.com"},{ "title": "两个按钮","action_url": "https://www.tmall.com" }
	 * @param btnorient   使用独立跳转ActionCard样式时的按钮排列方式，竖直排列(0)，横向排列(1)；必须与btn_json_list同时设置
	 * @Description:发送卡片消息，可以有不同按钮 sendCardMessage
	 */
	public boolean sendCardMessage(String yhm, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList, String btnorient) {
		if ("ActionCard".equals(yhm)){
			sendActionCardMessage(userId,title,msgcontent,btnJsonList);
		}else{
			String msgKey = "sampleActionCard1";
			String msgParam = "{\n" +
					"        'title': '" + title + "',\n" +
					"        'text': '" + msgcontent + "',\n" +
					"        'actionTitle': '" + btnJsonList.get(0).getTitle() + "',\n" +
					"        'actionURL': '" + btnJsonList.get(0).getActionUrl() + "',\n" +
					"    }";
			Map<String,Object> map = new HashMap<>();
			map.put("userId",userId);
			map.put("yhm",yhm);
			map.put("msgKey",msgKey);
			map.put("msgParam",msgParam);
			amqpTempl.convertAndSend("send.message.dispose.exchange","Send.message.dispose", com.alibaba.fastjson.JSONObject.toJSONString(map));
		}
		return true;
	}


	/**
	 * @Description: 经过订阅消息权限检验再发送消息
	 * @param ywid
	 * @param yhm
	 * @param userId
	 * @param title
	 * @param msgcontent
	 * @param btnJsonList
	 * @param btnorient
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/29 16:07
	 */
	public boolean sendCardDyxxMessage(String ywid,String yhid,String yhm, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList, String btnorient) {
		boolean messageFlag = true;
		if(StringUtil.isNotBlank(yhid) && StringUtil.isNotBlank(ywid)){
			Object object = redisUtil.hget("Grsz:"+yhid,ywid);
			if(object!=null){
				messageFlag = "1".equals(String.valueOf(object));
			}else{
				GrszDto grszDto = new GrszDto();
				grszDto.setYhid(yhid);
				grszDto.setGlxx(ywid);
				GrszDto dto = grszService.getDtoByYhidAndGlxx(grszDto);
				if(dto!=null) {
					messageFlag = "1".equals(dto.getSzz());
					redisUtil.hset("Grsz:"+yhid,ywid,dto.getSzz(),-1);
				}else{
					Object objectMr = redisUtil.hget("Grsz:MR",ywid);
					if(objectMr!=null){
						messageFlag = "1".equals(String.valueOf(objectMr));
						redisUtil.hset("Grsz:"+yhid,ywid,String.valueOf(objectMr),-1);
					}else{
						GrszDto grszDtoMr = new GrszDto();
						grszDtoMr.setYhid("MR");
						grszDtoMr.setGlxx(ywid);
						GrszDto dtoMr = grszService.getDtoByYhidAndGlxx(grszDtoMr);
						if(dtoMr!=null) {
							messageFlag = "1".equals(dtoMr.getSzz());
							redisUtil.hset("Grsz:MR",ywid,dtoMr.getSzz(),-1);
							redisUtil.hset("Grsz:"+yhid,ywid,dtoMr.getSzz(),-1);
						}else{
							redisUtil.hset("Grsz:MR",ywid,"1",-1);
							redisUtil.hset("Grsz:"+yhid,ywid,"1",-1);
						}
					}
				}
			}
		}
		if(messageFlag){
			sendCardMessage(yhm, userId, title, msgcontent, btnJsonList, btnorient);
		}
		return true;
	}

	/**
	 * 钉钉发送卡片消息 按钮横向排列
	 * @param userId     接收者的用户userid列表
	 * @param msgcontent 消息内容
	 * @Description:发送卡片消息，可以有不同按钮，按钮横向排列（内容换行要用‘\n\r’）
	 * @Method:sendDDMessage
	 * @Authod:zhang_cq
	 * @Date:2018/3/19 下午4:55
	 */
	public boolean sendCardMessageLa(String token, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList) {
		return sendCardMessage(token, userId, title, msgcontent, btnJsonList, "1");
	}

	/**
	 * 钉钉发送卡片消息 按钮纵向排列
	 * @param userId     接收者的用户userid列表
	 * @param msgcontent 消息内容
	 * @Description:发送卡片消息，可以有不同按钮，按钮纵向排列（内容换行要用‘\n\r’）
	 * @Method:sendDDMessage
	 * @Authod:zhang_cq
	 * @Date:2018/3/19 下午4:55
	 */
	public boolean sendCardMessagePo(String token, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList) {
		return sendCardMessage(token, userId, title, msgcontent, btnJsonList, "0");
	}

	/**
	 * 钉钉发送卡片消息 新线程发起rabbit调用【2.0版本】
	 * @param token       微信的toekn
	 * @param userId      接收微信对象的userid
	 * @param title       信息标题
	 * @param msgcontent  消息内容
	 * @param btnJsonList 按钮清单 ，需要定义名称和地址 {"title": "一个按钮","action_url": "https://www.taobao.com"},{ "title": "两个按钮","action_url": "https://www.tmall.com" }
	 * @param btnorient   使用独立跳转ActionCard样式时的按钮排列方式，竖直排列(0)，横向排列(1)；必须与btn_json_list同时设置
	 * @Description:发送卡片消息，可以有不同按钮,新启一个线程 sendCardMessage
	 */
	public void sendCardMessageThread(String token, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList, String btnorient) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				sendCardMessage(token, userId, title, msgcontent, btnJsonList, btnorient);
			}
		};
		thread.start();
	}

	public void sendCardDyxxMessageThread(String ywid,String yhid,String token, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList, String btnorient) {
		boolean messageFlag = true;
		if(StringUtil.isNotBlank(yhid) && StringUtil.isNotBlank(ywid)){
			Object object = redisUtil.hget("Grsz:"+yhid,ywid);
			if(object!=null){
				messageFlag = "1".equals(String.valueOf(object));
			}else{
				GrszDto grszDto = new GrszDto();
				grszDto.setYhid(yhid);
				grszDto.setGlxx(ywid);
				GrszDto dto = grszService.getDtoByYhidAndGlxx(grszDto);
				if(dto!=null){
					messageFlag = "1".equals(dto.getSzz());
					redisUtil.hset("Grsz:"+yhid,ywid,dto.getSzz(),-1);
				}else {
					Object objectMr = redisUtil.hget("Grsz:MR",ywid);
					if(objectMr!=null){
						messageFlag = "1".equals(String.valueOf(objectMr));
						redisUtil.hset("Grsz:"+yhid,ywid,String.valueOf(objectMr),-1);
					}else {
						GrszDto grszDtoMr = new GrszDto();
						grszDtoMr.setYhid("MR");
						grszDtoMr.setGlxx(ywid);
						GrszDto dtoMr = grszService.getDtoByYhidAndGlxx(grszDtoMr);
						if(dtoMr!=null){
							messageFlag = "1".equals(dtoMr.getSzz());
							redisUtil.hset("Grsz:MR",ywid,dtoMr.getSzz(),-1);
							redisUtil.hset("Grsz:"+yhid,ywid,dtoMr.getSzz(),-1);
						}else{
							redisUtil.hset("Grsz:MR",ywid,"1",-1);
							redisUtil.hset("Grsz:"+yhid,ywid,"1",-1);
						}
					}
				}
			}
		}
		if(messageFlag){
			sendCardMessageThread(token, userId, title,msgcontent, btnJsonList,btnorient);
		}
	}

	/**
	 * 钉钉发送卡片消息 直接调用【1.0版本】【废弃 2023.03.01】
	 * @param token       微信的toekn
	 * @param userId      接收微信对象的userid
	 * @param title       信息标题
	 * @param msgcontent  消息内容
	 * @param btnJsonList 按钮清单 ，需要定义名称和地址 {"title": "一个按钮","action_url": "https://www.taobao.com"},{ "title": "两个按钮","action_url": "https://www.tmall.com" }
	 * @param btnorient   使用独立跳转ActionCard样式时的按钮排列方式，竖直排列(0)，横向排列(1)；必须与btn_json_list同时设置
	 * @Description:发送卡片消息，可以有不同按钮 sendCardMessage
	 */
	public boolean sendCardMessageOld(String token, String userId, String title, String msgcontent, List<BtnJsonList> btnJsonList, String btnorient) {

		try {
			if (StringUtil.isBlank(token))
				return false;

			DBEncrypt db = new DBEncrypt();

			logger.error("sendCardMessage userId=" + userId + " title=" + title + " msgcontent=" + msgcontent + " btnJsonList=" + btnJsonList.get(0).getActionUrl());

			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_WORKMESS);
			OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
			req.setAgentId(Long.parseLong(db.dCode(DING_AGENT_ID)));
			req.setUseridList(userId);
			req.setToAllUser(false); // 是否发送给企业全部用户

			OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
			/*msg.setMsgtype("text");
			msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
			msg.getText().setContent(msgcontent);
			req.setMsg(msg);*/
			msg.setMsgtype("action_card");
			OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
			actionCard.setTitle(title);
			actionCard.setMarkdown(msgcontent);
			actionCard.setBtnOrientation(btnorient);
			actionCard.setBtnJsonList(btnJsonList);
			msg.setActionCard(actionCard);

			req.setMsg(msg);
			logger.error("sendCardMessage userId=" + userId + " 完成！");
			OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, token);
			Long errorCode = rsp.getErrcode();
			if (errorCode > 0) {
				logger.error("sendCardMessage userId=" + userId + " 发送错误：" + rsp.getErrmsg());
				logger.error(rsp.getErrmsg());
				return false;
			}
			logger.error("sendCardMessage userId=" + userId + " 正常结束！");
		} catch (ApiException e) {
			logger.error("sendCardMessageOld ApiException:" + e.getErrMsg());
		}catch (Exception e) {
			logger.error("sendCardMessageOld Exception:" + e.getMessage());
		}
		return true;
	}


	/**
	 * 钉钉发送消息到企业群的URL【1.0版接口，不支持新应用接入】
	 * @param yhid      yhid
	 * @param chatid     接收者的群组ID
	 * @param msgcontent 消息内容
	 * @Description:发送群组消息
	 * @Method:sendDDMessage
	 * @Authod:zhang_cq
	 * @Date:2018/3/19 下午4:55
	 */
	public boolean sendGroupMessage(String yhid, String chatid, String msgcontent) {
		try {
			logger.error("---------yhid:"+yhid+"chatid:"+chatid+"msgcontent:"+msgcontent+"-------------------------");
			if (StringUtil.isBlank(yhid))
				return false;
			String token = getDingTokenByUserId(yhid);
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_GROUPMESS);
			OapiChatSendRequest req = new OapiChatSendRequest();
			req.setChatid(chatid);
			req.setMsgtype("text");
			OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
			text.setContent(msgcontent);
			req.setText(text);
			logger.error("-------------发送群消息-----------------");
			OapiChatSendResponse rsp = client.execute(req, token);
			logger.error("---------msgcontent-------------------------");
			Long errorCode = rsp.getErrcode();
			logger.error("---------rsp.getErrcode()"+rsp.getErrcode()+"-------------------------");
			if (errorCode > 0) {
				logger.error(rsp.getErrmsg());
				return false;
			}
		} catch (ApiException e) {
			logger.error("sendGroupMessage ApiException:" + e.getErrMsg());
		}catch (Exception e) {
			logger.error("sendGroupMessage Exception:" + e.getMessage());
		}
		return true;
	}


	/**
	 * 钉钉群消息发送@消息的URL【未找到开发文档】
	 * @param msgcontent 消息内容
	 * @param atMobiles  @的人员List
	 *                   <p>
	 *                   通过自定义机器人发送群消息，可以@群人员 new
	 *                   DefaultDingTalkClient("机器人access_token");
	 *                   at.setIsAtAll(false); 是否@所有人，true是，false否
	 * @Description:发送群组消息(可以@群人员)
	 * @Method:sendGroupMessageAt
	 */
	public boolean sendGroupMessageAt(String msgcontent, List<String> atMobiles) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_GROUPMESSAT);
			OapiRobotSendRequest request = new OapiRobotSendRequest();
			request.setMsgtype("text");
			OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
			text.setContent(msgcontent);
			request.setText(text);
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setAtMobiles(atMobiles);
			at.setIsAtAll(false);
			request.setAt(at);
			OapiRobotSendResponse response = client.execute(request,"775a30b9683d7e41c45e4f05b6a0dd88d5699bd730313eaa1a0f1b7675f967a2");
			if (response.getCode().equals("OK")) {
				return true;
			} else {
				return true;
			}
		} catch (ApiException e) {
			logger.error("sendGroupMessageAt ApiException:" + e.getErrMsg());
			return false;
		}catch (Exception e) {
			logger.error("sendGroupMessageAt Exception:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 发送文件消息
	 *
	 * @param yhm   yhm
	 * @param userId  接收微信对象的userid
	 * @param mediaId 文件ID
	 * @return
	 */
	public boolean sendFileMessage(String yhm, String userId, String mediaId) {

		try {
			String token = getDingTokenByUserId(yhm);
			if (StringUtil.isBlank(token))
				return false;
			User user = redisUtil.hugetDto("Users", yhm);
			if(user==null)
				return false;
			Object wbcx=redisUtil.hget("WbcxInfo", user.getWbcxid());
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(wbcx));
			DBEncrypt db = new DBEncrypt();
			logger.error("sendFileMessage ------- token : " + "  userId : " + userId + "  mediaId : " + userId);
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_WORKMESS);
			OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
			req.setAgentId(Long.parseLong(db.dCode(jsonObject.getString("agentid"))));
			req.setUseridList(userId);
			req.setToAllUser(false); // 是否发送给企业全部用户

			OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
			msg.setMsgtype("file");
			OapiMessageCorpconversationAsyncsendV2Request.File file = new OapiMessageCorpconversationAsyncsendV2Request.File();
			file.setMediaId(mediaId);
			msg.setFile(file);

			req.setMsg(msg);

			OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, token);
			Long errorCode = rsp.getErrcode();
			logger.error("sendFileMessage ------- rsp : " + JSONObject.fromObject(rsp).toString());
			if (errorCode > 0) {
				logger.error(rsp.getErrmsg());
				return false;
			}
		} catch (ApiException e) {
			logger.error("sendFileMessage ApiException:" + e.getErrMsg());
		}catch (Exception e) {
			logger.error("sendFileMessage Exception:" + e.getMessage());
		}
		return true;
	}

	public boolean sendFileDyxxMessage(String ywid,String yhid,String yhm, String userId, String mediaId) {
		boolean messageFlag = true;
		if(StringUtil.isNotBlank(yhid) && StringUtil.isNotBlank(ywid)){
			Object object = redisUtil.hget("Grsz:"+yhid,ywid);
			if(object!=null){
				messageFlag = "1".equals(String.valueOf(object));
			}else{
				GrszDto grszDto = new GrszDto();
				grszDto.setYhid(yhid);
				grszDto.setGlxx(ywid);
				GrszDto dto = grszService.getDtoByYhidAndGlxx(grszDto);
				if(dto!=null){
					messageFlag = "1".equals(dto.getSzz());
					redisUtil.hset("Grsz:"+yhid,ywid,dto.getSzz(),-1);
				}else {
					Object objectMr = redisUtil.hget("Grsz:MR",ywid);
					if(objectMr!=null){
						messageFlag = "1".equals(String.valueOf(objectMr));
						redisUtil.hset("Grsz:"+yhid,ywid,String.valueOf(objectMr),-1);
					}else {
						GrszDto grszDtoMr = new GrszDto();
						grszDtoMr.setYhid("MR");
						grszDtoMr.setGlxx(ywid);
						GrszDto dtoMr = grszService.getDtoByYhidAndGlxx(grszDtoMr);
						if(dtoMr!=null) {
							messageFlag = "1".equals(dtoMr.getSzz());
							redisUtil.hset("Grsz:MR", ywid, dtoMr.getSzz(), -1);
							redisUtil.hset("Grsz:"+yhid,ywid,dtoMr.getSzz(),-1);
						}else{
							redisUtil.hset("Grsz:MR",ywid,"1",-1);
							redisUtil.hset("Grsz:"+yhid,ywid,"1",-1);
						}
					}
				}
			}
		}
		if(messageFlag){
			sendFileMessage(yhm, userId, mediaId);
		}
		return true;
	}

	/**
	 * 上传文件到钉钉上，同时返回mediaId,用于消息里使用
	 *
	 * @param yhm    yhm
	 * @param FilePath 文件本地路径
	 * @return
	 */
	public String uploadFileToDingTalk(String yhm, String FilePath, String FileName) {
		try {
			String token = getDingTokenByUserId(yhm);
			if (StringUtil.isBlank(token))
				return null;
			DingTalkClient client = new DefaultDingTalkClient(URL_SEND_FILE);
			OapiMediaUploadRequest req = new OapiMediaUploadRequest();
			req.setType("file");
			FileInputStream fileStream = new FileInputStream(FilePath);
			FileItem fileitem = new FileItem(FileName, fileStream);
			req.setMedia(fileitem);
			OapiMediaUploadResponse rsp = client.execute(req, token);
			Long errorCode = rsp.getErrcode();
			if (errorCode > 0) {
				logger.error(rsp.getErrmsg());
				return null;
			}
			logger.error("uploadFileToDingTalk ---------- media_id : " + rsp.getMediaId());
			return rsp.getMediaId();
		} catch (Exception e) {
			logger.error("uploadFileToDingTalk Exception:"+e.getMessage());
		}
		return null;
	}

	/**
	 * 更新钉钉签到信息
	 *
	 * @param yhm yhm 可根据yhm获取ddtoken
	 * @param departmentId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> checkinRecord(String yhm, String departmentId, Long startTime, Long endTime) {
		String token = getDingTokenByUserId(yhm);
		Map<String, Object> map = new HashMap<>();
		DingTalkClient client = new DefaultDingTalkClient(URL_CHECKIN_RECORD);
		OapiCheckinRecordRequest request = new OapiCheckinRecordRequest();
		request.setDepartmentId(departmentId);
		request.setStartTime(startTime);
		request.setEndTime(endTime);
		request.setOffset(0L);
		request.setOrder("asc");
		request.setSize(100L);
		request.setHttpMethod("GET");
		try {
			OapiCheckinRecordResponse response = client.execute(request, token);
			if (response.getErrcode() != 0) {
				map.put("errmsg", response.getErrmsg());
				return map;
			}
			response.getData();
			map.put("list", response.getData());
		} catch (ApiException e) {
			logger.error("checkinRecord ApiException:" + e.getMessage());
		}catch (Exception e) {
			logger.error("checkinRecord Exception:" + e.getMessage());
		}
		return map;
	}

	/**
	 * 更新钉钉考勤信息
	 */
	public Map<String, Object> checkinListRecord(String token, List<String> userIds, String checkDateFrom, String checkDateTo) {
		Map<String, Object> map = new HashMap<>();
		DingTalkClient client = new DefaultDingTalkClient(URL_CHECKIN_LISTRECORD);
		OapiAttendanceListRecordRequest request = new OapiAttendanceListRecordRequest();
		request.setCheckDateFrom(checkDateFrom);
		request.setCheckDateTo(checkDateTo);
		request.setUserIds(userIds);
		try {
			OapiAttendanceListRecordResponse response = client.execute(request, token);
			if (response.getErrcode() != 0) {
				map.put("errmsg", response.getErrmsg());
				return map;
			}
			map.put("list", response);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			logger.error("获取钉钉考勤信息数据失败！"+e.getMessage());
			logger.error("checkinListRecord ApiException:" + e.getMessage());
		}catch (Exception e) {
			logger.error("checkinListRecord Exception:" + e.getMessage());
		}
		return map;
	}

	/**
	 * 获取模板code
	 *
	 * @param yhm
	 * @return
	 */
	public Map<String, Object> getTemplateCode(String yhm, String name) {
		String token = getDingTokenByUserId(yhm);
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		DingTalkClient client = new DefaultDingTalkClient(URL_GET_PROCESSCODE);
		OapiProcessGetByNameRequest request = new OapiProcessGetByNameRequest();
		request.setName(name);
		OapiProcessGetByNameResponse rsponse = null;
		try {
			rsponse = client.execute(request, token);
		} catch (ApiException e) {
			logger.error("getTemplateCode:" + e.toString());
		}
		if ("0".equals(rsponse.getErrorCode())) {
			map.put("status", "success");
			map.put("message", rsponse.getProcessCode());
		} else {
			map.put("status", "fail");
			map.put("errcode", rsponse.getErrorCode());
			map.put("message", rsponse.getErrmsg());
		}
		return map;
	}
	public static com.aliyun.dingtalkworkflow_1_0.Client startProcessInstanceHeaderscreateClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalkworkflow_1_0.Client(config);
	}

	/**
	 * 获取表单实例详情
	 */
	public GetProcessInstanceResponse getProcessInstances(String yhm, String processInstanceId) throws Exception {
		String token = getDingTokenByUserId(yhm);
		com.aliyun.dingtalkworkflow_1_0.Client client = startProcessInstanceHeaderscreateClient();
		com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
		getProcessInstanceHeaders.xAcsDingtalkAccessToken = token;
		com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
				.setProcessInstanceId(processInstanceId);
		return client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new RuntimeOptions());
	}

	/**
	 * 自定审批人创建审批实例
	 *
	 * @param yhm
	 * @param templateCode 审批模板code
	 * @param approvers    最大列表长度20，多个审批人用逗号分隔，按传入的顺序依次审批
	 * @param userid       申请人
	 * @param dept         申请部门
	 * @param map          审批流表单控件，设置各表单项值
	 * @return
	 */
	public Map<String, Object> createInstanceNew(String yhm, String templateCode, String approvers, String userid, String dept, Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			com.aliyun.dingtalkworkflow_1_0.Client client = startProcessInstanceHeaderscreateClient();
			String token = getDingTokenByUserId(yhm);
			//获取指定人参数 ActionerKey 获取审批单流程中的节点信息
			ProcessForecastHeaders processForecastHeaders = new ProcessForecastHeaders();
			processForecastHeaders.xAcsDingtalkAccessToken = token;
			List<ProcessForecastRequest.ProcessForecastRequestFormComponentValues> processForecastRequestFormComponentValues = new ArrayList<>();
			//发起审批实例
			StartProcessInstanceHeaders startProcessInstanceHeaders = new StartProcessInstanceHeaders();
			startProcessInstanceHeaders.xAcsDingtalkAccessToken = token;
			List<StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues> formComponentValues = new ArrayList<>();
			for (Entry<String, String> entry : map.entrySet()) {
				StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues vo = new StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues();
				vo.setName(entry.getKey());
				vo.setValue(entry.getValue());
				formComponentValues.add(vo);

				ProcessForecastRequest.ProcessForecastRequestFormComponentValues forecastRequestFormComponentValues = new ProcessForecastRequest.ProcessForecastRequestFormComponentValues();
				forecastRequestFormComponentValues.setName(entry.getKey()).setValue(entry.getValue());
				processForecastRequestFormComponentValues.add(forecastRequestFormComponentValues);
			}

			ProcessForecastRequest processForecastRequest = new ProcessForecastRequest()
					.setProcessCode(templateCode)
					.setDeptId(Integer.valueOf(dept))
					.setUserId(userid)
					.setFormComponentValues(processForecastRequestFormComponentValues);
			ProcessForecastResponse processForecastResponse = client.processForecastWithOptions(processForecastRequest, processForecastHeaders, new RuntimeOptions());
			Integer statusCode = 200;
			if (statusCode.equals(processForecastResponse.getStatusCode())){
				DingTalkClient dingTalkClient = new DefaultDingTalkClient(URL_USER_GET_V2);
				OapiV2UserGetRequest req = new OapiV2UserGetRequest();
				req.setUserid(approvers.split(",")[0]);
				req.setLanguage("zh_CN");
				OapiV2UserGetResponse rsp = dingTalkClient.execute(req, token);
//				if ("0".equals(rsp.getErrorCode()) && StringUtil.isNotBlank(rsp.getResult().getManagerUserid())){
//					approvers = approvers+","+rsp.getResult().getManagerUserid();
//					//特殊判断 处理是否是销售总部人员
//					DingTalkClient defaultDingTalkClient = new DefaultDingTalkClient(URL_GET_PARENT_LIST);
//					OapiV2DepartmentListparentbyuserRequest listparentbyuserRequest = new OapiV2DepartmentListparentbyuserRequest();
//					listparentbyuserRequest.setUserid(rsp.getResult().getManagerUserid());
//					OapiV2DepartmentListparentbyuserResponse oapiV2DepartmentListparentbyuserResponse = defaultDingTalkClient.execute(listparentbyuserRequest, token);
//					if (0L == oapiV2DepartmentListparentbyuserResponse.getErrcode()){
//						boolean flag = true;
//						for (OapiV2DepartmentListparentbyuserResponse.DeptParentResponse deptParentResponse : oapiV2DepartmentListparentbyuserResponse.getResult().getParentList()) {
//							if (deptParentResponse.getParentDeptIdList().contains(835070809L)){//销售总部（新）部门id
//								approvers = approvers+",141219512926272616";//M0305 杨新新 钉钉id
//								flag = false;
//								break;
//							}
//						}
//						if (flag)
//							approvers = approvers+","+rsp.getResult().getManagerUserid();
//					}
//				}
				//判断审批流程中是否有自选审批人节点
                StringBuilder approversBuilder = new StringBuilder(approvers);
                while (approversBuilder.toString().split(",").length<1){
					approversBuilder.append(",").append(approversBuilder.toString().split(",")[approversBuilder.toString().split(",").length - 1]);
				}
                approvers = approversBuilder.toString();
                String[] strings = approvers.split(",");
				List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> targetSelectActionersList = new ArrayList<>();
				for (int i = 0; i < processForecastResponse.getBody().getResult().getWorkflowActivityRules().size() && i < strings.length; i++) {
					if ("target_select".equals( processForecastResponse.getBody().getResult().getWorkflowActivityRules().get(i).getActivityType())){
						StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners targetSelectActioners = new StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners()
								.setActionerKey(String.valueOf(processForecastResponse.getBody().getResult().getWorkflowActivityRules().get(i).getWorkflowActor().getActorKey()))
								.setActionerUserIds(Collections.singletonList(strings[i]));
						targetSelectActionersList.add(targetSelectActioners);
					}
				}
				StartProcessInstanceRequest startProcessInstanceRequest = new StartProcessInstanceRequest()
						.setOriginatorUserId(userid)
						.setProcessCode(templateCode)
						.setDeptId(Long.parseLong(dept))
						.setTargetSelectActioners(targetSelectActionersList)
						.setFormComponentValues(formComponentValues);
				StartProcessInstanceResponse response = client.startProcessInstanceWithOptions(startProcessInstanceRequest, startProcessInstanceHeaders, new RuntimeOptions());
				if (statusCode.equals(response.getStatusCode())){
					resultMap.put("status", "success");
					resultMap.put("instanceId", response.getBody().getInstanceId());
					return resultMap;
				}else{
					resultMap.put("status", "fail");
					resultMap.put("errcode", response.getStatusCode());
					resultMap.put("message", response.getBody());
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		resultMap.put("errcode", Integer.parseInt("400"));
		resultMap.put("message", "发起钉钉审批出错");
		resultMap.put("status", "fail");
		return resultMap;
	}



	/**
	 * 创建审批实例
	 *
	 * @param yhm
	 * @param templateCode 审批模板code
	 * @param approvers    最大列表长度20，多个审批人用逗号分隔，按传入的顺序依次审批
	 * @param userid       申请人
	 * @param cc_list      抄送人
	 * @param dept         申请部门
	 * @param map          审批流表单控件，设置各表单项值
	 * @return
	 */
	public Map<String, Object> createInstance(String yhm, String templateCode, String approvers, String cc_list, String userid, String dept, Map<String, String> map, List<Map<String, String>> mx_map, List<Map<String, String>> mx_table) {
//		//获取外网ip
//		String ip=null;
//		try {
//			URL url = new URL("https://www.trackip.net/i");
//			URLConnection URLconnection = url.openConnection();
//			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
//			int responseCode = httpConnection.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				System.out.println("成功");
//				InputStream in = httpConnection.getInputStream();
//				InputStreamReader isr = new InputStreamReader(in);
//				BufferedReader bufr = new BufferedReader(isr);
//				String str;
//				while ((str = bufr.readLine()) != null) {
//					ip=str+ip;
//				}
//				bufr.close();
//			} else {
//				System.err.println("失败");
//			}
//		} catch (Exception e) {
//			logger.error("获取外网地址错误：{}",e.getMessage());
//		}
//
//		logger.error("=========外网IP:"+ip);
//
//		System.out.println(ip);
		Map<String, Object> resultMap = new HashMap<>();
		String token = getDingTokenByUserId(yhm);
		DingTalkClient client = new DefaultDingTalkClient(URL_CREATE_INSTANCE);
		OapiProcessinstanceCreateRequest request = new OapiProcessinstanceCreateRequest();
		request.setProcessCode(templateCode); // 审批模板code
		request.setOriginatorUserId(userid); // 申请人
		request.setDeptId(Long.parseLong(dept)); // 申请部门
		// 审批流表单控件，设置各表单项值
		List<OapiProcessinstanceCreateRequest.FormComponentValueVo> formComponentValues = new ArrayList<>();
		for (Entry<String, String> entry : map.entrySet()) {
			OapiProcessinstanceCreateRequest.FormComponentValueVo vo = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
			vo.setName(entry.getKey());
			vo.setValue(entry.getValue());
			formComponentValues.add(vo);
		}

		List<List<OapiProcessinstanceCreateRequest.FormComponentValueVo>> final_list = new ArrayList<>();
		if (mx_map!=null&&!mx_map.isEmpty()) {
            for (Map<String, String> stringStringMap : mx_map) {
                List<OapiProcessinstanceCreateRequest.FormComponentValueVo> item_list = new ArrayList<>();
                for (Entry<String, String> t_entry : stringStringMap.entrySet()) {
                    OapiProcessinstanceCreateRequest.FormComponentValueVo item = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
                    item.setName(t_entry.getKey());
                    item.setValue(t_entry.getValue());
                    item_list.add(item);
                }
                final_list.add(item_list);
            }
			OapiProcessinstanceCreateRequest.FormComponentValueVo vo = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
			vo.setName("明细");
			vo.setValue(JSON.toJSONString(final_list));
			formComponentValues.add(vo);
		}
		if (mx_table!=null&&!mx_table.isEmpty()) {
            for (Map<String, String> stringStringMap : mx_table) {
                List<OapiProcessinstanceCreateRequest.FormComponentValueVo> item_list = new ArrayList<>();
                for (Entry<String, String> t_entry : stringStringMap.entrySet()) {
                    OapiProcessinstanceCreateRequest.FormComponentValueVo item = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
                    item.setName(t_entry.getKey());
                    item.setValue(t_entry.getValue());
                    item_list.add(item);
                }
                final_list.add(item_list);
            }
			OapiProcessinstanceCreateRequest.FormComponentValueVo vo = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
			vo.setName("表格");
			vo.setValue(JSON.toJSONString(final_list));
			formComponentValues.add(vo);
		}
		request.setApprovers(approvers);// 审批人
		request.setCcList(cc_list);
		request.setCcPosition("FINISH");
		request.setFormComponentValues(formComponentValues);
		OapiProcessinstanceCreateResponse response = null;
		try {
			response = client.execute(request, token);
		} catch (Exception e) {
			logger.error("createInstance:报错 token:" + token + " " + e.toString());
		}
		if ("0".equals(response.getErrorCode())) {
			resultMap.put("status", "success");
			resultMap.put("message", response.getBody());
		} else {
			resultMap.put("status", "fail");
			resultMap.put("errcode", response.getErrorCode());
			resultMap.put("message", response.getErrmsg());
		}
		return resultMap;
	}
	/**
	 * 创建审批实例
	 *
	 * @param yhm
	 * @param templateCode 审批模板code
	 * @param approvers    最大列表长度20，多个审批人用逗号分隔，按传入的顺序依次审批
	 * @param userid       申请人
	 * @param cc_list      抄送人
	 * @param dept         申请部门
	 * @param map          审批流表单控件，设置各表单项值
	 * @param targetSelectActionersList  自选审批
	 * @return
	 */
	public Map<String, Object> createInstance(String yhm, String templateCode, String approvers, String cc_list, String userid, String dept, Map<String, String> map, List<Map<String, String>> mx_map, List<Map<String, String>> mx_table,List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> targetSelectActionersList) {
		Map<String, Object> resultMap = new HashMap<>();
		com.aliyun.dingtalkworkflow_1_0.Client client;
		try {
			client = startProcessInstanceHeaderscreateClient();
		} catch (Exception e) {
			resultMap.put("status", "fail");
			resultMap.put("errcode", "500");
			resultMap.put("message", "创建连接失败");
			return resultMap;
		}
		String token = getDingTokenByUserId(yhm);
		StartProcessInstanceHeaders startProcessInstanceHeaders = new StartProcessInstanceHeaders();
		startProcessInstanceHeaders.xAcsDingtalkAccessToken = token;
		List<StartProcessInstanceRequestFormComponentValues> formComponentValues = new ArrayList<>();
		for (String name : map.keySet()) {
			StartProcessInstanceRequestFormComponentValues componentValues = new StartProcessInstanceRequestFormComponentValues();
			componentValues.setName(name);
			componentValues.setValue(map.get(name));
			formComponentValues.add(componentValues);
		}
		//明细数据未实现
		/*	if (!CollectionUtils.isEmpty(mx_map)) {
			for (Map<String, String> stringMap : mx_map) {
				List<StartProcessInstanceRequestFormComponentValuesDetailsDetails> formComponentDetails = new ArrayList<>();
				StartProcessInstanceRequestFormComponentValues componentValues = new StartProcessInstanceRequestFormComponentValues();
				for (String name : stringMap.keySet()) {
					StartProcessInstanceRequestFormComponentValuesDetailsDetails details = new StartProcessInstanceRequestFormComponentValuesDetailsDetails();
					details.setName(name);
					details.setValue(stringMap.get(name));
					formComponentDetails.add(details);
				}
				componentValues.setName("明细");
				componentValues.setValue(JSON.toJSONString(formComponentDetails));
				formComponentValues.add(componentValues);
			}
		}*/
		StartProcessInstanceRequestApprovers approvers0 = null;
		if (StringUtil.isNotBlank(approvers)){
			//会签：AND；或签：OR；单人：NONE
			approvers0 = new StartProcessInstanceRequestApprovers()
					//传入审批人，默认单人
					.setActionType("NONE")
					.setUserIds(Arrays.asList(
							approvers
					));
		}
		StartProcessInstanceRequest startProcessInstanceRequest = new StartProcessInstanceRequest()
				.setOriginatorUserId(userid)
				.setProcessCode(templateCode)
				.setDeptId(Long.parseLong(dept))
				.setCcPosition("FINISH")
				.setFormComponentValues(formComponentValues);
		if (!CollectionUtils.isEmpty(targetSelectActionersList)){
			startProcessInstanceRequest.setTargetSelectActioners(targetSelectActionersList);
		}
		if (approvers0!=null){
			startProcessInstanceRequest.setApprovers(Arrays.asList(
					approvers0
			));
		}
		if (StringUtil.isNotBlank(cc_list)){
			startProcessInstanceRequest.setCcList(Arrays.asList(
					cc_list
			));
		}
		Integer statusCode = 200;
		StartProcessInstanceResponse response;
		try {
			response = client.startProcessInstanceWithOptions(startProcessInstanceRequest, startProcessInstanceHeaders, new RuntimeOptions());
			if (statusCode.equals(response.getStatusCode())){
				resultMap.put("status", "success");
				resultMap.put("errcode", String.valueOf(response.getStatusCode()));
				resultMap.put("instanceId", response.getBody().getInstanceId());
				return resultMap;
			}
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("createInstance Robot+code1:" + err.code);
				logger.error("createInstance Robot+message1:" + err.message);
				resultMap.put("status", "fail");
				resultMap.put("errcode", err.code);
				resultMap.put("message", err.message);
			}
		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("createInstance Robot+code1:" + err.code);
				logger.error("createInstance Robot+message1:" + err.message);
				resultMap.put("status", "fail");
				resultMap.put("errcode", err.code);
				resultMap.put("message", err.message);
			}
		}
		return resultMap;
	}
	/**
	 * 获取审批钉盘空间
	 *
	 * @param token
	 * @param userid
	 * @return
	 */
	public Map<String, Object> getSpaceid(String token, String userid) {
		Map<String, Object> resultMap = new HashMap<>();

		DingTalkClient client = new DefaultDingTalkClient(URL_GET_SPACEID);
		OapiProcessinstanceCspaceInfoRequest request = new OapiProcessinstanceCspaceInfoRequest();
		request.setUserId(userid);
		OapiProcessinstanceCspaceInfoResponse response = null;
		try {
			response = client.execute(request, token);
		} catch (ApiException e) {
			logger.error(e.toString());
		}
		if ("0".equals(response.getErrorCode())) {
			resultMap.put("status", "success");
			resultMap.put("message", response.getBody());
		} else {
			resultMap.put("status", "fail");
			resultMap.put("errcode", response.getErrorCode());
			resultMap.put("message", response.getErrmsg());
		}
		return resultMap;
	}

	/**
	 * 注册业务回调
	 *
	 * @param yhm
	 * @return
	 */
	public Map<String, Object> registerCallbackByYhm(String yhm,String wbcxid,String url) {
		String token = getToken(wbcxid);
		return registerCallback(token,wbcxid,url);
	}
	/**
	 * 注册业务回调
	 *
	 * @param token
	 * @return
	 */
	public Map<String, Object> registerCallback(String token,String wbcxid,String url) {
		Object info = redisUtil.hget("WbcxInfo", wbcxid);
		Map<String, Object> resultMap = new HashMap<>();
		if (null != info){
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(info));
			if( StringUtil.isBlank(url)
					|| null == jsonObject.get("aeskey") || StringUtil.isBlank(String.valueOf(jsonObject.get("aeskey")))
					|| null == jsonObject.get("token") || StringUtil.isBlank(String.valueOf(jsonObject.get("token")))) {
				resultMap.put("status", "fail");
				resultMap.put("message", "外部程序信息缺失！");
				return resultMap;
			}

			// 先删除企业已有的回调
			deleteCallback(token);
			// 新注册回调
			DBEncrypt crypt = new DBEncrypt();
			DingTalkClient client = new DefaultDingTalkClient(URL_REGISTER_CALLBACK);
			OapiCallBackRegisterCallBackRequest request = new OapiCallBackRegisterCallBackRequest();
			request.setUrl(url);
			request.setAesKey(crypt.dCode(String.valueOf(jsonObject.get("aeskey"))));
			request.setToken(crypt.dCode(String.valueOf(jsonObject.get("token"))));
			request.setCallBackTag(Arrays.asList(BPMS_TASK_CHANGE, BPMS_INSTANCE_CHANGE));
			OapiCallBackRegisterCallBackResponse response;
			try {
				response = client.execute(request, token);
			} catch (ApiException e) {
				resultMap.put("status", "fail");
				resultMap.put("message", e.getErrMsg());
				logger.error("registerCallback ApiException:" + e);
				return resultMap;
			}
			if ("0".equals(response.getErrorCode())) {
				resultMap.put("status", "success");
				resultMap.put("message", response.getBody());
			} else {
				resultMap.put("status", "fail");
				resultMap.put("errcode", response.getErrorCode());
				resultMap.put("message", response.getErrmsg());
			}
		}
		return resultMap;
	}

	/**
	 * 删除回调
	 *
	 * @param token
	 * @return
	 */
	public Map<String, Object> deleteCallback(String token) {
		Map<String, Object> resultMap = new HashMap<>();
		DingTalkClient client = new DefaultDingTalkClient(URL_DELETE_CALLBACK);
		OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
		request.setHttpMethod("GET");
		OapiCallBackDeleteCallBackResponse response = null;
		try {
			response = client.execute(request, token);
		} catch (ApiException e) {
			logger.error(e.toString());
		}
		if ("0".equals(response.getErrorCode())) {
			resultMap.put("status", "success");
			resultMap.put("message", response.getBody());
		} else {
			resultMap.put("status", "fail");
			resultMap.put("errcode", response.getErrorCode());
			resultMap.put("message", response.getErrmsg());
		}
		return resultMap;
	}

	/**
	 * 获取钉钉回调信息
	 *
	 * @param yhm
	 * @return
	 */
	public Map<String, Object> getCallBackMessageByYhm(String yhm) throws ApiException {
		String token = getDingTokenByUserId(yhm);
		return getCallBackMessage(token);
	}
	/**
	 * 获取钉钉回调信息
	 *
	 * @param token
	 * @return
	 */
	public Map<String, Object> getCallBackMessage(String token) throws ApiException {
		Map<String, Object> resultMap = new HashMap<>();
		DingTalkClient client = new DefaultDingTalkClient(URL_GET_CALLBACK);
		OapiCallBackGetCallBackRequest req = new OapiCallBackGetCallBackRequest();
		req.setHttpMethod("GET");
		OapiCallBackGetCallBackResponse response;
		response = client.execute(req, token);
		if ("0".equals(response.getErrorCode())) {
			resultMap.put("status", "success");
			resultMap.put("message", response.getBody());
		} else {
			resultMap.put("status", "fail");
			resultMap.put("errcode", response.getErrorCode());
			resultMap.put("message", response.getErrmsg());
		}
		return resultMap;
	}

	/**
	 * 钉钉钉一下(语音文件)
	 *
	 * @param calledNumber     手机号
	 * @param voiceCode        语音ID
	 * @param CalledShowNumber 被叫显号
	 * @param outId            预留给调用方使用的ID，最终会通过在回执消息中将此ID带回给调用方。
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public SingleCallByVoiceResponse SingleCallByVoice(String calledNumber, String CalledShowNumber, String voiceCode, String outId) throws ClientException {
		//设置访问超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//云通信产品-语音API服务产品名称（产品名固定，无需修改）
		String product = "Dyvmsapi";
		//语音API服务产品域名（接口地址固定，无需修改）
		String domain = "dyvmsapi.aliyuncs.com";
		//初始化acsClient暂时不支持多region
		DBEncrypt dbEncrypt = new DBEncrypt();
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", dbEncrypt.dCode(accessKeyId), dbEncrypt.dCode(accessSecret));
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		//组装请求对象
		SingleCallByVoiceRequest request = new SingleCallByVoiceRequest();
		//必填-被叫显号,可在语音控制台中找到所购买的显号
		request.setCalledShowNumber(CalledShowNumber);
		//必填-被叫号码
		request.setCalledNumber(calledNumber);
		//必填-语音文件ID
		request.setVoiceCode(voiceCode);
		//可选-音量 取值范围 0--200
		request.setVolume(100);
		//可选-播放次数1~3
		request.setPlayTimes(1);
		//语速控制，取值范围：-500~500.
		request.setSpeed(100);
		//预留给调用方使用的ID，最终会通过在回执消息中将此ID带回给调用方。
		request.setOutId(outId);
		try {
			SingleCallByVoiceResponse response = acsClient.getAcsResponse(request);
			return response;
		} catch (Exception e) {
			logger.error("SingleCallByVoice Exception:" + e);
			return null;
		}
	}

	/**
	 * 钉钉钉一下(文本转语音)
	 *
	 * @param calledNumber 手机号
	 * @param ttsCode      文本模板ID
	 * @return outId 外部扩展字段,此ID将在回执消息中带回给调用方
	 */
	@SuppressWarnings("deprecation")
	public SingleCallByTtsResponse singleCallByTts(String calledNumber, String ttsCode, String ttsParam) throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//云通信产品-语音API服务产品名称（产品名固定，无需修改）
		String product = "Dyvmsapi";
		//语音API服务产品域名（接口地址固定，无需修改）
		String domain = "dyvmsapi.aliyuncs.com";
		// 初始化acsClient,暂不支持region化
		DBEncrypt dbEncrypt = new DBEncrypt();
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", dbEncrypt.dCode(accessKeyId), dbEncrypt.dCode(accessSecret));
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象-具体描述见控制台-文档部分内容
		SingleCallByTtsRequest request = new SingleCallByTtsRequest();
		// 必填-被叫显号,可在语音控制台中找到所购买的显号
		request.setCalledShowNumber("");
		// 必填-被叫号码
		request.setCalledNumber(calledNumber);
		// 必填-Tts模板ID
		request.setTtsCode(ttsCode);
		// 可选-当模板中存在变量时需要设置此值"{\"code\":\""+code+"\"}"ttsParam
		request.setTtsParam(ttsParam);
		// 可选-外部扩展字段,此ID将在回执消息中带回给调用方
		request.setOutId("");
		try {
			SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);
			return singleCallByTtsResponse;
		} catch (Exception e) {
			logger.error("singleCallByTts Exception:" + e);
			return null;
		}
	}

	/**
	 * @param ddids    钉钉ID集合
	 * @param ttsCode  语音模板ID
	 * @param ttsParam 模板变量{"AckNum":"123456"}
	 * @return
	 * @Description:钉钉钉一下（通过钉钉ID）
	 */
	public Map<String, Object> singleCallByDdid(List<String> ddids, String ttsCode, String ttsParam) {
		Map<String, Object> map = new HashMap<>();
		String token = getToken(null);
		List<String> phoneMsg = new ArrayList<>();
		List<String> sendMsg = new ArrayList<>();
		for (String ddid : ddids) {
			DingTalkClient client = new DefaultDingTalkClient(URL_USER_GET_V2);
			OapiV2UserGetRequest ougrequest = new OapiV2UserGetRequest();
			ougrequest.setUserid(ddid);
			ougrequest.setLanguage("zh_CN");
			try {
				//根据DDID获取手机号
				OapiV2UserGetResponse ougresponse = client.execute(ougrequest, token);

				if (ougresponse.getResult().getMobile() == null) {
					phoneMsg.add("未获取到手机号,钉钉id:" + ddid + "姓名:" + ougresponse.getResult().getName());
				}

				try {
					SingleCallByTtsResponse response = singleCallByTts(ougresponse.getResult().getMobile(), ttsCode, ttsParam);
					if (!response.getCode().contentEquals("OK")) {
						sendMsg.add("发送失败-手机号：" + ougresponse.getResult().getMobile() + "错误代码：" + response.getCode());
					} else {
						sendMsg.add("发送成功-手机号：" + ougresponse.getResult().getMobile());
					}

				} catch (ClientException e) {
					logger.error("singleCallByDdid ClientException:" + e);
				}
			} catch (ApiException e) {
				logger.error("singleCallByDdid ApiException:" + e);
			}
		}
		map.put("phoneMsg", phoneMsg);
		map.put("sendMsg", sendMsg);
		return map;
	}

	/**
	 * @param yhms    用户名集合
	 * @param ttsCode  语音模板ID
	 * @param ttsParam 模板变量{"AckNum":"123456"}
	 * @return
	 * @Description:钉钉钉一下（通过钉钉ID）
	 */
	public Map<String, Object> singleCallByYhm(List<String> yhms, String ttsCode, String ttsParam) {
		Map<String, Object> map = new HashMap<>();
		//String token = getToken();
		List<String> phoneMsg = new ArrayList<>();
		List<String> sendMsg = new ArrayList<>();
		sendMsg.add("准备根据清单发送语音消息：" + yhms.toString());
		logger.error("singleCallByYhm 准备开始发送语音消息：" + yhms.toString());
		for (String yhm : yhms) {
			DingTalkClient client = new DefaultDingTalkClient(URL_USER_GET_V2);
			OapiV2UserGetRequest ougrequest = new OapiV2UserGetRequest();
			User user = redisUtil.hugetDto("Users", yhm);
			ougrequest.setUserid(user.getDdid());
			ougrequest.setLanguage("zh_CN");
			try {
				Object info = redisUtil.hget("WbcxInfo", user.getWbcxid());
				String token;
				if (info != null){
					com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(String.valueOf(info));
					token = getToken(jsonObject.getString("appid"),jsonObject.getString("secret"));
				}else {
					token = getToken(DING_APP_KEY,DING_APP_SECRET);
				}
				//根据DDID获取手机号
				OapiV2UserGetResponse ougresponse = client.execute(ougrequest, token);

				if(ougresponse.isSuccess()) {
					if (ougresponse.getResult().getMobile() == null) {
						phoneMsg.add("未获取到手机号,钉钉id:" + user.getDdid() + "姓名:" + ougresponse.getResult().getName());
					}

					try {
						SingleCallByTtsResponse response = singleCallByTts(ougresponse.getResult().getMobile(), ttsCode, ttsParam);
						if (!response.getCode().contentEquals("OK")) {
							sendMsg.add("发送失败- 用户名:" + yhm + "  手机号：" + ougresponse.getResult().getMobile() + " 错误代码：" + response.getCode());
							logger.error("发送失败- 用户名:" + yhm + "  手机号：" + ougresponse.getResult().getMobile() + " 错误代码：" + response.getCode());
						} else {
							sendMsg.add("发送成功- 用户名:" + yhm + "  手机号：" + ougresponse.getResult().getMobile());
							logger.error("发送成功- 用户名:" + yhm + "  手机号：" + ougresponse.getResult().getMobile());
						}

					} catch (ClientException e) {
						logger.error("singleCallByYhm 用户名:" + yhm + "  ClientException:" + e.getMessage());
						phoneMsg.add(e.getMessage());
					}
				}else {
					logger.error("singleCallByYhm 用户名:" + yhm + "  Exception:" + ougresponse.getErrmsg());
					phoneMsg.add(ougresponse.getErrmsg());
				}
			} catch (ApiException e) {
				logger.error("singleCallByYhm 用户名:" + yhm + "   ApiException:" + e.getMessage());
				phoneMsg.add(e.getMessage());
			}catch (Exception e) {
				logger.error("singleCallByYhm 用户名:" + yhm + "   Exception: " + e);
			}
		}
		map.put("phoneMsg", phoneMsg);
		map.put("sendMsg", sendMsg);
		return map;
	}

	/**
	 * 外部发送消息（邮件、钉钉、微信）
	 *
	 * @param receivers 接收人信息
	 * @param message   消息内容
	 * @param title     消息标题
	 * @param token
	 * @return
	 * @author lifan
	 */
	public boolean sendOutMessage(List<Map<String, String>> receivers, String message, String title, String token) {
		// TODO Auto-generated method stub
		try {
			if (receivers == null) {
				return false;
			}
			for (Map<String, String> receiver : receivers) {
				String yx = receiver.get("yx");
				String wxid = receiver.get("wxid");
				String ddid = receiver.get("ddid");
				//邮箱存在，发送邮箱消息
				if (StringUtils.isNotBlank(yx)) {
					List<String> yxlist = new ArrayList<>();
					yxlist = StringUtil.splitMsg(yxlist, yx, "，|,");
					emailUtil.sendEmail(yxlist, title, message);
				}
				//微信存在发送微信信息
				if (StringUtils.isNotBlank(wxid)) {
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("wxid", wxid);
					paramMap.put("title", title);
					paramMap.put("message", message);
					paramMap.putAll(receiver);
					// 让服务器发送信息到相应的微信里
					String[] arrWxid = wxid.split("，|,");
                    for (String s : arrWxid) {
                        paramMap.put("wxid", s);
                        //t_restTemplate.postForObject(menuurl + "/wechat/sendOutMessage", paramMap, String.class);
                    }
				}
				//钉钉存在，发送钉钉消息
				if (StringUtils.isNotBlank(ddid)) {
					String[] arrddid = ddid.split("，|,");
                    for (String s : arrddid) {
                        sendWorkMessage(null, s, title, message);
                    }
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("sendOutMessage Exception: " + e);
		}
		return false;
	}

	/**
	 * 撤销钉钉审批事件
	 *
	 * @param yhm
	 * @param ProcessInstanceId 钉钉实例ID
	 * @param shyj              撤销说明
	 * @param OperatingUserid   撤销人员
	 * @return
	 */
	public Map<String, Object> cancelDingtalkAudit(String yhm, String ProcessInstanceId, String shyj, String OperatingUserid) {
		Map<String, Object> map = new HashMap<>();
		try {
			String token = getDingTokenByUserId(yhm);
			DingTalkClient client = new DefaultDingTalkClient(URL_DINGTALK_CANCEL);
			OapiProcessInstanceTerminateRequest req = new OapiProcessInstanceTerminateRequest();
			TerminateProcessInstanceRequestV2 processInstanceRequestV2 = new TerminateProcessInstanceRequestV2();
			processInstanceRequestV2.setProcessInstanceId(ProcessInstanceId);
			processInstanceRequestV2.setIsSystem(false);
			processInstanceRequestV2.setRemark(shyj);
			processInstanceRequestV2.setOperatingUserid(OperatingUserid);
			req.setRequest(processInstanceRequestV2);
			OapiProcessInstanceTerminateResponse rsp;
			rsp = client.execute(req, token);
			map.put("message", rsp.getBody());

		} catch (ApiException e) {
			// TODO Auto-generated catch block
			logger.error("cancelDingtalkAudit ApiException: " + e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("cancelDingtalkAudit Exception: " + e.getMessage());
		}
		return map;
	}

	/**
	 * @Description 获取员工花名册信息
	 * @Param [userlists, appkey, appsecret]
	 * @Return java.util.Map<String, Object>
	 */
	public List<EmpRosterFieldVo> getRosterInfo(List<String> userids, String token, String fieldFilterStr) {
		try {
			if (StringUtil.isBlank(token))
				return null;
			DBEncrypt db = new DBEncrypt();
			DingTalkClient client = new DefaultDingTalkClient(ROSTER_INFO);
			OapiSmartworkHrmEmployeeV2ListRequest req = new OapiSmartworkHrmEmployeeV2ListRequest();
			req.setUseridList(StringUtils.join(userids, ","));
			req.setFieldFilterList(fieldFilterStr);
			req.setAgentid(Long.parseLong(db.dCode(DING_AGENT_ID)));
			OapiSmartworkHrmEmployeeV2ListResponse rsp = client.execute(req, token);
			List<EmpRosterFieldVo> result = rsp.getResult();
			return result;
		} catch (ApiException e) {
			logger.error("getRosterInfo ApiException: " + e.getMessage());
		}catch (Exception e) {
			logger.error("getRosterInfo Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * @Description 获取离职人员列表
	 * @Param [token]
	 * @Return java.util.List<String>
	 */
	public List<String> getSeparatingEmployeeList(String token) {
		try {
			if (StringUtil.isBlank(token))
				return null;
			List<String> list = new ArrayList<>();
			Long nextCursor = 0L;
			DingTalkClient client = new DefaultDingTalkClient(SEPARATING_EMPLOYEE_LIST);
			OapiSmartworkHrmEmployeeQuerydimissionRequest req = new OapiSmartworkHrmEmployeeQuerydimissionRequest();
			while (null != nextCursor) {
				req.setOffset(nextCursor);
				req.setSize(50L);
				OapiSmartworkHrmEmployeeQuerydimissionResponse rsp = client.execute(req, token);
				Paginator result = rsp.getResult();
				nextCursor = result.getNextCursor();
				list.addAll(result.getDataList());
			}
			return list;
		} catch (ApiException e) {
			logger.error("getSeparatingEmployeeList ApiException: " + e);
		}catch (Exception e) {
			logger.error("getSeparatingEmployeeList Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * @Description 获取离职人员列表【2.0版本】
	 * @Param [appkey, appsecret]
	 * @Return java.util.List<String>
	 */
	public List<String> getSeparatingEmployeeListV2(String token) {
		try {
			if (StringUtil.isBlank(token))
				return null;
			Long nextToken = 0L;
			List<String> userIdList = new ArrayList<>();
			boolean hasNext = true;
			while (hasNext){
				com.aliyun.dingtalkhrm_1_0.Client hrmClient = createHrmClient();
				QueryDismissionStaffIdListHeaders queryDismissionStaffIdListHeaders = new QueryDismissionStaffIdListHeaders();
				queryDismissionStaffIdListHeaders.xAcsDingtalkAccessToken = token;
				QueryDismissionStaffIdListRequest queryDismissionStaffIdListRequest = new QueryDismissionStaffIdListRequest();
				queryDismissionStaffIdListRequest.setNextToken(nextToken);
				queryDismissionStaffIdListRequest.setMaxResults(50);
				QueryDismissionStaffIdListResponse queryDismissionStaffIdListResponse = hrmClient.queryDismissionStaffIdListWithOptions(queryDismissionStaffIdListRequest, queryDismissionStaffIdListHeaders, new RuntimeOptions());
				userIdList.addAll(queryDismissionStaffIdListResponse.getBody().getUserIdList());
				hasNext = queryDismissionStaffIdListResponse.getBody().getHasMore();
				nextToken = queryDismissionStaffIdListResponse.getBody().getNextToken();
			}
			return userIdList;
		} catch (Exception e) {
			logger.error("getSeparatingEmployeeListV2 Exception: " + e);
		}
		return null;
	}

	/**
	 * @Description 获取离职人员信息
	 * @Param [userids, token]
	 * @Return java.util.List<EmpDimissionInfoVo>
	 */
/*	public List<EmpDimissionInfoVo> getSeparatingEmployeeInfo(List<String> userids, String token) {
		try {
			if (StringUtil.isBlank(token))
				return null;
			List<EmpDimissionInfoVo> empDimissionInfoVos = new ArrayList<>();
			List<List<String>> lists = ListUtils.partition(userids, 50);
			DingTalkClient client = new DefaultDingTalkClient(SEPARATING_EMPLOYEE_INFO);
			OapiSmartworkHrmEmployeeListdimissionRequest req = new OapiSmartworkHrmEmployeeListdimissionRequest();
			for (List<String> list : lists) {
				req.setUseridList(StringUtil.join(list, ","));
				OapiSmartworkHrmEmployeeListdimissionResponse rsp = client.execute(req, token);
				List<EmpDimissionInfoVo> result = rsp.getResult();
				empDimissionInfoVos.addAll(result);
			}

			return empDimissionInfoVos;
		} catch (ApiException e) {
			logger.error("getSeparatingEmployeeInfo: " + e.toString());
		}
		return null;
	}*/

	/**
	 * @Description 获取离职人员信息【2.0版本】
	 * @Param [userids, appkey, appsecret]
	 * @Return List<QueryHrmEmployeeDismissionInfoResponseBody.QueryHrmEmployeeDismissionInfoResponseBodyResult>
	 */
	public List<QueryHrmEmployeeDismissionInfoResponseBody.QueryHrmEmployeeDismissionInfoResponseBodyResult> getSeparatingEmployeeInfoV2(List<String> userids, String token) {
		try {
			if (StringUtil.isBlank(token))
				return null;

			com.aliyun.dingtalkhrm_1_0.Client hrmClient = createHrmClient();
			QueryHrmEmployeeDismissionInfoHeaders queryHrmEmployeeDismissionInfoHeaders = new QueryHrmEmployeeDismissionInfoHeaders();
			queryHrmEmployeeDismissionInfoHeaders.xAcsDingtalkAccessToken = token;
			QueryHrmEmployeeDismissionInfoRequest queryHrmEmployeeDismissionInfoRequest = new QueryHrmEmployeeDismissionInfoRequest()
					.setUserIdList(userids);
			QueryHrmEmployeeDismissionInfoResponse queryHrmEmployeeDismissionInfoResponse = hrmClient.queryHrmEmployeeDismissionInfoWithOptions(queryHrmEmployeeDismissionInfoRequest, queryHrmEmployeeDismissionInfoHeaders, new RuntimeOptions());
			List<QueryHrmEmployeeDismissionInfoResponseBody.QueryHrmEmployeeDismissionInfoResponseBodyResult> result = queryHrmEmployeeDismissionInfoResponse.getBody().getResult();
			return result;
		} catch (Exception e) {
			logger.error("getSeparatingEmployeeInfoV2 Exception: " + e);
		}
		return null;
	}

	/**
	 * @Description 获取指定部门的所有父部门列表
	 * @Param [deptId, appkey, appsecret]
	 * @Return java.util.List<Long>
	 */
	public List<Long> getFDepartmentList(Long deptId, String appkey,String appsecret) {
		String token = getToken(appkey,appsecret);
		try {
			if (StringUtil.isBlank(token))
				return null;
			DingTalkClient client = new DefaultDingTalkClient(FDEPARTMENT_LIST);
			OapiV2DepartmentListparentbydeptRequest req = new OapiV2DepartmentListparentbydeptRequest();
			req.setDeptId(deptId);
			OapiV2DepartmentListparentbydeptResponse rsp = client.execute(req, token);
			if ("0".equals(String.valueOf(rsp.getErrcode()))) {
				return rsp.getResult().getParentIdList();
			}
			return null;
		} catch (ApiException e) {
			logger.error("getFDepartmentList ApiException: " + e);
		}catch (Exception e) {
			logger.error("getFDepartmentList Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * @Description 获取部门详情
	 * @Param [deptId, appkey, appsecret]
	 * @Return DeptGetResponse
	 */
	public DeptGetResponse getDepartmentDeails(Long deptId, String appkey,String appsecret) {
		try {
			String token = getToken(appkey,appsecret);
			if (StringUtil.isBlank(token))
				return null;
			DingTalkClient client = new DefaultDingTalkClient(DEPARTMENT_DEAILS);
			OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
			req.setDeptId(deptId);
			req.setLanguage("zh_CN");
			OapiV2DepartmentGetResponse rsp = client.execute(req, token);
			return rsp.getResult();
		} catch (ApiException e) {
			logger.error("getDepartmentDeails ApiException: " + e);
		}catch (Exception e) {
			logger.error("getDepartmentDeails Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * @Description 获取用户详情
	 * @Param [userid, appkey, appsecret]
	 * @Return UserGetResponse
	 */
	public UserGetResponse getUserDeails(String userid, String token) {
		try {
			if (StringUtil.isBlank(token))
				return null;
			DingTalkClient client = new DefaultDingTalkClient(URL_USER_GET_V2);
			OapiV2UserGetRequest req = new OapiV2UserGetRequest();
			req.setUserid(userid);
			req.setLanguage("zh_CN");
			OapiV2UserGetResponse rsp = client.execute(req, token);
			if ("0".equals(rsp.getErrorCode())) {
				return rsp.getResult();
			}
			return null;
		} catch (ApiException e) {
			logger.error("getUserDeails ApiException: " + e);
		}catch (Exception e) {
			logger.error("getUserDeails Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * 使用 Token 初始化账号Client
	 * @return dingtalkworkflow_1_0.Client
	 * @throws Exception
	 */
	public static com.aliyun.dingtalkworkflow_1_0.Client createWorkflowClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalkworkflow_1_0.Client(config);
	}

	/**
	 * 使用 Token 初始化账号Client
	 * @return dingtalkhrm_1_0.Client
	 * @throws Exception
	 */
	public static com.aliyun.dingtalkhrm_1_0.Client createHrmClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalkhrm_1_0.Client(config);
	}

	/**
	 * @Description 如果只传入startTime参数，要求时间距离当前时间不能超过120天，endTime不传则默认取当前时间。
	 * 如果传入startTime参数和endTime参数，要求时间范围不能超过120天，同时startTime时间距当前时间不能超过365天。
	 * 批量获取的实例ID个数（循环获取），最多不能超过10000个
	 * @Param [token, processCode, startTime, endTime(非必传), userIds(发起人userId列表，最大列表长度为10(非必传))]
	 * @Return List<String>
	 */
	public List<String> getApproveList(String token, String processCode, Long startTime, Long endTime, List<String> userIds) {
		try {
			if (StringUtil.isBlank(token))
				return null;
			//分页游标 不为空表示有更多数据
			Long nextToken = 0L;
			//分页参数，每页大小，最多传20。
			Long maxResults = 20L;
			com.aliyun.dingtalkworkflow_1_0.Client client = createWorkflowClient();
            ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders = new ListProcessInstanceIdsHeaders();
			listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = token;
			List<String> list = new ArrayList<>();
			while (null != nextToken) {
				ListProcessInstanceIdsRequest listProcessInstanceIdsRequest = new ListProcessInstanceIdsRequest()
						.setProcessCode(processCode)
						.setStartTime(startTime)
						.setEndTime(endTime)
						.setNextToken(nextToken)
						.setMaxResults(maxResults)
						.setUserIds(userIds);
				ListProcessInstanceIdsResponse listProcessInstanceIdsResponse = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest, listProcessInstanceIdsHeaders, new RuntimeOptions());
				if (listProcessInstanceIdsResponse.getBody().getSuccess()) {
					list.addAll(listProcessInstanceIdsResponse.getBody().getResult().getList());
					String nextToken_t = listProcessInstanceIdsResponse.getBody().getResult().getNextToken();
					nextToken = StringUtil.isBlank(nextToken_t) ? null : Long.valueOf(nextToken_t);
				} else {
					logger.error("getApproveList 接口请求失败-----" + JSON.toJSONString(listProcessInstanceIdsResponse.getBody()));
				}
			}
			return list;
		} catch (TeaException err) {
			logger.error("getApproveList TeaException:" + err.getMessage());
			if (!empty(err.code) && !empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("Robot+code5:" + err.code);
				logger.error("Robot+message5:" + err.message);
			}
		} catch (Exception _err) {
			logger.error("getApproveList Exception:" + _err.getMessage());
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!empty(err.code) && !empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("Robot+code6:" + err.code);
				logger.error("Robot+message6:" + err.message);
			}
		}
		return null;
	}


	/**
	 * @param msgcontent 消息内容
	 * @param @的人员List
	 *                   <p>
	 *                   通过自定义机器人发送群消息，可以@群人员 new
	 *                   DefaultDingTalkClient("机器人access_token");
	 *                   at.setIsAtAll(false); 是否@所有人，true是，false否
	 * @Description:发送群组消息(可以@群人员)
	 * @Method:sendGroupMessageAt
	 */
	public boolean sendGroupMessageAtTo(String msgcontent,String Webhook,String pictureUrl, String external) {
		boolean flag=false;
		try {
			DingTalkClient client = new DefaultDingTalkClient(Webhook);
			OapiRobotSendRequest request = new OapiRobotSendRequest();
			request.setMsgtype("feedCard");
			OapiRobotSendRequest.Feedcard Feedcard = new OapiRobotSendRequest.Feedcard();
			OapiRobotSendRequest.Links link = new OapiRobotSendRequest.Links();
			link.setMessageURL(external);
			link.setPicURL(pictureUrl);
			link.setTitle(msgcontent);
			List<OapiRobotSendRequest.Links> list = new ArrayList<>();
			list.add(link);
			Feedcard.setLinks(list);
			request.setFeedCard(Feedcard);
			OapiRobotSendResponse response = client.execute(request);
			if ("0".equals(response.getCode())&&"ok".equals(response.getErrmsg()));
			{
				flag=true;
			}
		} catch (Exception e) {
			logger.error("sendGroupMessageAtTo Exception:" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 钉钉获取单个审批实例详情的URL【1.0版本】
	 * @param processInstanceId 审批实例id
	 *
	 */
	public Map<String,String> getProcessMessage(String processInstanceId, String token) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(URL_GET_SHR);
			OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
			req.setProcessInstanceId(processInstanceId);
			OapiProcessinstanceGetResponse rsp = client.execute(req, token);
			Map<String,String> map = new HashMap<>();
			map.put("body",rsp.getBody());
			return map;
		} catch (ApiException e) {
			logger.error("getProcessMessage ApiException:"+ e.getMessage());
		}catch (Exception e) {
			logger.error("getProcessMessage Exception:"+ e.getMessage());
		}
		return null;
	}

	/**
	 * 钉钉获取单个审批实例详情的接口【2.0版本】
	 * @param wbcxid
	 * @param processInstanceId 审批实例id
	 * @return
	 */
	public GetProcessInstanceResponseBodyResult getApproveInfo(String wbcxid, String processInstanceId) {
		try {
			if (StringUtil.isBlank(wbcxid))
				return null;

			String token = getToken(wbcxid);

			com.aliyun.dingtalkworkflow_1_0.Client client = createWorkflowClient();
            GetProcessInstanceHeaders getProcessInstanceHeaders = new GetProcessInstanceHeaders();
			getProcessInstanceHeaders.xAcsDingtalkAccessToken = token;
			GetProcessInstanceRequest getProcessInstanceRequest = new GetProcessInstanceRequest()
					.setProcessInstanceId(processInstanceId);
			GetProcessInstanceResponse processInstanceWithOptions = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new RuntimeOptions());
			if ("true".equals(processInstanceWithOptions.getBody().getSuccess())) {
				return processInstanceWithOptions.getBody().getResult();
			} else {
				logger.error("getApproveInfo 接口请求失败-----" + JSON.toJSONString(processInstanceWithOptions.getBody()));
			}
		} catch (TeaException err) {
			logger.error("getApproveInfo TeaException:"+ err.getMessage());
			if (!empty(err.code) && !empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("getApproveInfo Robot+code7:" + err.code);
				logger.error("getApproveInfo Robot+message7:" + err.message);
			}
		} catch (Exception _err) {
			logger.error("getApproveInfo Exception:"+ _err.getMessage());
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!empty(err.code) && !empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("getApproveInfo Robot+code8:" + err.code);
				logger.error("getApproveInfo Robot+message8:" + err.message);
			}
		}
		return null;
	}

	/**
	 * 钉钉通过UserId获取钉钉token
	 * @param userId 钉钉id
	 *
	 */
	public String getDingTokenByUserId(String userId) {
		String token;
		try {
			if(StringUtil.isNotBlank(userId)){
				User user = redisUtil.hugetDto("Users", userId);
				if(user!=null){
					if (StringUtil.isNotBlank(user.getWbcxid())){
						Object robotAccessToken = redisUtil.hget("accessToken",user.getWbcxid());
						logger.error("================robotAccessToken:"+ String.valueOf(robotAccessToken));
						//Object robotRobotCode = redisUtil.hget("robotCode",user.getWbcxid());
						if (null == robotAccessToken ){
							Object info = redisUtil.hget("WbcxInfo", user.getWbcxid());
							if (info != null){
								com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(String.valueOf(info));
								token = getToken(jsonObject.getString("appid"),jsonObject.getString("secret"));
								logger.error("================根据外部程序redis数据获取token:"+token);
							}else{
								token = getRobotAccessToken();
								logger.error("================WbcxInfo为空，调用接口获取token:"+ token);
							}
							redisUtil.hset( "accessToken",user.getWbcxid(),token,5400L);
						}else{
							token = String.valueOf(robotAccessToken);
							logger.error("================robotAccessToken不为空，token值:"+ token);
						}
					}else{
						token = getRobotAccessToken();
						logger.error("================用户reids的外部程序id为空，调用接口获取token值:"+ token);
					}
				}else{
					token = getRobotAccessToken();
					logger.error("================用户reids为空，调用接口获取token值:"+ token);
				}
			}else{
				token = getRobotAccessToken();
				logger.error("================用户名为空，调用接口获取token值:"+ token);
			}
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
		return token;
	}

	/**
	 * 钉钉通过企业id获取钉钉token
	 * @param miniappid
	 * @return
	 */
	public String getDingTokenByMiniappid(String miniappid){
		String token = null;
		if (StringUtil.isNotBlank(miniappid)){
			DBEncrypt dbEncrypt = new DBEncrypt();
			Map<Object, Object> wbcxInfoMap = redisUtil.hmget("WbcxInfo");
			for(Object obj : wbcxInfoMap.values()) {
				com.alibaba.fastjson.JSONObject wbcxInfo = JSON.parseObject(obj.toString());
				if (wbcxInfo!=null){
					String lx = wbcxInfo.getString("lx");
					if (CharacterEnum.DINGDING.getCode().equals(lx)){
						String wbcxminiappid = wbcxInfo.getString("miniappid");
						String eCodeminiappid = dbEncrypt.eCode(miniappid);
						if (eCodeminiappid.equals(wbcxminiappid)){
							Object accessToken = redisUtil.hget("accessToken",wbcxInfo.getString("wbcxid"));
							if (accessToken!=null){
								token = (String) accessToken;
								return token;
							}
							token = getToken(wbcxInfo.getString("appid"),wbcxInfo.getString("secret"));
							redisUtil.hset( "accessToken",wbcxInfo.getString("wbcxid"),token,5400L);
							break;
						}
					}
				}
			}
		}
		return token;
	}

	public String getDingTokenByWbcxid(String wbcxid) {
		String token = null;
		if (StringUtil.isNotBlank(wbcxid)){
			Object accessToken = redisUtil.hget("accessToken", wbcxid);
			if (accessToken!=null){
				token =  String.valueOf(accessToken);
			}else {
				Object wbcxInfo = redisUtil.hget("WbcxInfo", wbcxid);
				if (wbcxInfo!=null){
					Map map = JSON.parseObject(String.valueOf(wbcxInfo), Map.class);
					token = getToken(String.valueOf(map.get("appid")),String.valueOf(map.get("secret")));
					redisUtil.hset( "accessToken",wbcxid,token,5400L);
				}
			}
		}
		return token;
	}
	public Map<String,Object> getDingInfoByMiniappid(String id) {
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(id)){
			Map<Object, Object> wbcxInfoMap = redisUtil.hmget("WbcxInfo");
			DBEncrypt dbEncrypt = new DBEncrypt();
			for(Object obj : wbcxInfoMap.values()) {
				com.alibaba.fastjson.JSONObject wbcxInfo = JSON.parseObject(obj.toString());
				if (wbcxInfo!=null){
					String lx = wbcxInfo.getString("lx");
					if (CharacterEnum.DINGDING.getCode().equals(lx)){
						String miniappid = wbcxInfo.getString("decodeid");
						if (StringUtil.isBlank(miniappid)){
							String eCodeminiappid = wbcxInfo.getString("miniappid");
							miniappid = dbEncrypt.dCode(eCodeminiappid);
						}
						if (id.equals(miniappid)){
							map.putAll(wbcxInfo);
							break;
						}

					}
				}
			}
		}
		return map;
	}
	/**
	 *	调用本接口，获取企业智能考勤报表中的列信息。
	 * 	通过获取列信息中的ID值，可以根据列的ID查询考勤智能报表中该列的统计数据，
	 */
	public List<ColumnForTopVo> getAttColumns(String token){
		DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_ATTCOLUMNS);
		OapiAttendanceGetattcolumnsRequest req = new OapiAttendanceGetattcolumnsRequest();
		OapiAttendanceGetattcolumnsResponse rsp;
		try {
			rsp = client.execute(req, token);
			return rsp.getResult().getColumns();
		} catch (ApiException e) {
			logger.error("获取企业智能考勤报表中的列信息失败！"+e.getMessage());
		}
		return null;
	}
	/**
	 *	调用本接口，获取钉钉智能考勤报表的列值数据
	 */
	public List<OapiAttendanceGetcolumnvalResponse.ColumnValForTopVo> getColumnVal(String token, String columnIdList, String userid, Date dateStart, Date dateEnd){
		DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_ATTCOLUMNVAl);
		OapiAttendanceGetcolumnvalRequest req = new OapiAttendanceGetcolumnvalRequest();
		req.setUserid(userid);
		req.setColumnIdList(columnIdList);
		req.setFromDate(dateStart);
		req.setToDate(dateEnd);
		OapiAttendanceGetcolumnvalResponse rsp;
		try {
			rsp = client.execute(req, token);
			return rsp.getResult().getColumnVals();
		} catch (ApiException e) {
			logger.error("获取钉钉智能考勤报表的列值数据失败！"+e.getMessage());
		}
		return null;
	}
	/**
	 *	调用本接口，获取钉钉智能考勤假期报表的列值数据
	 */
	public List<OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo> getLeaveColumnVal(String token, String columnList, String userid, Date dateStart, Date dateEnd){
		DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_LEAVEATTCOLUMNVAl);
		OapiAttendanceGetleavetimebynamesRequest req = new OapiAttendanceGetleavetimebynamesRequest();
		req.setUserid(userid);
		req.setLeaveNames(columnList);
		req.setFromDate(dateStart);
		req.setToDate(dateEnd);
		OapiAttendanceGetleavetimebynamesResponse rsp;
		try {
			rsp = client.execute(req, token);
			return rsp.getResult().getColumns();
		} catch (ApiException e) {
			logger.error("获取钉钉智能考勤假期报表的列值数据失败！"+e.getMessage());
		}
		return null;
	}
	/**
	 * 批量获取考勤组信息
	 */
	public List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> getSimpleGroups(String token){
		try {
			DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_SIMPLEGROUPS);
			OapiAttendanceGetsimplegroupsRequest req = new OapiAttendanceGetsimplegroupsRequest();
			List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> allGroups = new ArrayList<>();
			long offset = 0L;
			boolean hasMore = true;
			while (hasMore){
				req.setOffset(offset);
				//最大10
				req.setSize(10L);
				OapiAttendanceGetsimplegroupsResponse rsp = client.execute(req, token);
				allGroups.addAll(rsp.getResult().getGroups());
				offset = offset+10L;
				//若没数据返回false
				hasMore = rsp.getResult().getHasMore();
			}
			return allGroups;
		} catch (ApiException e) {
			logger.error("批量获取考勤组信息数据失败！"+e.getMessage());
		}
		return null;
	}
	/**
	 * 获取参与考勤人员
	 */
	public List<OapiAttendanceGroupMemberListResponse.TopGroupMemberVo> getMemberList(String token,String opUserId,Long groupId){
		try {

			DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_MEMBER_LIST);
			OapiAttendanceGroupMemberListRequest req = new OapiAttendanceGroupMemberListRequest();
			List<OapiAttendanceGroupMemberListResponse.TopGroupMemberVo> allMembers = new ArrayList<>();
			//游标
			Long cursor = 0L;
			boolean hasMore = true;
			while (hasMore){
				req.setCursor(cursor);
				req.setOpUserId(opUserId);
				req.setGroupId(groupId);
				OapiAttendanceGroupMemberListResponse rsp = client.execute(req, token);
				//若没数据返回false
				hasMore = rsp.getResult().getHasMore();
				cursor = rsp.getResult().getCursor();
				if (!CollectionUtils.isEmpty(rsp.getResult().getResult())){
					allMembers.addAll(rsp.getResult().getResult());
				}
			}
			return allMembers;
		} catch (ApiException e) {
			logger.error("获取参与考勤人员数据失败！"+e.getMessage());
		}
		return null;
	}
	/**
	 * 获取班次摘要信息
	 */
	public List<String> getSchedulingAbstracts(String token, String opUserId){
		List<String> list=new ArrayList<>();
		try {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/shift/list");
			OapiAttendanceShiftListRequest req = new OapiAttendanceShiftListRequest();
			req.setOpUserId(opUserId);
			Long cursor = 0L;
			req.setCursor(cursor);
			boolean hasMore = true;
			while (hasMore){
				req.setCursor(cursor);
				OapiAttendanceShiftListResponse rsp = client.execute(req, token);
				List<OapiAttendanceShiftListResponse.TopMinimalismShiftVo> result = rsp.getResult().getResult();
				hasMore = rsp.getResult().getHasMore();
				cursor = rsp.getResult().getCursor();
                for (OapiAttendanceShiftListResponse.TopMinimalismShiftVo topMinimalismShiftVo : result) {
                    list.add(String.valueOf(topMinimalismShiftVo.getId()));
                }
			}
		}catch (ApiException e){
			logger.error("获取班次摘要数据失败！"+e.getMessage());
		}
		return list;
	}
	/**
	 * 获取班次详情
	 * @return
	 */
	public OapiAttendanceShiftQueryResponse.TopShiftVo getSchedulingDetails(String token, String opUserId, String shift_id) {
		try {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/shift/query");
			OapiAttendanceShiftQueryRequest req = new OapiAttendanceShiftQueryRequest();
			req.setOpUserId(opUserId);
			req.setShiftId(Long.valueOf(shift_id));
			OapiAttendanceShiftQueryResponse rsp = client.execute(req, token);
			return rsp.getResult();
		} catch (ApiException e) {
			logger.error("获取班次详情数据失败！" + e.getMessage());
		}
		return null;
	}
	/**
	 * 查询假期规则
	 * @return
	 */
	public List<OapiAttendanceVacationTypeListResponse.Result> queryHolidayRuleList(String token,String op_userid,String vacation_source){
		try {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/vacation/type/list");
			OapiAttendanceVacationTypeListRequest req = new OapiAttendanceVacationTypeListRequest();
			req.setOpUserid(op_userid);
			req.setVacationSource(vacation_source);
			OapiAttendanceVacationTypeListResponse rsp = client.execute(req, token);
			return rsp.getResult();
		}catch (Exception e){
			logger.error("获取假期规则数据失败！" + e.getMessage());
		}
		return null;
	}
	/**
	 * 查询假期余额
	 * @param token
	 * @return
	 */
	public List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> queryHolidayBalance(String token, String leaveCode, String op_userid, String userids){
		try {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/vacation/quota/list");
			OapiAttendanceVacationQuotaListRequest req = new OapiAttendanceVacationQuotaListRequest();
			List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> leaveQuotaUserListVos=new ArrayList<>();
			long offset=0L;
			boolean hasMore=true;
			req.setLeaveCode(leaveCode);
			req.setOpUserid(op_userid);
			req.setUserids(userids);
			while (hasMore){
				req.setOffset(offset);
				req.setSize(50L);
				OapiAttendanceVacationQuotaListResponse rsp = client.execute(req, token);
				hasMore = rsp.getResult().getHasMore();
				leaveQuotaUserListVos.add(rsp.getResult());
			}
			return leaveQuotaUserListVos;
		}catch (Exception e){
			logger.error("获取假期信息失败！" + e.getMessage());
		}
		return null;
	}
	/**
	 * @description 批量更新假期配额
	 * @param leaveQuotasList,opUserid
	 * @return
	 */
	public boolean updateVacationQuota(List<OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas> leaveQuotasList,String opUserid,String access_token){
		DingTalkClient client = new DefaultDingTalkClient(VACATION_QUOTA_UPDATE);
		OapiAttendanceVacationQuotaUpdateRequest req = new OapiAttendanceVacationQuotaUpdateRequest();
		req.setLeaveQuotas(leaveQuotasList);
		req.setOpUserid(opUserid);
		OapiAttendanceVacationQuotaUpdateResponse rsp = null;
		boolean isSuccess = false;
		try {
			rsp = client.execute(req, access_token);
			isSuccess = rsp.isSuccess();
			if (!CollectionUtils.isEmpty(rsp.getResult())){
				logger.error("批量更新假期配额失败！"+JSON.toJSONString(rsp.getBody()));
			}
		} catch (ApiException e) {
			logger.error("批量更新假期配额失败！"+JSON.toJSONString(rsp.getBody()));
		}
		return isSuccess;
	}

	/**
	 * @description 获取在职员工信息
	 * @param token
	 * @return
	 */
	public List<String> getAllJobEmployee(String token){
		DingTalkClient client = new DefaultDingTalkClient(EMPLOYEE_QUERYONJOB);
		OapiSmartworkHrmEmployeeQueryonjobRequest req = new OapiSmartworkHrmEmployeeQueryonjobRequest();
		List<String> list = new ArrayList<>();
		Long nextCursor = 0L;
		OapiSmartworkHrmEmployeeQueryonjobResponse rsp;
		while (null != nextCursor) {
			req.setOffset(nextCursor);
			req.setSize(50L);
			req.setStatusList("2,3,5,-1");
			try {
				rsp = client.execute(req, token);
				OapiSmartworkHrmEmployeeQueryonjobResponse.PageResult result = rsp.getResult();
				nextCursor = result.getNextCursor();
				list.addAll(result.getDataList());
			} catch (ApiException e) {
				logger.error("获取在职人员数据失败！"+e.getMessage());
			}
		}
		return list;
	}
	/**
	 * @description 获取审批单流程中的节点信息
	 * @param token
	 * @param processInstanceId
	 * @param userid
	 * @param deptId
	 * @param formComponents
	 * @return
	 */
	public ProcessForecastResponseBody.ProcessForecastResponseBodyResult getProcessForecast(String token,String processInstanceId,String userid,Integer deptId,List<ProcessForecastRequest.ProcessForecastRequestFormComponentValues> formComponents) throws Exception {
		com.aliyun.dingtalkworkflow_1_0.Client client = createWorkflowClient();
		ProcessForecastHeaders processForecastHeaders = new ProcessForecastHeaders();
		processForecastHeaders.xAcsDingtalkAccessToken = token;
		ProcessForecastRequest processForecastRequest = new ProcessForecastRequest()
				.setProcessCode(processInstanceId)
				.setDeptId(deptId)
				.setUserId(userid)
				.setFormComponentValues(formComponents);
		try {
			ProcessForecastResponse processForecastResponse = client.processForecastWithOptions(processForecastRequest, processForecastHeaders, new RuntimeOptions());
			return processForecastResponse.getBody().getResult();
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("getProcessForecast Robot+code1:" + err.code);
				logger.error("getProcessForecast Robot+message1:" + err.message);
			}

		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("getProcessForecast Robot+code2:" + err.code);
				logger.error("getProcessForecast Robot+message2:" + err.message);
			}

		}
		return null;
	}
	/**
	 *  创建代办
	 * @param appUrl 手机端访问路径
	 * @param pcUrl PC端访问路径
	 * @param operatorId 发起人unionId
	 * @param subject 标题
	 * @param description 描述
	 * @param dueTime 截止时间
	 * @param executorIds 代办执行者unionId
	 * @param participantIds 参与者的unionId
	 * @param priority 优先级，取值：10：较低 20：普通 30：紧急 40：非常紧急
	 */
	public boolean createTodoTask(String token,String appUrl,String pcUrl,String operatorId,String subject,String description,Long dueTime,List<String> executorIds,List<String> participantIds,Integer priority) {
		CreateTodoTaskHeaders createTodoTaskHeaders = new CreateTodoTaskHeaders();
		createTodoTaskHeaders.xAcsDingtalkAccessToken = token;

		CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs notifyConfigs = new CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs()
				.setDingNotify("1");
		CreateTodoTaskRequest.CreateTodoTaskRequestContentFieldList contentFieldList0 = new CreateTodoTaskRequest.CreateTodoTaskRequestContentFieldList();
		CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl detailUrl = new CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl()
				//手机端点击代办打开的页面
				.setAppUrl(appUrl)
				//PC端点击代办打开的页面
				.setPcUrl(pcUrl);
		CreateTodoTaskRequest createTodoTaskRequest = new CreateTodoTaskRequest()
				.setOperatorId(operatorId)
				.setSubject(subject)
				.setCreatorId(operatorId)
				.setDescription(description)
				.setDueTime(dueTime)
				.setExecutorIds(executorIds)
				.setParticipantIds(participantIds)
				.setDetailUrl(detailUrl)
				.setContentFieldList(Arrays.asList(contentFieldList0))
				.setIsOnlyShowExecutor(true)
				.setPriority(priority)
				.setNotifyConfigs(notifyConfigs);
		try {
			com.aliyun.dingtalktodo_1_0.Client createTodoClient = createTodoClient();
			CreateTodoTaskResponse todoTaskWithOptions = createTodoClient.createTodoTaskWithOptions(operatorId, createTodoTaskRequest, createTodoTaskHeaders, new RuntimeOptions());
			return true;
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("createTodoTask Robot+code1:" + err.code);
				logger.error("createTodoTask Robot+message1:" + err.message);
			}
			return false;
		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				// err 中含有 code 和 message 属性，可帮助开发定位问题
				logger.error("createTodoTask Robot+code2:" + err.code);
				logger.error("createTodoTask Robot+message2:" + err.message);
			}
			return false;
		}
	}

	public boolean sendBackLog(String unionid,String subject,String description,String accessToken) throws Exception {
		boolean result = false;
		if(StringUtil.isNotBlank(accessToken)){
			if(StringUtil.isNotBlank(unionid)){
				Config config = new Config();
				config.protocol = "https";
				config.regionId = "central";
				com.aliyun.dingtalktodo_1_0.Client client = new com.aliyun.dingtalktodo_1_0.Client(config);
				CreateTodoTaskHeaders createTodoTaskHeaders = new CreateTodoTaskHeaders();
				createTodoTaskHeaders.xAcsDingtalkAccessToken = accessToken;
				CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs notifyConfigs = new CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs()
						.setDingNotify("1");
				CreateTodoTaskRequest.CreateTodoTaskRequestContentFieldList contentFieldList0 = new CreateTodoTaskRequest.CreateTodoTaskRequestContentFieldList();

				CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl detailUrl = new CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl();
				CreateTodoTaskRequest createTodoTaskRequest = new CreateTodoTaskRequest()
						.setSubject(subject)
						.setDescription(description)
						.setExecutorIds(java.util.Arrays.asList(
								unionid
						))
						.setParticipantIds(java.util.Arrays.asList(
								unionid
						))
						.setDetailUrl(detailUrl)
						.setContentFieldList(java.util.Arrays.asList(
								contentFieldList0
						))
						.setIsOnlyShowExecutor(true)
						.setPriority(20)
						.setNotifyConfigs(notifyConfigs);

				try {
					client.createTodoTaskWithOptions(unionid, createTodoTaskRequest, createTodoTaskHeaders, new RuntimeOptions());
				} catch (TeaException err) {
					if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
						// err 中含有 code 和 message 属性，可帮助开发定位问题
					}

				} catch (Exception _err) {
					TeaException err = new TeaException(_err.getMessage(), _err);
					if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
						// err 中含有 code 和 message 属性，可帮助开发定位问题
					}
				}
				result = true;
			}
		}
		return result;
	}

	public Map<String,Object> processInstances(String processInstanceId,String accessToken) throws Exception {
		com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
		config.protocol = "https";
		config.regionId = "central";
		com.aliyun.dingtalkworkflow_1_0.Client client = new com.aliyun.dingtalkworkflow_1_0.Client(config);
		com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
		getProcessInstanceHeaders.xAcsDingtalkAccessToken = accessToken;
		com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
				.setProcessInstanceId(processInstanceId);
		try {
			GetProcessInstanceResponse getProcessInstanceResponse = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
			Object object = getProcessInstanceResponse.getBody().getResult();
			String str = JSON.toJSONString(object);
			return JSON.parseObject(str, new TypeReference<Map<String, Object>>(){});
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("调用获取审批实例详情失败"+err.code+"message:"+err.getMessage());
			}
		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("调用获取审批实例详情失败"+err.code+"message:"+err.getMessage());
			}
		}
		return null;
	}

	/**
	 * @Description: 获取下载链接
	 * @param token
	 * @param fileId
	 * @param processInstanceId
	 * @param comFlag
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/6/5 9:33
	 */
	public String getDownloadUrl(String token,String fileId,String processInstanceId,boolean comFlag)throws Exception {
		String downloadUrl = "";
		com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
		config.protocol = "https";
		config.regionId = "central";
		com.aliyun.dingtalkworkflow_1_0.Client client = new com.aliyun.dingtalkworkflow_1_0.Client(config);
		com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileHeaders grantProcessInstanceForDownloadFileHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileHeaders();
		grantProcessInstanceForDownloadFileHeaders.xAcsDingtalkAccessToken = token;
		com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileRequest grantProcessInstanceForDownloadFileRequest = new com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileRequest()
				.setProcessInstanceId(processInstanceId)
				.setWithCommentAttatchment(comFlag)
				.setFileId(fileId);
		try {
			GrantProcessInstanceForDownloadFileResponse grantProcessInstanceForDownloadFileResponse = client.grantProcessInstanceForDownloadFileWithOptions(grantProcessInstanceForDownloadFileRequest, grantProcessInstanceForDownloadFileHeaders, new com.aliyun.teautil.models.RuntimeOptions());
			downloadUrl = grantProcessInstanceForDownloadFileResponse.getBody().getResult().getDownloadUri();
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("获取附件下载地址失败："+err.getMessage());
			}

		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("获取附件下载地址失败："+err.getMessage());
			}
		}
		return downloadUrl;
	}


	public Map<String,Object> getProcessInstanceIdList(String token,String processCode,long startTime,long endTime,long nextToken) throws Exception{
		com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
		config.protocol = "https";
		config.regionId = "central";
		com.aliyun.dingtalkworkflow_1_0.Client client = new com.aliyun.dingtalkworkflow_1_0.Client(config);
		com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
		listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = token;
		com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest()
				.setProcessCode(processCode)
				.setStartTime(startTime)
				.setEndTime(endTime)
				.setNextToken(nextToken)
				.setMaxResults(20L)
				.setStatuses(java.util.Arrays.asList(
						"COMPLETED","TERMINATED"
				));
		try {
			ListProcessInstanceIdsResponse listProcessInstanceIdsResponse = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest, listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions());
			Object object = listProcessInstanceIdsResponse.getBody().getResult();
			String str = JSON.toJSONString(object);
			return JSON.parseObject(str, new TypeReference<Map<String, Object>>(){});
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("获取processInstanceId失败!processCode:"+processCode+"message:"+err.getMessage());
			}
		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("获取processInstanceId失败!processCode:"+processCode+"message:"+err.getMessage());
			}
		}
		return null;
	}

	/**
	 * @Description: 授权下载审批钉盘文件
	 * @param token
	 * @param mapList
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/6/6 15:27
	 */
	public void authorizationToDownload(String token,List<Map<String,String>> mapList) throws Exception{
		com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
		config.protocol = "https";
		config.regionId = "central";
		com.aliyun.dingtalkworkflow_1_0.Client client = new com.aliyun.dingtalkworkflow_1_0.Client(config);
		com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthHeaders addApproveDentryAuthHeaders = new com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthHeaders();
		addApproveDentryAuthHeaders.xAcsDingtalkAccessToken = token;
		if(CollectionUtils.isEmpty(mapList)) {
			return;
		}
		DBEncrypt db = new DBEncrypt();
		List<com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest.AddApproveDentryAuthRequestFileInfos> list = new ArrayList<>();
		for (Map<String,String> map : mapList) {
			com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest.AddApproveDentryAuthRequestFileInfos fileInfos = new com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest.AddApproveDentryAuthRequestFileInfos()
					.setFileId(map.get("fileId"))
					.setSpaceId(Long.parseLong(db.dCode(spaceId)));
			list.add(fileInfos);
		}
		com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest addApproveDentryAuthRequest = new com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest()
				.setUserId(db.dCode(userId))
				.setFileInfos(list);
		try {
			client.addApproveDentryAuthWithOptions(addApproveDentryAuthRequest, addApproveDentryAuthHeaders, new com.aliyun.teautil.models.RuntimeOptions());
		} catch (TeaException err) {
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("获取文件下载授权失败!fileList:"+JSON.toJSONString(mapList)+"spaceId:"+spaceId + "message:"+err.getMessage());
			}
		} catch (Exception _err) {
			TeaException err = new TeaException(_err.getMessage(), _err);
			if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
				logger.error("获取文件下载授权失败!fileList:"+JSON.toJSONString(mapList)+"spaceId:"+spaceId + "message:"+err.getMessage());
			}
		}
	}

}
