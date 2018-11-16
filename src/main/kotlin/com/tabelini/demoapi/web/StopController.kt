package com.tabelini.demoapi.web

import com.tabelini.demoapi.domain.Arrival
import com.tabelini.demoapi.domain.StopService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

class NextArrivalRequest(val time: Int)

class LineByCoordAndTimeRequest(val x: Int, val y: Int, val time: Int)

@RestController
@Api(tags = ["Stops"], description = "Stops Information")
@RequestMapping("/stop")
class StopController(val stopService: StopService) {

    @ApiOperation(value = "Get the next arrival on a given stop and time",
            notes = "the time is expected to be informed on secs from 0:00:00")
    @GetMapping("/{stopId}/next_arrival")
    fun getNextArrival(@PathVariable stopId: Long, @RequestBody request: NextArrivalRequest): Arrival {
        return stopService.getNextArrivalByStopIdAndTime(stopId, request.time)
                ?: throw RestResourceNotFound("NEXT_ARRIVAL_NOT_FOUND",stopId.toString())
    }

    @ApiOperation(value = "Get the actual arrival(that contains the lineId on a given coord(x,y) and time",
            notes = "the time is expected to be informed on secs from 0:00:00")
    @GetMapping("/by_time_and_coord")
    fun getLineByCoordAndTime(@RequestBody request: LineByCoordAndTimeRequest): Arrival {
        return stopService.getByCoordAndTime(request.x, request.y, request.time)
        ?:throw RestResourceNotFound("LINE_BY_COORD_NOT_FOUND", "x:${request.x} y:${request.y}")
    }
}
