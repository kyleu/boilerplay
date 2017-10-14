package controllers.admin.audit

object AuditJson {
  val startJson = """{
    |  "action": "insert",
    |  "app": "boilerplay",
    |  "client": "127.0.0.1",
    |  "server": "localhost",
    |  "user": 1,
    |  "company": 15,
    |  "models": [
    |    { "t": "ad", "pk": ["123"] },
    |    { "t": "company", "pk": ["500"] }
    |  ],
    |  "tags": {
    |    "x": "y",
    |    "foo": "bar"
    |  }
    |}""".stripMargin

  val completeJson = """{
    |  "id": "???",
    |  "msg": "...",
    |  "tags": {
    |    "x": "z",
    |    "key": "val"
    |  },
    |  "inserted": [
    |    { "t": "ad", "pk": ["1"] }
    |  ]
    |}""".stripMargin
}
