# AL Carbon Calculator

## Description

Create the backend for a carbon calculator, using Java, Spring Boot and MongoDB.

There are only 3 endpoints that need to be implemented:

### [POST] /open/start-calc

Starts the calculation process. Receives the user basic info and stores it in the database. Returns the calculation's id
to be used in the following endpoints.

### [PUT] /open/info

Receives information needed to calculate the user's carbon emission (energy consumption, transportation and solid waste
production) and stores it in the
database.

### [GET] /open/result/{id}

Returns the carbon footprint for the calculation with the given id.

All these endpoints are already defined in the class `OpenRestController`. You should implement the methods in this
class.

## Calculator logic

### Energy consumption

The class `EnergyEmissionFactor` contains the emission factors for each brazilian state (UF). The emission follows the
formula:

```Carbon emission = energy consumption * emission factor```

### Transportation

The class `TransportationEmissionFactor` contains the emission factors for each type of transportation. The emission
follows the formula:

```Carbon emission = distance * transportation type emission factor```

### Solid waste

The class `SolidWasteEmissionFactor` contains the emission factors for recyclable and non-recyclable solid waste. The
emission follows the formula:

```Carbon emission = solid waste production * emission factor```

## Technical Notes

### Database

Run `docker compose up` to start the MongoDB database. The database will be populated with the default collection
contents defined in the `init-mongo.js` script when first started. These values are only for this test and should not be
considered real values for carbon emissions :smile:

If you need to reset the database to its initial state, you can run `docker compose down -v`, which will erase the
database and repopulate the initial values in the next start.

### Running the application

You can use your IDE of choice to run the application. The main class is `CarbonCalculatorApplication`. The server will
run
on port 8085 (http://localhost:8085).

There is a swagger documentation available on http://localhost:8085/swagger-ui.html.

### Classes already created

We created the classes for the RestController and the DTOs needed to execute its endpoints. There's no need to change
the DTOs.

We also created 3 basic models and their corresponding Repository interfaces for the carbon emission values that you
need to use in your implementations. These are the objects pre-populated in the
database. Feel free to add more methods to the *Repository interfaces as needed.

You will certainly need to create new classes to implement the logic for the endpoints and new models. Feel free to
organize the code as you see fit.

## Test evaluation

Your test will be evaluated both on the correctness of the implementation and the quality of the code.

There is no need to host this server. Publish your code in a public repository and share it with us, so we can download
your code and run it.

Good luck! :smile:

```