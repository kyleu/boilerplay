<!-- Generated File -->
# category

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| category_id                  | integer            | true   | true   | true     |
| name                         | string             | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | true     |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| filmCategoryCategoryIdFkey   | category_id        | [film_category](DatabaseTableFilmCategoryRow)| category_id
