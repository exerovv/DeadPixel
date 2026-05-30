package com.exerovv.deadpixel

import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrdersUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOverdueOrdersUseCase
import com.exerovv.deadpixel.feature.users.domain.model.User
import com.exerovv.deadpixel.feature.users.domain.repository.UsersRepository
import com.exerovv.deadpixel.feature.users.domain.usecase.GetUserByIdUseCase
import com.exerovv.deadpixel.core.network.UserRole
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class UseCasesTest {

    // region helpers

    private fun fakeOrder(id: Int = 1) = Order(
        id = id,
        workOrderNumber = "WO-$id",
        description = "Описание $id",
        status = OrderStatus.RECEIVED,
        estimatedCost = null,
        deadline = null,
        createdAt = "2024-01-01"
    )

    private fun fakeUser() = User(
        id = 1,
        name = "Тест Тестов",
        email = "test@test.com",
        role = UserRole.MASTER,
        isActive = true,
        registeredAt = null
    )

    // endregion

    // region fakes

    private class FakeOrdersRepository(
        private val orders: List<Order> = emptyList(),
        private val overdue: List<Order> = emptyList(),
        private val throws: Boolean = false
    ) : OrdersRepository {
        override suspend fun getOrders() =
            if (throws) throw RuntimeException("сеть недоступна") else orders
        override suspend fun getOverdueOrders() =
            if (throws) throw RuntimeException("сеть недоступна") else overdue
        override suspend fun getOrderById(orderId: Int): Order = throw NotImplementedError()
        override suspend fun getOrderAssignment(orderId: Int): OrderAssignment? = throw NotImplementedError()
        override suspend fun getOrderHistory(orderId: Int): List<OrderStatusHistory> = throw NotImplementedError()
        override suspend fun getOrdersByMaster(masterId: Int): List<Order> = throw NotImplementedError()
        override suspend fun getOrdersByStatus(status: String): List<Order> = throw NotImplementedError()
        override suspend fun createOrder(equipmentId: Int, description: String, deadline: String?, estimatedCost: Double?): Order = throw NotImplementedError()
        override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus, note: String?): Order = throw NotImplementedError()
        override suspend fun assignMaster(orderId: Int, masterId: Int) = throw NotImplementedError()
    }

    private class FakeUsersRepository(
        private val user: User? = null
    ) : UsersRepository {
        override suspend fun getUsers(): List<User> = throw NotImplementedError()
        override suspend fun getUserById(userId: Int): User =
            user ?: throw RuntimeException("пользователь не найден")
        override suspend fun setUserActive(userId: Int, value: Boolean) = throw NotImplementedError()
    }

    // endregion

    @Test
    fun `GetOverdueOrdersUseCase returns repository result`() = runTest {
        val expected = listOf(fakeOrder(1), fakeOrder(2))
        val useCase = GetOverdueOrdersUseCase(FakeOrdersRepository(overdue = expected))

        val result = useCase()

        assertEquals(expected, result)
    }

    @Test
    fun `GetOverdueOrdersUseCase propagates repository exception`() = runTest {
        val useCase = GetOverdueOrdersUseCase(FakeOrdersRepository(throws = true))

        val ex = runCatching { useCase() }.exceptionOrNull()

        assertEquals("сеть недоступна", ex?.message)
    }

    @Test
    fun `GetOrdersUseCase returns repository result`() = runTest {
        val expected = listOf(fakeOrder(10), fakeOrder(11))
        val useCase = GetOrdersUseCase(FakeOrdersRepository(orders = expected))

        val result = useCase()

        assertSame(expected, result)
    }

    @Test
    fun `GetUserByIdUseCase returns user from repository`() = runTest {
        val user = fakeUser()
        val useCase = GetUserByIdUseCase(FakeUsersRepository(user = user))

        val result = useCase(1)

        assertEquals(user.name, result.name)
        assertEquals(user.email, result.email)
    }
}
