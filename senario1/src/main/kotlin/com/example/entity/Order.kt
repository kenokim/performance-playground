package com.example.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var count: Int = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val orderItems: MutableList<OrderItem> = mutableListOf()
) 