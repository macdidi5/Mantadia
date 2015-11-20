<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utilfunc" uri="http://net.macdidi.mantadia/utilfunc" %>

<c:set var="pageTitle">修改使用者</c:set>
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
                <form action='admin_user.do?action=update&id=${user.id}' enctype='multipart/form-data' method='post'>
	                <table border='1' bordercolor='#AAA' cellspacing='0' cellpadding='0'>
	                    <tr>
	                        <td>編號</td>
	                        <td>${user.id}</td>
	                    </tr>                    
	                    <tr>
	                        <td>帳號</td>
	                        <td><input type='text' name='account' value='${user.account}' /></td>
	                    </tr>
	                    <tr>
	                        <td>密碼</td>
	                        <td><input type='text' name='password' value='${user.password}' /></td>
	                    </tr>
	                    <tr>
	                        <td>姓名</td>
	                        <td><input type='text' name='name' value='${user.name}' /></td>
	                    </tr>
	                    <tr>
	                        <td>性別</td>
	                        <td>
	                            <select name='gender'>
	                                <option value='1' <c:if test="${user.gender == 1}">selected</c:if>>男</option>
	                                <option value='0' <c:if test="${user.gender == 0}">selected</c:if>>女</option>
	                            </select>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>職務</td>
	                        <td>
	                            <select name='role'>
                                    <c:forEach var="roleName" items="${utilfunc:getRoleNameList()}" varStatus="counter">
                                        <option value='${counter.count}'
                                        <c:if test="${user.role == counter.count}">
                                            selected
                                        </c:if>
                                        >${roleName}
                                        </option>
                                    </c:forEach>                                
	                            </select>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>備註</td>
	                        <td><input type='text' name='note' value='${user.note}' /></td>
	                    </tr>
	                    <tr>
	                        <td>圖片</td>
	                        <td>
                                <c:if test="${user.imageId != 0}">
                                    <img src='GetImageServlet.view?imageId=${user.imageId}'/>
                                </c:if>
                                <br>
                                設定圖片: <input type='file' name='image' />                                
	                        </td>
	                    </tr>
	                    <tr>
	                        <td colspan='2'>
	                            <input type='submit' value='修改'>&nbsp;
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