package io.example.board.aspect.log

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(
    RetentionPolicy.RUNTIME
)
annotation class Timer