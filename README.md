# Extended PostgreSQL types for Hibernate 5.0

[![](https://jitpack.io/v/vkuzel/Hibernate-PostgreSQL-Extended-Dialect.svg)](https://jitpack.io/#vkuzel/Hibernate-PostgreSQL-Extended-Dialect)

## Features

* [Partial support of mapping PostgreSQL arrays to Lists.](#postgresql-arrays)
* [Mapping of JSON type to Map.](#json)
* Java 8 time API is supported by [hibernate-java8](https://repo1.maven.org/maven2/org/hibernate/hibernate-java8/) package and since [Hibernate 5.2](https://github.com/hibernate/hibernate-orm/releases/tag/5.2.0/) by hibernate-core.

When this project started I intended to create custom dialect for some ProstgreSQL types that are not included in standard Hibernate library.
Then I found that making this as a library with set of custom types is better idea.
So please use this project more like an example of adding library with custom types than "final product".

## Getting started

Just add a dependency to your project and that is all.

**Gradle**
````groovy
    allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }

    dependencies {
            compile 'com.github.vkuzel:Hibernate-PostgreSQL-Extended-Dialect:v0.5.0'
    }
````
**Maven**
````xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

    <dependency>
	    <groupId>com.github.vkuzel</groupId>
	    <artifactId>Hibernate-PostgreSQL-Extended-Dialect</artifactId>
	    <version>v0.5.0</version>
	</dependency>
````

## Eamples

Create a database table.

```sql
    CREATE TABLE test_entity (
      id                     BIGSERIAL,
      flat_array             INT [],
      multidimensional_array INT [] [],
      date                   DATE,
      time                   TIME,
      date_time              TIMESTAMP,
      json                   JSON
    );
```
Create an entity.
```java
    @Entity
    public class TestEntity {
      @Id
      private long id;
      private List<Integer> flatArray;
      private List<List<Integer>> multidimensionalArray;
      private LocalDate date;
      private LocalTime time;
      private LocalDateTime dateTime;
      private Map<String, Object> json;

      // getters and setters omitted
    }
```
And then use repository to persist it. Or call a query.
```java
    public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
      @Query(value = "SELECT flat_array FROM test_entity LIMIT 1", nativeQuery = true)
      List<Integer> array();
    }
```

## Implementation details

### PostgreSQL arrays

There is only partial support of PostgreSQL arrays implemented by this library. This means that you can use array (List) types in your entities or queries to select or persist data but Hibernate won't be able to create tables with array columns.

It's because list element types are not available at a runtime. To find out of what type of elements list hold library has to iterate over the list and checks it's elements. Unfortunately this is not a good solution when tables are created. Check out [ArrayTypeDescriptor.createPgArray](src/main/java/com/github/vkuzel/hibernate/type/descriptor/java/PostgresArrayTypeDescriptor.java#L40) method for more details.

### JSON

Mapping of Java type `Map<String, Object>` to JSON columns is done by [Jackson library](http://wiki.fasterxml.com/JacksonHome). Right now project does not support `jsonb` or mapping to Java objects. Possibly this will be added in future.
