/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package water

import org.apache.spark.h2o.H2OFrame
import org.joda.time.MutableDateTime
import water.fvec.{Chunk, NewChunk, Vec}

class TimeTransform extends MRTask[TimeSplit] {
  def doIt(days: H2OFrame): H2OFrame =
    new H2OFrame(doAll(Array(Vec.T_NUM, Vec.T_NUM), days).outputFrame(Array[String]("Month", "DayOfWeek"), null))

  override def map(in: Array[Chunk], out: Array[NewChunk]): Unit = {
    val days = in(0)
    val month = out(0)
    val dayOfWeek = out(1)
    val mdt = new MutableDateTime()
    for (i <- 0 until days.len) {
      val msec = days.at8(i) * (1000L * 60 * 60 * 24)
      mdt.setMillis(msec)
      month.addNum(mdt.getMonthOfYear - 1)
      dayOfWeek.addNum(mdt.getDayOfWeek - 1)
    }
  }
}
