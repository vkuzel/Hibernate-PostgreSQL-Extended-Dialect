# Hibernate Postgres Extended Dialect

## Features

* [Partial support of mapping PostgreSQL arrays to Lists.](#postgresql-arrays)
* Mapping of date/time types to Java 8 LocalDate, LocalTime and LocalDateTime types.
* Mapping of JSON to Map.

## Getting started

Download the library [hibernate-postgres-extended-dialect-0.3.0.jar](build/libs/hibernate-postgres-extended-dialect-0.3.0.jar) and place it into your project.

Make sure all dependent libraries are available on classpath.
* `org.postgresql:postgresql:9.4.+`
* `org.hibernate:hibernate-core:4.3.+`
* `com.fasterxml.jackson.core:jackson-databind:2.6.+`

Configure the Hibernate to use new dialect. Place following line into your `application.properties` file. This is configuration for Spring JPA.
```
spring.jpa.database-platform=com.github.vkuzel.hibernate.dialect.PostgreSQL9ExtendedDialect
```

## Eamples

* Create a database table.

  ```sql
  CREATE TABLE test_entity (
    id BIGSERIAL,
    arr INT[],
    multi_arr INT[][]
  );
  ```
* Create an entity.

  ```java
  @Entity
  public class TestEntity {
      @Id
      private long id;
      private List<Integer> arr;
      private List<List<Integer>> multiArr;

      // getters and setters omitted
  }
  ```
* And then use repository to persist it or call database directly.

  ```java
  public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
      @Query(value = "SELECT arr FROM test_entity LIMIT 1", nativeQuery = true)
      List<Integer> array();
  }
  ```

## Implementation details

### PostgreSQL arrays

There is only partial support of PostgreSQL arrays implemented by this dialect. This means that you can use array (List) types in your entities or queries to select or persist data but Hibernate won't be able to create tables with array columns.

List element types are not available at runtime. To find out of what type elements are this library iterates over the list and actually checks it's elements types. Unfortunately this is not a good solution when tables are created. Check out [ArrayTypeDescriptor.createPgArray method](src/main/java/com/github/vkuzel/hibernate/type/descriptor/java/PostgresArrayTypeDescriptor.java#L40) for more details.
