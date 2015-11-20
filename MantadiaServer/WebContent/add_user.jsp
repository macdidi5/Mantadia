<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.macdidi.mantadia.domain.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utilfunc" uri="http://net.macdidi.mantadia/utilfunc" %>

<c:set var="pageTitle">新增使用者</c:set>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="default.css" media="screen"/>
    <title>${pageTitle}</title>
</head>
<body>
<div class="container">
    <div class="main">
        <jsp:include page="/WEB-INF/include/banner.jsp">
            <jsp:param name="subTitle" value="${pageTitle}" />
        </jsp:include>
        
        <div class="content">
            <div class="item">
				<form action='admin_user.do?action=add' enctype='multipart/form-data' method='post'>
                    <table border='1' bordercolor='#AAA' cellspacing='0' cellpadding='0'>
                        <tr>
                            <td>帳號</td>
                            <td><input type='text' name='account' /></td>
                        </tr>
                        <tr>
                            <td>密碼</td>
                            <td><input type='text' name='password' /></td>
                        </tr>
                        <tr>
                            <td>姓名</td>
                            <td><input type='text' name='name' /></td>
                        </tr>
                        <tr>
                            <td>性別</td>
                            <td>
                                <select name='gender'>
                                    <option value='1'>男</option>
                                    <option value='0'>女</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>職務</td>
                            <td>
                                <select name='role'>
	                                <c:forEach var="roleName" items="${utilfunc:getRoleNameList()}" varStatus="counter">
	                                    <option value='${counter.count}'>${roleName}</option>
	                                </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>備註</td>
                            <td><input type='text' name='note' /></td>
                        </tr>
                        <tr>
                            <td>圖片</td>
                            <td><input type='file' name='image' /></td>
                        </tr>
                        <tr>                          
                            <td colspan='2'>
                                <input type='submit' value='新增'>&nbsp;
                                <input type="button" value="取消" onClick="history.go(-1);return true;">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        
        <jsp:include page="/WEB-INF/include/function.jsp" />
        <div class="clearer"><span></span></div>
    </div>
    
    <%@ include file="/WEB-INF/include/footer.jsp"%>
</div>
</body>
</html>