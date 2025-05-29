package com.example.dao;
import com.example.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
public class ProductDAO {
	 public boolean addProduct(Product product) throws SQLException {
	        String sql = "INSERT INTO Products (ProductName, Category, Price, Quantity) VALUES (?, ?, ?, ?)";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setString(1, product.getProductName());
	            pstmt.setString(2, product.getCategory());
	            pstmt.setBigDecimal(3, product.getPrice());
	            pstmt.setInt(4, product.getQuantity());
	            
	            int rowsAffected = pstmt.executeUpdate();
	            return rowsAffected > 0;
	        }
	    }

	    public Product getProductById(int productId) throws SQLException {
	        String sql = "SELECT * FROM Products WHERE ProductID = ?";
	        Product product = null;
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setInt(1, productId);
	            ResultSet rs = pstmt.executeQuery();
	            
	            if (rs.next()) {
	                product = new Product();
	                product.setProductId(rs.getInt("ProductID"));
	                product.setProductName(rs.getString("ProductName"));
	                product.setCategory(rs.getString("Category"));
	                product.setPrice(rs.getBigDecimal("Price"));
	                product.setQuantity(rs.getInt("Quantity"));
	            }
	        }
	        return product;
	    }

	    public List<Product> getAllProducts() throws SQLException {
	        List<Product> products = new ArrayList<>();
	        String sql = "SELECT * FROM Products ORDER BY ProductID";
	        try (Connection conn = DatabaseUtil.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                Product product = new Product();
	                product.setProductId(rs.getInt("ProductID"));
	                product.setProductName(rs.getString("ProductName"));
	                product.setCategory(rs.getString("Category"));
	                product.setPrice(rs.getBigDecimal("Price"));
	                product.setQuantity(rs.getInt("Quantity"));
	                products.add(product);
	            }
	        }
	        return products;
	    }

	    public boolean updateProduct(Product product) throws SQLException {
	        String sql = "UPDATE Products SET ProductName = ?, Category = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setString(1, product.getProductName());
	            pstmt.setString(2, product.getCategory());
	            pstmt.setBigDecimal(3, product.getPrice());
	            pstmt.setInt(4, product.getQuantity());
	            pstmt.setInt(5, product.getProductId());
	            
	            int rowsAffected = pstmt.executeUpdate();
	            return rowsAffected > 0;
	        }
	    }

	    public boolean deleteProduct(int productId) throws SQLException {
	        String sql = "DELETE FROM Products WHERE ProductID = ?";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setInt(1, productId);
	            int rowsAffected = pstmt.executeUpdate();
	            return rowsAffected > 0;
	        }
	    }

	    // --- Report Methods ---

	    public List<Product> getProductsByPriceGreaterThan(BigDecimal minPrice) throws SQLException {
	        List<Product> products = new ArrayList<>();
	        String sql = "SELECT * FROM Products WHERE Price > ? ORDER BY Price DESC";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setBigDecimal(1, minPrice);
	            ResultSet rs = pstmt.executeQuery();
	            
	            while (rs.next()) {
	                Product product = new Product();
	                product.setProductId(rs.getInt("ProductID"));
	                product.setProductName(rs.getString("ProductName"));
	                product.setCategory(rs.getString("Category"));
	                product.setPrice(rs.getBigDecimal("Price"));
	                product.setQuantity(rs.getInt("Quantity"));
	                products.add(product);
	            }
	        }
	        return products;
	    }

	    public List<Product> getProductsByCategory(String category) throws SQLException {
	        List<Product> products = new ArrayList<>();
	        String sql = "SELECT * FROM Products WHERE Category = ? ORDER BY ProductName";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setString(1, category);
	            ResultSet rs = pstmt.executeQuery();
	            
	            while (rs.next()) {
	                Product product = new Product();
	                product.setProductId(rs.getInt("ProductID"));
	                product.setProductName(rs.getString("ProductName"));
	                product.setCategory(rs.getString("Category"));
	                product.setPrice(rs.getBigDecimal("Price"));
	                product.setQuantity(rs.getInt("Quantity"));
	                products.add(product);
	            }
	        }
	        return products;
	    }

	    // "Top N products by sales or quantity" - Using Quantity for simplicity here
	    public List<Product> getTopNProductsByQuantity(int n) throws SQLException {
	        List<Product> products = new ArrayList<>();
	        // Ensure N is positive to avoid SQL errors with LIMIT
	        if (n <= 0) return products; 

	        String sql = "SELECT * FROM Products ORDER BY Quantity DESC LIMIT ?";
	        try (Connection conn = DatabaseUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setInt(1, n);
	            ResultSet rs = pstmt.executeQuery();
	            
	            while (rs.next()) {
	                Product product = new Product();
	                product.setProductId(rs.getInt("ProductID"));
	                product.setProductName(rs.getString("ProductName"));
	                product.setCategory(rs.getString("Category"));
	                product.setPrice(rs.getBigDecimal("Price"));
	                product.setQuantity(rs.getInt("Quantity"));
	                products.add(product);
	            }
	        }
	        return products;
	    }
}
