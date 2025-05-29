<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Product" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>

<%
    // Retrieve request parameters for success/error messages
    String successMessageParam = request.getParameter("success");
    String errorMessageParam = request.getParameter("error");

    // Retrieve request attribute for error messages from servlets
    String errorMessageAttr = (String) request.getAttribute("errorMessage");

    // Determine the final success message to display
    String displaySuccessMessage = null;
    if (successMessageParam != null && !successMessageParam.isEmpty()) {
        if ("ProductUpdated".equals(successMessageParam)) {
            displaySuccessMessage = "Product updated successfully!";
        } else if ("ProductDeleted".equals(successMessageParam)) {
            displaySuccessMessage = "Product deleted successfully!";
        } else {
            displaySuccessMessage = "Operation successful!"; // Generic success
        }
    }

    // Determine the final error message to display (parameter takes precedence for simplicity)
    String displayErrorMessage = null;
    if (errorMessageParam != null && !errorMessageParam.isEmpty()) {
        displayErrorMessage = "Error: " + errorMessageParam;
    } else if (errorMessageAttr != null && !errorMessageAttr.isEmpty()) {
        displayErrorMessage = errorMessageAttr;
    }

    // Get the list of products
    List<Product> productList = null;
    Object productListObj = request.getAttribute("productList");
    if (productListObj instanceof List) {
        try {
            productList = (List<Product>) productListObj;
        } catch (ClassCastException e) {
            if (displayErrorMessage == null) displayErrorMessage = "Error: Product data is invalid.";
            else displayErrorMessage += " Product data is invalid.";
            productList = null; 
        }
    }
    
    // For formatting currency
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US); // Or your preferred Locale
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
    <script>
        function confirmDelete(productId) {
            if (confirm('Are you sure you want to delete this product?')) {
                window.location.href = '<%= request.getContextPath() %>/deleteProduct?id=' + productId + '&confirm=yes';
            }
        }
    </script>
</head>
<body>
    <div class="container"> <%-- Container div starts --%>
        
        <jsp:include page="header.jsp" /> <%-- Check 'header.jsp' thoroughly for unclosed tags --%>
        
        <h2>All Products</h2>

        <%-- Display success message if present --%>
        <% if (displaySuccessMessage != null) { %>
            <p class="success-message"><%= displaySuccessMessage %></p>
        <% } %>
        
        <%-- Display error message if present --%>
        <% if (displayErrorMessage != null) { %>
            <p class="error-message"><%= displayErrorMessage %></p>
        <% } %>

        <p><a href="<%= request.getContextPath() %>/addProduct" class="button-link">Add New Product</a></p>

        <%-- Check if productList exists and is not empty --%>
        <% if (productList != null && !productList.isEmpty()) { %>
            <table> <%-- Table starts --%>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Loop through each product in the list --%>
                    <% for (Product product : productList) { 
                        String productName = (product.getProductName() != null) ? product.getProductName() : "";
                        String category = (product.getCategory() != null) ? product.getCategory() : "";
                        String formattedPrice = "N/A";
                        if (product.getPrice() != null) {
                            try {
                                formattedPrice = currencyFormatter.format(product.getPrice());
                            } catch (Exception e) {
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
                            <td>
                                <a href="<%= request.getContextPath() %>/editProduct?id=<%= product.getProductId() %>" class="action-link">Edit</a>
                                <a href="#" onclick="confirmDelete(<%= product.getProductId() %>); return false;" class="action-link delete">Delete</a>
                            </td>
                        </tr>
                    <% } // end for loop %>
                </tbody>
            </table> <%-- Table ends --%>
        <% } else { // Else, if productList is null or empty %>
            <p>No products found.</p>
        <% } // end if productList check %>
        
    </div> <%-- Container div ends --%>
</body>
</html>