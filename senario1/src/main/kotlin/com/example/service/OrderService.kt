package com.example.service

import com.example.entity.Order
import com.example.repository.OrderRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    @Cacheable(value = ["orders"], key = "#userId + '-' + #startDate + '-' + #endDate")
    fun getOrdersWithItems(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<Order> {
        return orderRepository.findOrdersWithItemsByUserIdAndDateRange(userId, startDate, endDate)
    }

    @Transactional
    fun decreaseOrderCount(id: Long): Boolean {
        // First query: Check if order exists and has positive count
        val order = orderRepository.findOrderWithPositiveCount(id)
        if (order == null) {
            return false
        }

        // Second query: Decrease count
        val updated = orderRepository.decreaseCount(id)
        return updated > 0
    }
} 