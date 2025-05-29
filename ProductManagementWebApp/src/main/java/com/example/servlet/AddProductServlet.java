package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/addProduct")
public class AddProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productName = request.getParameter("productName");
        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");

        StringBuilder errorMessage = new StringBuilder();

        // Validation
        if (productName == null || productName.trim().isEmpty())
            errorMessage.append("Product Name cannot be empty. ");
        if (category == null || category.trim().isEmpty())
            errorMessage.append("Category cannot be empty. ");

        BigDecimal price = null;
        try {
            if (priceStr == null || priceStr.trim().isEmpty()) {
                errorMessage.append("Price cannot be empty. ");
            } else {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0)
                    errorMessage.append("Price cannot be negative. ");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Invalid Price format. ");
        }

        int quantity = 0;
        try {
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                errorMessage.append("Quantity cannot be empty. ");
            } else {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0)
                    errorMessage.append("Quantity cannot be negative. ");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Invalid Quantity format. ");
        }

        if (errorMessage.length() > 0) {
            request.setAttribute("errorMessage", errorMessage.toString());
            request.setAttribute("productName", productName);
            request.setAttribute("category", category);
            request.setAttribute("price", priceStr);
            request.setAttribute("quantity", quantityStr);
            request.getRequestDispatcher("product_form.jsp").forward(request, response);
            return;
        }

        Product newProduct = new Product(productName, category, price, quantity);

        try {
            boolean success = productDAO.addProduct(newProduct);
            if (success) {
                response.sendRedirect("listProducts");
            } else {
                request.setAttribute("errorMessage", "Failed to add product. Please try again.");
                request.getRequestDispatcher("product_form.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("product_form.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("formAction", "addProduct");
        request.setAttribute("formTitle", "Add New Product");
        request.getRequestDispatcher("product_form.jsp").forward(request, response);
    }
}
