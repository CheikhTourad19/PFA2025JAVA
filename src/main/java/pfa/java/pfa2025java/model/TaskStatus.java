package pfa.java.pfa2025java.model;

public enum TaskStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed");

    private final String dbValue;

    TaskStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public static TaskStatus fromString(String text) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.dbValue.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for: " + text);
    }

    public String toDbValue() {
        return this.dbValue;
    }
}