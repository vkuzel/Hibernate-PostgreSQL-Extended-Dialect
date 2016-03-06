# Hibernate Postgres Extended Dialect

Dialect adds partial support for PostgreSQL arrays in Hibernate. Originally designed for use with Spring Data JPA.

Partial support means that Hibernate cannot create database based on entities but it can use existing.

## How to use it

* Download the library [hibernate-postgres-extended-dialect-0.1.0.jar](build/libs/hibernate-postgres-extended-dialect-0.1.0.jar) and place it into your project. Library depends on `org.postgresql:postgresql:9.4.+` and `org.hibernate:hibernate-core:4.3.+` projects which are part of Spring Boot 1.3.3.
* Configure Hibernate so it will use new dialect. Following configuration is for Sping JPA.

  ```
  spring.jpa.database-platform=com.github.vkuzel.hibernate.dialect.PostgreSQL9ExtendedDialect
  ```
* Create a database table.

  ```sql
  CREATE TABLE test_entity (
    id BIGSERIAL,
    arr INT[],
    multi_arr INT[][]
  );
  ```
* Create entity with List types.

  ```java
  @Entity
  public class TestEntity {
      @Id
      private long id;
      private List<Integer> arr;
      private List<List<Integer>> multiArr;

      // getters and setters ommited
  }
  ```
* Or call database directly from repository.

  ```java
  public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
      @Query(value = "SELECT arr FROM test_entity LIMIT 1", nativeQuery = true)
      List<Integer> array();
  }
  ```