<!-- Generated File -->
# film

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| film_id                      | integer            | true   | true   | true     | nextval('film_film_id_seq'::regclass)
| title                        | string             | true   | false  | true     |
| description                  | string             | false  | false  | false    |
| release_year                 | long               | false  | false  | false    |
| language_id                  | integer            | true   | false  | true     |
| original_language_id         | integer            | false  | false  | true     |
| rental_duration              | integer            | true   | false  | false    | 3
| rental_rate                  | decimal            | true   | false  | false    | 4.99
| length                       | integer            | false  | false  | false    |
| replacement_cost             | decimal            | true   | false  | false    | 19.99
| rating                       | enum               | false  | false  | true     | 'G'::mpaa_rating
| last_update                  | timestamptz        | true   | false  | true     | now()
| special_features             | list               | false  | false  | false    |
| fulltext                     | string             | true   | false  | true     |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| filmActorFilmIdFkey          | film_id            | [film_actor](DatabaseTableFilmActorRow)| film_id
| filmCategoryFilmIdFkey       | film_id            | [film_category](DatabaseTableFilmCategoryRow)| film_id
| inventoryFilmIdFkey          | film_id            | [inventory](DatabaseTableInventoryRow) | film_id
