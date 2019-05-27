<!-- Generated File -->
# actor

## Columns

| Name                         | Type               | NotNull| Unique | Indexed  | Default
|------------------------------|--------------------|--------|--------|----------|--------------------
| actor_id                     | integer            | true   | true   | true     |
| first_name                   | string             | true   | false  | false    |
| last_name                    | string             | true   | false  | true     |
| last_update                  | timestamptz        | true   | false  | false    |

## References

| Name                         | Target             | Table                                  | Column
|------------------------------|--------------------|----------------------------------------|--------------------
| filmActorActorIdFkey         | actor_id           | [film_actor](DatabaseTableFilmActorRow)| actor_id
