<!-- Generated File -->
# customer

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| customer_id                  | integer            | true   | true   | true     | nextval('customer_customer_id_seq1'::regclass)
| store_id                     | integer            | true   | false  | true     |
| first_name                   | string             | true   | false  | true     |
| last_name                    | string             | true   | false  | true     |
| email                        | string             | false  | false  | true     |
| address_id                   | integer            | true   | false  | true     |
| activebool                   | boolean            | true   | false  | false    | true
| create_date                  | date               | true   | false  | false    | ('now'::text)::date
| last_update                  | timestamptz        | false  | false  | false    | now()
| active                       | long               | false  | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| paymentCustomerIdFkey        | customer_id        | [payment](DatabaseTablePaymentRow)     | customer_id
| rentalCustomerIdFkey         | customer_id        | [rental](DatabaseTableRentalRow)       | customer_id
