# Dropwizard Boilerplate

## Dependencies

* [Guice](https://github.com/google/guice)
* [Hibernate (JPA Provider)](http://hibernate.org/orm/)
* [HSQLDB](http://hsqldb.org/)

## Build

```
mvn install
```

### Run

```
java -jar target/dropwizard-boilerplate-0.0.1-SNAPSHOT.jar server app.yml
```

### Test

Create task

```
curl -H "Content-Type: application/json" -X POST -d '{"userName":"franzwong","content":"hello world"}' http://localhost:8080/tasks
```

Get tasks
```
curl http://localhost:8080/tasks\?userName\=franzwong
```
