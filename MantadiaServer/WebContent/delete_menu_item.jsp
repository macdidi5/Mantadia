<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">刪除菜單項目</c:set>
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
			            <td>編號</td>
			            <td>${menuItem.id}</td>
			        </tr>
                       <tr>
                           <td>種類</td>
                           <td>${menuItem.menuTypeName}</td>
                       </tr>
                       <tr>
                           <td>名稱</td>
                           <td>${menuItem.name}</td>
                       </tr>
                       <tr>
                           <td>價格</td>
                           <td>${menuItem.price}</td>
                       </tr>
                       <tr>
                           <td>備註</td>
                           <td>${menuItem.note}</td>
                       </tr>
	                   <tr>
	                       <td>圖片</td>
	                       <td>
                               <c:if test="${menuItem.imageId != 0}">
                                   <img src='GetImageServlet.view?imageId=${menuItem.imageId}'/>
                               </c:if>                               
	                       </td>
	                   </tr>                            
                       <tr>
                           <td colspan='2'>
                               <form action='admin_menu_item.do?action=delete&id=${menuItem.id}' method='post'>
                                   <input type='submit' value=刪除>&nbsp;
                                   <input type="button" value="取消" onClick="history.go(-1);return true;">
                               </form>
                           </td>
                       </tr>
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