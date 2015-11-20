<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">使用者</c:set>
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
                <table border='1' bordercolor='#AAA' cellspacing='0' cellpadding='0'>
                    <tr>
                        <th>編號</th>
                        <th>帳號</th>
                        <th>密碼</th>
                        <th>姓名</th>
                        <th>性別</th>
                        <th>職務</th>
                        <th colspan='3'>操作</th>
                    </tr>

                    <c:forEach var="user" items="${userDaoImpl.all}">
	                    <tr>
	                        <td>${user.id}</td>
	                        <td>${user.account}</td>
	                        <td>${user.password}</td>
	                        <td>${user.name}</td>
	                        <td>${user.genderName}</td>
	                        <td>${user.roleName}</td>
	                        <td><a href='get_user.do?action=update&id=${user.id}'>修改</a></td>
	                        <td><a href='get_user.do?action=delete&id=${user.id}'>刪除</a></td>
	                        <td>
		                        <c:if test="${user.imageId != 0}">
		                            <a href='javascript: openwindow(${user.imageId})'>圖片</a>
		                        </c:if>
		                        <c:if test="${user.imageId == 0}">
		                            <img src='img/pf.png'/>
		                        </c:if>
	                        </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        
        <jsp:include page="/WEB-INF/include/function.jsp" />
        <div class="clearer"><span></span></div>
    </div>
    
    <%@ include file="/WEB-INF/include/footer.jsp"%>
</div>
</body>
</html>

<%@ include file="/WEB-INF/include/image_window.html" %>