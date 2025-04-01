package com.example.config

import com.example.entity.Order
import com.example.entity.OrderItem
import com.example.repository.OrderRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@Configuration
class DataInitializer {

    @Bean
    fun initData(orderRepository: OrderRepository): CommandLineRunner {
        return CommandLineRunner {
            // 이미 데이터가 있다면 생성하지 않음
            if (orderRepository.count() > 0) {
                println("Data already exists. Skipping data initialization.")
                return@CommandLineRunner
            }

            println("Starting data initialization...")
            
            // 현재 시간으로부터 30일 전까지의 랜덤한 시간으로 주문 생성
            val now = LocalDateTime.now()
            val orders = (1..500).map { orderId ->
                val randomDays = Random.nextInt(0, 30)
                val randomHours = Random.nextInt(0, 24)
                val randomMinutes = Random.nextInt(0, 60)
                
                val createdAt = now.minus(randomDays, ChronoUnit.DAYS)
                    .minus(randomHours, ChronoUnit.HOURS)
                    .minus(randomMinutes, ChronoUnit.MINUTES)
                
                val order = Order(
                    id = orderId.toLong(),
                    count = Random.nextInt(1, 10),
                    userId = Random.nextLong(1, 101), // 1-100 사이의 랜덤한 userId
                    createdAt = createdAt
                )
                
                // 각 주문마다 1-5개의 주문 항목 생성
                val orderItems = (1..Random.nextInt(1, 6)).map { itemId ->
                    OrderItem(
                        id = (orderId * 100 + itemId).toLong(),
                        order = order
                    )
                }
                
                order.orderItems.addAll(orderItems)
                order
            }
            
            orderRepository.saveAll(orders)
            println("Data initialization completed. Created ${orders.size} orders.")
        }
    }
} 