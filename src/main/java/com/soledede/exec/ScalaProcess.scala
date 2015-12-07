package com.soledede.exec


import java.util.concurrent.LinkedBlockingQueue

import com.soledede.exec.entity.{Msg, Progress}

import scala.collection.mutable
import scala.collection.mutable.StringBuilder
import sys.process._

/**
  * Created by soledede on 2015/11/26.
  */
object ScalaProcess {
  val eventQueue = new LinkedBlockingQueue[String](10000)

  @volatile var execStatus: Int = -1
  @volatile var stdout = new scala.StringBuilder()
  @volatile var stderr = new scala.StringBuilder()
  val lock = new Object

  def exec(cmd: String): Msg = {
    stdout = new scala.StringBuilder()
    stderr = new scala.StringBuilder()
    execStatus = cmd ! ProcessLogger(stdout append _, stderr append _)
    val msg = new Msg(stdout.toString(), stderr.toString(), execStatus)
    execStatus = -1
    msg
  }

  def execMkdir(dir: String): Msg = {
    stdout = new scala.StringBuilder()
    stderr = new scala.StringBuilder()
    //mkdir "mkdir -p /data/ftp/solr/searchcloud/xml/zhong/"!
    s"mkdir -p /data/ftp/solr/searchcloud/xml/${dir}/" ! ProcessLogger(stdout append _, stderr append _)
    val msg = new Msg(stdout.toString(), stderr.toString(), execStatus)
    execStatus = -1
    msg
  }

  def execAsyn(newDir: String) = {
    stdout = new scala.StringBuilder()
    stderr = new scala.StringBuilder()
    lock.synchronized {
      //println(newDir)
      //"echo -e  /usr/local/solr/solr/bin/post -p 10032 -c searchcloud /data/ftp/solr/searchcloud/xml/zhong/*.XML" #>> new java.io.File("/data/ftp/solr/searchcloud/xml/zhong_tmp.sh")!
      s"echo -e  /usr/local/solr/solr/bin/post -p 10032 -c searchcloud /data/ftp/solr/searchcloud/xml/${newDir}/*.XML" #>> new java.io.File(s"/data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh") ! ProcessLogger(stdout append _, stderr append _)
      //"echo -e mv /data/ftp/solr/searchcloud/xml/zhong/*.XML /data/ftp/solr/searchcloud/backup/" #>> new java.io.File("/data/ftp/solr/searchcloud/xml/zhong_tmp.sh")!
      s"echo -e mv /data/ftp/solr/searchcloud/xml/${newDir}/*.XML /data/ftp/solr/searchcloud/backup/" #>> new java.io.File(s"/data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh") ! ProcessLogger(stdout append _, stderr append _)
      //"echo -e rm -rf /data/ftp/solr/searchcloud/xml/zhong/" #>> new java.io.File("/data/ftp/solr/searchcloud/xml/zhong_tmp.txt")!
      s"echo -e rm -rf /data/ftp/solr/searchcloud/xml/${newDir}" #>> new java.io.File(s"/data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh") ! ProcessLogger(stdout append _, stderr append _)
      s"echo -e rm -rf /data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh" #>> new java.io.File(s"/data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh") ! ProcessLogger(stdout append _, stderr append _)
      // "chmod 777 /data/ftp/solr/searchcloud/xml/zhong_tmp.txt"!
      s"chmod 777 /data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh" ! ProcessLogger(stdout append _, stderr append _)
      //"sh /data/ftp/solr/searchcloud/xml/zhong_tmp.txt"!
      s"sh /data/ftp/solr/searchcloud/xml/${newDir}_tmp.sh" ! ProcessLogger(stdout append _, stderr append _)
      execStatus = -1
    }
  }

  private var thread = new Thread("exec Thread") {
    setDaemon(true)

    override def run() {
      while (true) {
        val dirName = eventQueue.take()
        execAsyn(dirName)
      }
    }
  }
  thread.start()

  def main(args: Array[String]) {
    execAsyn("zhong")
  }
}
