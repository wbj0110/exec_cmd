package com.soledede.exec


import java.util.concurrent.LinkedBlockingQueue

import com.soledede.exec.entity.{Msg, Progress}

import scala.collection.mutable.StringBuilder
import sys.process._

/**
  * Created by soledede on 2015/11/26.
  */
object ScalaProcess {
  val eventQueue = new LinkedBlockingQueue[Int](1)
  val cmd = "sh /data/shell/incIndex.sh"

  @volatile var execStatus: Int = -1
  @volatile var stdout = new String
  @volatile var stderr = new String
  val lock = new Object

  def exec(cmd: String): Msg = {
    stdout = ""
    stderr = ""
    execStatus = cmd ! ProcessLogger(stdout = _, stderr = _)
    val msg = new Msg(stdout.toString(), stderr.toString(), execStatus)
    stdout = ""
    stderr = ""
    execStatus = -1
    msg
  }

  def execAsyn(cmd: String) = {

    lock.synchronized {
      stdout = ""
      stderr = ""
      execStatus = cmd ! ProcessLogger(stdout = _, stderr = _)
      execStatus = -1
    }
  }

  private var thread = new Thread("exec Thread") {
    setDaemon(true)
    override def run() {
      while(true){
        eventQueue.take()
        execAsyn(cmd)
      }
    }
  }
  thread.start()
}
