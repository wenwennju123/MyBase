<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
	<meta charset="UTF-8">
	<%--   	../../../jquery    表示根据当前路径深度，回到web根目录下找到jquery文件夹，再找到jquery文件夹下的css文件
		   	http://127.0.0.1:8080/crm/ 直接是应用根目录，可以使用el动态获取
		   	${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/--%>
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		/*入口函数*/
		$(function () {
			/*给登录按钮 loginBtn 添加 click 单击事件*/
			$("#loginBtn").click(function () {
				/*收集用户输入的参数*/
				let loginAct = $.trim($("#loginAct").val());
				let loginPwd = $.trim($("#loginPwd").val());
				let isRemAct = $("#isRemAct").prop("checked");
				/*进行表单验证 前端拦截，此时还未进入ajax 验证用户名或者密码是否为空*/
				if (loginAct === ""){
					$("#msg").html("用户名不能为空");
					/*直接结束当前函数*/
					return;
				}
				if (loginPwd === ""){
					$("#msg").html("密码不能为空");
					/*直接结束当前函数*/
					return;
				}
				/*发送异步请求*/
				$.ajax({
					url:'settings/qx/user/login.action',
					data:{
						loginAct:loginAct,
						loginPwd:loginPwd,
						isRemAct:isRemAct
					},
					type:'post',
					dataType:'json',
					/*处理响应回的json*/
					success:function (data) {
						if (data.code === "1" ){
							/*验证成功，跳转到主页面（Controller）*/
							window.document.location.href="workbench/index.action";
							$("#loginBtn").html("登陆成功");
						}else {
							/*登录失败，获取msg并展示到消息框中 html（innerHtml）是html语法和text都可以，text（innerText）只能显示纯文本*/
							$("#msg").html(data.message);
							$("#loginBtn").html("登陆");
						}
					},
					/*发送ajax请求之前，执行的函数，状态码为1，即为open方法已经调用，但是请求还未发送时
					原生ajax核心是XMLHttpRequest有0-4状态码readyState
					0:请求未初始化 unsent
        			1:服务器链接已建立 opened
        			2:请求已收到 headers_received
        			3:正在处理请求 loading
        			4:请求已完成且响应已就绪 done*/
					/*该函数的返回值T/F可以决定ajax是否真的向后台发送请求。可以在此处进行二次拦截*/
					beforeSend:function () {
						/*显示正在验证，防止登录时间过长*/
						$("#loginBtn").html("登陆中...");
						return true;
					}
				});
			});
			/*当输入框获取焦点时，清空msg span*/
			$("#loginAct").focus(function () {
				$("#msg").html("");
			});
			$("#loginPwd").focus(function () {
				$("#msg").html("");
			});
			/*给整个浏览器窗口添加键盘按下时间，并判定enter键，用户按下enter键时，触发loginBtn单击事件*/
			$(window).keydown(function (e) {
				/*判断是否是回车键 a--65 空格-32 回车-13*/
				if (e.keyCode === 13) {
					$("#loginBtn").click();
				}
			});
		});
	</script>
	<%--设置背景图片 background--%>
	<style>
		body {
			background-image: url("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/image/background.JPG");
			background-size: cover;
			background-repeat: no-repeat;
			background-attachment: fixed;
		}
	</style>
</head>
<body>
	<%--<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/background.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>--%>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
			&nbsp;&nbsp;美妆园区管理CRM
			<span style="font-size: 14px;">
				&nbsp;&nbsp;@Leowork脱敏展示
			</span>
		</div>
	</div>
	
	<div style="position: absolute; top: calc(50% - 200px); right: 100px;width:450px;height:400px;border:1px solid #D5D5D5;border-radius: 10px;background-color: rgba(255, 255, 255, 0.75)">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="loginAct" class="form-control" type="text" placeholder="脱敏展示用户名：admin" value="${cookie.loginAct.value}">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" class="form-control" type="password" placeholder="脱敏展示密码：123456" value="${cookie.loginPwd.value}">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<input id="isRemAct" type="checkbox" checked> 记住用户
						</label>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<label>
							<input id="isRemPwd" type="checkbox"> 记住密码
						</label>
					</div>
					<button id="loginBtn" type="button" class="btn btn-primary btn-lg btn-block" style="width: 350px; position: relative;top: 45px;">登录</button>
					<span id="msg" style="display: block; margin-top: 60px; text-align: right; color: rgba(0, 0, 0, 0.85);"></span>
				</div>
			</form>
		</div>
	</div>
</body>
</html>