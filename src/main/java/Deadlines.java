import java.time.DateTimeException;
import java.time.LocalDateTime;

public class Deadlines extends Task {
    private LocalDateTime deadline;

    public static Deadlines create(String rawLine) throws DukeException {
        if (rawLine.length() == 0) {
            throw new DukeException("Err: Empty Description");
        }
        String[] instructions = rawLine.split(" /by ");
        if (instructions.length != 2) {
            throw new DukeException("Err: No deadline given. Format - deadline <description> /by <deadline>");
        }
        try {
            LocalDateTime deadline = LocalDateTime.parse(instructions[1], Task.standardDateTimeParser);
            return new Deadlines(instructions[0], deadline);
        } catch (DateTimeException e) {
            throw new DukeException.DukeDateTimeException(instructions[1]);
        }
    }
    public Deadlines(String item, LocalDateTime deadline) {
        super(item);
        this.deadline = deadline;
    }
    @Override
    public String fileString() {
        return String.format(
                "deadline %d %s /by %s",
                super.isDone ? 1 : 0,
                super.description,
                this.deadline
                );
    }

    @Override
    public String toString() {
        return String.format(
                "[D][%s] %s (by %s)",
                super.getStatusIcon(),
                super.description,
                this.deadline
        );
    }
}