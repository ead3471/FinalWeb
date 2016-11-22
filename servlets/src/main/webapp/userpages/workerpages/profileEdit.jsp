<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="specs-out" uri="http://ead3471.com" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="lang/dialogs"/>
<html>
<head>
    <title><fmt:message key="profileEdit.title"/></title>
</head>
<body>

<form action="/profileEdit/" method="post" enctype="multipart/form-data">
    <table>
        <tr>
            <td>
                <img src="${sessionScope.user.photoUrl}" height="200" />
            </td>
            <td>
                <input type="file" name="photo">
                <fmt:message key="profileEdit.choosePhotoButton"/>
            </td>
        </tr>

        <tr>
            <td>
                <fmt:message key="profileEdit.name"/>
            </td>

            <td>
                <input type="text" name="name" value="${sessionScope.user.fullName}">
            </td>
        </tr>


        <tr>
            <td>
                <fmt:message key="profileEdit.newPasswordFirst"/>
            </td>
            <td>
                <input name=password1 type="password" value="">
            </td>
        </tr>

        <tr>
            <td>
                <fmt:message key="profileEdit.newPasswordSecond"/>
            </td>
            <td>
                <input name="password2" type="password" value="">
            </td>
        </tr>

        <tr>
            <td>
                <h2>
                    <fmt:message key="profile.specs"/>
                </h2>
            </td>
        </tr>

        <tr>
            <td>
                <specs-out:print specsList="${requestScope.userSpecs}"/>
            </td>
        </tr>

        <tr>
            <td>
                <input type="submit" value=<fmt:message key="profileEdit.submitUserUpdate"/>
            </td>
        </tr>
    </table>
</form>


</body>
</html>