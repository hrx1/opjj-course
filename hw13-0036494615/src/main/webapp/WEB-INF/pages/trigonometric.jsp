<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<% //postavlja na zadano ako nema atributa
	String picked = (String) session.getAttribute("pickedBgCol");
	if(picked == null) {
		picked = "#FFFFFF";
		session.setAttribute("pickedBgCol", picked);
	}
	
%>
<html>
<body bgcolor="<%=picked%>">
	<head>
	<meta charset="utf-8">
	</head>
	
	<body>
		<h1>Tablica sin i cos vrijednosti</h1>
		
		<table>
		<thead>
		<tr><th>Degrees</th><th>sin</th><th>cos</th></tr>
		</thead>
		<tbody>
			<c:forEach var="result" items="${trigonometricTable}"> 
				<tr><td>${result.degree}</td><td>${result.sinValue}</td><td>${result.cosValue}</td></tr>
		 	</c:forEach>
		</tbody>
		</table>
	</body>
</html>