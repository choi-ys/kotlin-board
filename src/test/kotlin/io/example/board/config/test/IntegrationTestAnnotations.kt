package io.example.board.config.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTestAnnotations : BaseTestAnnotations(){

    @Autowired
    lateinit var mockMvc: MockMvc
}