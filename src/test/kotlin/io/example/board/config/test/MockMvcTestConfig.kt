package io.example.board.config.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@AutoConfigureMockMvc
open class MockMvcTestConfig : BaseTestConfig() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun setUp() {
        mockMvc =
            MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter<DefaultMockMvcBuilder>({ request: ServletRequest?, response: ServletResponse, chain: FilterChain ->
                    request?.characterEncoding = "UTF-8"
                    response.characterEncoding = "UTF-8"
                    chain.doFilter(request, response)
                }).build()
    }

    @Throws(Exception::class)
    fun get(requestUrl: String): ResultActions {
        return mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
    }

    @Throws(Exception::class)
    fun getWithParameterMap(
        requestUrl: String,
        parameterMap: MultiValueMap<String, String>
    ): ResultActions {
        return mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .params(parameterMap)
        )
    }

    @Throws(Exception::class)
    fun <T> post(requestUrl: String, requestBody: T): ResultActions {
        return mockMvc.perform(
            post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
    }
}