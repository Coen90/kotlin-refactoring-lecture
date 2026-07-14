package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.service.user.UserService
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUserTest() {
        //given
        val request = UserCreateRequest("박현태", null)

        //when
        userService.saveUser(request)

        //then
        val result = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("박현태")
        assertThat(result[0].age).isNull()
    }

    @Test
    fun getUsersTest() {
        //given
        userRepository.saveAll(
            listOf(
                User("A", 20),
                User("B", null),
            )
        )

        //when
        val results = userService.getUsers()

        //then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    fun updateUserNametest() {
        //given
        val user = userRepository.save(User("박현태", 20))
        val request = UserUpdateRequest(user.id, "박채움")

        //when
        userService.updateUserName(request)

        //then
        val results = userService.getUsers()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("박채움")
        assertThat(results[0].age).isEqualTo(20)
    }

    @Test
    fun deleteUserTest() {
        //given
        val user = userRepository.save(User("박현태", 20))

        //when
        userService.deleteUser(user.name)

        //then
        val results = userService.getUsers()
        assertThat(results).isEmpty()
    }
}