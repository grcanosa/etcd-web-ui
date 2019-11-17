package com.grcanosa.etcdweb.model

case class EtcdWebProfile(id: Int
                          ,label: String
                          , server: String
                          , port: Int
                          , credentials: Option[EtcdCredentials]
                          , baseKey : String
                          , separator: Char)

