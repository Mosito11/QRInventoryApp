package com.example.qrinventoryapp

import com.example.qrinventoryapp.data.RoomEntitiesRepository
import com.example.qrinventoryapp.data.RoomEntity
import com.example.qrinventoryapp.data.UserEntitiesRepository
import com.example.qrinventoryapp.data.UserEntity
import com.example.qrinventoryapp.ui.home.AppMode
import com.example.qrinventoryapp.ui.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class FakeUserEntitiesRepository : UserEntitiesRepository {
    private val _flowAll = MutableSharedFlow<List<UserEntity>>(replay = 1)
    override fun getAllUserEntitiesStream(): Flow<List<UserEntity>> = _flowAll

    suspend fun emitUserList(users: List<UserEntity>) {
        _flowAll.emit(users)
    }

    private val _flowById = MutableSharedFlow<UserEntity>(replay = 1)
    override fun getUserEntityByIdStream(id: Int): Flow<UserEntity> = _flowById

    suspend fun emitUser(user: UserEntity) {
        _flowById.emit(user)
    }
}

class FakeRoomEntitiesRepository : RoomEntitiesRepository {
    private val _flowAll = MutableSharedFlow<List<RoomEntity>>(replay = 1)
    override fun getAllRoomEntitiesStream(): Flow<List<RoomEntity>> = _flowAll

    suspend fun emitRoomList(rooms: List<RoomEntity>) {
        _flowAll.emit(rooms)
    }

    private val _flowById = MutableSharedFlow<RoomEntity>(replay = 1)
    override fun getRoomEntityByIdStream(id: Int): Flow<RoomEntity> = _flowById

    suspend fun emitRoom(room: RoomEntity) {
        _flowById.emit(room)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUserRepository: FakeUserEntitiesRepository
    private lateinit var fakeRoomRepository: FakeRoomEntitiesRepository
    private lateinit var fakeViewModel: HomeViewModel

    @Before
    fun setupTest() {
        Dispatchers.setMain(dispatcher)
        fakeUserRepository = FakeUserEntitiesRepository()
        fakeRoomRepository = FakeRoomEntitiesRepository()
        fakeViewModel = HomeViewModel(fakeUserRepository, fakeRoomRepository)
    }

    @After
    fun closeTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun selectMode_changeSelectedMode() {
        fakeViewModel.selectMode(AppMode.INVENTORY)
        assertEquals(AppMode.INVENTORY, fakeViewModel.uiState.value.selectedMode)
    }

    @Test
    fun selectUser_changeSelectedUserId() {
        fakeViewModel.selectUser(5)
        assertEquals(5, fakeViewModel.uiState.value.selectedUserId)
    }

    @Test
    fun selectRoom_changeSelectedRoomId() {
        fakeViewModel.selectRoom(10)
        assertEquals(10, fakeViewModel.uiState.value.selectedRoomId)
    }

    @Test
    fun isScanEnabled_whenModeSetToCONTROL() {
        fakeViewModel.selectMode(AppMode.CONTROL)
        assertTrue(fakeViewModel.uiState.value.isScanEnabled)
    }

    @Test
    fun isScanEnabled_whenModeSetToINVENTORY_andWhenUserAndRoomSelected() {
        fakeViewModel.selectMode(AppMode.INVENTORY)
        assertEquals(false, fakeViewModel.uiState.value.isScanEnabled)

        fakeViewModel.selectUser(1)
        assertEquals(false, fakeViewModel.uiState.value.isScanEnabled)

        fakeViewModel.selectRoom(2)
        assertEquals(true, fakeViewModel.uiState.value.isScanEnabled)
    }

    @Test
    fun emittingAllUsersUpdatesAvailableUsersInUiState() = runTest {
        val testUsers = listOf(UserEntity(id = 1, name = "Fake User"))
        fakeUserRepository.emitUserList(testUsers)
        assertEquals(testUsers, fakeViewModel.uiState.value.availableUsers)
    }

    @Test
    fun emittingAllRoomsUpdatesAvailableRoomsInUiState() = runTest {
        val testRooms = listOf(RoomEntity(id = 1, name = "Fake Room"))
        fakeRoomRepository.emitRoomList(testRooms)
        assertEquals(testRooms, fakeViewModel.uiState.value.availableRooms)
    }

    @Test
    fun emittingUserByIdUpdatesSelectedUserInUiState() = runTest {
        val testUser = UserEntity(id = 1, name = "Fake User")
        fakeUserRepository.emitUser(testUser)
        fakeViewModel.selectUser(testUser.id)
        assertEquals(testUser.id, fakeViewModel.uiState.value.selectedUserId)
    }

    @Test
    fun emittingRoomByIdUpdatesSelectedRoomInUiState() = runTest {
        val testRoom = RoomEntity(id = 1, name = "Fake Room")
        fakeRoomRepository.emitRoom(testRoom)
        fakeViewModel.selectRoom(testRoom.id)
        assertEquals(testRoom.id, fakeViewModel.uiState.value.selectedRoomId)
    }
}