<!-- Generated File -->
# inventory

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| inventory_id                 | long               | true   | true   | true     |
| film_id                      | integer            | true   | false  | true     |
| store_id                     | integer            | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| rentalInventoryIdFkey        | inventory_id       | [rental](DatabaseTableRentalRow)       | inventory_id
