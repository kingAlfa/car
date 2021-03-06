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
    <form:form id="logingForm" modelAttribute="login" action="loginProcess" method="post">
        <tab align="center">
            <tr>
                <td>
                    <form:label path="username" >UserName: </form:label>
                </td>
                <td>
                    <form:input path="username" name="username" id="username"></form:input>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="password">Password:</form:label>
                </td>
                <td>
                    <form:password path="password" name="password" id="password" />
                </td>
            </tr>
            <tr>
                <td></td>
                <td align="left">
                    <form:button id="login" name="login">Login</form:button>
                </td>
            </tr>

        </tab>
    </form:form>
    <table align="center">
        <tr>
            <td style="font-style: italic; color: red;">${message}</td>
        </tr>
    </table>

</body>
</html>
