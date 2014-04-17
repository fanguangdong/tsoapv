<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	
<html>
<head>
    <title>用户列表</title>
    <%@ include file="../common.jsp" %>
   
    <script type="text/javascript">
    	function mouseIn(a) {
    		
    		a.style.background='#F5F9FC';
    	}
    	
    	function mouseOut(a) {
    		a.style.background='#F5F9AC';
    	}
    	
    	
    </script>
</head>
<body>

<div id="Title_bar">
    <div id="Title_bar_Head"> 
        <div id="Title_Head"></div>
        <div id="Title"><!--页面标题-->
            <img border="0" width="13" height="13" src="style/images/title_arrow.gif"/> 用户管理
        </div>
        <div id="Title_End"></div>
    </div>
</div>

<div id="MainArea">
    <table cellspacing="0" cellpadding="0" class="TableStyle" >
       
        <!-- 表头-->
        <thead>
            <tr align=center valign=middle id=TableTitle>
                <td width="100">登录名</td>
                <td width="100">姓名</td>
                <td width="40">性别</td>
                <td width="100">入职日期</td>
                <td width="100">身份证号</td>
                <td width="100">电话</td>
                <td width="100">邮箱</td>
                <td width="100">所属部门</td>
                <td width="200">岗位</td>
                <td>备注</td>
                <td>相关操作</td>
            </tr>
        </thead>
        
        <!--显示数据列表-->
        <tbody id="TableData" class="dataContainer" >
        	<s:iterator value="#userList">
        		<tr class="TableDetail1 template" >
	                <td>${loginName}&nbsp;</td>
	                <td>${name}&nbsp;</td>
	                <td><s:property value="%{gender == 0 ? '女' : '男'}"/>&nbsp;</td>
	                <td>${hiredate }</td>
	                <td>${idCard }</td>
	                <td>${phoneNumber }</td>
	                <td>${email }</td>
	                <td>${department.name}&nbsp;</td>
	                <td>${roles}</td>
	                <td>${description}&nbsp;</td>
	                <td>
	                	<s:if test="#session.user.hasPrivilege('/user_delete')">
							<a onClick="return delConfirm()" href="user_delete.action?id=${id }">删除</a>
						</s:if>
						<s:else>
							<font color="gray">删除</font>
						</s:else>
						
						<s:if test="#session.user.hasPrivilege('/user_updateUI')">
							<a href="user_updateUI.action?id=${id }">修改</a>
						</s:if>
						<s:else>
							<font color="gray">修改</font>
						</s:else>
						
	                    <s:if test="#session.user.isAdmin()">
	                    	<a href="#" onClick="return window.confirm('您确定要初始化密码为1234吗？')">初始化密码</a>
	                    </s:if>
						
	                </td>
            	</tr>
        	</s:iterator>
            
        </tbody>
    </table>
    
    <!-- 其他功能超链接 -->
    <div id="TableTail">
        <div id="TableTail_inside">
            <a href="user_addUI.action"><img src="style/images/createNew.png" /></a>
        </div>
    </div>
</div>

<script type="text/javascript">
    	$(".TableDetail1 template").click(function() {alert(33)});
    	
    	
    </script>
</body>
</html>



  	
