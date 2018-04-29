package com.polito.kotlin.RestTurism.resource

import com.polito.kotlin.RestTurism.Place
import com.polito.kotlin.RestTurism.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/management")
class ManagementResource {

    @CrossOrigin ( origins = ["http://localhost:63342"])
    @PostMapping("/newPlace")
    fun newPlace(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "pass") pass: String) =
                 //@RequestBody(required = true) place: Place)
            logUser(User(mail, "", pass))

    @CrossOrigin ( origins = ["http://localhost:63342"])
    @GetMapping ("/login")
    fun login(@RequestParam(value = "mail") mail: String,
              @RequestParam(value = "pass") pass: String) =
            logManager(User(mail, "", pass))

    @CrossOrigin (origins = ["http://localhost:63342"])
    @PostMapping ("/register")
    fun register(@RequestParam(value = "mail") mail: String,
                 @RequestParam(value = "name") name: String,
                 @RequestParam(value = "pass") pass: String) =
            newManager(User(mail, name, pass))
}