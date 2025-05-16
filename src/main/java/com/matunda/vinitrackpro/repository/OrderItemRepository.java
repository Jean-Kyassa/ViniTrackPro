package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Use the proper relationship path in method names
    List<OrderItem> findByOrder_Id(Long orderId);

    List<OrderItem> findByProduct_Id(Long productId);

    @Query("SELECT oi.product.id, SUM(oi.quantity) as totalQuantity FROM OrderItem oi " +
            "JOIN oi.order o WHERE o.status = 'COMPLETED' " +
            "AND o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.id ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProductsInPeriod(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p.category.id, SUM(oi.quantity * oi.price) as totalSales FROM OrderItem oi " +
            "JOIN oi.product p JOIN oi.order o WHERE o.status = 'COMPLETED' " +
            "GROUP BY p.category.id ORDER BY totalSales DESC")
    List<Object[]> findSalesByCategory();

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Long getTotalSoldQuantityByProductId(Long productId);
}
//
//@Repository
//public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
//
//    List<OrderItem> findByOrderId(Long orderId);
//
//    List<OrderItem> findByProductId(Long productId);
//    @Query("SELECT oi.productId, SUM(oi.quantity) as totalQuantity FROM OrderItem oi " +
//    "JOIN oi.order o WHERE o.status = 'COMPLETED' " +
//    "AND o.orderDate BETWEEN :startDate AND :endDate " +
//    "GROUP BY oi.productId ORDER BY totalQuantity DESC")
//List<Object[]> findTopSellingProductsInPeriod(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
//
//@Query("SELECT p.category.id, SUM(oi.totalPrice) as totalSales FROM OrderItem oi " +
//    "JOIN oi.product p JOIN oi.order o WHERE o.status = 'COMPLETED' " +
//    "GROUP BY p.category.id ORDER BY totalSales DESC")
//List<Object[]> findSalesByCategory();
//
//@Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productId = :productId")
//Long getTotalSoldQuantityByProductId(Long productId);
//}
