<%--
  Created by IntelliJ IDEA.
  User: king
  Date: 06/03/2021
  Time: 01:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>login</title>
</head>
<body>
    <form:form id="logingForm" action="" method="post">
        <tab align="center">
            <tr>
                <td>
                    <form:label path="username" >UserName: </form:label>
                </td>
                <td>
                    <form:input path="username" name="username" id="username"></form:input>
                </td>
            </tr>
        </tab>
    </form:form>

</body>
</html>
