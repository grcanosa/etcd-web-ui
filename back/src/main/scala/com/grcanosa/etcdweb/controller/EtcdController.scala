package com.grcanosa.etcdweb.controller

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.grcanosa.etcdweb.model.{JsonConverter, KeyValue}
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

trait EtcdController  extends Directives with JsonConverter with LazyLogging{


  val INTERNAL_SERVER_ERROR = (e: Throwable) => s"Internal error ${e.toString}"
  val KEY_NOT_FOUND_ERROR = (key: String) => s"Key $key not found"



  def etcdActor: ActorRef


  val keyvalue = pathPrefix("keyvalue"){
    path(Remaining) { key =>
      concat(
        get {
          logger.info(s"Requesting $key")
          onComplete(etcdActor ? GetEntry(key)){
            case Success(v) => v match {
              case Some(kv: KeyValue) => complete(kv)
              case None => complete(StatusCodes.NotFound, KEY_NOT_FOUND_ERROR(key))
            }
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        },
        post{
          decodeRequest {
            entity(as[KeyValue]) { keyValue =>

            }
          }
        }
        delete {
          onComplete(configurationControllerActor ? DeleteEntry(key)){
            case Success(v) => v match {
              case 0 => complete(StatusCodes.NotFound, KEY_NOT_FOUND_PROBLEM(key))
              case _ => complete(StatusCodes.OK)
            }
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        }
      )
    }
  }




  val entryRoute: Route = pathPrefix("Entry") {
    path(Remaining) { subpath =>
      val key = "/"+subpath
      concat(
        get {
          logger.info(s"Requesting $key")
          onComplete(configurationControllerActor ? GetEntry(key)){
            case Success(v) => v match {
              case Some(kv: KeyValueEntry) => complete(kv)
              case None => complete(StatusCodes.NotFound, KEY_NOT_FOUND_PROBLEM(key))
            }
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        },
        delete {
          onComplete(configurationControllerActor ? DeleteEntry(key)){
            case Success(v) => v match {
              case 0 => complete(StatusCodes.NotFound, KEY_NOT_FOUND_PROBLEM(key))
              case _ => complete(StatusCodes.OK)
            }
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        }
      )
    }
  }

  val valueRoute: Route = pathPrefix("Value"){
    path(Remaining){ subpath =>
      val key = "/"+subpath
      get {
        onComplete(configurationControllerActor ? GetEntry(key)){
          case Success(v) => v match {
            case Some(kv: KeyValueEntry) => complete(kv.value)
            case None => complete(StatusCodes.NotFound, KEY_NOT_FOUND_PROBLEM(key))
          }
          case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
        }
      }
    }
  }

  val setEntryRoute: Route = path("SetEntry") {
    post {
      decodeRequest {
        entity(as[KeyValueEntry]) { keyValue =>
          onComplete(configurationControllerActor ? SetEntry(keyValue)){
            case Success(_) => complete(StatusCodes.OK,"OK")
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        }
      }
    }
  }

  val entriesRoute: Route = pathPrefix("Entries") {
    path(Remaining) { subpath =>
      val wildcard = "/"+subpath
      concat (
        get {
          onComplete(configurationControllerActor ? GetEntries(wildcard)){
            case Success(v) => v match {
              case kvList: Seq[KeyValueEntry] => complete(kvList)
              case problem: ProblemDetails => complete(StatusCodes.NotFound, problem)
            }
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        },
        delete {
          onComplete(configurationControllerActor ? DeleteEntries(wildcard)){
            case Success(v) => complete(StatusCodes.OK,s"Removed $v entries")
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        }
      )
    }
  }

  val copyEntriesRoute: Route = path("CopyEntries") {
    post {
      decodeRequest{
        entity(as[CopyDirectories]){ copyDirectoriesDto =>
          onComplete(configurationControllerActor ? CopyDirectoriesKeys(copyDirectoriesDto)){
            case Success((n1,n2)) if n1 == n2 => complete(StatusCodes.OK,s"Copied $n1 keys")
            case Success((n1,n2)) => complete(StatusCodes.NotFound,SET_KEY_ERROR_PROBLEM(s"$n2 keys setted instead of $n1"))
            case Failure(e) => complete(StatusCodes.InternalServerError,INTERNAL_ERROR_PROBLEM(e))
          }
        }
      }
    }
  }


  //
  val setEntries: Route = path("SetEntries")  {
    post {
      decodeRequest {
        entity(as[Seq[KeyValueEntry]]) { keyValues =>
          onComplete(configurationControllerActor ? SetEntries(keyValues)){
            case Success(n) if n == keyValues.size => complete(StatusCodes.OK, s"Set $n keys correctly")
            case Success(n) => complete(StatusCodes.NotFound, SET_KEY_ERROR_PROBLEM(s"$n keys setted instead of ${keyValues.size}"))
            case Failure(e) => complete(StatusCodes.InternalServerError, INTERNAL_ERROR_PROBLEM(e))
          }
        }
      }
    }
  }


  val ALLROUTES: Route = pathPrefix("Configuration") {
    concat(entryRoute, valueRoute, setEntries, setEntryRoute, entriesRoute, copyEntriesRoute)
  }


}
