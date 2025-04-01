package com.example.repository

import com.example.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<Order, Long> {
    @Query("""
        SELECT o FROM Order o 
        JOIN FETCH o.orderItems oi 
        WHERE o.id = :userId 
        AND o.createdAt >= :startDate 
        AND o.createdAt < :endDate
        AND o.count > 0
    """)
    fun findOrdersWithItemsByUserIdAndDateRange(
        @Param("userId") userId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Order>

    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.count > 0")
    fun findOrderWithPositiveCount(@Param("id") id: Long): Order?

    @Modifying
    @Query("UPDATE Order o SET o.count = o.count - 1 WHERE o.id = :id AND o.count > 0")
    fun decreaseCount(@Param("id") id: Long): Int
} 