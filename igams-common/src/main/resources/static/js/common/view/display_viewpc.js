var _load=$.fn.load;
var _msg=''
$.fn.extend({
	load:function(url,param,calbck){
		//其他操作和处理
		//..
		//此处用apply方法调用原来的load方法，因为load方法属于对象，所以不可直接对象._load（...）
		if(!param){
			param = {};
		}
		param["access_token"] = $("#ac_tk").val();
		return _load.apply(this,[url,param,calbck]);
	}
});
window.addEventListener('message', (event) => {
	_msg=event.data
	updateColor()

})
function dealSpaceQuery(idStr){
	//去除字符中所有空格
	// $(idStr).val($(idStr).val().replaceAll(" ",""));
	//截取字符串第一个空格之前的数据进行返回，，$(idStr).val().indexOf(" ")<=0，即不存在空格或第一个字符就是空格，此时不做处理，维持原字符串
	if ($(idStr).val().indexOf(" ") > 0){
		$(idStr).val(    $(idStr).val().substring( 0 , $(idStr).val().indexOf(" ") )    );
	}
}
function updateColor(){
	setTimeout(()=>{
		$("#sl_yx_con").css("display","flex")
		if(_msg.color!=null&&_msg.color!=""){
			$(".table").addClass("tabledark")
			document.documentElement.style.setProperty('--dark', _msg.color);

		}
		if(_msg.dark!=null&&_msg.dark=='true'){
			document.documentElement.style.setProperty('--darkcolor', "#0a0a0a");
			document.documentElement.style.setProperty('--darkcolor_td', "#0a0a0a");
			document.documentElement.style.setProperty('--darkbackground', "rgb(208, 208, 208)");
			document.documentElement.style.setProperty('--hoverback', "#0c1f32");
			document.documentElement.style.setProperty('--hovercolor', "rgb(208, 208, 208)");
			document.documentElement.style.setProperty('--sxback', "#0a0a0a");
			document.documentElement.style.setProperty('--slidpx', "1px");
			document.documentElement.style.setProperty('--oacolor', "white");
			$(".form-control").addClass('infoTextarea change')

			$("tr").each(function(){
				if($(this).attr("class")==undefined||$(this).attr("class")==null){
					$(this).css("background-color","black")
				}
			})
			$(".chosen-container").bind("DOMNodeInserted",function(e){
				$(".active-result").css("color","rgb(208, 208, 208)")
			})

		}
		if(_msg.dark!=null&&_msg.dark=='false'){
			document.documentElement.style.setProperty('--darkcolor', "white");

			document.documentElement.style.setProperty('--darkbackground', "black");
			document.documentElement.style.setProperty('--hoverback', "#DFECFE");
			document.documentElement.style.setProperty('--hovercolor', "black");
			document.documentElement.style.setProperty('--sxback', "#fff1dc");
			document.documentElement.style.setProperty('--slidpx', "0px");
			document.documentElement.style.setProperty('--oacolor', "black");
			$(".form-control").removeClass('infoTextarea change')
			$("tr").each(function(){
				if($(this).attr("class")==undefined||$(this).attr("class")==null){
					$(this).css("background-color","white")
				}
			})

			$(".chosen-container").bind("DOMNodeInserted",function(e){
				$(".active-result").css("color","black")
			})
		}
		$(".table>tbody").bind("DOMNodeInserted",function(e){
			if(_msg.dark!=null&&_msg.dark=='true'){
				document.documentElement.style.setProperty('--darkcolor', "#0a0a0a");
				document.documentElement.style.setProperty('--darkcolor_td', "#0a0a0a");
				document.documentElement.style.setProperty('--darkbackground', "rgb(208, 208, 208)");
				document.documentElement.style.setProperty('--hoverback', "#0c1f32");
				document.documentElement.style.setProperty('--hovercolor', "rgb(208, 208, 208)");
				document.documentElement.style.setProperty('--sxback', "#0a0a0a");
				document.documentElement.style.setProperty('--slidpx', "1px");
				document.documentElement.style.setProperty('--oacolor', "black");
				$(".form-control").addClass('infoTextarea change')


				$(".chosen-container").bind("DOMNodeInserted",function(e){
					$(".active-result").css("color","rgb(208, 208, 208)")
				})
				$("tr").each(function(){
					if($(this).attr("class")==undefined||$(this).attr("class")==null){
						$(this).css("background-color","black")
					}
				})
			}
			if(_msg.dark!=null&&_msg.dark=='false'){
				document.documentElement.style.setProperty('--darkcolor', "white");
				document.documentElement.style.setProperty('--darkbackground', "black");
				document.documentElement.style.setProperty('--hoverback', "#DFECFE");
				document.documentElement.style.setProperty('--hovercolor', "black");
				document.documentElement.style.setProperty('--sxback', "#fff1dc");
				document.documentElement.style.setProperty('--slidpx', "0px");
				document.documentElement.style.setProperty('--oacolor', "black");
				$(".form-control").removeClass('infoTextarea change')
				$("tr").each(function(){
					if($(this).attr("class")==undefined||$(this).attr("class")==null){
						$(this).css("background-color","white")
					}
				})

				$(".chosen-container").bind("DOMNodeInserted",function(e){
					$(".active-result").css("color","black")
				})
			}

		})

	},100)
}
$(function(){
	var view_url = $("#urlPrefix").val()+$("#view_url").val();

	if(!$("#view_url") || $("#view_url").val()==""){
		$.alert("未获取到显示的路径信息");
		return;
	}

	$("#disdiv_vue").load(view_url,function(){
		updateColor()
		$('[data-bs-toggle="tooltip"]').tooltip()
	});
	$('#disdiv_vue').on('scroll',function(){
		if($('#sjxx_formSearch #sjxx_list').offset() && $('#sjxx_formSearch #sjxx_list').offset()!= "null"){
			if($('#sjxx_formSearch #sjxx_list').offset().top - 122 < 0){
				if(!$('#sjxx_formSearch .js-affix').hasClass("affix"))
				{
					$('#sjxx_formSearch .js-affix').removeClass("affix-top").addClass("affix");
				}
				$('#sjxx_formSearch .js-affix').css({
					'top': '0px',
					"z-index":1000,
					"width":'100%'
				});
			}else{
				if(!$('#sjxx_formSearch .js-affix').hasClass("affix-top"))
				{
					$('#sjxx_formSearch .js-affix').removeClass("affix").addClass("affix-top");
				}
				$('#sjxx_formSearch .js-affix').css({
					'top': '0px',
					"z-index":1,
					"width":'100%'
				});
			}
		}

	})
});