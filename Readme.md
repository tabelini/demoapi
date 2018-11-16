# Demo API

To start the app use:

./gradlew bootRun

For access to the swagger documentation use: http://localhost:8081/swagger-ui.html#/


## Design decisions

### Data

The data was given in CSV file, it could easily be added to an sql database
(the keys were given and it seems to follow an relational model, for that we could use both h2 for a small
amount of data or postgres for a more robust solution), but the test could be on how to implement the access
patterns so just let then in memory

### Layered architecture
The architecture chosen was layered that closely follow spring guide lines and would enable us to easily migrate
our repositories to an sql database using in this case flyway for migrations and the JOOQ or even the 
jdbc template for data access

### Build system
Gradle was chosen for no specific reason, but familiarity we could use maven too

### Language
Kotlin was suggested for this test, but I really love the language features that speed up development by
a lot and give us a lot of confidence with the nullability check

### tests
No tests were added to follow the 4 hour constraint it will be added in a later commit but will go over
the time constraint so it's up to you if do you want to consider it or not
