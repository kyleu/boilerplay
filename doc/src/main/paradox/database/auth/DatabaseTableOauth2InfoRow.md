<!-- Generated File -->
# oauth2_info

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| provider                     | string             | true   | true   | true     |
| key                          | string             | true   | true   | true     |
| access_token                 | string             | true   | false  | false    |
| token_type                   | string             | false  | false  | false    |
| expires_in                   | long               | false  | false  | false    |
| refresh_token                | string             | false  | false  | false    |
| params                       | hstore             | false  | false  | false    |
| created                      | timestamp          | true   | false  | false    |

