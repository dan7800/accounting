# Accountabilibuddies
#### SWEN-343 Garfield Accounting Project

### Prerequisites
* Java JDK 8
* Maven
* MySQL

### Setup
Update the database connection parameters in `accounting.yaml` to suit your installation. Be sure to create the database specified by the JDBC `url` parameter.

### Building
```
mach package
```

### Running
```
java -jar target/accounting-1.0-SNAPSHOT.jar server accounting.yaml
```

### Testing
```
maven test
```

## Contribution Guidelines
* Must receive two thumbs up from anyone other than the contributer
* Merge your own request after receiving feedback 
