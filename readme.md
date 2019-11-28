## Assignment 2

### Setup
1. Import this project into Eclipse as a OSGi workspace.
2. Open launch.bndrun in assignment2.converter.servlet and click __Run OSGi__.


#### Query the servlet
Query the servlet by entering URLs in this format: http://localhost:8080/convert?v=88&from=Kg&to=lb

#### Gogo shell
- `list` will show available commands with instructions on how to use them.
- `add` will add a new conversion. The units needed for the conversion needs to be implemented. The format is: `add Kg lb "lb = 0.45359237 * Kg" `. Currently implemented units are Kilos, pounds, fahrenheit and celcius. 
- `convert` will convert a value from one unit to another. The format is: `convert Kg lb 88`
