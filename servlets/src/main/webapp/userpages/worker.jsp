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
    <meta http-equiv="Cache-Control" content="private">
</head>
<body onload=loadContent("/profile/")>

<jsp:include page="/logout.jsp"/>

<table border="0" width="100%">
    <tbody>


    <tr valign="top">
        <td width="20%"><!--navigation-->
            <div class="navmenu">
                <a href="#" onclick=loadContent("/profile/")><fmt:message key="worker.profileLabel"/></a>
                <a href="#" onclick=loadContent("/messages/?action=listAll")><fmt:message key="worker.messagesLabel"/></a>
                <a href="#" onclick=loadContent("/profileEdit/")><fmt:message key="worker.profileEditLabel"/></a>


            </div>


        </td>


        <td  height="100%"><!--TODO: растягивание по вертикали в зависимости от размеров  вставляемого документа??? -->
            <div  id="content" >




            </div>


        </td>

    </tr>


    </tbody>

</table>


<script language="JavaScript">

    function loadContent(url) {

            document.getElementById("content").innerHTML='<object type="text/html" width=100% height=1000 data='+url+' ></object>';

    }
</script>

</body >
</html >
