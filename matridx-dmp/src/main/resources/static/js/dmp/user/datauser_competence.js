$('#competenceTree').jstree( {'plugins':["wholerow","checkbox"],
	'core' : {
		'data' : {
			'url' : '/dmp/user/dataUserCompetenceTree',
			'data' : function (node) {
				var map = {};
				map['rzid'] = jQuery("#rzid").val();
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