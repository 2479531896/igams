package com.matridx.igams.common.enums;

/**
 * 项目状态枚举类
 * @author fuwb
 * @time 20150424
 */
public enum StatusEnum {
	//项目状态  00，10，15，20，25，30，35，40，41，42，50，55，70，71，72，80，90
	CHECK_NO("00","未提交"),//未审核
	//项目状态专用
	CHECK_SUBMIT("10","提交申请"),//提交审核
	CHECK_UNPASS("15","提交申请未通过"),//审核未通过
	CHECK_PASS("80","审核通过"),//审核通过
	CHECK_MALMOD("20","物料修改"),//物料修改
	CHECK_MOVE_UNPPASS("02","移交审核不通过"),//移交审核不通过
	CHECK_METERING_UNPASS("03","计量审核不通过"),//计量审核不通过
	CHECK_TEST_CALIBRATION("04","验证审核不通过"),//验证审核不通过
	CHECK_SERVICE_CALIBRATION("05","维修/保养审核不通过"),//维修/保养审核不通过
	CHECK_DISCARD_CALIBRATION("06","报废审核不通过"),//报废审核不通过
	CHECK_MOVE_SUBMIT("07","移交审核中"),//移交审核中
	CHECK_METERING_SUBMIT("08","计量审核中"),//计量审核中
	CHECK_TEST_SUBMIT("09","验证审核中"),//验证审核中
	CHECK_SERVICE_SUBMIT("10","维修/保养审核中"),//维修/保养审核中
	CHECK_DISCARD_SUBMIT("11","报废审核中"),//报废审核中
	;
	
	private final String code;
	private final String value;
	
	StatusEnum(String code, String value) {
		this.value = value;
		this.code = code;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCode(){
		return code;
	}
	
	public static String getValueByCode(String code){
		for (StatusEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}