package com.grcanosa.etcdweb.test

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Props, SpawnProtocol}
import akka.util.Timeout
import com.grcanosa.etcdweb.test.Buncher.Batch

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object MainTest extends App{

  import akka.actor.typed.scaladsl.AskPattern._
  val system: ActorSystem[SpawnProtocol.Command] = ActorSystem(Buncher.main(),"main")
  implicit val ec: ExecutionContext = system.executionContext
  implicit val timeout = Timeout(3 seconds)
//
  val futBatch: Future[ActorRef[Batch]] = system.ask(
    SpawnProtocol.Spawn(Buncher.batch()
    ,"batch"
    ,Props.empty
    ,_))






}
