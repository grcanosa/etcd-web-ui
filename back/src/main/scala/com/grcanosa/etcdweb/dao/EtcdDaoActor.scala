package com.grcanosa.etcdweb.dao


import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import com.grcanosa.etcdweb.model.{EtcdCredentials, EtcdWebProfile}

object EtcdDaoActor{

  sealed trait EtcdActorRequest
  case class GetKey(key: String, profile: EtcdWebProfile) extends EtcdActorRequest
  case class SetKey(key: String, value: String, profile: EtcdWebProfile)

  sealed trait EtcdActorResponse

  def apply(): Behavior[EtcdActorRequest] = Behaviors.receive{ (ctx, msg) =>
    msg match {
      case GetKey(key, profile) =>
      case SetKey(key, value, profile) =>
    }
    Behaviors.same
  }


  def apply2(): Behavior[EtcdActorRequest] = Behaviors.setup{ ctx =>


    var etcd = 4



    Behaviors.receiveMessage{
      case GetKey(profile: EtcdWebProfile, key: String) =>

      Behaviors.same
    }
  }
}

class EtcdDaoActor {



}
