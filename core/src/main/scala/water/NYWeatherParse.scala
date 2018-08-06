package water

import org.joda.time.{DateTimeZone, MutableDateTime}


case class NYWeather(val Days: Option[Long],
                     val HourLocal: Option[Int],
                     val DewPoint: Option[Float],
                     val HumidityFraction: Option[Float],
                     val Prcp1Hour: Option[Float],
                     val Temperature: Option[Float],
                     val WeatherCode1: Option[String]) {
  def isWrongRow(): Boolean = (0 until productArity).map(idx => productElement(idx)).forall(e => e == None)
}

object NYWeatherParse extends Serializable {

  import water.support.ParseSupport._

  def apply(row: Array[String]): NYWeather = {
    val yearLocal = float(row(0))
    val monthLocal = float(row(1))
    val dayLocal = float(row(2))
    val hourLocal = float(row(3))
    val msec: Option[Long] = if (yearLocal.isDefined && monthLocal.isDefined && dayLocal.isDefined && hourLocal.isDefined) {
      Some(new MutableDateTime(yearLocal.get.toInt,
        monthLocal.get.toInt, dayLocal.get.toInt, hourLocal.get.toInt, 0, 0, 0, DateTimeZone.UTC).getMillis)
    } else {
      None
    }
    // Compute days since epoch
    val days = if (msec.isDefined) Some(msec.get / (1000 * 60 * 60 * 24)) else None

    NYWeather(
      days,
      if (hourLocal.isDefined) Some(hourLocal.get.toInt) else None,
      float(row(23)),
      float(row(24)),
      float(row(25)),
      float(row(30)),
      str(row(33))
    )
  }
}
