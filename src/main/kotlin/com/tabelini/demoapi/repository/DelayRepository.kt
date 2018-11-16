package com.tabelini.demoapi.repository

import com.tabelini.demoapi.domain.Line
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.FileReader
import javax.annotation.PostConstruct

interface DelayRepository {
    fun getByName(name: String): Long?
}

@Repository
class DelayRepositoryCSVImpl: DelayRepository {

    private val logger = KotlinLogging.logger {}

    @Value("\${data.path}")
    lateinit var dataPath:String

    @Value("\${data.files.delays}")
    lateinit var csvFile: String

    @Value("\${data.headers.line_name}")
    lateinit var lineNameHeader:String

    @Value("\${data.headers.delay}")
    lateinit var delayHeader:String

    val data = hashMapOf<String, Long>()

    @PostConstruct
    fun setup() {
        val csvFileReader = FileReader("$dataPath$csvFile")
        val csvRecords = CSVFormat.DEFAULT
                .withHeader(lineNameHeader, delayHeader)
                .withFirstRecordAsHeader()
                .parse(csvFileReader)
        for (record in csvRecords) {
            val name: String? = record.get(lineNameHeader)
            val delay = record.get(delayHeader).toLongOrNull()
            if (name == null || delay == null) {
                logger.warn { "Invalid record: {name: '$name', delay: '$delay' }" }
            } else {
                data[name] = delay
            }
        }
    }

    override fun getByName(name: String): Long? {
        return data[name]
    }
}
