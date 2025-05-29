<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- This is header.jsp --%>
<header class="main-header">
    <h1>Product Management System</h1>
    <nav>
        <ul>
            <li><a href="<%= request.getContextPath() %>/index.jsp">Home</a></li>
            <li><a href="<%= request.getContextPath() %>/addProduct">Add Product</a></li>
            <li><a href="<%= request.getContextPath() %>/listProducts">View Products</a></li>
            <li><a href="<%= request.getContextPath() %>/reports">Reports</a></li>
        </ul>
    </nav>
</header>