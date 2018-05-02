package com.polito.kotlin.RestTurism

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Polygon
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.ByteArrayOutputStream

@ResponseStatus(HttpStatus.CONFLICT)
class UserAlreadyRegisteredException(override var message:String) : Exception(message)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UserNotFoundException(override var message:String) : Exception(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class NoImageFoundException(override var message:String) : Exception(message)

@ResponseBody
data class image(val jpeg : ByteArrayOutputStream)

data class PointOfInterest(val lat: Double, val lon: Double, val name: String, val description: String)

class User {

    @SerializedName("mail")
    @Expose
    var mail: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("pass")
    @Expose
    var pass: String? = null

    constructor() {}

    constructor(mail: String, name: String, pass: String) : super() {
        this.mail = mail
        this.name = name
        this.pass = pass
    }

}

class Place //(val coordinates: Point, val name: String, val description: String, val area: Polygon)
{
    @SerializedName("coordinates")
    @Expose
    var coordinates: Point? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("area")
    @Expose
    var area: Polygon? = null

    @SerializedName("manager")
    @Expose
    var manager: String? = null

    constructor() {}

    constructor(coordinates: Point, name: String, description: String, area: Polygon, manager: String)
    {
        this.coordinates = coordinates
        this.name = name
        this.description = description
        this.area = area
        this.manager = manager
    }
}