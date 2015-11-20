<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">新增菜單種類</c:set>
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
				<form action='admin_menu_type.do?action=add' method='post'>
                    <table border='1' bordercolor='#AAA' cellspacing='0' cellpadding='0'>
                        <tr>
                            <td>名稱</td>
                            <td><input type='text' name='name' /></td>
                        </tr>
                        <tr>
                            <td>備註</td>
                            <td><input type='text' name='note' /></td>
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
