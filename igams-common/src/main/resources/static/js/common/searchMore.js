/*
 * @desc:用于所有的项目list页面，在需要多条件检索时调用。 
 * @date2015年11月12日16:07 
 * @author: liuyj
 * @调用位置: jsp页面head部<script type="text/javascript" src="<%=systemPath%>/js/globalweb/comm/searchMore.js"></script> 
 */
	//单选条件类型
	var singleConditionType = "sfzq,fkbj,dbm,sfjs,sftj,cxyqbj,jdjg,ysjg,wcbj,qglx,htlx,lllx,txlb,clbj,sfsy,sfjs,sfzfyf,fkbj,kpbj,jcdxlx,kcl,bgzt,sfsd,dqnf,zqlx,sftg,sflyck,sfqs,sfqr,sftg,ccdg,fpwh,sfqdht,sfjsdh,lcxh,zxdnr,sbcs,yqgnyz,sjcs,sjcc,sjwzxhbmx,sjclhjs,xnyz,ysjl,sfyz,sfjl,kczt,sfqq,sflyxj,wdzt,scbj,fffs,dcbjflg,sfgk,sfkj,tgbj,sfty,sfnd,lb,jczp,oncoqx,zdfs";//是否危险品，是否收费，是否正确，是否付款，合作关系，是否接收，是否统计,测序仪器标记,提醒类别,处理标记,onco权限
	/**
	* 是否是空
	*/
	function isEmpty(str) {
		if(null == str || typeof str == 'undefined' || 'null'==str || jQuery.trim(str)==''){
			return true;
		}
		return false;
	}
  
  /**
   * 用于替换字符串中的特殊符号为转义符号（一般为id）
   * 以用于jquery获取元素（特殊字符与jquery中选择器使用冲突）
   * @param str
   */
  function escapeSel(str){
	  if(str==null||str.replace==null){
		  return str;
	  }
	  return str.replace(/(\.|&)/g,"\\$1");
  }
  
  /**
   * 增加状态条件
   * @return
   */
  function addTj(aType, aVal,aForm) {
      // 检索条件ID的保存
      var obj = $("#"+aForm +" #" + aType + "_id_tj");
      if (isEmpty(obj.val())) {
          obj.val(aVal);
      } else {
          obj.val(obj.val() + "','" + aVal);
      }
      var flag = true;
      var typeArr = singleConditionType.split(',');
      for(var i = 0;i<typeArr.length;i++){
    	  if(aType==typeArr[i]){
    		 flag = false; 
    	  }
      }
      // 单选条件
      if (flag == false) {
    	  var arrayVal = new Array();
    	  arrayVal = obj.val().split(',');
          if (arrayVal.length > 1) {
        	  var oldVal = arrayVal[0].substring(0,arrayVal[0].length - 1);
        	  // 删除旧值
        	  jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(oldVal)).attr("style","text-decoration:none;cursor:pointer;");
              jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(oldVal)).attr("onclick","addTj('"+aType+"','"+oldVal+"','"+aForm+"')");
              jQuery("#"+aForm +" #"+aType+"_li_"+escapeSel(oldVal)).remove();
              // 设置新值
              obj.val(aVal);
          }
      }
      // 选择条件部部门条件追加
      var getspan = jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(aVal)).find("span").html();
      jQuery("#"+aForm +" #sl_add_ul").append("<li id='"+aType+"_li_"+aVal+"'><a style='text-decoration:none;cursor:pointer;' title='点击取消' onclick=delTj('" + aType + "','"+aVal+"','"+aForm+"');><span>"+getspan+"</span></a></li>");
      jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(aVal)).attr("style","text-decoration:none;cursor:pointer;background-color:#C6CBD0 !important;color:#fff !important;");
      jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(aVal)).attr("onclick","delTj('"+ aType +"','"+aVal+"','"+aForm+"')");
      if($("#"+aForm +" #sl_add_ul").html()!=""){
          $("#"+aForm +" #sl_yx_con").show();
      }
      // 进行检索
      //setTimeout(function(){
      //    searchResult();
      //}, 20);
  }

  // 查询条件删除处理
  function delTj(aType, aVal,aForm) {
      // 检索条件服务对象ID的清空
      var obj = $("#"+aForm +" #"+aType+"_id_tj");
      if (!isEmpty(obj.val())) {
          var temp = "";
          var objVal = obj.val().split("','");
          for (var i=0; i<objVal.length; i++) {
              if (objVal[i]==aVal) {
                  continue;
              }
              if (temp=="") {
                  temp = objVal[i];
              } else {
                  temp = temp + "','" + objVal[i];
              }
          }
          jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(aVal)).attr("style","text-decoration:none;cursor:pointer;");
          jQuery("#"+aForm +" #"+aType+"_id_"+escapeSel(aVal)).attr("onclick","addTj('"+aType+"','"+aVal+"','"+aForm+"')");
          jQuery("#"+aForm +" #"+aType+"_li_"+escapeSel(aVal)).remove();
          
          obj.val(temp);
      }
      if($("#"+aForm +" #sl_add_ul").html()==""){
          $("#"+aForm +" #sl_yx_con").hide();
      }
      // 进行检索
      //setTimeout(function(){
    	//  searchResult();
      //}, 20);
  }

	/**
	 * 显示更多的事件函数（不应该在业务js中再写一遍！），通过如下方式调用绑定（非初始情况）
	 * jQuery("#xxx_id").find("[name='more']").each(function(){
			$(this).on("click", s_showMoreFn);
		});
	 */
	var s_showMoreFn = function(){
		if ($(this).text() == "更多>>") {
			$(this).text("收起<<");
			$(this).parent().parent(".sl_sel_list").find("ul").removeClass("item_more_hidden");
		} else {
			$(this).text("更多>>");
			$(this).parent().parent(".sl_sel_list").find("ul").addClass("item_more_hidden");
		}
	}; 
	
	$(function(){
	});
	
