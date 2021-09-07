<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% //postavlja na zadano ako nema atributa
	String picked = (String) session.getAttribute("pickedBgCol");
	if(picked == null) {
		picked = "#FFFFFF";
		session.setAttribute("pickedBgCol", picked);
	}
	
%>
<html>
<body bgcolor="<%=picked%>">

<a href="setcolor?pickedBgCol=%23FFF">White</a> <br>
<a href="setcolor?pickedBgCol=%23FF0000">Red</a> <br>
<a href="setcolor?pickedBgCol=%2300FF00">Green</a> <br>
<a href="setcolor?pickedBgCol=%2300FFFF">Cyan</a> <br>

</body>
</html>