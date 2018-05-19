package com.polito.kotlin.RestTurism

import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import com.mongodb.client.model.geojson.LineString
import com.mongodb.client.model.geojson.Polygon
import java.io.*
import javax.mail.internet.InternetAddress

//Funzione di registrazione di un nuovo utente
fun newUser(user: User)
{
    val mail = InternetAddress(user.mail)
    mail.validate()

    if (mongoReg(user))
        return
    else
        throw UserAlreadyRegisteredException("User already registered")
}

fun newManager(user: User)
{
    val mail = InternetAddress(user.mail)
    mail.validate()

    if (mongoRegManager(user))
        return
    else
        throw UserAlreadyRegisteredException("User already registered")
}

fun logUser(user: User)
{
    if (mongoLog(user))
        return
    else
        throw UserNotFoundException("User not registered")
}

fun logManager(user: User) : List<com.polito.kotlin.RestTurism.Point>
{
    if (mongoLogManager(user))
    {
        val list = mutableListOf<com.polito.kotlin.RestTurism.Point>()
        for (place : Place in mongoMan(user.mail!!))
        {
            list.add(makePoint(place))
        }
        return list
    }
    else
        throw UserNotFoundException("User not registered")
}

fun matchPoints (lat : Double, lon: Double): List<PointOfInterest>
{
    var list = mutableListOf<PointOfInterest>()

    for (place: Place in mongoPos(Point(Position(lat, lon))))
    {
        try {
            list.add(PointOfInterest(place.coordinates!!.coordinates.values[0],
                                     place.coordinates!!.coordinates.values[1],
                                     place.name!!,
                                     place.description!!,
                                     place.image!!))
        }
        catch (npe: NullPointerException)
        {
            continue
        }
    }
    return list
}

fun imageBack(name:String) : ByteArrayInputStream
{
    try {
        val bytes = File("./images/$name").readBytes()
        val stream = bytes.inputStream()
        return stream
    }
    catch (fnfe: FileNotFoundException)
    {
        throw NoImageFoundException("No Image Found")
    }
}

fun matchLine(sLat: Double, sLon: Double, fLat: Double, fLon: Double): List<PointOfInterest>
{
    var list = mutableListOf<PointOfInterest>()
    var line = listOf<Position>(Position(sLat,sLon), Position(fLat,fLon))
    for (place: Place in mongoRoad(LineString(line)))
    {
        try {
            list.add(PointOfInterest(place.coordinates!!.coordinates.values[0],
                    place.coordinates!!.coordinates.values[1],
                    place.name!!,
                    place.description!!,
                    place.image!!))
        }
        catch (npe: NullPointerException)
        {
            continue
        }
    }
    return list
}

fun addPlace(mail:String, poi: PointOfInterest): com.polito.kotlin.RestTurism.Point
{
    var polygon = Polygon(listOf(Position(poi.lat+0.001, poi.lon+0.001),
                                 Position(poi.lat+0.001, poi.lon-0.001),
                                 Position(poi.lat-0.001, poi.lon-0.001),
                                 Position(poi.lat-0.001, poi.lon+0.001),
                                 Position(poi.lat+0.001, poi.lon+0.001)))
    val place = Place(Point(Position(poi.lat, poi.lon)), poi.name, poi.description, polygon, mail, poi.image)

    val point = makePoint(place)

    if (mongoNewPlace(place))
        return point
    else
        throw InternalError()
}

fun makePoint(place: Place) : com.polito.kotlin.RestTurism.Point
{
    val area =  mutableListOf<com.polito.kotlin.RestTurism.Position>()

    for (pos : Position in place.area!!.coordinates.exterior.toTypedArray())
        area.add(com.polito.kotlin.RestTurism.Position(pos.values[0],pos.values[1]))

    return com.polito.kotlin.RestTurism.Point(com.polito.kotlin.RestTurism.Position(place.coordinates!!.position.values.get(0),place.coordinates!!.position.values.get(1)),
            place.name!!,
            place.description!!,
            area.toTypedArray(),
            place.image!!)
}