$('#menuTree').jstree( {'plugins':["wholerow","checkbox"],
	'core' : {
		'data' : {
			'url' : '/systemrole/role/menuTree',
			'data' : function (node) {
				var map = {};
				map['jsid'] = jQuery("#jsid").val();
				map['access_token'] = $("#ac_tk").val();
				return map;
            }
		}
	}
}).on('loaded.jstree', function(e, data){
	var wH  = $(window).height();
	$(this).parents('.modal-dialog').height(550);
	$(this).parents('.bootbox-body').height(550);
	var dialogH = $(this).parents('.modal-dialog').height();
	var top = (wH - dialogH)/2;
	$(this).parents('.modal-dialog').css("top",top);
});