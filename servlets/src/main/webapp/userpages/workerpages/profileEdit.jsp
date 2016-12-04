<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="specs" uri="http://ead3471.com" %>

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
    <table border="0">
        <tr>
            <td>
                <img src="${sessionScope.user.photoUrl}" height="200" />
            </td>
            <td>
                <input type="file" name="photo" value="<fmt:message key="profileEdit.choosePhotoButton"/>">

            </td>
        </tr>

        <tr>
            <td>
                <input type="submit" value=<fmt:message key="profileEdit.submitPhotoUpload"/>>
            </td>
        </tr>
        </table>
</form>

<form id="user_info" action="/profileEdit/" method="post" onSubmit= "return validateTextForm()">
     <table border="1">

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
                <input name="password" type="password" value="">
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
            <td colspan="2">
                <h2>
                    <fmt:message key="profile.specs"/>
                </h2>
            </td>
        </tr>

         <tr>
             <td>
                ${specs:printChecked(requestScope.userSpecs,requestScope.allSpecs)}
             </td>
         </tr>


         <tr>
             <td>
                 <input type="submit" value=<fmt:message key="profileEdit.submitUserUpdate"/>>
             </td>
         </tr>

    </table>
</form>

<script language="JavaScript">
    function  validateTextForm() {
        console.log("in validation form");
        var form=document.getElementById("user_info")  ;
        if(form.password.value!=(form.password2.value)){
            alert("<fmt:message key="profileEdit.notEqualPasswords"/>");
            form.password.className="error_form";
            form.password2.className="error_form";
            return false;
        }

        return true;
    }
</script>


</body>



</html>