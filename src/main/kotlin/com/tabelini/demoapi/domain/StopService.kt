package com.tabelini.demoapi.domain

import com.tabelini.demoapi.repository.StopRepository
import com.tabelini.demoapi.repository.TimeRepository
import org.springframework.stereotype.Service

interface StopService {
    fun getNextArrivalByStopIdAndTime(stopId: Long, time: Int): Arrival?
    fun getByCoordAndTime(x:Int, y:Int, time: Int): Arrival?
}

@Service
class StopServiceImpl(val timeRepository: TimeRepository, val stopRepository: StopRepository) : StopService {
    override fun getNextArrivalByStopIdAndTime(stopId: Long, time: Int): Arrival? {
        return timeRepository.getNextArrivalByStopIdAndTime(stopId, time)
    }

    override fun getByCoordAndTime(x:Int, y:Int, time: Int): Arrival?{
        println("Stop on coordinates: $stopRepository")
        return stopRepository.getByCoord(x,y)?.let { timeRepository.getActualArrival(it.id, time) }
    }
}
