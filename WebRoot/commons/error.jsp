<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ include file="/WEB-INF/jsp/common.jsp"%>
<html>
  <head>
    <%@ include file="/commons/meta.jsp"%>
    <title>Error Page</title>
  </head>

  <body>

    <div id="content">
      <%
        //Exception from JSP didn't log yet ,should log it here.
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        LogFactory.getLog(requestUri).error(exception.getMessage(), exception);
      %>
		
	  <h1><font color="red">服务器内部错误</font></h1>	
      <h3>System Runtime Error: <br><%=exception.getMessage()%></h3>
      <br>

      <button onclick="history.back();">Back</button>
      <br>

      <div id="detail_error_msg" style="display:block">
        <pre><%exception.printStackTrace(new java.io.PrintWriter(out));%></pre>
      </div>
    </div>
  </body>
</html>
