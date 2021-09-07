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
		<h1>Glasanje za omiljeni bend:</h1>
		<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
		<ol>
		<c:forEach var="voteOption" items="${voteOptions}">
		 <li><a href="glasanje-glasaj?id=${voteOption.ID}">${voteOption.name}</a></li>
		</c:forEach>
		</ol>		
		
		
	</body>
</html>