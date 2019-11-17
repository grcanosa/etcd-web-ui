package com.grcanosa.etcdweb.test

import akka.actor.typed.{ActorRef, Behavior, SpawnProtocol}
import akka.actor.typed.scaladsl.{Behaviors, TimerScheduler}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
object Buncher {
  trait Msg
  final case class Batch(messages: Vector[Msg])

  private case object TimerKey
  private case object Timeout extends Msg


  def main(): Behavior[SpawnProtocol.Command] = Behaviors.setup{ ctx =>
    SpawnProtocol()
  }

//  val batch = ctx.spawn(batch(),"batch")
//  val buncher = ctx.spawn(behavior(batch,15 seconds, 10),"buncher")
//  Behaviors.empty

  def batch(): Behavior[Batch] = Behaviors.receive{ (ctx,msg) =>
    println(s"Receive $msg")
    Behaviors.same
  }


  def behavior(target: ActorRef[Batch], after: FiniteDuration, maxSize: Int): Behavior[Msg] =
    Behaviors.withTimers(timers => idle(timers, target, after, maxSize))

  private def idle(timers: TimerScheduler[Msg], target: ActorRef[Batch],
                   after: FiniteDuration, maxSize: Int): Behavior[Msg] = {
    Behaviors.receive { (ctx, msg) =>
      timers.startSingleTimer(TimerKey, Timeout, after)
      active(Vector(msg), timers, target, after, maxSize)
    }
  }

  private def active(buffer: Vector[Msg], timers: TimerScheduler[Msg],
                     target: ActorRef[Batch], after: FiniteDuration, maxSize: Int): Behavior[Msg] = {
    Behaviors.receive { (ctx, msg) =>
      msg match {
        case Timeout =>
          target ! Batch(buffer)
          idle(timers, target, after, maxSize)
        case msg =>
          val newBuffer = buffer :+ msg
          if (newBuffer.size == maxSize) {
            timers.cancel(TimerKey)
            target ! Batch(newBuffer)
            idle(timers, target, after, maxSize)
          } else
            active(newBuffer, timers, target, after, maxSize)
      }
    }
  }
}
