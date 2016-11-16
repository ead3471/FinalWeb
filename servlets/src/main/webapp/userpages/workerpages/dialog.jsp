<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/TimeFormatter.tld" prefix="joda-format"%>


<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<c:set var="mainUser" scope="session" value="${session.user}"/>
<c:set var="dialog" scope="request" value="${requestScope.dialog}"/>


<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="dialog.title"/></title>
    <link href="/userpages/workerpages/STYLE.CSS" rel="stylesheet" type="text/css">
</head>
<body>
<table border="1">


    <tr>

        <td colspan="4">
            <form action="send">
            <table>
                <tr><td>
                <input type="text" name="text"/>
                    <input type="hidden" value="">
                </td></tr>
                <tr><td>
                <input type="submit"/>
                </td>
                </tr>
            </table>
            </form>



        </td>
    </tr>

    </form>
    <c:forEach items="${dialog}" var="message">
        <tr >
            <td width="10%" class="message_sender"><a  href="/showuser/id=${message.fromUser.id}"> ${message.fromUser.fullName}</a></td>
            <td width="10%" class="message_reciever"><a  href="/showuser/id=${message.toUser.id}"> ${message.toUser.fullName}</a></td>
            <td class="message_text">${message.text}</td>
            <td width="10%" class="message_time"><joda-format:format time="${message.timeStamp}" zone="${sessionScope.zoneId}"/></td>
        </tr>

    </c:forEach>


</table>

</body>
</html>
