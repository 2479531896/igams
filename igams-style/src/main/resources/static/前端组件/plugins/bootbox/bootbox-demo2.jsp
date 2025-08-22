<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<script type="text/javascript">
	function closeModal(){
		$.alert("操作成功",function() {
			$.closeModal("window111");
		});
	}
	</script>
	<style type="text/css">
		.modal-width{
			
		}
	</style>
	</head>
	<body>

		<%--<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">
				<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">
				报名对象设置
			</h4>
		</div>
		<div class="modal-body">--%>

			<!-- Nav tabs -->
			<ul class="nav nav-tabs sl_nav_tabs" role="tablist"  >
				<li  class="nav-item" >
					<a  class="nav-link active" href="#home" role="tab" data-bs-toggle="tab">组合对象</a>
				</li>
				<li class="nav-item" >
					<a class="nav-link"  href="#profile" role="tab" data-bs-toggle="tab">学生对象</a>
				</li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content sl_bmdxsz">
				<div class="tab-pane active " id="home">
					<div class="sl_bor">
						<form class="form-horizontal" role="form">
							<div class="row ">
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											学院
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option>
													学院
												</option>
												<option></option>
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											校区
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											年级
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											层次
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											专业
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option>
													英语
												</option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											性别
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											方向
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											学生类别
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="" class="col-sm-4 control-label">
											班级
										</label>
										<div class="col-sm-8">
											<select class="form-control">
												<option></option>
												<option></option>
											</select>
										</div>
									</div>
								</div>
							</div>
							
						</form>
						<p class="sl_bar_btn">
							<button type="button" class="btn btn-default btn-sm" onclick="closeModal()">
								限制
							</button>
							<button type="button" class="btn btn-default btn-sm">
								面向
							</button>
						</p>
					</div>
					<textarea></textarea>
				</div>
				<div class="tab-pane" id="profile">
					<div class="sl_bor">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th>
										<input name="" type="checkbox" value="">
									</th>
									<th>
										选择
									</th>
									<th>
										学号
									</th>
									<th>
										姓名
									</th>
									<th>
										性别
									</th>
									<th>
										班级
									</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input name="" type="checkbox" value="">
									</td>
									<td></td>
									<td>
										xh100001
									</td>
									<td>
										王明
									</td>
									<td>
										性别
									</td>
									<td>
										计1301班
									</td>
								</tr>
							</tbody>
						</table>
						<p class="sl_bar_btn">
							<button type="button" class="btn btn-default btn-sm">
								限制
							</button>
							<button type="button" class="btn btn-default btn-sm">
								面向
							</button>
						</p>
					</div>
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th>
									对象类型
								</th>
								<th>
									学号
								</th>
								<th>
									姓名
								</th>
								<th>
									性别
								</th>
								<th>
									班级
								</th>
								<th>
									操作
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									面向
								</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td>
									限制
								</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</div>
				
			</div>
		<%--</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">
				关 闭
			</button>
			<button type="button" class="btn btn-primary" onclick="closeModal.call(this);">
				保 存
			</button>
		</div>

	--%></body>
</html>
