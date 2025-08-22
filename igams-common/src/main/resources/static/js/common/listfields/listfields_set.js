
//点击+图标添加字段
var seletLi = function(obj){
	var selectLi = $(obj).parent().parent();
	selectLi.find("i").remove();
	var selectCol = selectLi.clone();
	selectLi.remove();
	$("#list2").append(selectCol);
	/*selectCol.hover(function(){
		createDelEvent(this);
	},function(){
		createOutEvent(this);
	});*/
	
	sortData();
}
//点击-图标去除字段
var unseletLi = function(obj){
	var selectLi = $(obj).parent().parent();
	selectLi.find("i").remove();
	var selectCol = selectLi.clone();
	selectLi.remove();
	selectCol.find("#xszd").attr("name","xszd");
	$("#list1").append(selectCol)
	sortData();
}

function createAddEvent(t){
	$(t).children("div").addClass("export-select");
	$(t).children("div").append('<i class="fa fa-plus-circle fa-lg text-success"></i>');
	$(t).children("div").find('i').mousedown(function(){
		seletLi($(this));
		return false;
	});
	return false;
}

function createDelEvent(t){
	$(t).children("div").addClass("export-select");
	$(t).children("div").append('<i class="fa fa-minus-circle fa-lg text-danger"></i>');
	$(t).children("div").find('i').mousedown(function(){
		unseletLi($(this));
		return false;
	});
}

function createOutEvent(t){
	$(t).children("div").removeClass("export-select");
	$(t).children("div").find('i').remove();
	sortData();
}

$("#list1, #list2").dragsort({
	dragSelector: "div",
    dragSelectorExclude:"input, textarea, .sorting-line-desc",
	dragBetween: true,
	dragEnd: saveOrder,
	placeHolderTemplate: "<li class='placeHolder'><div></div></li>",
	scrollSpeed: 5
});

function saveOrder() {
	var data = $("#list1 li").map(function(){
		return
		$(this).children().html();
	}).get();
	$("input[name=list1SortOrder]").val(data.join("|"));
};

var search_timer;
$(document).ready(function () {
	$('#search_input').keyup(function () {
        clearTimeout(search_timer); // 清除计时器
        var t = this;
	    search_timer = setTimeout(function () { // 使用计时器，防止快速输入或打拼音的时候进行无用的过滤，提升性能
            filterItems(t);
        },500);
    });
});

function filterItems(t) {
    var $t = $(t);
    var v = $t.val();
    if(v == ''){
        $t.closest('.search-box').siblings('.dragsort').find('li').show(250);
        return false;
    }
    $t.closest('.search-box').siblings('.dragsort').find('li>div').each(function () {
        var t = $(this).text();
        if(t.indexOf(v) >= 0){
            $(this).parent('li').show(250);
        }else{
            $(this).parent('li').hide(250);
        }
    });
}
function sortData() {
	var $li = this;
	var _choseListDiv = $("#list2 div[id='choseListDiv']");
	for(var i=0;i<_choseListDiv.length;i++){
		$(_choseListDiv[i]).find("#xszd").attr("name","choseList["+i+"]");
	}
	$("#list1").children('li').not('.sorting-line').each(function(){	
		$(this).children("div").find('i').remove();
		//createAddEvent($li);
		$(this).off().hover(function(){
			createAddEvent(this);
		},function(){
			createOutEvent(this);
		});
		
	});
	
	$("#list2").children('li').each(function(){
		$(this).children("div").find('i').remove();
		//createDelEvent($li);
		$(this).off().hover(function(){
			createDelEvent(this);
		},function(){
			createOutEvent(this);
		});
		
	});
	if($li.length>0){
		$li.mouseover();
	}
	
};

jQuery(function(){
	//选择文件类型
	$("input[name='fileType']").unbind("change").change(function(){
		var fileType = $("input[name='fileType']:checked").val();
	})

	//鼠标悬停事件
	$("#list1 li").not(".sorting-line").unbind().hover(function(){
		createAddEvent(this);
	},function(){
		createOutEvent(this);
	});

	$("#list2 li").unbind().hover(function(){
		createDelEvent(this);
	},function(){
		createOutEvent(this);
	});

	$("#right-all").unbind("click").click(function(){
		var selAll = $("#list1 li").not(".sorting-line").clone()
		$("#list2").append(selAll);
		$("#list1 li").not(".sorting-line").remove();
		sortData();
	})

	$("#left-all").unbind("click").click(function(){
		var selAll = $("#list2").html();
		var _choseListDiv = $("#list2 div[id='choseListDiv']");
		for(var i=0;i<_choseListDiv.length;i++){
			$("#list1").append($(_choseListDiv[i]).parent())
		}
		$("#list2").empty();
		sortData();
	})

});


