<!-- Generated File -->
# category

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| category_id                  | integer            | true   | true   | true     |
| name                         | string             | true   | false  | false    |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| filmCategoryCategoryIdFkey   | category_id        | [film_category](DatabaseTableFilmCategoryRow)| category_id
