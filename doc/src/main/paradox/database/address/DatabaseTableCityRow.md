<!-- Generated File -->
# city

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| city_id                      | integer            | true   | true   | true     | nextval('city_city_id_seq'::regclass)
| city                         | string             | true   | false  | true     |
| country_id                   | integer            | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | false    | now()

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| addressCityIdFkey            | city_id            | [address](DatabaseTableAddressRow)     | city_id
