<!-- Generated File -->
# rental

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| rental_id                    | long               | true   | true   | true     |
| rental_date                  | timestamptz        | true   | true   | true     |
| inventory_id                 | long               | true   | true   | true     |
| customer_id                  | integer            | true   | true   | true     |
| return_date                  | timestamptz        | false  | false  | true     |
| staff_id                     | integer            | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| paymentRentalIdFkey          | rental_id          | [payment](DatabaseTablePaymentRow)     | rental_id
