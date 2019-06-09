<!-- Generated File -->
# staff

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| staff_id                     | integer            | true   | true   | true     |
| first_name                   | string             | true   | false  | true     |
| last_name                    | string             | true   | false  | true     |
| address_id                   | integer            | true   | false  | true     |
| email                        | string             | false  | false  | true     |
| store_id                     | integer            | true   | false  | true     |
| active                       | boolean            | true   | false  | false    |
| username                     | string             | true   | false  | true     |
| password                     | string             | false  | false  | false    |
| last_update                  | timestamptz        | true   | false  | false    |
| picture                      | byteArray          | false  | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| paymentStaffIdFkey           | staff_id           | [payment](DatabaseTablePaymentRow)     | staff_id
| rentalStaffIdFkey            | staff_id           | [rental](DatabaseTableRentalRow)       | staff_id
