package com.tabelini.demoapi.repository

import com.tabelini.demoapi.domain.Arrival
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.FileReader
import javax.annotation.PostConstruct

interface TimeRepository {
    companion object {
        private val TIME_REGEX = Regex("""^(\d{1,2}):(\d{2}):(\d{2}).*${'$'}""")
        fun timeToSeconds(timeStr: String): Int? {
            return TIME_REGEX.find(timeStr)?.let {
                val hours = it.groups[1]?.value?.toIntOrNull()
                val minutes = it.groups[2]?.value?.toIntOrNull()
                val seconds = it.groups[3]?.value?.toIntOrNull()
                return if (hours != null && minutes != null && seconds != null) {
                    hours * 3600 + minutes * 60 + seconds
                } else null
            }
        }
    }

    fun getNextArrivalByStopIdAndTime(stopId: Long, time: Int): Arrival?

    fun getActualArrival(stopId: Long, time: Int): Arrival?
}

@Repository
class TimeRepositoryImpl : TimeRepository {

    private val logger = KotlinLogging.logger {}

    @Value("\${data.path}")
    lateinit var dataPath: String

    @Value("\${data.files.times}")
    lateinit var csvFile: String

    @Value("\${data.headers.line_id}")
    lateinit var lineIdHeader: String

    @Value("\${data.headers.stop_id}")
    lateinit var stopIdHeader: String

    @Value("\${data.headers.time}")
    lateinit var timeHeader: String

    val data = hashMapOf<Long, Array<Arrival>>()

    @PostConstruct
    fun setup() {
        val tempData = hashMapOf<Long, ArrayList<Arrival>>()
        val csvFileReader = FileReader("$dataPath$csvFile")
        val csvRecords = CSVFormat.DEFAULT
                .withHeader(lineIdHeader, stopIdHeader, timeHeader)
                .withFirstRecordAsHeader()
                .parse(csvFileReader)
        for (record in csvRecords) {
            val lineId = record.get(lineIdHeader).toLongOrNull()
            val stopId = record.get(stopIdHeader).toLongOrNull()
            val timeStr: String? = record.get(timeHeader)
            val seconds = timeStr?.let { TimeRepository.timeToSeconds(timeStr) }
            if (lineId == null || stopId == null || seconds == null) {
                logger.warn { "Invalid record: {lineId: '$lineId', stopId: '$stopId', timeStr: '$timeStr' }" }
            } else {
                tempData.getOrPut(stopId){ arrayListOf()}
                tempData[stopId]?.add(Arrival(lineId, stopId, seconds))
            }
        }
        for (stopData in tempData) {
            stopData.value.sortBy { it.time }
            data[stopData.key] = stopData.value.toTypedArray()
        }
    }

    override fun getNextArrivalByStopIdAndTime(stopId: Long, time: Int): Arrival? {
        return data[stopId]?.first { arrival -> arrival.time >= time }
    }

    override fun getActualArrival(stopId: Long, time: Int): Arrival? {
        return data[stopId]?.first { arrival -> arrival.time == time }
    }

}
