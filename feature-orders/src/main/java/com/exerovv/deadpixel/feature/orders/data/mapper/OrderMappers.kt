package com.exerovv.deadpixel.feature.orders.data.mapper

import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderAssignmentDto
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderDto
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderStatusHistoryDto
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory

fun OrderDto.toDomain() = Order(
    id = id,
    workOrderNumber = workOrderNumber,
    description = description,
    status = status,
    estimatedCost = estimatedCost,
    deadline = deadline,
    createdAt = createdAt
)

fun OrderAssignmentDto.toDomain() = OrderAssignment(
    id = id,
    orderId = orderId,
    masterId = masterId,
    assignedAt = assignedAt,
    isActive = isActive
)

fun OrderStatusHistoryDto.toDomain() = OrderStatusHistory(
    id = id,
    previousStatus = previousStatus,
    newStatus = newStatus,
    changedBy = changedBy,
    note = note,
    changedAt = changedAt
)
