package com.polito.kotlin.RestTurism

import com.mongodb.MongoClient
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.geojson.Point
import org.bson.codecs.pojo.PojoCodecProvider

import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries

private  val mon = MongoClient("localhost",27017)
private val db = mon.getDatabase("Cicero")
private var col = db.getCollection("Users", User::class.java)
private var manager = db.getCollection("Managers", User::class.java)
private var places = db.getCollection("Places", Place::class.java)
private val codec = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()))

fun mongoCodec()
{
    col = col.withCodecRegistry(codec)
    places = places.withCodecRegistry(codec)
    manager = manager.withCodecRegistry(codec)
}

//Registrazione di un nuovo utente
fun mongoReg(user: User): Boolean
{
    try {
        if (col.find(eq("mail", user.mail)).count() > 0) {
            return false
        }
        else
        {
            col.insertOne(user)
            return true
        }
    } catch (iae: IllegalArgumentException){
        return false
    }
}

fun mongoRegManager(user: User): Boolean
{
    try {
        if (manager.find(eq("mail", user.mail)).count() > 0) {
            return false
        }
        else
        {
            manager.insertOne(user)
            return true
        }
    } catch (iae: IllegalArgumentException){
        return false
    }
}

//Login di un utente registrato
fun mongoLog(user: User) : Boolean
{
    try {
        val log = col.find(eq("mail", user.mail)).first()
        if ( log != null )
        {
            return log.pass == user.pass
        }
        else
            return false
    }
    catch (iae: IllegalArgumentException)
    {
        return false
    }
}

fun mongoLogManager(user: User) : Boolean
{
    try {
        val log = manager.find(eq("mail", user.mail)).first()
        if ( log != null )
        {
            return log.pass == user.pass
        }
        else
            return false
    }
    catch (iae: IllegalArgumentException)
    {
        return false
    }
}

fun mongoPos(point: Point): List<Place>
{
    var list = mutableListOf<Place>()

    try {

        list.addAll(places.find(geoIntersects("area", point)))

        return list
    }
    catch (e: NullPointerException)
    {
        return listOf()
    }
}

fun mongoMan(manager:String): List<Place>
{
    var list = mutableListOf<Place>()

    try {
        list.addAll(places.find(eq("manager", manager)))

        return list
    }
    catch (e: NullPointerException)
    {
        return listOf()
    }
}