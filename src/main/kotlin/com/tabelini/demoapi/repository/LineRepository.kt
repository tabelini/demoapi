package com.tabelini.demoapi.repository

import com.tabelini.demoapi.domain.Line
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.FileReader
import javax.annotation.PostConstruct

interface LineRepository {
    fun getById(id: Long): Line?
}

@Repository
class LineRepositoryCSVImpl : LineRepository {

    private val logger = KotlinLogging.logger {}

    @Value("\${data.path}")
    lateinit var dataPath:String

    @Value("\${data.files.lines}")
    lateinit var csvFile: String

    @Value("\${data.headers.line_id}")
    lateinit var lineIdHeader:String

    @Value("\${data.headers.line_name}")
    lateinit var lineNameHeader:String

    val data = hashMapOf<Long, Line>()

    @PostConstruct
    fun setup() {
        val csvFileReader = FileReader("$dataPath$csvFile")
        val csvRecords = CSVFormat.DEFAULT
                .withHeader(lineIdHeader, lineNameHeader)
                .withFirstRecordAsHeader()
                .parse(csvFileReader)
        for (record in csvRecords) {
            val id = record.get(lineIdHeader).toLongOrNull()
            val name: String? = record.get(lineNameHeader)
            if (id == null || name == null) {
                logger.warn { "Invalid record: {id: '$id', name: '$name' }" }
            } else {
                data[id] = Line(id, name)
            }
        }
    }

    override fun getById(id: Long): Line? {
        return data[id]
    }
}
