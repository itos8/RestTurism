package com.polito.kotlin.RestTurism

import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import com.mongodb.client.model.geojson.LineString
import com.mongodb.client.model.geojson.Polygon
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.*
import javax.mail.internet.InternetAddress
import java.util.Random



//Funzione di registrazione di un nuovo utente
fun newUser(user: User)
{
    println("Registered user: ${user.mail}")
    val mail = InternetAddress(user.mail)
    mail.validate()

    if (mongoReg(user))
        return
    else
        throw UserAlreadyRegisteredException("User already registered")
}

fun newManager(user: User)
{
    println("Registered manager: ${user.mail}")
    val mail = InternetAddress(user.mail)
    mail.validate()

    if (mongoRegManager(user))
        return
    else
        throw UserAlreadyRegisteredException("User already registered")
}

fun logUser(user: User)
{
    println("Logged user: ${user.mail}")
    if (mongoLog(user))
        return
    else
        throw UserNotFoundException("User not registered")
}

fun logManager(user: User) : List<com.polito.kotlin.RestTurism.Point>
{
    println("Logged Manager: ${user.mail}")
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
    println("Position received: $lat , $lon")
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

fun addPlace(mail:String, poi: com.polito.kotlin.RestTurism.Point): com.polito.kotlin.RestTurism.Point
{
    val file = Paths.get(poi.image)

    var pol = mutableListOf<Position>()

    for (elem: com.polito.kotlin.RestTurism.Position in poi.polygon)
        pol.add(Position(elem.lat, elem.lon))

    println("New place: ${poi}")

    val place = Place(Point(Position(poi.position.lat, poi.position.lon)), poi.name, poi.description, Polygon(pol), mail, file.fileName.toString())

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

fun imageSave(image: ByteArray) : String
{
    val generatedString = generateRandom("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")

    var file = FileOutputStream(File("./images/$generatedString.jpg"))

    file.write(image)

    return generatedString+".jpg"
}

private fun generateRandom(aToZ: String): String {
    val rand = Random()
    val res = StringBuilder()
    for (i in 0..8) {
        val randIndex = rand.nextInt(aToZ.length)
        res.append(aToZ[randIndex])
    }
    return res.toString()
}