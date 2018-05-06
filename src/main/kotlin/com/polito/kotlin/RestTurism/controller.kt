package com.polito.kotlin.RestTurism

import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import com.mongodb.client.model.geojson.LineString
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

fun logManager(user: User) : List<PointOfInterest>
{
    if (mongoLogManager(user))
    {
        val list = mutableListOf<PointOfInterest>()
        for (place : Place in mongoMan(user.mail!!))
        {
            list.add(PointOfInterest(place.coordinates!!.position.values.get(0),
                                     place.coordinates!!.position.values.get(1),
                                     place.name!!,
                                     place.description!!,
                                     place.image!!))
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