package ui;

import java.sql.PreparedStatement;

public class PSQuery {
    private PreparedStatement preparedStatement;
    private String errorMsg;

    public PSQuery(PreparedStatement preparedStatement, String errorMsg){
        this.preparedStatement = preparedStatement;
        this.errorMsg = errorMsg;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
