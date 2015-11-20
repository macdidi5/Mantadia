<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utilfunc" uri="http://net.macdidi.mantadia/utilfunc" %>

<c:set var="pageTitle">日報表</c:set>
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
                <form action='daily_report.do' method='post'>
                    日期：
                    <c:if test="${not empty requestScope.orders}">
                        ${utilfunc:getDateSelect(param.year, param.month, param.day)}
                    </c:if>
                    <c:if test="${empty requestScope.orders}">
                        ${utilfunc:getDateNowSelect()}
                    </c:if>
                    <input type='submit' value='確定'>
                </form>
                
                <c:if test="${empty requestScope.orders and not empty param.year}">
                    <p>=== 這個日期沒有訂單資訊 ===</p>
                </c:if>
                
                <c:if test="${not empty requestScope.orders}">
                    <table border='1' bordercolor='#AAA' cellspacing='0' cellpadding='0'>
                        <tr>
                         <th>編號</th>
                         <th>時間</th>
                         <th>服務人員</th>
                         <th>桌號</th>
                         <th>人數</th>
                         <th>狀態</th>
                         <th>備註</th>
                         <th>金額</th>
                        </tr>
                        <c:forEach var="order" items="${requestScope.orders}">
                            <tr>
                                <td>${order.id}</td>
                                <td>${order.time}</td>
                                <td>${order.userName}</td>
                                <td>${order.tablesId}</td>
                                <td>${order.number}</td>
                                <td>${order.statusName}</td>
                                <td>${order.note}</td>
                                <td align='right'>${order.amount}</td>
                            </tr>
                        </c:forEach>
                       <tr>
                           <td colspan='6'>數量: ${requestScope.size}</td>
                           <td align='right'>合計</td>
                           <td align='right'>${requestScope.total}</td>
                       </tr>
                     </table>
                </c:if>
            </div>
        </div>
        
        <jsp:include page="/WEB-INF/include/function.jsp" />
        <div class="clearer"><span></span></div>
    </div>
    
    <%@ include file="/WEB-INF/include/footer.jsp"%>
</div>
</body>
</html>
