<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>

<c:set var="messagesList" scope="request" value="${requestScope.allMessages}"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="messages.title"/></title>
</head>
<table>

    <tbody>

    <c:forEach items="${messagesList}" var="message">
        <tr>
            <td>${message.text}</td>
        </tr>
    </c:forEach>




    </tbody>


</table>

<body>



</body>
</html>
