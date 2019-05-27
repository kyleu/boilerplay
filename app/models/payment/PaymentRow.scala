/* Generated File */
package models.payment

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object PaymentRow {
  implicit val jsonEncoder: Encoder[PaymentRow] = (r: PaymentRow) => io.circe.Json.obj(
    ("paymentId", r.paymentId.asJson),
    ("customerId", r.customerId.asJson),
    ("staffId", r.staffId.asJson),
    ("rentalId", r.rentalId.asJson),
    ("amount", r.amount.asJson),
    ("paymentDate", r.paymentDate.asJson)
  )

  implicit val jsonDecoder: Decoder[PaymentRow] = (c: io.circe.HCursor) => for {
    paymentId <- c.downField("paymentId").as[Long]
    customerId <- c.downField("customerId").as[Int]
    staffId <- c.downField("staffId").as[Int]
    rentalId <- c.downField("rentalId").as[Long]
    amount <- c.downField("amount").as[BigDecimal]
    paymentDate <- c.downField("paymentDate").as[ZonedDateTime]
  } yield PaymentRow(paymentId, customerId, staffId, rentalId, amount, paymentDate)

  def empty(
    paymentId: Long = 0L,
    customerId: Int = 0,
    staffId: Int = 0,
    rentalId: Long = 0L,
    amount: BigDecimal = BigDecimal(0),
    paymentDate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    PaymentRow(paymentId, customerId, staffId, rentalId, amount, paymentDate)
  }
}

final case class PaymentRow(
    paymentId: Long,
    customerId: Int,
    staffId: Int,
    rentalId: Long,
    amount: BigDecimal,
    paymentDate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("paymentId", Some(paymentId.toString)),
    DataField("customerId", Some(customerId.toString)),
    DataField("staffId", Some(staffId.toString)),
    DataField("rentalId", Some(rentalId.toString)),
    DataField("amount", Some(amount.toString)),
    DataField("paymentDate", Some(paymentDate.toString))
  )

  def toSummary = DataSummary(model = "paymentRow", pk = paymentId.toString, title = s"customerId: $customerId, staffId: $staffId")
}
