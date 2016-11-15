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

<div >
    <c:forEach items="${requestScope.allMessages}" var="message">

        <span class="message_class">

            <a class="message-sender" href=""> ${message.fromUser.fullName}</a>
            <a class="message-text" href="/messages/?action=listBetween&id1=${message.fromUser.id}&id2=${message.toUser.id}">${message.text}</a>
           <joda-format:format time="${message.timeStamp }"/>
            <hr>
            <br>
        </span>


    </c:forEach>
</div>
</body>
<script language="JavaScript">
    function  getFormattedDate( time) {

        var date=new Date(time);
        return date.format("yyyy-mm-dd")

    }
</script>
</html>
