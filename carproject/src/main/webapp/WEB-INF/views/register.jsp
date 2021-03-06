<%--
  Created by IntelliJ IDEA.
  User: king
  Date: 06/03/2021
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Registration</title>
</head>

<body>
<form:form id="regForm" modelAttribute="user" action="registerProcess" method="post">

    <table align="center">
        <tr>
            <td>
                <form:label path="username">Username</form:label>
            </td>
            <td>
                <form:input path="username" name="username" id="username" />
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="password">Password</form:label>
            </td>
            <td>
                <form:password path="password" name="password" id="password" />
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="firstname">FirstName</form:label>
            </td>
            <td>
                <form:input path="firstname" name="firstname" id="firstname" />
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="lastname">LastName</form:label>
            </td>
            <td>
                <form:input path="lastname" name="lastname" id="lastname" />
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="email">Email</form:label>
            </td>
            <td>
                <form:input path="email" name="email" id="email" />
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="adress">Adress</form:label>
            </td>
            <td>
                <form:input path="adress" name="adress" id="adress" />
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="phone">Phone</form:label>
            </td>
            <td>
                <form:input path="phone" name="phone" id="phone" />
            </td>
        </tr>

        <tr>
            <td></td>
            <td>
                <form:button id="register" name="register">Register</form:button>
            </td>
        </tr>
        <tr></tr>

    </table>
</form:form>

</body>

</html>
