<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html lang=${language}>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="login.title"/></title>
</head>
<body>
<form action="/login/" method="post">
    <c:if test="${not empty requestScope.login_msg}">
        <p><font color="red"> ${requestScope.login_msg} </font></p>
    </c:if>
    <table>
        <tr>
            <td>
                <table>
                    <tr>
                        <th><fmt:message key="login.helloLabel"/></th>
                    <tr>
                    <tr>
                        <td><fmt:message key="login.loginLabel"/></td>
                        <td><input type="text" name="user"></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="login.passLabel"/></td>
                        <td><input type="password" name="authString"></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value=<fmt:message key="login.submitLabel"/>>
                        </td>
                    </tr>

                    <tr>
                        <td><input type="hidden" name="userDestinationUrl" value="${requestScope.userDestinationUrl}">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

    </table>
</form>

</body>
</html>