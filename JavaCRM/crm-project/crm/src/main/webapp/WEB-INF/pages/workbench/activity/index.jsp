<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<meta charset="UTF-8">
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
	<%--jQuery--%>
	<script type="text/javascript" src="jquery/jquery-3.7.1.js"></script>
	<%--bootstrap--%>
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<%--bootstrap-datetimepicker--%>
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<%--bootstrap-pagination--%>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
	<%--JS--%>
	<script type="text/javascript">
		/*入口函数*/
		$(function(){
			/*给创建按钮添加单击事件，单机后显示创建活动信息的模态窗口*/
			$("#createActivityBtn").click(function () {
				/*完成前置任务，例如初始化数据，清空上一次填写的表单内容，重置from表单中所有元素对象等*/
				$("#createActivityForm").get(0).reset();
				/*选择器 model属性来显示 比toggle属性参数更加灵活*/
				$("#createActivityModal").modal("show");
			});

			/*给保存按钮添加单击事件*/
			$("#saveCreateActivityBtn").click(function () {
				/*收集参数，并发送给后台*/
				let owner = $("#create-marketActivityOwner").val();
				let name = $.trim($("#create-marketActivityName").val());
				let startDate = $("#create-startDate").val();
				let endDate = $("#create-endDate").val();
				let cost = $.trim($("#create-cost").val());
				let description = $.trim($("#create-description").val());
				/*进行表单验证，不合法数据进行前台拦截*/
				if(owner === ""){
					alert("所有者不能为空");
					return;
				}
				if(name === ""){
					alert("名称不能为空");
					return;
				}



				if(description.length > 200){
					alert("描述不得超过两百字");
					return;
				}



				if(startDate !== "" && endDate !== ""){
					/*判断两个日期的大小，字符串比较大小依照字典顺序逐个比较*/
					if(startDate > endDate){
						alert("结束日期不可早于开始日期");
						return;
					}
				}
				/*使用正则表达式验证cost为正整数的字符串*/
				let regExp = /^(([1-9]\d*)|0)$/;
				if (!regExp.test(cost)) {
					alert("成本必须是正整数");
					return;
				}
				/*数据验证合法，发送ajax请求*/
				$.ajax({
					url:"workbench/activity/saveCreateActivity.action",
					data:{
						owner:owner,
						name:name,
						startDate:startDate,
						endDate:endDate,
						cost:cost,
						description:description
					},
					type:'post',
					dataType:'json',
					success:function (data) {
						if (data.code === "1"){
							/*保存成功，关闭模态窗口*/
							$("#createActivityModal").modal("hide");
							/*刷新列表，查询并显示第 1 页数据，保持每页显示条数不变*/
							queryActivityByConditionForPage(1, $("#bottom-pagination").bs_pagination('getOption', 'rowsPerPage'));
						}else {
							/*保存失败，提示信息，不关闭模态窗口*/
							alert(data.message);
							$("#createActivityModal").modal("show");
						}
					}
				})
			});

			/*传参设置日历插件格式，参数为JS对象，给容器添加事件调用工具函数*/
			/*选中多个容器统一添加 类选择器*/
			$(".date").datetimepicker({
				format:'yyyy-mm-dd',/*格式，与后台java所需要的格式对应*/
				minView:'month',/*可选择的最小视图*/
				initialDate:new Date(),/*初始化显示的日期*/
				autoclose:true,/*设置选择完成日期或者时间之后，日历是否自动关闭*/
				todayBtn:true,/*设置是否显示 今天 按钮*/
				clearBtn:true/*设置是否显示清空按钮*/
			});

			/*当市场活动主页面加载完成，查询所有市场活动数据的第 1 页以及所有数据的总条数，默认每页显示 10 条记录*/
			queryActivityByConditionForPage(1, 10);

			/*给查询活动按钮添加单击事件，用户单击查询按钮，调用查询函数*/
			$("#queryActivityBtn").click(function () {
				/*显示第 1 页，使用插件自带的工具函数获取当前的每页显示条数，即为保证每页显示条数不变*/
				queryActivityByConditionForPage(1, $("#bottom-pagination").bs_pagination('getOption', 'rowsPerPage'));
			});

			/*给全选按钮添加单击事件，实现全选与取消全选功能*/
			$("#checkbox-all").click(function () {
				/*一种获取属性的方法$("#checkbox-all").prop("checked")
				也可以使用 this 来获取当前事件中的对象
				if (this.checked === true) {
					属性为true，则列表中所有checkbox状态设置为 已选中checked；反之则取消全选
					使用父子选择器，并使用type进行过滤，将 #activity-tbody 下的所有的 input 的 checkbox 标签都选中，修改他们的属性
					父子选择器中 右尖括号只能获取第一级（直接）的子标签， 空格可以获取全部子标签
					$("#activity-tbody input[type = 'checkbox']").prop("checked", true);
				}else {
					$("#activity-tbody input[type = 'checkbox']").prop("checked", false);
				}*/


				/*将上述if代码进行简化*/
				$("#activity-tbody input[type = 'checkbox']").prop("checked", this.checked);
			});

			/*实现全选功能中的：列表中被手动全选，则全选的选择框状态为已选中；若已全选，用户取消了列表中一个选择，则全选框置为未选中*/
			/*注意：列表中的标签元素，是ajax异步请求之后，根据相应结果动态生成的。无法直接在入口函数中注册事件*/
			/*且每次ajax请求获取相应信息，都应该再给动态生成的标签注册一次事件*/
			/*故采用jQuery中的on函数，动态添加事件，可以理解为将父子选择器用on分开写*/
			$("#activity-tbody").on("click", "input[type = 'checkbox']", function () {
				/*内部JS代码不变，只是保证注册的时机是动态的，代码执行时依旧执行JS*/
				/*可以通过比较列表中所有checkbox的数量与所有被选中checkbox的数量来判断*/
				/*也可以遍历父子选择器所选中的jQuery对象数组，判断每一个checkbox的checked属性*/
				if ($("#activity-tbody input[type = 'checkbox']").size() === $("#activity-tbody input[type = 'checkbox']:checked").size()) {
					$("#checkbox-all").prop('checked', true);
				}else {
					$("#checkbox-all").prop('checked', false);
				}
			});

			/*给删除市场活动按钮，添加单击事件*/
			$("#deleteActivityBtn").click(function () {
				/*收集参数，获取列表中所有被选中的checkbox*/
				let checkedIds = $("#activity-tbody input[type = 'checkbox']:checked");
				/*首先判断用户是否没有选中记录，即为数组长度为0，前台进行拦截并提示*/
				if (checkedIds.size() === 0){
					alert("请选择要删除的市场活动记录");
					return;
				}
				/*弹出确认窗口，让用户确认删除，注意confirm属于阻塞函数，返回值是true false*/
				if (window.confirm("请确认删除记录")){
					/*用户确认删除*/
					/*遍历数组，取出所有的id*/
					let ids = "";
					$.each(checkedIds, function () {
						/*获取每个对象的id，拼串 id=xxx&id=xxx&... 拼在地址后 发给后台*/
						ids += "id=" + this.value + "&";
					})
					/*截取末位字符 多余的 & */
					ids = ids.substring(0, ids.length - 1);
					/*发送到后端*/
					$.ajax({
						url:'workbench/activity/deleteActivityIds.action',
						data:ids, /*字符串形式的数据提交会被自动拼接到url后面，注意格式*/
						type:'post',
						dataType:'json',
						success:function (data) {
							if (data.code === "1"){
								/*删除成功，则刷新列表，显示第一页数据，并保持每页显示条数不变*/
								queryActivityByConditionForPage(1, $("#bottom-pagination").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								/*删除失败则提示信息*/
								alert(data.message);
							}
						}
					});
				}
			});

			/*给修改按钮添加单击事件*/
			$("#editActivityBtn").click(function () {
				/*收集参数id*/
				/*获取列表中被选中的checkbox*/
				let checkedId = $("#activity-tbody input[type = 'checkbox']:checked");
				/*当选择 0 条时，提示信息*/
				if (checkedId.size() === 0){
					alert("请选择要修改的记录");
					return;
				}
				/*当选择 2 条及以上时，提示信息*/
				if (checkedId.size() > 1){
					alert("请选择一条要修改的记录");
					return;
				}
				/*获取参数值，即为获取之前加载记录时，绑定到checkedbox的value属性上的id*/
				let id = checkedId.val();
				/*发送请求*/
				$.ajax({
					url:'workbench/activity/queryActivityById.action',
					data:{
						id:id
					},
					type:'post',
					dataType:'json',
					success:function (data) {
						/*对模态窗口中的表单中的元素进行赋值，即把市场活动的信息显示在修改的模态窗口上*/
						/*id赋值给 隐藏域*/
						$("#editActivity-id").val(data.id);
						/*owner赋值给 所有者下拉列表，当赋值给select下拉列表的value属性时，会自动匹配对应的下拉列表项进行选中*/
						$("#edit-marketActivityOwner").val(data.owner);
						/*name赋值给 名称*/
						$("#edit-marketActivityName").val(data.name);
						/*startDate与endDate赋值给 开始日期与结束日期*/
						$("#edit-startTime").val(data.startDate);
						$("#edit-endTime").val(data.endDate);
						/*cost赋值给 成本*/
						$("#edit-cost").val(data.cost);
						/*description赋值给 描述*/
						$("#edit-describe").val(data.description);
						/*弹出模态窗口*/
						$("#editActivityModal").modal("show")
					}
				});
			});


		});

		/*封装分页查询市场活动函数*/
		function queryActivityByConditionForPage(pageNo, pageSize) {
			/*首先收集参数*/
			let name = $("#query-name").val();
			let owner = $("#query-owner").val();
			let startDate = $("#query-startDate").val();
			let endDate = $("#query-endDate").val();
			/*发送请求*/
			$.ajax({
				url: "workbench/activity/queryActivityByConditionForPage.action",
				data: {
					name:name,
					owner:owner,
					startDate:startDate,
					endDate:endDate,
					pageNo:pageNo,
					pageSize:pageSize
				},
				type: 'post',
				dataType: 'json',
				success:function (data) {
					/*显示市场活动列表，遍历ActivityList，拼接tr标签，即为所有行。成为大字符串，传回tbody标签。使用Vue会更加简便*/
					let activityListTbodyStr = "";
					/*JS中，函数也可以作为参数， 循环下标 循环变量*/
					$.each(data.activityList, function (index, obj) {
						activityListTbodyStr += "<tr class=\"active\">";
						/*让对应的checkbox绑定对应记录的id，方便后期对记录进行操作*/
						activityListTbodyStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
						activityListTbodyStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='detail.html';\">" + obj.name + "</a></td>";
						activityListTbodyStr += "<td>" + obj.owner + "</td>";
						activityListTbodyStr += "<td>" + obj.startDate + "</td>";
						activityListTbodyStr += "<td>" + obj.endDate + "</td></tr>";
					});
					/*将拼接好的字符串传给展示容器tbody*/
					$("#activity-tbody").html(activityListTbodyStr);
					/*总条数为 data.totalRows，计算总页数，注意弱类型*/
					let totalPages = 1;
					totalPages = Math.ceil(data.totalRows/pageSize);
					/*对分页插件调用bs_pagination工具函数，显示翻页信息，需要传入总数据条数等参数*/
					$("#bottom-pagination").bs_pagination({
						currentPage: pageNo, /*当前页号*/

						rowsPerPage: pageSize, /*每页显示条数*/
						totalRows: data.totalRows, /*总条数*/
						totalPages: totalPages, /*总页数，必填*/

						visiblePageLinks: 5, /*可用的最大翻页卡片数*/

						showGoToPage: true,
						showRowsInfo: true,
						showRowsPerPage: true,
						/*用户切换页号后，触发事件，执行此函数，*/
						onChangePage: function (event, pageObj) {
							/*再次调用查询函数查询出对应数据记录，手动刷新列表，传参：当前的pageNo与pageSize*/
							queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
							/*归位全选按钮，状态为未选中，此行也可以写到query函数中，写在切换页号的回调函数中同理*/
							$("#checkbox-all").prop("checked", false);
						}
					});
				}
			});
		}
	</script>
</head>

<body>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
					<%--创建市场活动form表单--%>
					<form id="createActivityForm" class="form-horizontal" role="form">
						<%--所有者以及活动名称输入--%>
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<%--选择创建者菜单，需要选择所有用户 使用jstl c:foreach动态获取生成下拉列表--%>
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="user">
									<%--id作为真正对user的识别 uuid生成 显示为注册用户名--%>
									<option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						<%--开始日期与结束日期输入 使用timepicker插件完成日期统一格式输入·--%>
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control date" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control date" id="create-endDate" readonly>
							</div>
						</div>
						<%--成本输入--%>
                        <div class="form-group">
                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<%--描述信息输入--%>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
					</form>
				</div>
				<%--窗口底部按钮--%>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveCreateActivityBtn" type="button" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<%--抬头--%>
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<%--主体部分--%>
				<div class="modal-body">
					<%--用户填写表单部分--%>
					<form class="form-horizontal" role="form">
						<%--添加隐藏域，保存id便于操作--%>
						<input type="hidden" id="editActivity-id">
						<%--所有者，名称--%>
						<div class="form-group">
							<%--所有者--%>
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<%--选择创建者菜单，需要选择所有用户 使用jstl c:foreach动态获取生成下拉列表--%>
								<select class="form-control" id="edit-marketActivityOwner">
									<%--userList为request域中保存，在工作区切换页面时请求转发就已经查出--%>
									<c:forEach items="${userList}" var="user">
										<%--id作为真正对user的识别 uuid生成 显示为注册用户名--%>
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<%--名称--%>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>
						<%--开始日期与结束日期，使用日历插件--%>
						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control date" id="edit-startTime" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control date" id="edit-endTime" >
						</div>
						<%--成本--%>
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="1000.00">
							</div>
						</div>
						<%--描述--%>
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe" >123</textarea>
							</div>
						</div>
					</form>
				</div>
				<%--底部 关闭 更新 按钮--%>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>

	<%--<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>--%>

	<%-- 市场活动主页面标题 --%>
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>

	<%-- 市场活动主页面 --%>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
			<%--查询行--%>
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  <%--名称输入--%>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
					<%--所有者输入--%>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>
					<%--开始日期输入 使用插件--%>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control date" type="text" id="query-startDate" readonly/>
				    </div>
				  </div>
					<%--结束日期输入 使用插件--%>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control date" type="text" id="query-endDate" readonly/>
				    </div>
				  </div>
					<%--查询按钮--%>
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				</form>
			</div>
			<%--创建 修改 删除 按钮 以及文件导入导出工具行--%>
			<%--data-target方式开启模态窗口不利于维护，修改为单击事件触发--%>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createActivityBtn" type="button" class="btn btn-primary">
					  <span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="editActivityBtn" type="button" class="btn btn-default">
					  <span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteActivityBtn" type="button" class="btn btn-danger">
					  <span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" >
						<span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default">
						<span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default">
						<span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>

			<%--市场活动数据展示--%>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkbox-all"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<%--循环展示数据--%>
					<tbody id="activity-tbody"></tbody>
				</table>
				<%--底部分页栏，bs_pagination插件--%>
				<div id="bottom-pagination"></div>
			</div>

		</div>
	</div>
</body>
</html>