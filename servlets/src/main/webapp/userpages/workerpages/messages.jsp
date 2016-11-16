<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/TimeFormatter.tld" prefix="joda-format"%>


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
    <link href="/userpages/workerpages/STYLE.CSS" rel="stylesheet" type="text/css">
</head>

<body>

<table class="messages">

    <c:forEach items="${requestScope.allMessages}" var="message">

     <tr >
            <td width="10%" class="message_sender"><a  href=""> ${message.fromUser.fullName}</a></td>
         <td width="10%" class="message_reciever"><a  href=""> ${message.toUser.fullName}</a></td>
            <td class="message_text"><a  href="/messages/?action=listBetween&id1=${message.fromUser.id}&id2=${message.toUser.id}">${message.text}</a></td>

         <td width="10%" class="message_time"><joda-format:format time="${message.timeStamp}" zone="${sessionScope.zoneId}"/></td>
     </tr>

    </c:forEach>
</table>
</body>


</html>
