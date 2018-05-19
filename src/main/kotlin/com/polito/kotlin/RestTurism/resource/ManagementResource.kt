package com.polito.kotlin.RestTurism.resource

import com.polito.kotlin.RestTurism.Place
import com.polito.kotlin.RestTurism.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/management")
class ManagementResource {

    @CrossOrigin ( origins = ["*"])
    @PostMapping("/newPlace", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun newPlace(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "pass") pass: String,
                 @RequestBody(required = true) place: PointOfInterest) : PointOfInterest
            {
                logManager(User(mail, "", pass))
                return addPlace(mail, place)
            }

    @CrossOrigin ( origins = ["*"])
    @GetMapping ("/login")
    fun login(@RequestParam(value = "mail") mail: String,
              @RequestParam(value = "pass") pass: String) =
            logManager(User(mail, "", pass))

    @CrossOrigin (origins = ["*"])
    @PostMapping ("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "name") name: String,
                 @RequestParam(value = "pass") pass: String) =
            newManager(User(mail, name, pass))
}