<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>

<html lang=${language}>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="index.label"/></title>
</head>
<body>
<h1><fmt:message key="index.welcomeMsg"/></h1>
<table>
    <tr>
        <td>
            <form action="/" method="post">
                <select id="language" name="language" onchange="submit()" value=${language}>
                    <option value="en" ${language == 'en_EN' ? 'selected' : ''}>English</option>
                    <option value="ru" ${language == 'ru_RU' ? 'selected' : ''}>Русский</option>
                </select>
            </form>
        </td>
    </tr>

    <tr>
        <td>
            <%@include file="/login.jsp"%>
        </td>
    </tr>

    <tr>
        <td><h1><fmt:message key="index.registerLabel"/></h1></td>
    </tr>

    <tr>
        <td>
            <%@include file="/register.jsp"%>

        </td>

    </tr>

</table>

</body>
</html>