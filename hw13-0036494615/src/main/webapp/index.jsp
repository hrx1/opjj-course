<%@ page import="java.util.Date,java.util.Calendar" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% //postavlja na zadano ako nema atributa
	String picked = (String) session.getAttribute("pickedBgCol");
	if(picked == null) {
		picked = "#FFFFFF";
		session.setAttribute("pickedBgCol", picked);
	}
	
%>
<html>
<body bgcolor="<%=picked%>">
		<a href="colors.jsp">Background color chooser</a> <br>
		<a href="trigonometric?a=0&b=90">Trigonometric table</a>

	<form action="trigonometric" method="GET">
		Početni kut:<br>
		<input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br>
		<input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>
		
		<a href="stories/funny.jsp">Funny Story</a><br>
		
		<a href="powers?a=1&b=100&n=3">Excel Table of Powers</a><br>

		<a href="appinfo.jsp">Application Info</a><br>

</body>
</html>