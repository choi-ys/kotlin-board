package io.example.board.domain.dto.response.common

import org.springframework.validation.FieldError
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021/09/13 1:07 오전
 */
data class DetailFieldError(
    var objectName: String,
    var field: String,
    var code: String?,
    var rejectMessage: String?,
    var rejectedValue: Any?
) {
    companion object {
        fun mapTo(errors: MutableList<FieldError>): List<DetailFieldError> {
            return errors.stream()
                .map {
                    DetailFieldError(
                        objectName = it.objectName,
                        field = it.field,
                        code = it.code,
                        rejectMessage = it.defaultMessage,
                        rejectedValue = it.rejectedValue
                    )
                }.collect(Collectors.toList())
        }
    }
}
