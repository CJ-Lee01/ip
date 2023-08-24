public class Deadlines extends Task {
    private String deadline;

    public static Deadlines create(String rawLine) throws DukeException {
        if (rawLine.length() == 0) {
            throw new DukeException("Err: Empty Description");
        }
        String[] instructions = rawLine.split(" /by ");
        if (instructions.length != 2) {
            throw new DukeException("Err: No deadline given. Format - deadline <description> /by <deadline>");
        }
        return new Deadlines(instructions[0], instructions[1]);
    }
    public Deadlines(String item, String deadline) {
        super(item);
        this.deadline = deadline;
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