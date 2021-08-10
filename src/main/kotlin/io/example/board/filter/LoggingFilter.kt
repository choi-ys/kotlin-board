package io.example.board.filter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

@Component
class LoggingFilter : Filter{

    /**
     * httpServletRequest, httpServletResponse를 이용하여 요청/응답 객체의 buffer를 읽을 경우
     * DispatcherServlet을 통해 Contrlloer로 요청/응답 객체가 전달되지 않으므로
     *
     * Spring의 ContentCachingRequestWrapper, ContentCachingResponseWrapper를 활용하여
     * doFilter() 실행 이후 요청/응답 객체를 캐싱 하여 요청/응답 객체에 접근 가능
     */
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        /**
         * 전처리 : 요청/응답 객체의 buffer 크기 초기화
         */
        val httpServletRequest = ContentCachingRequestWrapper(request as HttpServletRequest)
        val httpServletResponse = ContentCachingResponseWrapper(response as HttpServletResponse)

        chain?.doFilter(httpServletRequest, httpServletResponse)

        /**
         * 후처리 : 할당된 메모리에 요청/응답 객체 buffer를 캐싱
         */
        val requestUri = httpServletRequest.requestURI
        val requestContent = String(httpServletRequest.contentAsByteArray)

        val responseStatus = httpServletResponse.status
        val responseContent = String(httpServletResponse.contentAsByteArray)

        /**
         * 응답 객체의 byte를 읽게 되면 실제 반환되는 응답객체가 없으므로, 로깅을 위한 응답객체를 복사하여 사용
         */
        httpServletResponse.copyBodyToResponse()

        logger.info("[Request ::] URI: [{}], Body: {}", requestUri, requestContent)
        logger.info("[Response ::] Status: [{}], Body: {}", responseStatus, responseContent)
    }
}