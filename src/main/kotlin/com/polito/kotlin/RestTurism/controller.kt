package com.polito.kotlin.RestTurism

import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
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

fun logManager(user: User) : List<String>
{
    if (mongoLogManager(user))
    {
        val list = mutableListOf<String>()
        for (place : Place in mongoMan(user.mail!!))
        {
            list.add(place.name!!);
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
                                     place.description!!))
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
        //val outStream = ByteArrayOutputStream()
        //IOUtils.copy(stream, outStream)
        return stream
    }
    catch (fnfe: FileNotFoundException)
    {
        throw NoImageFoundException("No Image Found")
    }
}