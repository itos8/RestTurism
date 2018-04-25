package com.polito.kotlin.RestTurism

import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import javax.mail.internet.AddressException
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

fun logUser(user: User)
{
    if (mongoLog(user))
        return
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