<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>

<head>
<title>天时传媒</title>

<base href="<%=basePath %>"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>


<frameset rows="100,*,25" framespacing="0" border="0" frameborder="0">

    <frame src="home_top.action" name="TopMenu"  scrolling="no" noresize />
    
    <frameset cols="180,*" id="resize">
        <frame noresize name="menu" src="home_left.action" scrolling="yes" />
        <frame noresize name="right" src="home_right.action" scrolling="yes" />
    </frameset>
    
    <frame noresize name="status_bar" scrolling="no" src="home_bottom.action" />
</frameset>


<noframes>
<body>
</body>
</noframes>
</html>
