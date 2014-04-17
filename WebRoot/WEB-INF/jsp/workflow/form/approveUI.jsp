<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
	<title>审批处理</title>
	<%@ include file="/WEB-INF/jsp/common.jsp" %>
</head>
<body>

<!-- 标题显示 -->
<div id="Title_bar">
    <div id="Title_bar_Head">
        <div id="Title_Head"></div>
        <div id="Title"><!--页面标题-->
            <img border="0" width="13" height="13" src="style/images/title_arrow.gif"/> 审批处理
        </div>
        <div id="Title_End"></div>
    </div>
</div>

<!--显示表单内容-->
<div id=MainArea>
    <s:form action="form_approve">
    	<s:hidden name="formId"></s:hidden>
    	<s:hidden name="taskId"></s:hidden>
    	<s:hidden name="approval" value="true"></s:hidden>
    
		<div class="ItemBlock_Title1"><!-- 信息说明 --><div class="ItemBlock_Title1">
        	<img border="0" width="4" height="7" src="style/blue/images/item_point.gif" /> 申请信息 </div> 
        </div>
        <div class="ItemBlockBorder">
            <div class="ItemBlock">
                <table cellpadding="0" cellspacing="0" class="mainForm">
					<tr>
						<td><a href="form_downloadApplication.action" style="text-decoration: underline">[点击下载申请的文档]</a></td>
					</tr>
                </table>
            </div>
        </div>
	
		<div class="ItemBlock_Title1"><!-- 信息说明 --><div class="ItemBlock_Title1">
        	<img border="0" width="4" height="7" src="style/blue/images/item_point.gif" /> 审批信息 </div> 
        </div>
        <div class="ItemBlockBorder">
            <div class="ItemBlock">
                <table cellpadding="0" cellspacing="0" class="mainForm">
                    <tr>
                        <td>审批意见</td>
                        <td><s:textarea name="comments" cssClass="TextareaStyle" cssStyle="width: 500px;"/></td>
                    </tr>
                    <s:if test="#outcomes.size() > 1">
                    <tr>
                        <td>选择下一步</td>
                        <td>
                        	<s:select name="outcome" list="#outcomes"></s:select>
                        </td>
                    </tr>
                    </s:if>
                </table>
            </div>
        </div>
		
		<!-- 表单操作 -->
        <div id="InputDetailBar" style="float:none">
			<!--onclick事件在submit之前触发-->
			<input type="image" src="style/blue/images/button/agree.png"/>
			<input type="image" onclick="document.forms[0].approval.value = 'false'" src="style/blue/images/button/disagree.png"/>
            <a href="javascript:history.go(-1);"><img src="style/images/goBack.png"/></a>
        </div>
		
    </s:form>
</div>
 
<div class="Description">
	说明：<br />
	1，同意：本次审批通过，流程继续执行。如果所有的环节都通过，则表单的最终状态为：已通过。<br />
	2，不同意：本次审批未通过，流程结束，不再继续执行。表单的最终状态为：未通过。<br />
</div>



</body>
</html>
