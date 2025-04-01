package com.example.controller

import com.example.service.OrderService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping
    fun getOrders(
        @RequestParam userId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ) = orderService.getOrdersWithItems(userId, startDate, endDate)

    @PostMapping("/{id}/decrease-count")
    fun decreaseOrderCount(@PathVariable id: Long): ResponseEntity<Map<String, Boolean>> {
        val success = orderService.decreaseOrderCount(id)
        return ResponseEntity.ok(mapOf("success" to success))
    }
} 