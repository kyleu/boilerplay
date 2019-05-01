<!-- Generated File -->
# note

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| id                           | uuid               | true   | true   | true     |
| rel_type                     | string             | false  | false  | true     |
| rel_pk                       | string             | false  | false  | true     |
| text                         | string             | true   | false  | true     |
| author                       | uuid               | true   | false  | true     |
| created                      | timestamp          | true   | false  | true     |

