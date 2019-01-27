<!-- Generated File -->
# audit

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| id                           | uuid               | true   | true   | true     |
| act                          | string             | true   | false  | true     |
| app                          | string             | true   | false  | true     |
| client                       | string             | true   | false  | true     |
| server                       | string             | true   | false  | true     |
| user_id                      | uuid               | true   | false  | true     |
| tags                         | json               | true   | false  | false    |
| msg                          | string             | true   | false  | false    |
| started                      | timestamp          | true   | false  | false    |
| completed                    | timestamp          | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| auditRecordAuditIdFkey       | id                 | [audit_record](DatabaseTableAuditRecordRow)| audit_id
