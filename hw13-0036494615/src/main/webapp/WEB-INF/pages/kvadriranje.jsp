<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="utf-8">
	</head>
	
	<body>
		<h1>Stranica s kvadratima</h1>
		
		<table>
		<thead>
		<tr><th>Broj</th><th>Kvadrat</th></tr>
		</thead>
		<tbody>
			<c:forEach var="zapis" items="$(rezultat)"> 
				<tr><td>$(zapis.broj)</td><td>$(zapis.kvadrat)</td></tr>
		 	</c:forEach>
		</tbody>
		</table>
	</body>
</html>