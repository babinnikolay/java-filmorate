# java-filmorate

Simple Filmorate project.

DB table structure and relationships

![alt text](./QuickDBD-export.png)

Table: Film

| column_name   | column_type | column_keys |
|---------------|-------------|-------------|
| film_id       | long        | PK          |
| name          | string      |             |
| description   | string      |             |
| release_date  | datetime    |             |
| duration      | datetime    |             |
| rating_id     | enum_id     |             |

Table: User

| column_name | column_type | column_keys |
|-------------|-------------|-------------|
| user_id     | long        | PK          |
| email       | string      |             |
| login       | string      |             |
| name        | datetime    |             |
| birthday    | datetime    |             |

Table: FilmLike

| column_name | column_type | column_keys         |
|-------------|-------------|---------------------|
| film_id     | long        | FK >-< Film.film_id |
| user_id     | long        | FK >-< User.user_id |

Table: FilmGenre

| column_name | column_type | column_keys         |
|-------------|-------------|---------------------|
| film_id     | long        | FK  >- Film.film_id |
| genre_id    | enum_id     |                     |

Table: UserFriend

| column_name      | column_type | column_keys          |
|------------------|-------------|----------------------|
| user_id          | long        | FK  >-< User.user_id |
| friend_id        | long        | FK >-< User.user_id  |
| friend_status_id | enum_id     |                      |
