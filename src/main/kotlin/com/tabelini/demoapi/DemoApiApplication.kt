package com.tabelini.demoapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.tabelini.demoapi.web", "com.tabelini.demoapi.repository",
    "com.tabelini.demoapi.domain"])
class DemoApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoApiApplication::class.java, *args)
}
