<%--
  Created by IntelliJ IDEA.
  User: king
  Date: 03/03/2021
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>panier</title>
</head>
<body>
    <p>${message}</p>
    <h1>Bonjour ${username} !!</h1>
    <table class="table table-bordered table-striped table-hover ">
        <tr>
            <th>Photo</th>
            <th>Nom </th>
            <th>Prix</th>
            <th>description</th>
            <th>date</th>
        </tr>
        <c:forEach items="${list}" var="produit">
            <tr >
                <td> ${produit.urlPhoto } </td>
                <td>${produit.nom_prod}</td>
                <td>${produit.prix}</td>
                <td>${produit.description }</td>
                <td>${produit.date_pub }</td>

            </tr>
        </c:forEach>
    </table>
    <p align="rigth">Total produit : ${total}</p>

</body>
</html>
