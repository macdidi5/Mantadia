<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle">餐桌狀態</c:set>
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
                        <th>桌號</th>
                        <th>狀態</th>
                        <th>訂單號碼</th>
                        <th>訂單時間</th>
                        <th>服務人員</th>
                        <th>客人數量</th>
                        <th>訂單狀態</th>
                    </tr>
                    <c:forEach var="table" items="${tablesDaoImpl.allStatus}">
                        <tr>
                            ${table}
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