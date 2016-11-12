<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="register.title"/></title>
</head>


<body>
<form action="/register" method="post">
    <table>
        <tr>
            <th colspan="2">
                <fmt:message key="login.hello"/>
            </th>
        </tr>

        <tr>
            <td>
                <fmt:message key="register.nameLabel"/>
            </td>
            <td>
                <input type="text" name="name" value="">

            </td>
        </tr>

        <tr>
            <td>
                <fmt:message key="register.loginLabel"/>
            </td>
            <td>
                <input type="text" name="login" value="">
            </td>
        </tr>

        <tr>
            <td>
                <fmt:message key="register.passLabel"/>
            </td>
            <td>
                <input type="password" name="pass1" value="">
            </td>
        </tr>

        <tr>
            <td>
                <fmt:message key="register.pass2Label"/>
            </td>
            <td>

                <input type="password" name="pass2" value="">
            </td>
        </tr>
    </table>
</form>

</body>
</html>
