<%--
  Created by IntelliJ IDEA.
  User: Freemind
  Date: 2016-11-10
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Client page</title>
</head>
<body>
<h1> Hello client ${sessionScope.user.login}</h1>

<jsp:include page="/logout.jsp"/>
</body>
</html>
