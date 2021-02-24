<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Products</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
	<h1>Les produits ${mess } ...</h1>
	<p>La valeur du dao : ${products.getId } </p>

<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
  <ul class="navbar-nav">
    <li class="nav-item active">
      <a class="nav-link" href="#">Ecom</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="#">Home</a>
    </li>
    <li>
     	<form class="form-inline my-2 my-lg-0">
      		<input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
     		 <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
   		 </form>
  	</li>
  	<li class="nav-item">
      <a class="nav-link" href="#">Mon Compte</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="#">Mon Panier</a>
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
	</tr>
	
		<tr>
			<td>Photo </td>
			<td>Ommoton</td>
			<td>10</td>
			<td>Omoton avec son clavier et sa souris</td>
			<td>Aujourdh'hui a 23:49</td>
			
		</tr>
	
</table>

</body>
</html>