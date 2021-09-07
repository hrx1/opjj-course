<%@page import="hr.fer.zemris.java.tecaj_13.dao.DAOProvider"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionMethods"
import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionKeys"
import="hr.fer.zemris.java.tecaj_13.model.BlogEntry" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%	boolean isLoggedIn = SessionMethods.isLoggedIn(session);
	String firstName = null;
	String lastName = null;
	String email = request.getParameter("comment-email");
	if(email == null) email = "";
	boolean authorLoggedIn = false;

	if(isLoggedIn) {
		firstName = (String) session.getAttribute(SessionKeys.userFirstName);
		lastName = (String) session.getAttribute(SessionKeys.userLastName);
		authorLoggedIn = session.getAttribute(SessionKeys.userNick)
								.equals(((BlogEntry)request.getAttribute("blog_show"))
									.getCreator().getNick());
		
		email = DAOProvider.getDAO().getUserEmail((String)session.getAttribute(SessionKeys.userNick));
	}
	
	String message = null;
	if("true".equals(request.getParameter("success"))) {
		message = "Blog added/edited successfuly";
	}
%>

<html>
<style>
textarea { width:100%; background:white; color:black; height:30%}

</style>
<body>
				<a href="${pageContext.request.contextPath}/index.jsp">Index page</a>

	<c:choose>
		<c:when test="<%=isLoggedIn %>">
			<div>
				<%=firstName%> <%=lastName %> <a href="${pageContext.request.contextPath}/servleti/logout">Logout</a>
			</div>
		</c:when>
		<c:otherwise>
			<p>Not logged in</p>
		</c:otherwise>
	</c:choose>
	
	
	<hr>
	
	<!-- Show blog  -->
	<p>Author: <a href="../${blog_show.creator.nick }">${blog_show.creator.nick }</a></p>
	<h3 style="color:blue">${blog_show.title }</h1>
	<textarea disabled="disabled">${blog_show.text }</textarea>
	<hr>
	<p>Date: ${blog_show.createdAt } </p>
	<p><i>Last modification: ${blog_show.lastModifiedAt}</i></p>
	
	<hr>
	<c:if test="<%=message != null%>"> <p style="color:green"><%=message %></p> </c:if>
	
	<c:if test="<%=authorLoggedIn %>"> <a href="${pageContext.request.contextPath}/servleti/author/${blog_show.creator.nick}/edit?blogID=${blog_show.id}"> Edit blog </a> </c:if>
	
	<hr>
	
	<h4>Comments:</h4>
	<c:choose>
	<c:when test="${empty blog_show.comments }"> ... nema komentara </c:when>
	
	<c:otherwise>
	<table>
		<tr>
			<th>Email</th><th>Message</th><th>Posted on</th>
		</tr>
	
	<c:forEach var="comment" items="${blog_show.comments}">
		<tr>
			<td>${comment.usersEMail }</td><td style="padding-left:50px; padding-right:50px">${comment.message}</td><td>${ comment.postedOn}</td>
		</tr>
	</c:forEach>
	</table>
	</c:otherwise>
	</c:choose>
	
	<hr>
	
	<div>Add Comment:</div>
			<form method="post" action="${pageContext.request.contextPath}/servleti/author/${blog_show.creator.nick}/${blog_show.id}">
				Email:<br>
				<input type="text" name="comment-email" value="<%=email%>"> <br> 
				Comment:<br>
				<input type="text" name="comment-value" value="${commentFormKey.value }"> <br> 
				<input type="hidden" name="comment-sent" value="true">
				
				<p style="color: red">${commentFormKey.errorMessage }</p>
				<input type="submit" value="Submit">
			</form>
	
</body>
</html>
