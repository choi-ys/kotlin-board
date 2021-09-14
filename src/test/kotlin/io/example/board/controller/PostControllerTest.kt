package io.example.board.controller

import io.example.board.config.test.IntegrationTestConfig
import io.example.board.domain.dto.request.PostRequest
import io.example.board.util.generator.MemberGenerator
import io.example.board.util.generator.PostGenerator
import io.example.board.util.generator.TokenGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author : choi-ys
 * @date : 2021/09/13 2:35 오후
 */
@DisplayName("Controller:Post")
@Import(TokenGenerator::class, PostGenerator::class, MemberGenerator::class)
internal class PostControllerTest : IntegrationTestConfig() {

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Autowired
    lateinit var postGenerator: PostGenerator

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    private val POST_URL = "/post"

    @Test
    @DisplayName("API:[200]게시글 생성")
    fun post() {
        // Given
        val title = "게시글 제목"
        val content = "게시글 본문"
        val postRequest = PostRequest(title, content)

        val bearerToken = TokenGenerator.makeBearerToken(tokenGenerator.generateToken().accessToken)

        // When
        val resultActions = this.mockMvc.perform(
            MockMvcRequestBuilders.post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, bearerToken)
                .content(this.objectMapper.writeValueAsBytes(postRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("API:[403]게시글 생성")
    fun post_Fail_CauseNoCredentials() {
        // Given
        val title = "게시글 제목"
        val content = "게시글 본문"
        val postRequest = PostRequest(title, content)

        // When
        val resultActions = this.post(POST_URL, postRequest)

        // Then
        resultActions.andDo(print())
            .andExpect(status().isForbidden)
    }

    @Test
    @DisplayName("API:[200]게시글 조회")
    fun postDetail() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val savedPost = postGenerator.savedPost(savedMember)

        val bearerToken = TokenGenerator.makeBearerToken(tokenGenerator.generateToken(savedMember).accessToken)

        // When
        val resultActions = this.mockMvc.perform(
            MockMvcRequestBuilders.get(POST_URL + "/" + savedPost.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, bearerToken)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("API:[403]게시글 조회")
    fun postDetail_Fail_CauseNoCredentials() {
        // Given
        val savedPost = postGenerator.savedPost()

        // When
        val resultActions = this.mockMvc.perform(
            MockMvcRequestBuilders.get(POST_URL + "/" + savedPost.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isForbidden)
    }
}