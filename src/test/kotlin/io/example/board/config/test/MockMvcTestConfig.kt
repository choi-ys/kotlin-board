package io.example.board.config.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.util.MultiValueMap

@EnableMockMvc
open class MockMvcTestConfig : BaseTestConfig() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

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