package com.tabelini.demoapi.web

import com.tabelini.demoapi.domain.LineService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Lines"], description = "Lines Information")
@RequestMapping("/line")
class LinesController(val lineService: LineService) {

    /**
     * Seems very reasonable to use the ids instead of the name,
     * even if the delays are by name, I always prefer to use ids on the rest api
     * one thing to notice is that if in the foreseeable future exists the possibility
     * of using an NoSQL database would be ideal to take and return the id as a string
     * because it enables the possibility of using UUIDs for example, because an
     * distributed counter for unique long ids is not normally supported by NoSQL databases
     */
    @ApiOperation(value = "Get the expected delay by the line id",
            notes = "the delay is given in seconds")
    @GetMapping("/{lineId}/delay")
    fun getLineDelay(@PathVariable lineId:Long): Map<Long, Long> {
        return lineService.getDelayById(lineId)?.let {
            mapOf(lineId to it)
        }?: throw RestResourceNotFound("NO_DELAY_FOUND",lineId.toString())
    }

}
