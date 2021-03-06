<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>list-produits</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
<c:if test="${username != null}">
    <h1>Bonjour : ${username}</h1>
</c:if>


<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <ul class="navbar-nav">
        <li class="nav-item active">
            <a class="nav-link" href="#">Ecom</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/list">Home</a>
        </li>
        <li>
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
        </li>
        <li class="nav-item">
            <c:choose>
            <c:when test="${sessionScope.userSession != null}">
                <a class="nav-link" href="${pageContext.request.contextPath}/register">Mon Compte</a>
            </c:when>
                <c:when test="${sessionScope.userSession == null}">
                    <a class="nav-link" href="#">Mon Compte</a>
                </c:when>
            </c:choose>
        </li>
        <li class="nav-item">
           <c:choose>
               <c:when test="${sessionScope.userSession != null}">
                   <a class="nav-link" href="${pageContext.request.contextPath}/panier">Mon Panier: ${total}</a>
               </c:when>
               <c:when test="${sessionScope.userSession == null}">
                   <a class="nav-link" href="${pageContext.request.contextPath}/login">Mon Panier: ${total}</a>
               </c:when>
           </c:choose>

        </li>
    </ul>
</nav>


<table class="table table-bordered table-striped table-hover">
    <tr>
        <th>Photo</th>
        <th>Nom </th>
        <th>Prix</th>
        <th>description</th>
        <th>date</th>
        <th></th>
    </tr>
    <c:forEach items="${list}" var="produit">
        <tr >
            <td><a href="/produit/${produit.id}"> ${produit.urlPhoto } </td>
            <td>${produit.nom_prod}</td>
            <td>${produit.prix}</td>
            <td>${produit.description }</td>
            <td>${produit.date_pub }</td>
            <td>
                <c:choose>
                    <c:when test="${sessionScope.userSession != null}">
                        <form  id="addPanier"  action="${pageContext.request.contextPath}/ajouterPanier/${produit.id}" method="post">

                            <input name="qte" type="number" value="1" style="width: 60px">
                            <button type="submit"> Ajouter au panier</button>
                        </form>
                    </c:when>
                    <c:when test="${sessionScope.userSession == null}">
                        <p> <a href="${pageContext.request.contextPath}/login">Ajouter au Panier</a> </p>


                    </c:when>
                </c:choose>

            </td>

        </tr>
    </c:forEach>
</table>


</body>
</html>