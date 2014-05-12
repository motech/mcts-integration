package mcts.care.data.migration.exception;


public class DataMigrationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataMigrationException(String message) {
        super(message);
    }

    public DataMigrationException(String message, Throwable e) {
        super(message, e);
    }
}
