package com.example.servlet;


import com.example.dao.ProductDAO;
import com.example.model.Product;

// Import javax.servlet packages
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/reports") // This annotation remains the same
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // This GET request will simply show the reports selection page
        request.getRequestDispatcher("reports.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reportType = request.getParameter("reportType");
        List<Product> reportResults = new ArrayList<>();
        String reportTitle = "Report Results";
        String errorMessage = null;

        try {
            if ("priceGreaterThan".equals(reportType)) {
                String minPriceStr = request.getParameter("minPrice");
                if (minPriceStr == null || minPriceStr.trim().isEmpty()) {
                    errorMessage = "Minimum Price is required for this report.";
                } else {
                    try {
                        BigDecimal minPrice = new BigDecimal(minPriceStr);
                        if (minPrice.compareTo(BigDecimal.ZERO) < 0) {
                             errorMessage = "Minimum Price cannot be negative.";
                        } else {
                            reportResults = productDAO.getProductsByPriceGreaterThan(minPrice);
                            reportTitle = "Products with Price Greater Than " + minPrice;
                        }
                    } catch (NumberFormatException e) {
                        errorMessage = "Invalid Minimum Price format.";
                    }
                }
            } else if ("byCategory".equals(reportType)) {
                String category = request.getParameter("categoryName");
                 if (category == null || category.trim().isEmpty()) {
                    errorMessage = "Category Name is required for this report.";
                } else {
                    reportResults = productDAO.getProductsByCategory(category);
                    reportTitle = "Products in Category: " + category;
                }
            } else if ("topNByQuantity".equals(reportType)) {
                String topNStr = request.getParameter("topN");
                if (topNStr == null || topNStr.trim().isEmpty()) {
                    errorMessage = "Number of products (N) is required for this report.";
                } else {
                    try {
                        int topN = Integer.parseInt(topNStr);
                        if (topN <= 0) {
                            errorMessage = "N must be a positive number.";
                        } else {
                            reportResults = productDAO.getTopNProductsByQuantity(topN);
                            reportTitle = "Top " + topN + " Products by Quantity";
                        }
                    } catch (NumberFormatException e) {
                         errorMessage = "Invalid format for N (number of products).";
                    }
                }
            } else {
                 errorMessage = "Invalid report type selected."; // Or no report type selected
                 if (reportType == null || reportType.trim().isEmpty()){
                     errorMessage = "Please select a report type.";
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log this
            errorMessage = "Database error generating report: " + e.getMessage();
        }

        if (errorMessage != null) {
            request.setAttribute("reportError", errorMessage);
            // Preserve user input for report criteria when an error occurs
            request.setAttribute("selectedReportType", reportType);
            if ("priceGreaterThan".equals(reportType)) request.setAttribute("submittedMinPrice", request.getParameter("minPrice"));
            if ("byCategory".equals(reportType)) request.setAttribute("submittedCategoryName", request.getParameter("categoryName"));
            if ("topNByQuantity".equals(reportType)) request.setAttribute("submittedTopN", request.getParameter("topN"));
            
            request.getRequestDispatcher("reports.jsp").forward(request, response);
        } else {
            request.setAttribute("reportResults", reportResults);
            request.setAttribute("reportTitle", reportTitle);
            request.getRequestDispatcher("report_result.jsp").forward(request, response);
        }
    }
}
