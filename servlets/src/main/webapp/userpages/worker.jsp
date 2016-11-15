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
    <title>Worker page</title>
    <link href="/userpages/workerpages/STYLE.CSS" rel="stylesheet" type="text/css">
</head>
<body>

<h1> Hello worker ${sessionScope.user.fullName}</h1>

<table border="1" width="100%">
    <tbody>

    <tr>
        <td><!--Photo-->
            <img src=${sessionScope.user.photoUrl}>
        </td>
        <td>
            <h1><fmt:message key="worker.mainInfoLabel"/></h1>
        </td>
    </tr>

    <tr valign="top">
        <td width="20%"><!--navigation-->
            <div class="navmenu">
                <a href="#" onclick=loadContent("/userpages/workerpages/profileEdit.jsp")><fmt:message key="worker.profileLabel"/></a>
                <a href="#" onclick=loadContent("../messages/?action=listAll")><fmt:message key="worker.messagesLabel"/></a>


            </div>


        </td>


        <td height="100%"><!--main load place-->
            <div id="content" >




            </div>


        </td>

    </tr>


    </tbody>

</table>

<jsp:include page="/logout.jsp"/>
<script language="JavaScript">

    function loadContent(url) {

            document.getElementById("content").innerHTML='<object type="text/html" width=100% height=100% data='+url+' ></object>';

    }
</script>

</body >
</html >
