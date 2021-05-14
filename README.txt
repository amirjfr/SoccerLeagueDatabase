## Steps to Run the Application in IntelliJ
1. Pull from GitHub
2. Open in IntelliJ
3a. Set the src folder as a Source and resources folder as a Resource in "Project Structure"
3b. Add the jdbc JAR in resources as a dependency in "Project Structure"
4. Set an output folder
5. Tunnel to the CS servers
6. Run the queries in init.sql
7. Run the main method in controller.Controller
8. Login using your Oracle credentials

* Running init.sql will create all of our tables and fill them with some tuples!
    * Please run this prior to using our application!
* queries.sql contains ALL the queries that we used, labelled with their corresponding type and what they do
* The login window will attempt to connect to your Oracle instance on the CS servers.

## Implementation Details
* We specify each query in SoccerGUI that is represented in our program as a CustomQuery.
* Our tabs, specified by OperationTab, will display input fields depending on the uiArguments array in CustomQuery
* We create the PreparedStatement and verify that the arguments are the right type using
    orderedAttributeTypes in CustomQuery.createPreparedStatement on line 63.
* We execute the query. If the query is an Update/Delete/Insert that doesn't return any rows, we display the status of
the query
    * If the query is a SELECT query, then we take the result set and extract all the rows as a 2D string array.
        This array is then displayed in OperationTab.setOutputs on line 127.