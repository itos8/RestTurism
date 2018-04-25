package com.polito.kotlin.RestTurism.resource

import org.springframework.web.bind.annotation.*
import com.polito.kotlin.RestTurism.*
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/rest/users")
class UserResource {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "name") name: String,
                 @RequestParam(value = "pass") pass: String) =
            newUser(User(mail,name,pass))

    @GetMapping("/points")
    fun sendListPoints(@RequestParam(value = "latitude") lat: Double,
                       @RequestParam(value = "longitude") lon: Double,
                       @RequestParam(value = "mail") mail:String,
                       @RequestParam(value = "pass") pass:String) : List<PointOfInterest>
    {
        logUser(User(mail, "", pass))
        return matchPoints(lat, lon)
    }

    @GetMapping("/login")
    fun login(@RequestParam(value = "mail") mail:String,
              @RequestParam(value = "pass") pass:String) =
            logUser(User(mail, "", pass))
}