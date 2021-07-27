package io.example.board.config.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTestAnnotations : BaseTestAnnotations(){

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun setUp() {
        mockMvc =
            webAppContextSetup(webApplicationContext).addFilter<DefaultMockMvcBuilder>({ request: ServletRequest?, response: ServletResponse, chain: FilterChain ->
                response.characterEncoding = "UTF-8"
                chain.doFilter(request, response)
            }).build()
    }

}