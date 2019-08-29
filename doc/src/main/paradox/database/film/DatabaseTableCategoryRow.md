<!-- Generated File -->
# category

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| category_id                  | integer            | true   | true   | true     | nextval('category_category_id_seq'::regclass)
| name                         | string             | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | true     | now()

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| filmCategoryCategoryIdFkey   | category_id        | [film_category](DatabaseTableFilmCategoryRow)| category_id
