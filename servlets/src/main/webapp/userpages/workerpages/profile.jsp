<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/Specs.tld" prefix="specs" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="profile.title"/></title>
    <meta http-equiv="Cache-Control" content="private">
</head>
<body>

<table border="0">

    <tbody>

    <tr>
        <td rowspan="2">
            <img src="${requestScope.viewedUser.photoUrl}" height="200"/>
        </td>
        <td>
            ${requestScope.viewedUser.fullName}
        </td>



    </tr>




    <tr>
        <td>
            <h2>
                ${requestScope.viewedUser.rate}
            </h2>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            <h2>
                <fmt:message key="profile.specs"/>
            </h2>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            ${specs:printUserSpecs(requestScope.userSpecs)}
        </td>

    </tr>


    </tbody>

</table>


</body>
</html>
