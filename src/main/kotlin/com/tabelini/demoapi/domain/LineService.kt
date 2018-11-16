package com.tabelini.demoapi.domain

import com.tabelini.demoapi.repository.DelayRepository
import com.tabelini.demoapi.repository.LineRepository
import org.springframework.stereotype.Service

interface LineService{
    fun getById(id:Long): Line?
    fun getDelayById(id:Long): Long?
}

@Service
class LineServiceImpl(val lineRepository: LineRepository, val delayRepository: DelayRepository):LineService {
    override fun getById(id: Long): Line? {
        return lineRepository.getById(id)
    }

    override fun getDelayById(id: Long): Long? {
        lineRepository.getById(id)?.let {
            return delayRepository.getByName(it.name)
        }
    }
}
