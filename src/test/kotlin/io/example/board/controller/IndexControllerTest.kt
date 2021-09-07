package io.example.board.controller

import io.example.board.config.test.EnableMockMvc
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
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
@AutoConfigureMockMvc
@Import(MemberGenerator::class)
@DisplayName("Controller:Index")
@Transactional
internal class IndexControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("API:목차")
    fun index() {
        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/index")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }
}