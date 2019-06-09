<!-- Generated File -->
# film

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| film_id                      | integer            | true   | true   | true     |
| title                        | string             | true   | false  | true     |
| description                  | string             | false  | false  | false    |
| release_year                 | long               | false  | false  | false    |
| language_id                  | integer            | true   | false  | true     |
| original_language_id         | integer            | false  | false  | true     |
| rental_duration              | integer            | true   | false  | false    |
| rental_rate                  | decimal            | true   | false  | false    |
| length                       | integer            | false  | false  | false    |
| replacement_cost             | decimal            | true   | false  | false    |
| rating                       | enum               | false  | false  | true     |
| last_update                  | timestamptz        | true   | false  | true     |
| special_features             | list               | false  | false  | false    |
| fulltext                     | string             | true   | false  | true     |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| filmActorFilmIdFkey          | film_id            | [film_actor](DatabaseTableFilmActorRow)| film_id
| filmCategoryFilmIdFkey       | film_id            | [film_category](DatabaseTableFilmCategoryRow)| film_id
| inventoryFilmIdFkey          | film_id            | [inventory](DatabaseTableInventoryRow) | film_id
