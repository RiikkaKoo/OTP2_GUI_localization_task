# APPLICATION SETUP
This documentaion explains the steps to setup the application and running the application.

## CREATE THE DATABASE

1. Create a MariaDB database with the database_script.sql. The script creates both the actual database and its tables.
2. Run the seed_data.sql to insert all the localized UI texts to the database.

## INITIAL SETUP

1. Copy the project to your IDE:

```
git clone https://github.com/RiikkaKoo/OTP2_GUI_localization_task.git
```

2. Move to the DB-localization branch.


## RUN THE APPLICATION FROM IDE

1. Check the src/main/resources/db.properties file. Make sure that the db.url is set to localhost.
2. Replace the db.rootUser and db.rootPassword values with your MariaDB root username and password.

```
db.url = jdbc:mariadb://localhost:3306/trip_calculator
db.rootUser = root
db.rootPassword = rootPassword
```
3. Locate the Main class and run it.

## RUN THE APPLICATION FROM DOCKER CONTAINER

1. Check the src/main/resources/db.properties file. Make sure that the db.url is set to host.docker.internal.
2. Replace the db.rootUser and db.rootPassword values with your MariaDB root username and password.

```
db.url = jdbc:mariadb://host.docker.internal:3306/trip_calculator
db.rootUser = root
db.rootPassword = rootPassword
```

3. Make sure your Docker engine is runnig and build the Docker image:
   
```
docker build -t riikkakoo/trip_calculator_db:latest .
```

4. Make sure you have Xming installed and running.
5. Run the image in a container:

```
docker run riikkakoo/trip_calculator_db:latest
```
