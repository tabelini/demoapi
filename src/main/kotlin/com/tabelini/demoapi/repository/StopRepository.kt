package com.tabelini.demoapi.repository

import com.tabelini.demoapi.domain.Stop
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.FileReader
import javax.annotation.PostConstruct

interface StopRepository {
    fun getByCoord(x: Int, y: Int): Stop?
}

@Repository
class StopRepositoryCSVImpl : StopRepository {

    private val logger = KotlinLogging.logger {}

    @Value("\${data.path}")
    lateinit var dataPath: String

    @Value("\${data.files.stops}")
    lateinit var csvFile: String

    @Value("\${data.headers.stop_id}")
    lateinit var stopIdHeader: String

    @Value("\${data.headers.x_coord}")
    lateinit var xCoordHeader: String

    @Value("\${data.headers.y_coord}")
    lateinit var yCoordHeader: String

    val data = hashMapOf<Long, Stop>()

    @PostConstruct
    fun setup() {
        val csvFileReader = FileReader("$dataPath$csvFile")
        val csvRecords = CSVFormat.DEFAULT
                .withHeader(stopIdHeader, xCoordHeader, yCoordHeader)
                .withFirstRecordAsHeader()
                .parse(csvFileReader)
        for (record in csvRecords) {
            val stopId = record.get(stopIdHeader).toLongOrNull()
            val x = record.get(xCoordHeader).toIntOrNull()
            val y = record.get(yCoordHeader).toIntOrNull()
            if (stopId == null || x == null || y == null) {
                logger.warn { "Invalid record: {stopId: '$stopId', x: '$x', y: '$y' }" }
            } else {
                data[stopId] = Stop(stopId, x, y)
            }
        }
    }

    override fun getByCoord(x: Int, y: Int): Stop?{
        return data.values.find{ stop -> stop.x == x && stop.y == y }
    }
}
