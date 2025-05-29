package com.example.servlet;

import com.example.dao.ProductDAO;

// Import javax.servlet packages
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteProduct") // This annotation remains the same
public class DeleteProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String confirm = request.getParameter("confirm");

        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("listProducts?error=MissingProductIdForDelete");
            return;
        }

        try {
            int productId = Integer.parseInt(idStr);

            if ("yes".equals(confirm)) {
                boolean success = productDAO.deleteProduct(productId);
                if (success) {
                    response.sendRedirect("listProducts?success=ProductDeleted");
                } else {
                    response.sendRedirect("listProducts?error=ProductNotDeletedOrNotFound");
                }
            } else {
                // This logic assumes client-side confirmation.
                // If 'confirm' is not 'yes', it redirects with an error.
                // This is fine as per the original logic.
                response.sendRedirect("listProducts?error=DeletionNotConfirmed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("listProducts?error=InvalidProductIdFormatForDelete");
        } catch (SQLException e) {
            e.printStackTrace();
            // It's generally better to not expose raw SQL error messages directly to the user in the URL.
            // Consider logging the full e.getMessage() and showing a generic error.
            // For now, keeping it similar to the original:
            // response.sendRedirect("listProducts?error=DatabaseErrorOnDelete:" + e.getMessage());
            // A slightly better approach for user feedback:
            response.sendRedirect("listProducts?error=DatabaseErrorOnDelete");
        }
    }
}