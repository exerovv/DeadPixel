package com.exerovv.deadpixel

import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.orders.data.mapper.toDomain
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderDto
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.users.data.remote.toDomain
import com.exerovv.deadpixel.feature.users.data.remote.dto.UserDto
import com.exerovv.deadpixel.feature.workplans.data.remote.toDomain
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.WorkPlanDto
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.WorkPlanItemDto
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun `OrderDto toDomain maps all fields correctly`() {
        val dto = OrderDto(
            id = 7,
            workOrderNumber = "WO-007",
            equipmentId = 2,
            createdBy = 1,
            status = OrderStatus.IN_PROGRESS,
            description = "Ремонт ноутбука",
            estimatedCost = 3500.0,
            deadline = "2024-06-01T00:00:00",
            createdAt = "2024-05-01T10:00:00",
            updatedAt = "2024-05-02T10:00:00"
        )

        val order = dto.toDomain()

        assertEquals(7, order.id)
        assertEquals("WO-007", order.workOrderNumber)
        assertEquals("Ремонт ноутбука", order.description)
        assertEquals(OrderStatus.IN_PROGRESS, order.status)
        assertEquals(3500.0, order.estimatedCost)
        assertEquals("2024-06-01T00:00:00", order.deadline)
        assertEquals("2024-05-01T10:00:00", order.createdAt)
    }

    @Test
    fun `UserDto toDomain maps MANAGER role correctly`() {
        val dto = UserDto(
            id = 3,
            name = "Иван Петров",
            email = "ivan@test.com",
            role = "MANAGER",
            isActive = true
        )

        val user = dto.toDomain()

        assertEquals(3, user.id)
        assertEquals("Иван Петров", user.name)
        assertEquals("ivan@test.com", user.email)
        assertEquals(UserRole.MANAGER, user.role)
        assertEquals(true, user.isActive)
    }

    @Test
    fun `UserDto toDomain with unknown role falls back to MASTER`() {
        val dto = UserDto(
            id = 5,
            name = "Unknown",
            email = "x@test.com",
            role = "SUPERADMIN",
            isActive = false
        )

        val user = dto.toDomain()

        assertEquals(UserRole.MASTER, user.role)
    }

    @Test
    fun `WorkPlanDto toDomain maps ACTIVE status and item count`() {
        val itemDto = WorkPlanItemDto(
            id = 10,
            workPlanId = 1,
            description = "Диагностика",
            status = "PENDING"
        )
        val dto = WorkPlanDto(
            id = 1,
            orderId = 42,
            status = "ACTIVE",
            items = listOf(itemDto)
        )

        val plan = dto.toDomain()

        assertEquals(WorkPlanStatus.ACTIVE, plan.status)
        assertEquals(42, plan.orderId)
        assertEquals(1, plan.items.size)
        assertEquals(WorkPlanItemStatus.PENDING, plan.items.first().status)
        assertEquals("Диагностика", plan.items.first().description)
    }
}
