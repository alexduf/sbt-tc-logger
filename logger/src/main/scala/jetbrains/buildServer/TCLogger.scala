/*
 * Copyright 2013-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */

package jetbrains.buildServer.sbtlogger

import sbt._


class TCLogger(ap: LogAppender, cd: String) extends BasicLogger {

  val appender: LogAppender = ap
  val scope: String = cd

  def logAll(events: Seq[LogEvent]) = {
    events.foreach(log)
  }

  def log(level: sbt.Level.Value, message: => String) {
      if (level==Level.Debug || level==Level.Info) {
        //we don't need to wrap debug and info messages, we will show them as is
        return
      }
      appender.log(level, message, scope)
  }

  def control(event: ControlEvent.Value, message: => String) {
      log(sbt.Level.Info, message)
  }

  def success(message: => String) {
    if(successEnabled) {
      log(sbt.Level.Info, message)
    }
  }

  def trace(t: => Throwable): Unit = {
    val traceLevel = getTrace
    if(traceLevel >= 0)
    		println(StackTrace.trimmed(t, traceLevel))
  }

}