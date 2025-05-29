<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // --- Retrieve attributes and parameters for error display and form repopulation ---
    String reportError = (String) request.getAttribute("reportError");

    // Values submitted by the user, potentially set back by the servlet on error
    String selectedReportType = (String) request.getAttribute("selectedReportType");
    if (selectedReportType == null) { // Fallback to param if attribute not set
        selectedReportType = request.getParameter("reportType");
    }
    if (selectedReportType == null) { // Default if still null
        selectedReportType = "";
    }

    String submittedMinPrice = (String) request.getAttribute("submittedMinPrice");
    if (submittedMinPrice == null) {
        submittedMinPrice = request.getParameter("minPrice");
    }
    if (submittedMinPrice == null) submittedMinPrice = "";


    String submittedCategoryName = (String) request.getAttribute("submittedCategoryName");
    if (submittedCategoryName == null) {
        submittedCategoryName = request.getParameter("categoryName");
    }
    if (submittedCategoryName == null) submittedCategoryName = "";

    String submittedTopN = (String) request.getAttribute("submittedTopN");
    if (submittedTopN == null) {
        submittedTopN = request.getParameter("topN");
    }
    if (submittedTopN == null) submittedTopN = "";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generate Reports</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
    <script>
        function showReportFields() {
            var reportType = document.getElementById("reportType").value;
            var priceFields = document.getElementById("priceGreaterThanFields");
            var categoryFields = document.getElementById("categoryFields");
            var topNFields = document.getElementById("topNFields");

            // Hide all conditional fields first
            if(priceFields) priceFields.style.display = "none";
            if(categoryFields) categoryFields.style.display = "none";
            if(topNFields) topNFields.style.display = "none";

            // Show the relevant fields based on selection
            if (reportType === "priceGreaterThan" && priceFields) {
                priceFields.style.display = "block";
            } else if (reportType === "byCategory" && categoryFields) {
                categoryFields.style.display = "block";
            } else if (reportType === "topNByQuantity" && topNFields) {
                topNFields.style.display = "block";
            }
        }
        // Call on page load to set the initial state of the conditional fields
        // and also when the script is parsed (if elements are already in DOM)
        window.onload = showReportFields; 
        // For good measure, especially if scripts are in head and DOM might not be fully ready
        // or if content is loaded dynamically (though not the case here)
        // document.addEventListener('DOMContentLoaded', showReportFields); 
    </script>
</head>
<body>
    <div class="container">
        <%-- Ensure _header.jsp is scriptlet-based --%>
        <jsp:include page="header.jsp" /> 
        
        <h2>Generate Product Reports</h2>

        <%-- Display error message if it exists --%>
        <% if (reportError != null && !reportError.isEmpty()) { %>
            <p class="error-message"><%= reportError %></p>
        <% } %>

        <form action="<%= request.getContextPath() %>/reports" method="post">
            <div>
                <label for="reportType">Select Report Type:</label>
                <select id="reportType" name="reportType" onchange="showReportFields()" required>
                    <option value="">-- Select Report --</option>
                    <option value="priceGreaterThan" <% if ("priceGreaterThan".equals(selectedReportType)) { out.print("selected"); } %>>
                        Products with Price Greater Than
                    </option>
                    <option value="byCategory" <% if ("byCategory".equals(selectedReportType)) { out.print("selected"); } %>>
                        Products in a Specific Category
                    </option>
                    <option value="topNByQuantity" <% if ("topNByQuantity".equals(selectedReportType)) { out.print("selected"); } %>>
                        Top N Products by Quantity
                    </option>
                </select>
            </div>

            <%-- Conditional input fields for different report types --%>
            <div id="priceGreaterThanFields" style="display:none;">
                <label for="minPrice">Minimum Price:</label>
                <input type="number" id="minPrice" name="minPrice" step="0.01" min="0" 
                       value="<%= submittedMinPrice %>">
            </div>

            <div id="categoryFields" style="display:none;">
                <label for="categoryName">Category Name:</label>
                <input type="text" id="categoryName" name="categoryName" 
                       value="<%= submittedCategoryName %>">
            </div>

            <div id="topNFields" style="display:none;">
                <label for="topN">Top N (e.g., 5):</label>
                <input type="number" id="topN" name="topN" min="1" 
                       value="<%= submittedTopN %>">
            </div>
            
            <div>
                <button type="submit">Generate Report</button>
            </div>
        </form>
    </div> <%-- End of .container div --%>
</body>
</html>