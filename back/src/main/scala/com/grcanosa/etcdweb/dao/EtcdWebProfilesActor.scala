package com.grcanosa.etcdweb.dao

import akka.actor.typed.scaladsl.Behaviors
import com.grcanosa.etcdweb.model.EtcdWebProfile

object EtcdWebProfilesActor {

  def apply() = Behaviors.setup{ ctx =>

    val profiles = List.empty[EtcdWebProfile]


  }

}
