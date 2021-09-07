<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" import="java.util.Random" import="java.util.concurrent.TimeUnit" import="java.text.SimpleDateFormat" import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%	String picked = (String) session.getAttribute("pickedBgCol");
if(picked == null) {
	picked = "#FFFFFF";
	session.setAttribute("pickedBgCol", picked);
}

	String[] colors = {"#FFF","#000","#FF0000","#00FF00","#0000FF","#FFFF00"};
	Random rand = new Random();
	String color = colors[rand.nextInt(colors.length)];
	
	
	long difference = System.currentTimeMillis() - (Long) session.getServletContext().getAttribute("timeStart");
	String days = String.valueOf((int)TimeUnit.MILLISECONDS.toDays(difference));
	String hours = String.valueOf((int)TimeUnit.MILLISECONDS.toHours(difference) % (24));
	String minutes = String.valueOf((int)TimeUnit.MILLISECONDS.toMinutes(difference) % (60));
	String seconds = String.valueOf((int)TimeUnit.MILLISECONDS.toSeconds(difference) % (60));
	String ms = String.valueOf((int)TimeUnit.MILLISECONDS.toMillis(difference) % (1000));

%>
<html>
<body bgcolor="<%=picked%>">
<h1>Time running: </h1>
<p>Days: <%=days%><br> Hours:<%=hours%> <br> Minutes:<%=minutes%> <br> Seconds:<%=seconds%> <br> Milliseconds:<%=ms%></p><br>

</body>
</html>