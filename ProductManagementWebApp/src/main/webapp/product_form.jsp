<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.model.Product" %>
<%@ page import="java.math.BigDecimal" %>

<%
    // --- Retrieve attributes set by the servlet ---
    String formTitleAttr = (String) request.getAttribute("formTitle");
    String pageTitle = (formTitleAttr != null && !formTitleAttr.isEmpty()) ? formTitleAttr : "Product Form";

    String formActionAttr = (String) request.getAttribute("formAction");
    // Default action is 'addProduct' if 'formAction' attribute is not set
    String actionUrl = request.getContextPath() + "/" + 
                       ((formActionAttr != null && !formActionAttr.isEmpty()) ? formActionAttr : "addProduct");

    String errorMessage = (String) request.getAttribute("errorMessage");

    // --- Initialize product object and field values for the form ---
    Product product = null;
    Object productObj = request.getAttribute("product");
    if (productObj instanceof Product) {
        product = (Product) productObj;
    }

    String productIdStr = "";
    String productNameValue = "";
    String categoryValue = "";
    String priceValue = "";
    String quantityValue = ""; // Default to empty, will be populated if product or param exists

    boolean editing = false; // Flag to indicate if we are in edit mode

    // Populate values from the 'product' attribute if it exists (for edit or error repopulation)
    if (product != null) {
        if (product.getProductId() > 0) { // Assuming ProductID is int and > 0 means it's an existing product
            productIdStr = String.valueOf(product.getProductId());
            editing = true; 
        }
        // Use getters and provide default empty string if null
        productNameValue = (product.getProductName() != null) ? product.getProductName() : "";
        categoryValue = (product.getCategory() != null) ? product.getCategory() : "";
        
        if (product.getPrice() != null) {
            priceValue = product.getPrice().toPlainString(); // Use toPlainString for BigDecimal
        }
        // Quantity is int. If product is not null, getQuantity() will return its value (even 0).
        quantityValue = String.valueOf(product.getQuantity()); 
    }

    // --- Fallback to request parameters if values are still empty ---
    // This is crucial for repopulating the form after a validation error 
    // if the servlet didn't set a 'product' object back, or if fields in 'product' were null.
    if (productNameValue.isEmpty() && request.getParameter("productName") != null) {
        productNameValue = request.getParameter("productName");
    }
    if (categoryValue.isEmpty() && request.getParameter("category") != null) {
        categoryValue = request.getParameter("category");
    }
    // For number fields, if empty, setting from param helps. If 'product' had a 0, it's already set.
    if (priceValue.isEmpty() && request.getParameter("price") != null) {
        priceValue = request.getParameter("price");
    }
    if (quantityValue.isEmpty() && request.getParameter("quantity") != null) { // If product was null, quantityValue would be empty
        quantityValue = request.getParameter("quantity");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/validation.js" defer></script>
</head>
<body>
    <div class="container">
        
        <jsp:include page="header.jsp" /> <%-- THIS IS THE MOST LIKELY SOURCE OF THE ERROR --%>
        
        <h2><%= pageTitle %></h2>

        <%-- Display error message if it exists --%>
        <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
            <p class="error-message"><%= errorMessage %></p>
        <% } %>

        <form id="productForm" action="<%= actionUrl %>" method="post">
            <%-- Hidden field for productId if in edit mode --%>
            <% if (editing && !productIdStr.isEmpty()) { %>
                <input type="hidden" name="productId" value="<%= productIdStr %>">
            <% } %>

            <div>
                <label for="productName">Product Name:</label>
                <input type="text" id="productName" name="productName" value="<%= productNameValue %>" required>
                <span class="error-text" id="productNameError"></span>
            </div>
            <div>
                <label for="category">Category:</label>
                <input type="text" id="category" name="category" value="<%= categoryValue %>" required>
                 <span class="error-text" id="categoryError"></span>
            </div>
            <div>
                <label for="price">Price:</label>
                <input type="number" id="price" name="price" step="0.01" min="0" value="<%= priceValue %>" required>
                <span class="error-text" id="priceError"></span>
            </div>
            <div>
                <label for="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" min="0" value="<%= quantityValue %>" required>
                <span class="error-text" id="quantityError"></span>
            </div>
            <div>
                <button type="submit">Save Product</button>
                <a href="<%= request.getContextPath() %>/listProducts" class="button-link">Cancel</a>
            </div>
        </form>
    </div> <%-- End of .container div --%>
</body>
</html>