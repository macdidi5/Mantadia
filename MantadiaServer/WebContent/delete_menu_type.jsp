<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">刪除菜單種類</c:set>
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
			            <td>${menuType.id}</td>
			        </tr>
                    <tr>
                        <td>名稱</td>
                        <td>${menuType.name}</td>
                    </tr>
                    <tr>
                        <td>備註</td>
                        <td>${menuType.note}</td>
                    </tr>
                    <tr>
                        <td colspan='2'>
                            <form action='admin_menu_type.do?action=delete&id=${menuType.id}' method='post'>
                               <input type='submit' value='刪除'>&nbsp;
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
