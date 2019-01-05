<!-- Generated File -->
# password_info

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| provider                     | string             | true   | true   | true     |
| key                          | string             | true   | true   | true     |
| hasher                       | string             | true   | false  | false    |
| password                     | string             | true   | false  | false    |
| salt                         | string             | false  | false  | false    |
| created                      | timestamp          | true   | false  | false    |

