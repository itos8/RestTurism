package com.polito.kotlin.RestTurism.resource

import com.polito.kotlin.RestTurism.Place
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/management")
class ManagementResource {

    @PostMapping("/newPlace")
    fun newPlace(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "pass") pass: String,
                 @RequestBody(required = true) place: Place)
    {

    }
}