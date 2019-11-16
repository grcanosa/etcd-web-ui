package com.grcanosa.etcdweb.model

trait JsonConverter {
  import spray.json._
  import spray.json.DefaultJsonProtocol._

  val keyValueConverter = jsonFormat2(KeyValue)

}
