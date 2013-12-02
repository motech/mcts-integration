package mcts.care.data.migration.exception;

public class DataMigrationException extends RuntimeException {

    public DataMigrationException(String message) {
        super(message);
    }

    public DataMigrationException(String message, Throwable e) {
        super(message, e);
    }
}
