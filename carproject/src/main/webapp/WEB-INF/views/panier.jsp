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
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <ul class="navbar-nav">
        <li class="nav-item active">
            <a class="nav-link" href="#">${username}</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/list">Home</a>
        </li>

    </ul>
</nav>

    <table class="table table-bordered table-striped table-hover ">
        <tr>
            <th>Photo</th>
            <th>Nom </th>
            <th>Prix</th>
            <th>description</th>
            <th>Quantite</th>
        </tr>
        <c:forEach items="${list}" var="produit">
            <tr >
                <td> ${produit.urlPhoto } </td>
                <td>${produit.nom_prod}</td>
                <td>${produit.prix}</td>
                <td>${produit.description }</td>
                <td></td>

            </tr>
        </c:forEach>
    </table>
    <button align="right">Valider </button>
    <p align="right">Total produit : ${total}</p>

</body>
</html>
