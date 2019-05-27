<!-- Generated File -->
# country

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| country_id                   | integer            | true   | true   | true     |
| country                      | string             | true   | false  | false    |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| cityCountryIdFkey            | country_id         | [city](DatabaseTableCityRow)           | country_id
