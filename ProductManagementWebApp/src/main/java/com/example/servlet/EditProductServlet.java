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
import java.sql.SQLException;

@WebServlet("/editProduct") // This annotation is the same
public class EditProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("listProducts?error=MissingProductId");
            return;
        }

        try {
            int productId = Integer.parseInt(idStr);
            Product existingProduct = productDAO.getProductById(productId);
            if (existingProduct != null) {
                request.setAttribute("product", existingProduct);
                request.setAttribute("formAction", "updateProduct");
                request.setAttribute("formTitle", "Edit Product");
                request.getRequestDispatcher("product_form.jsp").forward(request, response);
            } else {
                response.sendRedirect("listProducts?error=ProductNotFound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("listProducts?error=InvalidProductIdFormat");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error loading product for edit: " + e.getMessage());
            request.getRequestDispatcher("error_page.jsp").forward(request, response);
        }
    }
}