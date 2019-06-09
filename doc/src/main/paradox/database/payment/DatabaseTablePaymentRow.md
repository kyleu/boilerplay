<!-- Generated File -->
# payment

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| payment_id                   | long               | true   | true   | true     |
| customer_id                  | integer            | true   | false  | true     |
| staff_id                     | integer            | true   | false  | true     |
| rental_id                    | long               | true   | false  | true     |
| amount                       | decimal            | true   | false  | true     |
| payment_date                 | timestamptz        | true   | false  | false    |

