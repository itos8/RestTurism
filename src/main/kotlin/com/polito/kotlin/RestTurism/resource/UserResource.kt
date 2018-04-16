package com.polito.kotlin.RestTurism.resource

import org.springframework.web.bind.annotation.*
import com.polito.kotlin.RestTurism.*

@RestController
@RequestMapping("/rest/users")
class UserResource {

    @PostMapping("/register")
    fun register(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "name") name: String,
                 @RequestParam(value = "pass") pass: String) =
            newUser(User(mail,name,pass))

    @GetMapping("/points")
    fun sendListPoints(@RequestParam(value = "latitude") lat: Double,
                       @RequestParam(value = "longitude") lon: Double) =
            matchPoints(lat, lon)

}