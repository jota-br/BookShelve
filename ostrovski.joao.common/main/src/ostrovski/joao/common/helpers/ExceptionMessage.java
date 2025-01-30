package ostrovski.joao.common.helpers;

public enum ExceptionMessage {

    NULL_CONTROLLER("Controller value can't be null [setting controller]"),
    NULL_PARAM("Required parameters can't be null"),
    NULL_NODE("Null node can't be set"),
    NULL_VAR_RETURN("Null variable returned"),
    ILLEGAL_NUM_PARAM("Parameters out of range"),
    ILLEGAL_PARAM("Required parameters can't be empty or blank"),
    METHOD_FAILURE("Method can't conclude due to several failures"),
    INVALID_USER_INPUT("User input is invalid"),
    DATABASE_CONNECTION_ERROR("Failed to connect to the database"),
    FILE_NOT_FOUND("The specified file was not found"),
    ACCESS_DENIED("Access to the resource is denied"),
    OPERATION_TIMED_OUT("Operation timed out"),
    INVALID_FORMAT("Invalid format"),
    UNSUPPORTED_OPERATION("This operation is not supported"),
    NETWORK_ERROR("Network error occurred"),
    NULL_POINTER("Null pointer exception occurred");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}