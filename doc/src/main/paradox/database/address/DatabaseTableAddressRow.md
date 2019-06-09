<!-- Generated File -->
# address

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| address_id                   | integer            | true   | true   | true     |
| address                      | string             | true   | false  | true     |
| address2                     | string             | false  | false  | true     |
| district                     | string             | true   | false  | true     |
| city_id                      | integer            | true   | false  | true     |
| postal_code                  | string             | false  | false  | true     |
| phone                        | string             | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | true     |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| customerAddressIdFkey        | address_id         | [customer](DatabaseTableCustomerRow)   | address_id
| staffAddressIdFkey           | address_id         | [staff](DatabaseTableStaffRow)         | address_id
| storeAddressIdFkey           | address_id         | [store](DatabaseTableStoreRow)         | address_id
