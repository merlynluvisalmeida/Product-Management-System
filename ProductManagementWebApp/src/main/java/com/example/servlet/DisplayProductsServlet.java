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
import java.util.List;

@WebServlet("/listProducts") // This annotation is the same for both javax and jakarta
public class DisplayProductsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> productList = productDAO.getAllProducts();
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("product_list.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace(); // Log this
            request.setAttribute("errorMessage", "Error fetching products: " + e.getMessage());
            // Ensure you have an error_page.jsp or handle errors appropriately
            request.getRequestDispatcher("error_page.jsp").forward(request, response); 
        }
    }
}