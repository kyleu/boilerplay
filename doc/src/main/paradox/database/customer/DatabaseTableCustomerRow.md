<!-- Generated File -->
# customer

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| customer_id                  | integer            | true   | true   | true     |
| store_id                     | integer            | true   | false  | true     |
| first_name                   | string             | true   | false  | false    |
| last_name                    | string             | true   | false  | true     |
| email                        | string             | false  | false  | false    |
| address_id                   | integer            | true   | false  | true     |
| activebool                   | boolean            | true   | false  | false    |
| create_date                  | date               | true   | false  | false    |
| last_update                  | timestamptz        | false  | false  | false    |
| active                       | long               | false  | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| paymentCustomerIdFkey        | customer_id        | [payment](DatabaseTablePaymentRow)     | customer_id
| rentalCustomerIdFkey         | customer_id        | [rental](DatabaseTableRentalRow)       | customer_id
