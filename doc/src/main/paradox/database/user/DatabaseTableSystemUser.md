<!-- Generated File -->
# system_users

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| id                           | uuid               | true   | true   | true     |
| username                     | string             | false  | true   | true     |
| provider                     | string             | true   | true   | true     |
| key                          | string             | true   | true   | true     |
| prefs                        | string             | true   | false  | false    |
| role                         | string             | true   | false  | false    |
| created                      | timestamp          | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| noteAuthorFkey               | id                 | [note](DatabaseTableNote)              | author
