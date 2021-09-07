<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*" import="hr.fer.zemris.java.hw13.servlets.GlasanjeRezultatiServlet.VoteResult"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<% //postavlja na zadano ako nema atributa
	String picked = (String) session.getAttribute("pickedBgCol");
	if(picked == null) {
		picked = "#FFFFFF";
		session.setAttribute("pickedBgCol", picked);
	}
	
	List<VoteResult> pobjednici = new LinkedList<VoteResult>();
	List<VoteResult> results = (List<VoteResult>) session.getServletContext().getAttribute("votingResults");
	int currValue = -1;
	for(VoteResult vr : results) {
		int vrValue = Integer.parseInt(vr.getNumberOfVotes());
		
		if(currValue == -1) {
			currValue = vrValue;
			pobjednici.add(vr);
		}
		else {
			if(vrValue != currValue) {
				break;
			}
			else {
				pobjednici.add(vr);
			}
		}
	}
%>

<html>
<body bgcolor="<%=picked%>">
	<head>
	<meta charset="utf-8">
	</head>
	
	<body>
		<h1>Rezultati glasanja</h1>
		<table border="1" cellspacing="0" class="rez">
		<thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
		<tbody>
			<c:forEach var="result" items="${votingResults}">
				 <tr><td>${result.name}</td><td>${result.numberOfVotes}</td></tr>
			</c:forEach>
		</tbody>
		</table>
		<h2>>Grafički prikaz rezultata</h2>
		 <img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />
		 
		 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a></p>
		 
		 <h2>Razno</h2>
		 <p>Pjesme trenutnog/trenutnih pobjednika</p>
		 <ul>
		 
		 	<c:forEach var="pobjednik" items="<%=pobjednici%>">
				 <li><a href="${pobjednik.song}">${pobjednik.name}</a></li>
			</c:forEach>
			</ul>
		 
		 
	</body>
</html>