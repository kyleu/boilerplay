<!-- Generated File -->
# flyway_schema_history

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| installed_rank               | long               | true   | true   | true     |
| version                      | string             | false  | false  | false    |
| description                  | string             | true   | false  | false    |
| type                         | string             | true   | false  | false    |
| script                       | string             | true   | false  | false    |
| checksum                     | long               | false  | false  | false    |
| installed_by                 | string             | true   | false  | false    |
| installed_on                 | timestamp          | true   | false  | false    | now()
| execution_time               | long               | true   | false  | false    |
| success                      | boolean            | true   | false  | true     |

