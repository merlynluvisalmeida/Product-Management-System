<%@ page language="java" contentType="text/html; charset=UTF-8" 
         pageEncoding="UTF-8" isErrorPage="true" %>
<%-- No JSTL taglib needed --%>

<%
    // Retrieve errorMessage attribute if it was set explicitly by a servlet before forwarding
    String customErrorMessage = (String) request.getAttribute("errorMessage");

    // The implicit 'exception' object is available because isErrorPage="true"
    // No need to explicitly retrieve it from request.getAttribute("javax.servlet.error.exception")
    // unless you want to be extremely verbose or handle cases where 'exception' might be null
    // if this page is accessed directly without an error occurring (though 'isErrorPage' implies it's for errors).

    String exceptionName = null;
    String exceptionMessage = null;

    if (exception != null) {
        exceptionName = exception.getClass().getName(); // Get the full class name of the exception
        exceptionMessage = exception.getMessage();
        
        // Optional: For debugging, you might want to print the stack trace to the server console
        // System.err.println("Error Page Exception Details:");
        // exception.printStackTrace(); 
        // WARNING: Do NOT print stack traces directly to the HTML output in a production environment
        // as it can reveal sensitive information.
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="container">
        <h1>An Error Occurred</h1>
        <p class="error-message">
            We are sorry, but an unexpected error has occurred.
        </p>

        <%-- Display custom error message if provided by a servlet --%>
        <% if (customErrorMessage != null && !customErrorMessage.isEmpty()) { %>
            <p><strong>Details:</strong> <%= customErrorMessage %></p>
        <% } %>

        <%-- Display details from the exception object if an exception occurred --%>
        <% if (exception != null) { %>
             <p><strong>Exception Type:</strong> <%= exceptionName %></p>
             <% if (exceptionMessage != null && !exceptionMessage.isEmpty()) { %>
                <p><strong>Message:</strong> <%= exceptionMessage %></p>
             <% } %>
             <%-- 
                WARNING: Displaying stack traces in production is a security risk.
                This is for debugging purposes only. Consider removing or commenting out
                the stack trace display for a live application.
             --%>
             <%-- 
             <p><strong>Stack Trace (for debugging):</strong></p>
             <pre><% exception.printStackTrace(new java.io.PrintWriter(out)); %></pre>
             --%>
        <% } %>
        
        <p><a href="<%= request.getContextPath() %>/index.jsp">Go to Homepage</a></p>
    </div> <%-- End of .container div --%>
</body>
</html>