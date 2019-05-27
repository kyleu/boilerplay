<!-- Generated File -->
# city

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| city_id                      | integer            | true   | true   | true     |
| city                         | string             | true   | false  | false    |
| country_id                   | integer            | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| addressCityIdFkey            | city_id            | [address](DatabaseTableAddressRow)     | city_id
