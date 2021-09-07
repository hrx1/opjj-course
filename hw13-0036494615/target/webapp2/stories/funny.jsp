<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" import="java.util.Random" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% //postavlja na zadano ako nema atributa
String picked = (String) session.getAttribute("pickedBgCol");
if(picked == null) {
	picked = "#FFFFFF";
	session.setAttribute("pickedBgCol", picked);
}


	String[] colors = {"#FFF","#000","#FF0000","#00FF00","#0000FF","#FFFF00"};
	Random rand = new Random();
	String color = colors[rand.nextInt(colors.length)];
%>
<html>
<body bgcolor="<%=picked%>">

<p style="color:<%=color%>;">Past cu ovu vjestinu</p>

</body>
</html>