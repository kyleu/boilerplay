<!-- Generated File -->
# note

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| id                           | uuid               | true   | true   | true     |
| rel_type                     | string             | false  | false  | false    |
| rel_pk                       | string             | false  | false  | false    |
| text                         | string             | true   | false  | false    |
| author                       | uuid               | true   | false  | false    |
| created                      | timestamp          | true   | false  | false    |

