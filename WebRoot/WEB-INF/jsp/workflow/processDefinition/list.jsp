<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
    <title>审批流程列表</title>
    <%@ include file="/WEB-INF/jsp/common.jsp"%>
    
    <script type="text/javascript">
	    function showProcessImage( pdId ){
	    	
	    	// var test1 = encodeURI("中");
	    	// var test2 = encodeURI(encodeURI("中"));
	    	// alert("中，" + test1 + "，" + test2);
	    	
	    	pdId = encodeURI(pdId); // 进行URL编码
	    	pdId = encodeURI(pdId); // 进行URL编码（第2次）
	    	var url = "${pageContext.request.contextPath}/processDefinition_showProcessImage.action?pdId=" + pdId + "&t=" + new Date();
	    	
	    	window.showModalDialog(url, null, "dialogHeight:500px; dialogWidth:600px; resizable:yes");
        }
        
        function deleteProcessDefinition(pdKey) {
        	pdKey = encodeURI(pdKey);
        	
        	return pdKey;
        }
    </script> 
</head>
<body>

<div id="Title_bar">
    <div id="Title_bar_Head">
        <div id="Title_Head"></div>
        <div id="Title"><!--页面标题-->
            <img border="0" width="13" height="13" src="style/images/title_arrow.gif"/> 审批流程管理
        </div>
        <div id="Title_End"></div>
    </div>
</div>

<div id="MainArea">
    <table cellspacing="0" cellpadding="0" class="TableStyle">
       
        <!-- 表头-->
        <thead>
            <tr align=center valign=middle id=TableTitle>
            	<td width="200px">流程名称</td>
				<td width="80px">最新版本</td>
                <td width="300px">说明</td>
                <td>相关操作</td>
            </tr>
        </thead>

		<!--显示数据列表-->
        <tbody id="TableData" class="dataContainer">
        
        <s:iterator value="#pdList">
			<tr class="TableDetail1 template">
				<td>${name}&nbsp;</td>
				<td align="CENTER">${version}&nbsp;</td>
				<td>${description}&nbsp;</td>
				<td><s:a action="processDefinition_delete" onclick="return delConfirm()">
						<s:param name="key">${key}</s:param>
						删除
					</s:a>
					
					<a href="javascript: showProcessImage('${id}')">查看流程图</a> 
				</td>
			</tr>
        </s:iterator>

        </tbody>
    </table>
    
    <!-- 其他功能超链接 -->
    <div id="TableTail">
        <div id="TableTail_inside">
            <table border="0" cellspacing="0" cellpadding="10" align="left">
                <tr>
			        <td><div class="FuncBtn">
                            <div class=FuncBtnHead></div>
                            <div class=FuncBtnMemo>
								<s:a action="processDefinition_addUI">部署流程定义文档</s:a>
							</div>
                            <div class=FuncBtnTail></div>
                        </div></td>
                </tr>
			</table>
        </div>
    </div>
</div>

<div class="Description">
	说明：<br />
	1，列表显示的是所有流程定义（不同Key）的最新版本。<br />
	2，删除流程定义时，此Key的所有版本的流程定义都会被删除。<br />
	3，查看流程图时显示的这个最新版本的流程定义的图片。
</div>

</body>
</html>
	