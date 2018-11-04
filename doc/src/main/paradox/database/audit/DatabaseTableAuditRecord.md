<!-- Generated File -->
# audit_record

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| id                           | uuid               | true   | true   | true     |
| audit_id                     | uuid               | true   | false  | false    |
| t                            | string             | true   | false  | true     |
| pk                           | array              | true   | false  | true     |
| changes                      | json               | true   | false  | true     |

