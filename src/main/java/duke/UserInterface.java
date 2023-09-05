package duke;

import java.util.List;
import java.util.Scanner;

/**
 * User Interface for the Duke program.
 */
public class UserInterface {

    /** Scanner to scan line input. */
    private Scanner sc = new Scanner(System.in);
    /** Storage to store the save file. */
    private Storage storage;
    /** List to keep track of tasks while program is running. */
    private StoreList list;

    /**
     * Creates a user interface from a Storage object and a StoreList object.
     *
     * @param storage The Storage object to be used by the user interface.
     * @param list The StoreList object to be used by the user interface.
     */
    public UserInterface(Storage storage, StoreList list) {
        this.list = list;
        this.storage = storage;
    }

    /**
     * Checks if there is another command to be read from the user, and returns true if there is and false otherwise.
     *
     * @return True if there is another command to be read from the user, and false otherwise.
     */
    public boolean hasNextCommand() {
        return sc.hasNext();
    }

    /**
     * Starts the user interface by reading the tasks from the storage file and adding them to the list of tasks.
     *
     * @throws DukeException If there is an error reading the tasks from the storage file.
     */
    public void start() throws DukeException {
        Task[] tasks = Parser.parseFile(this.storage);
        this.list.addTasks(List.of(tasks));
    }

    /**
     * Reads commands from the user and processes them until the user enters the "bye" command.
     */
    public void readCommandLine() {
        Reading:
        while (true) {
            String line = sc.nextLine();
            if (line.length() == 0) {
                display("Err: No command input");
                continue;
            }
            String[] instructions = line.split(" ", 2);
            Commands cmd = Commands.get(instructions[0]);
            String response;
            switch (cmd) {
            case bye:
                this.save();
                break Reading;
            case list:
                response = list.toString();
                display(response);
                break;
            case deadline:
                // Fallthrough
            case todo:
                // Fallthrough
            case event:
                try {
                    Task task = Parser.parseTask(cmd, instructions);
                    response = list.add(task);
                    display(response);
                } catch (DukeException e) {
                    display(e.toString());
                }
                break;
            case mark:
                try {
                    response = list.markDone(instructions[1]);
                    display(response);
                } catch (IndexOutOfBoundsException e) {
                    display("Err: Index not in range of list.");
                }
                break;
            case unmark:
                try {
                    response = list.markUndone(instructions[1]);
                    display(response);
                } catch (IndexOutOfBoundsException e) {
                    display("Err: Index not in range of list.");
                }
                break;
            case delete:
                try {
                    response = list.delete(instructions[1]);
                    display(response);
                } catch (IndexOutOfBoundsException e) {
                    display("Err: Index not in range of list.");
                }
                break;
            case find:
                response = Parser.findAll(instructions, list);
            default:
                response = "Err: Unknown command - " + instructions[0];
                display(response);
                break;
            }
        }
        sc.close();
    }

    /**
     * Saves the current state of the list of tasks to the storage file.
     */
    public void save() {
        String saveText = list.showSaveText();
        storage.write(saveText);
    }

    /**
     * Exits the user interface by saving the current state of the list of tasks to the storage file and closing any resources used by the user interface.
     */
    public void exit() {
        this.save();
        sc.close();
    }

    /**
     * Displays a string to a user in a formatted manner.
     *
     * @param s The string to be displayed to the user.
     */
    public static void display(String s) {
        System.out.println(wrapper(s));
    }

    private static String wrapper(String line) {
        String frame = "=====================";
        return String.format("%s\n%s", line, frame);
    }

    /**
     * Gives the response of parsing the line.
     *
     * @param line The line to be parsed
     * @return The response.
     */
    public String parseLine(String line) {
        if (line.length() == 0) {
            return wrapper("Err: No command input");
        }
        String[] instructions = line.split(" ", 2);
        Commands cmd = Commands.get(instructions[0]);
        String response;
        switch (cmd) {
        case bye:
            this.save();
            return wrapper("Bye");
        case list:
            response = list.toString();
            return wrapper(response);
        case deadline:
            // Fallthrough
        case todo:
            // Fallthrough
        case event:
            try {
                Task task = Parser.parseTask(cmd, instructions);
                response = list.add(task);
                return wrapper(response);
            } catch (DukeException e) {
                return wrapper(e.toString());
            }
        case mark:
            try {
                response = list.markDone(instructions[1]);
                return wrapper(response);
            } catch (IndexOutOfBoundsException e) {
                return wrapper("Err: Index not in range of list.");
            }
        case unmark:
            try {
                response = list.markUndone(instructions[1]);
                return wrapper(response);
            } catch (IndexOutOfBoundsException e) {
                return wrapper("Err: Index not in range of list.");
            }
        case delete:
            try {
                response = list.delete(instructions[1]);
                return wrapper(response);
            } catch (IndexOutOfBoundsException e) {
                return wrapper("Err: Index not in range of list.");
            }
        case find:
            response = Parser.findAll(instructions, list);
            return wrapper(response);
        default:
            response = "Err: Unknown command - " + instructions[0];
            return wrapper(response);
        }
    }
}
