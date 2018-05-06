package com.polito.kotlin.RestTurism.resource

import org.springframework.web.bind.annotation.*
import com.polito.kotlin.RestTurism.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.jws.soap.SOAPBinding

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

    @CrossOrigin ( origins = ["http://localhost:63342"])
    @GetMapping("/login")
    fun login(@RequestParam(value = "mail") mail:String,
              @RequestParam(value = "pass") pass:String) =
            logUser(User(mail, "", pass))

    @CrossOrigin (origins = ["*"])
    @GetMapping("/images", produces = arrayOf(MediaType.IMAGE_JPEG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
    fun returnImage(@RequestParam(value = "mail") mail:String,
                    @RequestParam(value = "pass") pass:String,
                    @RequestParam(value = "image") image:String) : ByteArray
    {
        logUser(User(mail, "", pass))
        return imageBack(image).readBytes()
    }

    @CrossOrigin (origins = ["*"])
    @GetMapping("/road")
    fun onTheRoad(@RequestParam(value = "startingLat") sLat: Double,
                  @RequestParam(value = "startingLon") sLon: Double,
                  @RequestParam(value = "finalLat") fLat: Double,
                  @RequestParam(value = "finalLon") fLon: Double,
                  @RequestParam(value = "mail") mail: String,
                  @RequestParam(value = "pass") pass: String) : List<PointOfInterest>
    {
        logUser(User(mail, "", pass))
        return matchLine(sLat,sLon,fLat,fLon)
    }
}