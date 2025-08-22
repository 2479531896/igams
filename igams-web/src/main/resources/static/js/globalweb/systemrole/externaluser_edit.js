
function chanceRole(){
    var url = "/role/role/pagedataRoleList?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择确认角色', chooseQrjsConfig);
}

function addPartner(url){
    $.showDialog(url,'伙伴新增',addPartnerConfig);
}

function setPartner(url){
    $.showDialog(url+"?yhid="+$("#externaluser_ajaxForm #yhid").val(),'伙伴设置',partnerInstallUserConfig);
}

function addBgfs(url){
    $.showDialog(url,'报告方式',addBasicConfig);
}

var addBasicConfig = {
    width: "900px",
    modalName: "addModal",
    gridName	: "tabGrid",
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
				var $this = this;
                if ($("#ajaxForm").valid()) {
                	$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

                    submitForm("ajaxForm", function (responseText, statusText) {
                        // responseText 可能是 xmlDoc, jsonObj, html, text, 等等...
                        // statusText 	描述状态的字符串（可能值："No Transport"、"timeout"、"notmodified"---304 "、"parsererror"、"success"、"error"
                    	if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
								var jclb = $("#dataFormSearch #jclb option:selected").val();
                            	var jclbmc = $("#dataFormSearch #jclb option:selected").text();
                            	//清除元素数据
								setTimeout(function () {
									//$($this["document"]).clearElements();
	                            	$($this["document"]).each(function() {
										if($(this).is('input,select,textarea')){
											$(this).clearFields();
											$(this).trigger("chosen:updated");
										}else{
											//筛选要清除数据的元素
											var elements = $(this).find('input,select,textarea');
											//筛选自定义清除数据的元素
											var elementFilters = $(elements).filter('[data-clear="true"]');
											if(elementFilters.length > 0){
												$(elementFilters).clearFields();
												$(elementFilters).trigger("chosen:updated");
											}else{
												$(elements).clearFields();
												$(elements).trigger("chosen:updated");
											}

										}
									});
								}, 500);
								//保留基础数据类别
                            	$("#dataFormSearch #jclb").val(jclb);
								$("#dataFormSearch #jclb").text(jclbmc);
								jQuery('#dataFormSearch #jclb').trigger("chosen:updated");
								$('#dataFormSearch #tb_list').bootstrapTable('refresh');
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
                    }, ".modal-footer > button");
                } else {

                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

var addPartnerConfig = {
	width		: "1000px",
	modalName	:"addPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				$("#ajaxForm #hbmc").val($("#ajaxForm #hbmc").val().trim());
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var kzsz={"zdfs":$('#ajaxForm input[name="zdfs"]:checked').val(),"zdfst":$('#ajaxForm input[name="zdfst"]:checked').val()};
                $("#ajaxForm #kzsz").val(JSON.stringify(kzsz));
				var email=$("#ajaxForm #yx").val().split(",");

				var emailRegExp= /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
				if(email.length>1||email[0]!=""){
					for (let i = 0; i < email.length; i++) {
						var trim = $.trim(email[i]);
						if(!emailRegExp.test(trim))	{
							$.alert(trim+"不是正确的邮箱地址！");
							return false;
						}
					}
				}
				var yx="";
				if(email.length>1||email[0]!=""){
					for (let i = 0; i < email.length; i++) {
						 yx=yx+$.trim(email[i])+",";
					}
				}
				if (yx!=""){
					yx = yx.substring(0, yx.lastIndexOf(','));
				}
				$("#ajaxForm #yx").val(yx);
				var selkh=$("#ajaxForm #khids").tagsinput('items');
				var hzgsmc="";
				for(var i=0;i<selkh.length;i++){
					hzgsmc+=","+selkh[i].text;
				}
				if(hzgsmc){
					$("#ajaxForm  #hzgsmc").val(hzgsmc.substring(1));
				}
				var $this = this;
				var opts = $this["options"]||{};
				var jcdwids="";
				$("#ajaxForm #tb_partner_list").find("tbody").find("tr").each(function() {
					if($(this).attr("data-uniqueid")){
						jcdwids = jcdwids + ","+ $(this).attr("data-uniqueid");
					}
				});
				jcdwids = jcdwids.substr(1);
				$("#ajaxForm  #jcdwids").val(jcdwids);
				var json_t=[];
				var val = $("#ajaxForm  #bgmb_json").val();
				var flag=false;
				if(val!="" && val!=null){
					json_t=JSON.parse(val);
					for (var i = 0; i < json_t.length; i++) {
						var xh=i+1;
						var bgmbid=$("#ajaxForm  #bgmbid_"+xh).val();
						json_t[i].bgmbid=bgmbid;
						var bgmbid2=$("#ajaxForm  #bgmbid2_"+xh).val();
						json_t[i].bgmbid2=bgmbid2;
						if(bgmbid!=null&&bgmbid!=''){
							flag=true;
						}
					}
					$("#ajaxForm #bgmb_json").val(JSON.stringify(json_t));
				}
				if(!flag){
					$.alert("请至少选择一个项目的模板！");
					return false;
				}

				//
//					var alldate = $("#ajaxForm #tb_partner_list").bootstrapTable('getData');
//					$("#ajaxForm  #jcdws").val(JSON.stringify(alldate));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                //下面为收费标准处理
				var xmhasChild = [];
				var zxmList = JSON.parse($("#ajaxForm #zxmList").val());
				for(var i=0 ; i<zxmList.length ; i++){
					if(zxmList[i].fcsid != null && zxmList[i].fcsid != undefined && zxmList[i].fcsid != ''){
						if(!xmhasChild.contains(zxmList[i].fcsid)){
							xmhasChild.push(zxmList[i].fcsid);
						}
					}
				}

				if(t_map.rows != null && t_map.rows.length > 0){
					var json = [];
					var repeatXm =[];
					var repeatZxm =[];
					for(var i=0;i<t_map.rows.length;i++){
						if (t_map.rows[i].xm == ''||t_map.rows[i].xm == undefined||t_map.rows[i].xm == null||t_map.rows[i].sfbz == ''||t_map.rows[i].sfbz == undefined||t_map.rows[i].sfbz == null){
							$.alert("收费标准：第"+(i+1)+"行，项目不能为空！或 收费标准不能为空！");
							return false;
						}
						if (xmhasChild.contains(t_map.rows[i].xm)){
							//有子项目
							if (t_map.rows[i].zxm == ''||t_map.rows[i].zxm == undefined||t_map.rows[i].zxm == null){
								$.alert("收费标准：第"+(i+1)+"行，子项目未填！");
								return false;
							}
							if(repeatXm.contains(t_map.rows[i].xm)){
								//存在项目重复，比较子项目
								if(repeatZxm.contains(t_map.rows[i].zxm)){
									$.alert("收费标准：第"+(i+1)+"行，子项目重复！");
									return false;
								}else {
									repeatZxm.push(t_map.rows[i].zxm);
								}
							}else {
								//项目不重复
								repeatXm.push(t_map.rows[i].xm);
								repeatZxm.push(t_map.rows[i].zxm);
							}
						}else {
							//无子项目
							if(repeatXm.contains(t_map.rows[i].xm)){
								$.alert("收费标准：第"+(i+1)+"行，项目重复！");
								return false;
							}else {
								repeatXm.push(t_map.rows[i].xm);
							}
						}

					}
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
						sz.hbid = t_map.rows[i].hbid;
						sz.xm = t_map.rows[i].xm;
						sz.zxm = t_map.rows[i].zxm;
						sz.sfbz = t_map.rows[i].sfbz;
						sz.tqje = t_map.rows[i].tqje;
						sz.ksrq = t_map.rows[i].ksrq;
						sz.jsrq = t_map.rows[i].jsrq;
						json.push(sz);
					}
					$("#ajaxForm #sfbz_json").val(JSON.stringify(json));
				}
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								preventResubmitForm(".modal-footer > button", false);
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var chooseQrjsConfig = {
	width: "800px",
	height: "500px",
	modalName: "chooseQrjsModal",
	formName: "exceptionList_Form",
	offAtOnce: true, // 当数据提交成功，立刻关闭窗口
	buttons: {
		success: {
			label: "确 定",
			className: "btn-primary",
			callback: function () {
				if (!$("#RoleForm").valid()) {
					return false;
				}
				var $this = this;
				var opts = $this["options"] || {};
				var sel_row = $('#RoleForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
				if (sel_row.length == 1) {
					$('#externaluser_ajaxForm #jsid').val(sel_row[0].jsid);
					$('#externaluser_ajaxForm #jsmc').val(sel_row[0].jsmc);
					$.closeModal(opts.modalName);
				} else {
					$.error("请选中一行");
					return false;
				}
				return false;
			}
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

function searchExternalUserResult(yhid){
    $.ajax({
        type:'post',
        async: false,
        url:"/systemrole/user/pagedataExternalUser",
        cache: false,
        data: {"yhid":yhid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data.xtyhDto){
                $("#externaluser_ajaxForm #yhm").text(data.xtyhDto.yhm);
                $("#externaluser_ajaxForm #zsxm").text(data.xtyhDto.zsxm);
                $("#externaluser_ajaxForm #jgmc").text(data.xtyhDto.jgmc);
            }
            if(data.xtjsDtos!=null && data.xtjsDtos.length>0){
                var jsmc="";
                for(var i = 0; i < data.xtjsDtos.length; i++){
                    if(yhid==data.xtjsDtos[i].yhid){
                        jsmc=jsmc+","+data.xtjsDtos[i].jsmc;
                    }
                }
                jsmc=jsmc.substring(1);
                $("#externaluser_ajaxForm #jsmc").text(jsmc);
            }
            if(data.hbqxDtos!=null && data.hbqxDtos.length>0){
                var hbmc="";
                for(var i = 0; i < data.hbqxDtos.length; i++){
                    hbmc=hbmc+","+data.hbqxDtos[i].hbmc;
                }
                hbmc=hbmc.substring(1);
                $("#externaluser_ajaxForm #hbmc").text(hbmc);
            }
        }
    });
}

var partnerInstallUserConfig = {
		width		: "1200px",
		modalName	: "partnerInstallUserModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						$.alert("请填写完整信息！");
						return false;
					}
					var num=0;
					$("#ajaxForm .hb input").each(function(){
						if(this.checked){
							num++;
						}
					})
					var $this = this;
					var opts = $this["options"]||{};
					var ids="";
					var size=$(".hb input[name='hbid']:checked").length;
					for(var i=0;i<size;i++){
						ids=ids+","+$(".hb input[name='hbid']:checked")[i].value;
					}
		    		ids = ids.substr(1);
		    		$("#ajaxForm #ids").val(ids);
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									//信息回显
                                    searchExternalUserResult($("#externaluser_ajaxForm #yhid").val());
                                    preventResubmitForm(".modal-footer > button", false);
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//事件绑定
function btnBind(){
    //根据输入信息模糊查询
    $('#externaluser_ajaxForm #cxybh').typeahead({
        source: function(query, process) {
            return $.ajax({
                url: '/experiment/library/pagedataCxyList',
                type: 'post',
                data: {
                    "cxybh": query,
                    "mc": query,
                    "access_token":$("#ac_tk").val()
                },
                dataType: 'json',
                success: function(result) {
                    var resultList = result.cxylist
                        .map(function(item) {
                            var aItem = {
                                cxyid: item.cxyid,
                                text:item.cxybh+"-"+item.mc,
                                cxybh: item.cxybh,
                                mc: item.mc,
                                xh: item.xh,
                                jt2bj:item.jt2bj,
                                jcdw:item.jcdw
                            };
                            return JSON.stringify(aItem);
                        });
                    return process(resultList);
                }
            });
        },
        matcher: function(obj) {
            var item = JSON.parse(obj);
            return ~item.text.toLowerCase().indexOf(
                this.query.toLowerCase())
        },
        sorter: function(items) {
            var beginswith = [],
                caseSensitive = [],
                caseInsensitive = [],
                item;
            var aItem;
            while (aItem = items.shift()) {
                var item = JSON.parse(aItem);
                if (!item.text.toLowerCase().indexOf(
                        this.query.toLowerCase()))
                    beginswith.push(JSON.stringify(item));
                else if (~item.text.indexOf(this.query))
                    caseSensitive.push(JSON.stringify(item));
                else
                    caseInsensitive.push(JSON.stringify(item));
            }
            return beginswith.concat(caseSensitive,
                caseInsensitive)
        },
        highlighter: function(obj) {
            var item = JSON.parse(obj);
            var xsrn = item.text;
            var query = this.query.replace(
                /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
            return xsrn.replace(new RegExp('(' + query +
                ')', 'ig'), function($1, match) {
                return '<strong>'+match+ '</strong>'
            })
        },
        updater: function(obj) {
            var item = JSON.parse(obj);
            $('#externaluser_ajaxForm #cxybh').val(item.cxybh);
            $('#externaluser_ajaxForm #mc').val(item.mc);
            $('#externaluser_ajaxForm #xh').val(item.xh);
            if(item.jt2bj=="1"){
                $("#externaluser_ajaxForm input[value='1']").attr("checked",true)
            }else{
                $("#externaluser_ajaxForm input[value='0']").attr("checked",true)
            }
            $("#externaluser_ajaxForm option[id='"+item.jcdw+"']").prop('selected','true');
            return item.cxybh;
        }
    });
}

$(document).ready(function(){
    //绑定事件
	btnBind();
    //基本初始化
    $('#externaluser_ajaxForm .zdpbCheck').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
        // 当开关状态改变时触发
        onSwitchChange : function(event, state) {
            if (state == true) {
                 $('#collapse'+event.target.id.replace("zdpbCheck","")).collapse('show');
            } else {
                 $('#collapse'+event.target.id.replace("zdpbCheck","")).collapse('hide');
            }
        }
    });
     $("#externaluser_ajaxForm .zdpbCheck").bootstrapSwitch('state', false);
});