package com.exerovv.deadpixel

import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.auth.domain.usecase.LogoutUseCase
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrdersUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOverdueOrdersUseCase
import com.exerovv.deadpixel.feature.orders.presentation.OrdersViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelsTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region helpers

    private fun fakeOrder(id: Int = 1) = Order(
        id = id,
        workOrderNumber = "WO-$id",
        description = "Заказ $id",
        status = OrderStatus.RECEIVED,
        estimatedCost = null,
        deadline = null,
        createdAt = "2024-01-01"
    )

    private fun managerTokenManager() = mockk<TokenManager>(relaxed = true).also {
        every { it.getUserRole() } returns UserRole.MANAGER
        every { it.getUserId() } returns 1
    }

    private fun masterTokenManager() = mockk<TokenManager>(relaxed = true).also {
        every { it.getUserRole() } returns UserRole.MASTER
        every { it.getUserId() } returns 2
    }

    private class FakeOrdersRepository(
        private val orders: List<Order> = emptyList(),
        private val overdue: List<Order> = emptyList(),
        private val throws: Boolean = false
    ) : OrdersRepository {
        override suspend fun getOrders() =
            if (throws) throw RuntimeException("ошибка сети") else orders
        override suspend fun getOverdueOrders() =
            if (throws) throw RuntimeException("ошибка сети") else overdue
        override suspend fun getOrderById(orderId: Int): Order = throw NotImplementedError()
        override suspend fun getOrderAssignment(orderId: Int): OrderAssignment? = throw NotImplementedError()
        override suspend fun getOrderHistory(orderId: Int): List<OrderStatusHistory> = throw NotImplementedError()
        override suspend fun getOrdersByMaster(masterId: Int): List<Order> = throw NotImplementedError()
        override suspend fun getOrdersByStatus(status: String): List<Order> = throw NotImplementedError()
        override suspend fun createOrder(equipmentId: Int, description: String, deadline: String?, estimatedCost: Double?): Order = throw NotImplementedError()
        override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus, note: String?): Order = throw NotImplementedError()
        override suspend fun assignMaster(orderId: Int, masterId: Int) = throw NotImplementedError()
    }

    // endregion

    // region OrdersViewModel

    @Test
    fun `OrdersViewModel initial state has no error and empty orders`() {
        val repo = FakeOrdersRepository()
        val vm = OrdersViewModel(GetOrdersUseCase(repo), masterTokenManager())

        assertNull(vm.state.value.error)
    }

    @Test
    fun `OrdersViewModel load success populates orders list`() = runTest {
        val orders = listOf(fakeOrder(1), fakeOrder(2))
        val repo = FakeOrdersRepository(orders = orders)
        val vm = OrdersViewModel(GetOrdersUseCase(repo), masterTokenManager())

        assertEquals(orders, vm.state.value.orders)
        assertFalse(vm.state.value.isLoading)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `OrdersViewModel load failure sets error and clears loading`() = runTest {
        val repo = FakeOrdersRepository(throws = true)
        val vm = OrdersViewModel(GetOrdersUseCase(repo), masterTokenManager())

        assertNotNull(vm.state.value.error)
        assertFalse(vm.state.value.isLoading)
        assertTrue(vm.state.value.orders.isEmpty())
    }

    @Test
    fun `OrdersViewModel isManager reflects token role`() {
        val vm = OrdersViewModel(GetOrdersUseCase(FakeOrdersRepository()), managerTokenManager())
        assertTrue(vm.isManager)
    }

    // endregion

    // region MainViewModel

    @Test
    fun `MainViewModel overdueOrders loads on init for manager`() = runTest {
        val overdue = listOf(fakeOrder(5), fakeOrder(6))
        val repo = FakeOrdersRepository(overdue = overdue)
        val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)

        val vm = MainViewModel(logoutUseCase, GetOverdueOrdersUseCase(repo), managerTokenManager())

        assertEquals(overdue, vm.overdueOrders.value)
    }

    @Test
    fun `MainViewModel overdueOrders stays empty for non-manager`() = runTest {
        val overdue = listOf(fakeOrder(5))
        val repo = FakeOrdersRepository(overdue = overdue)
        val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)

        val vm = MainViewModel(logoutUseCase, GetOverdueOrdersUseCase(repo), masterTokenManager())

        assertTrue(vm.overdueOrders.value.isEmpty())
    }

    @Test
    fun `MainViewModel refreshOverdueOrders updates list for manager`() = runTest {
        val initial = listOf(fakeOrder(1))
        val refreshed = listOf(fakeOrder(1), fakeOrder(2))
        var callCount = 0
        val repo = object : OrdersRepository {
            override suspend fun getOrders(): List<Order> = emptyList()
            override suspend fun getOverdueOrders(): List<Order> {
                callCount++
                return if (callCount == 1) initial else refreshed
            }
            override suspend fun getOrderById(orderId: Int): Order = throw NotImplementedError()
            override suspend fun getOrderAssignment(orderId: Int): OrderAssignment? = throw NotImplementedError()
            override suspend fun getOrderHistory(orderId: Int): List<OrderStatusHistory> = throw NotImplementedError()
            override suspend fun getOrdersByMaster(masterId: Int): List<Order> = throw NotImplementedError()
            override suspend fun getOrdersByStatus(status: String): List<Order> = throw NotImplementedError()
            override suspend fun createOrder(equipmentId: Int, description: String, deadline: String?, estimatedCost: Double?): Order = throw NotImplementedError()
            override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus, note: String?): Order = throw NotImplementedError()
            override suspend fun assignMaster(orderId: Int, masterId: Int) = throw NotImplementedError()
        }
        val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)
        val vm = MainViewModel(logoutUseCase, GetOverdueOrdersUseCase(repo), managerTokenManager())

        vm.refreshOverdueOrders()

        assertEquals(refreshed, vm.overdueOrders.value)
    }

    // endregion
}
