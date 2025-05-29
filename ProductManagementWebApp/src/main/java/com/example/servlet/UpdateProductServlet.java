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

@WebServlet("/updateProduct") // This annotation remains the same
public class UpdateProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");

        String errorMessage = "";
        Product productToUpdate = new Product(); // To preserve input if validation fails

        // Basic Validation
        int productId = 0;
         try {
            if (idStr == null || idStr.trim().isEmpty()) {
                 errorMessage += "Product ID is missing for update. ";
            } else {
                productId = Integer.parseInt(idStr);
                productToUpdate.setProductId(productId); // Set ID for repopulating form
            }
        } catch (NumberFormatException e) {
            errorMessage += "Invalid Product ID format. ";
        }

        if (productName == null || productName.trim().isEmpty()) {
            errorMessage += "Product Name cannot be empty. ";
        }
        productToUpdate.setProductName(productName); // Set for repopulating form

        if (category == null || category.trim().isEmpty()) {
            errorMessage += "Category cannot be empty. ";
        }
        productToUpdate.setCategory(category); // Set for repopulating form
        
        BigDecimal price = null;
        try {
            if (priceStr == null || priceStr.trim().isEmpty()) {
                 errorMessage += "Price cannot be empty. ";
            } else {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    errorMessage += "Price cannot be negative. ";
                }
            }
            // Set price on productToUpdate for repopulation, even if invalid (will be string original or 0)
            if (priceStr != null) productToUpdate.setPrice(price != null ? price : new BigDecimal(priceStr.replaceAll("[^\\d.]", ""))); 
            else productToUpdate.setPrice(BigDecimal.ZERO);

        } catch (NumberFormatException e) {
            errorMessage += "Invalid Price format. ";
            if (priceStr != null) productToUpdate.setPrice(BigDecimal.ZERO); // placeholder
            else productToUpdate.setPrice(BigDecimal.ZERO);
        }
        

        int quantity = 0;
        try {
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                errorMessage += "Quantity cannot be empty. ";
            } else {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    errorMessage += "Quantity cannot be negative. ";
                }
            }
            // Set quantity on productToUpdate for repopulation
             if (quantityStr != null) productToUpdate.setQuantity(quantity);
             else productToUpdate.setQuantity(0);

        } catch (NumberFormatException e) {
            errorMessage += "Invalid Quantity format. ";
            if (quantityStr != null) productToUpdate.setQuantity(0); // placeholder
            else productToUpdate.setQuantity(0);
        }


        if (!errorMessage.isEmpty()) {
            request.setAttribute("errorMessage", errorMessage.trim());
            // Repopulate the form with entered values if validation fails
            // Ensure productToUpdate has all fields set (even if temporarily with defaults or parsed invalid values)
            if (productToUpdate.getProductName() == null) productToUpdate.setProductName(request.getParameter("productName"));
            if (productToUpdate.getCategory() == null) productToUpdate.setCategory(request.getParameter("category"));
            // Price and Quantity are trickier to repopulate perfectly if original input was completely unparseable,
            // but we try to preserve what we can or show what was submitted.
            // The JSP uses `param.fieldName` as a fallback if `product.fieldName` is null/empty, which helps here.

            request.setAttribute("product", productToUpdate);
            request.setAttribute("formAction", "updateProduct");
            request.setAttribute("formTitle", "Edit Product");
            request.getRequestDispatcher("product_form.jsp").forward(request, response);
            return;
        }

        // If validation passed, create the final Product object for DB update
        Product updatedProduct = new Product(productId, productName, category, price, quantity);

        try {
            boolean success = productDAO.updateProduct(updatedProduct);
            if (success) {
                response.sendRedirect("listProducts?success=ProductUpdated");
            } else {
                request.setAttribute("errorMessage", "Failed to update product. Product might not exist or no changes were made.");
                request.setAttribute("product", updatedProduct); // Send back the data attempted to update
                request.setAttribute("formAction", "updateProduct");
                request.setAttribute("formTitle", "Edit Product");
                request.getRequestDispatcher("product_form.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.setAttribute("product", updatedProduct); // Send back the data attempted to update
            request.setAttribute("formAction", "updateProduct");
            request.setAttribute("formTitle", "Edit Product");
            request.getRequestDispatcher("product_form.jsp").forward(request, response);
        }
    }
}