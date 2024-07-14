<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta charset="UTF-8">
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
	<%--引入外部css--%>
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<%--引入外部js--%>
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/echarts/echarts.js"></script>
	<script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>

	<script type="text/javascript" >
		$(function () {
			let myChart1 = echarts.init(document.getElementById('chart1'));
			let option1 = {
				series: {
					type: 'sankey',
					layout: 'none',
					emphasis: {
						focus: 'adjacency'
					},



					data: [
						{
							name: 'a'
						},
						{
							name: 'b'
						},
						{
							name: 'a1'
						},
						{
							name: 'a2'
						},
						{
							name: 'b1'
						},
						{
							name: 'c'
						}
					],
					links: [
						{
							source: 'a',
							target: 'a1',
							value: 5
						},
						{
							source: 'a',
							target: 'a2',
							value: 3
						},
						{
							source: 'b',
							target: 'b1',
							value: 8
						},
						{
							source: 'a',
							target: 'b1',
							value: 3
						},
						{
							source: 'b1',
							target: 'a1',
							value: 1
						},
						{
							source: 'b1',
							target: 'c',
							value: 2
						}
					]
				}
			};
			option1 && myChart1.setOption(option1);


		})
	</script>
	<%--设置背景图片 background3--%>
	<%--<style>
		body {
			background-image: url("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/image/background3.JPG");
			background-size: cover;
			background-repeat: no-repeat;
			background-attachment: fixed;
		}
	</style>--%>
	<title>工作台</title>
</head>
<body>
	<%--添加一些的统计的表格以及 记事本功能--%>
	<%--添加广告-收益-日期相关图表 1-12月 底部点击切换，上部图表轮播特效--%>
	<div id="chart1" style="width: 80%; height: 80%;"></div>


</body>
</html>