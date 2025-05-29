<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Product" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>

<%
    // Retrieve the report title
    String reportTitle = "Report Results"; // Default title
    Object reportTitleObj = request.getAttribute("reportTitle");
    if (reportTitleObj instanceof String && !((String)reportTitleObj).isEmpty()) {
        reportTitle = (String) reportTitleObj;
    }

    // Retrieve the list of report results
    List<Product> reportResults = null;
    Object reportResultsObj = request.getAttribute("reportResults");
    if (reportResultsObj instanceof List) {
        try {
            // This cast is generally safe if you trust the servlet
            reportResults = (List<Product>) reportResultsObj;
        } catch (ClassCastException e) {
            // Log this error or display a generic data error message
            // For simplicity, we'll let it fall through to "No products found"
            reportResults = null; 
        }
    }

    // For formatting currency
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US); // Or your preferred Locale
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Report Results</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="container">
        <%-- Ensure _header.jsp is scriptlet-based --%>
        <jsp:include page="header.jsp" /> 
        
        <h2><%= reportTitle %></h2>

        <%-- Check if reportResults list exists and is not empty --%>
        <% if (reportResults != null && !reportResults.isEmpty()) { %>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Quantity</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Loop through each product in the reportResults list --%>
                    <% for (Product product : reportResults) { 
                        // Null checks for safety, though getters should ideally handle this
                        String productName = (product.getProductName() != null) ? product.getProductName() : "";
                        String category = (product.getCategory() != null) ? product.getCategory() : "";
                        String formattedPrice = "N/A"; // Default if price is null or formatting fails
                        
                        if (product.getPrice() != null) {
                            try {
                                formattedPrice = currencyFormatter.format(product.getPrice());
                            } catch (Exception e) {
                                // Fallback if formatting fails (e.g., invalid BigDecimal value)
                                formattedPrice = product.getPrice().toPlainString() + " (format error)";
                            }
                        }
                    %>
                        <tr>
                            <td><%= product.getProductId() %></td>
                            <td><%= productName %></td>
                            <td><%= category %></td>
                            <td><%= formattedPrice %></td>
                            <td><%= product.getQuantity() %></td>
                        </tr>
                    <% } // End of for loop %>
                </tbody>
            </table>
        <% } else { // Else, if reportResults is null or empty %>
            <p>No products found matching the report criteria.</p>
        <% } // End of if reportResults check %>
        
        <p><a href="<%= request.getContextPath() %>/reports" class="button-link">Back to Reports</a></p>
    </div> <%-- End of .container div --%>
</body>
</html>