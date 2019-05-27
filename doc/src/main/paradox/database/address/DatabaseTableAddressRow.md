<!-- Generated File -->
# address

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| address_id                   | integer            | true   | true   | true     |
| address                      | string             | true   | false  | false    |
| address2                     | string             | false  | false  | false    |
| district                     | string             | true   | false  | false    |
| city_id                      | integer            | true   | false  | true     |
| postal_code                  | string             | false  | false  | false    |
| phone                        | string             | true   | false  | false    |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| customerAddressIdFkey        | address_id         | [customer](DatabaseTableCustomerRow)   | address_id
| staffAddressIdFkey           | address_id         | [staff](DatabaseTableStaffRow)         | address_id
| storeAddressIdFkey           | address_id         | [store](DatabaseTableStoreRow)         | address_id
