<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">菜單種類</c:set>
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
			            <th>名稱</th>
			            <th>備註</th>
			            <th colspan='2'>操作</th>
			        </tr>
			        
                    <c:forEach var="menuType" items="${menuTypeDaoImpl.all}">
                        <tr>
                            <td>${menuType.id}</td>
                            <td>${menuType.name}</td>
                            <td>${menuType.note}</td>
                            <td><a href='get_menu_type.do?action=update&id=${menuType.id}'>修改</a></td>
                            <td><a href='get_menu_type.do?action=delete&id=${menuType.id}'>刪除</a></td>
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
