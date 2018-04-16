package com.polito.kotlin.RestTurism

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RestTurismApplication

fun main(args: Array<String>) {
    mongoCodec()
    runApplication<RestTurismApplication>(*args)
}
