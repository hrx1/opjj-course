<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>
<body>
	<head>
	<meta charset="utf-8">
	</head>
	
	<body>
		<h1>${pollTitle}</h1>
		<p>${pollMsg}</p>
		<ol>
			<c:if test="${empty votingOptions}">
				<p>Anketa nema mogucih odabira.</p>
			</c:if>
		
		<c:forEach var="vop" items="${votingOptions}">
		
		 <li><a href="glasanje-glasaj?id=${vop.ID}&pollID=${pollID}">${vop.name}</a></li>
		</c:forEach>
		</ol>		
		
		
	</body>
</html>