<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="/WEB-INF/pages/globalweb/head/pagehead_v5.ini"%>

		<script type="text/javascript">
	jQuery(function($) {
		$("#modalTest").click(function() {
			$.window("modal2.jsp","提示窗口",{
				width:"900px",
				modalName:"window111",
				buttons:{
					success : {
						label : "确 定",
						className : "btn-success",
						callback : function() {
							$.alert("操作成功",function() {
								
							});
							return false;
						}
					},
					danger : {
						label : "Danger!",
						className : "btn-danger",
						callback : function() {
							
							return false;
						}
					},
					main : {
						label : "Click ME!",
						className : "btn-primary",
						callback : function() {
						}
					}
				}
			});
				
		});
	});
</script>
<style type="text/css">
</style>
	</head>


	<body>


		<!-- 按钮触发模态框 -->
		<button class="btn btn-primary btn-lg" id="modalTest">
			开始演示模态框ssssssssss
		</button>





	</body>
</html>