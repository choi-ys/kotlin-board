package io.example.board.controller

import io.example.board.config.test.EnableMockMvc
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021/09/07 7:47 오후
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableMockMvc
@DisplayName("Controller:Index")
@Transactional
internal class IndexControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val INDEX = "/index"

    @Test
    @DisplayName("API:목차[200]")
    fun index() {
        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(INDEX)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }
}