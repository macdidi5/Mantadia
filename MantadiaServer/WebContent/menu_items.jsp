<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">菜單項目</c:set>
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
			            <th>種類</th>
			            <th>名稱</th>
			            <th>價格</th>
			            <th width='30%'>備註</th>
			            <th colspan='3'>操作</th>
			        </tr>
                    
                    <c:forEach var="menuItem" items="${menuItemDaoImpl.all}">
                        <tr>
                            <td>${menuItem.id}</td>
                            <td>${menuItem.menuTypeName}</td>
                            <td>${menuItem.name}</td>
                            <td>${menuItem.price}</td>
                            <td>${menuItem.note}</td>
                            <td><a href='get_menu_item.do?action=update&id=${menuItem.id}'>修改</a></td>
                            <td><a href='get_menu_item.do?action=delete&id=${menuItem.id}'>刪除</a></td>
                            <td>
                                <c:if test="${menuItem.imageId != 0}">
                                    <a href='javascript: openwindow(${menuItem.imageId})'>圖片</a>
                                </c:if>
                                <c:if test="${menuItem.imageId == 0}">
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