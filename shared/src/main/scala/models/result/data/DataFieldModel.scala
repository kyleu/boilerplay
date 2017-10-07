package models.result.data

trait DataFieldModel {
  def toDataFields: Seq[DataField]
}
