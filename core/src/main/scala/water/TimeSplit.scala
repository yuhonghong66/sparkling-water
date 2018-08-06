package water

import org.apache.spark.h2o.H2OFrame
import water.fvec.{Chunk, NewChunk, Vec}

class TimeSplit extends MRTask[TimeSplit] {
  def doIt(time: H2OFrame): H2OFrame =
    new H2OFrame(doAll(Array(Vec.T_NUM), time).outputFrame(Array[String]("Days"), null))

  override def map(msec: Chunk, day: NewChunk): Unit = {
    for (i <- 0 until msec.len) {
      day.addNum(msec.at8(i) / (1000 * 60 * 60 * 24)); // Days since the Epoch
    }
  }
}
