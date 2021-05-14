package ui;

import enums.AttributeType;
import enums.QueryType;

import java.sql.*;
import java.util.Map;

public class CustomQuery {

    private String queryName;
    private String[] uiArguments;
    private String[] orderedArguments;
    private String[] columnNames;
    private AttributeType[] orderedAttributeTypes;
    private Map<String, String> attributeMap;
    private String sqlQuery;
    private QueryType queryType;

    public CustomQuery(String queryName, String[] uiArguments, AttributeType[] attributeTypes, String sqlQuery, QueryType queryType) {
        this.queryName = queryName;
        this.uiArguments = uiArguments;
        this.orderedArguments = uiArguments;
        this.orderedAttributeTypes = attributeTypes;
        this.sqlQuery = sqlQuery;
        this.queryType = queryType;
        this.columnNames = null;
    }

    // Constructor specifying order of arguments
    public CustomQuery(String queryName, String[] uiArguments, String[] orderedArguments, AttributeType[] orderedAttributeTypes, String sqlQuery, QueryType queryType) {
        this.queryName = queryName;
        this.uiArguments = uiArguments;
        this.orderedArguments = orderedArguments;
        this.orderedAttributeTypes = orderedAttributeTypes;
        this.sqlQuery = sqlQuery;
        this.queryType = queryType;
        this.columnNames = null;
    }

    public CustomQuery(String queryName, String[] uiArguments, AttributeType[] attributeTypes, String[] columnNames, String sqlQuery, QueryType queryType) {
        this.queryName = queryName;
        this.uiArguments = uiArguments;
        this.orderedArguments = uiArguments;
        this.orderedAttributeTypes = attributeTypes;
        this.sqlQuery = sqlQuery;
        this.queryType = queryType;
        this.columnNames = columnNames;
    }

    // Constructor specifying order of arguments
    public CustomQuery(String queryName, String[] uiArguments, String[] orderedArguments, AttributeType[] orderedAttributeTypes, String[] columnNames, String sqlQuery, QueryType queryType) {
        this.queryName = queryName;
        this.uiArguments = uiArguments;
        this.orderedArguments = orderedArguments;
        this.orderedAttributeTypes = orderedAttributeTypes;
        this.sqlQuery = sqlQuery;
        this.queryType = queryType;
        this.columnNames = columnNames;
    }


    public PSQuery createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        String errorMsg = "";

        for (int i = 0; i < orderedArguments.length; i++) {
            // Set attributes in PreparedStatement with data sanitation
            switch (orderedAttributeTypes[i]) {
                case INT:
                    try{
                        ps.setInt(i + 1, Integer.parseInt(attributeMap.get(orderedArguments[i])));
                    } catch (NumberFormatException e){
                        errorMsg = "Please ensure " + orderedArguments[i] + " is a valid integer!";
                    }
                    break;

                case VARCHAR:
                    ps.setString(i + 1, attributeMap.get(orderedArguments[i]));
                    break;
                case DATE:
                    try{
                        ps.setDate(i + 1, Date.valueOf(attributeMap.get(orderedArguments[i])));
                    } catch (IllegalArgumentException e){
                        errorMsg = "Please ensure " + orderedArguments[i] + " is a valid date of format yyyy-mm-dd!";
                    }
                    break;
                case TIMESTAMP:
                    try{
                        ps.setTimestamp(i + 1, Timestamp.valueOf(attributeMap.get(orderedArguments[i])));
                    } catch (IllegalArgumentException e){
                        errorMsg = "Please ensure " + orderedArguments[i] + " is a valid timestamp of format yyyy-mm-dd hh:mm:ss!";
                    }
                    break;
            }
        }

        PSQuery psQuery = new PSQuery(ps, errorMsg);
        return psQuery;
    }

    public String getQueryName() {
        return queryName;
    }

    public String[] getUiArguments() {
        return uiArguments;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setAttributeMap(Map<String, String> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public String toString() {
        return queryName;
    }
}
