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
    <p>Votre panier ici</p>
    <p>Panier id : ${panier.id}</p>
    <p>Panier id : ${panier.quantite}</p>

    <p>Quantity = ${quantity}</p>
</body>
</html>
