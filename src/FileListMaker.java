import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
public class FileListMaker {
    public static final Scanner in = new Scanner(System.in);
    private static ArrayList<String> myArrList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;

    public static void main(String[] args) {
        boolean done = false;
        String choice;
        do {
            displayMenu();
            choice = SafeInput.getRegExString(in,
                    "Choose A,D,I,M,O,S,C,V or Q", "[AaDdIiMmOoSsCcVvQq]");

            switch (choice.toUpperCase()) {
                case "A":
                    add();
                    break;
                case "D":
                    delete();
                    break;
                case "I":
                    insert();
                    break;
                case "M":
                    move();
                    break;
                case "O":
                    openFile();
                    break;
                case "S":
                    saveCurrentFile();
                    break;
                case "C":
                    clear();
                    break;
                case "V":
                    view();
                    break;
                case "Q":
                    done = quit();
                    break;
            }
        } while (!done);
    }
    private static void displayMenu() {
        System.out.println("\nA-Add/D-Delete/I-Insert/M-Move/O-Open/S-Save/C-Clear/V-View/Q-Quit");
        if (!myArrList.isEmpty()) {
            System.out.println("Current list: " + myArrList);
        } else {
            System.out.println("List is empty.");
        }
    }

    private static void add() {
        String item = SafeInput.getNonZeroLenString(in, "Enter item to add");
        myArrList.add(item);
        needsToBeSaved = true;
    }

    private static void delete() {
        if (myArrList.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }
        int index = SafeInput.getRangedInt(in, "Enter index to delete", 0, myArrList.size()-1);
        myArrList.remove(index);
        needsToBeSaved = true;
    }

    private static void insert() {
        String getString = SafeInput.getNonZeroLenString(in,"What string would you like to insert? ");
        if(myArrList.isEmpty()){
            myArrList.add(getString);
            needsToBeSaved = true;
            System.out.println("The list is empty so spot 1 is the only spot you could insert. ");
            return;
        }
        int getIndex = SafeInput.getRangedInt(in,"Enter the index of the string you want to insert in: ", 0, myArrList.size()-1);
        myArrList.add(getIndex, getString );
        needsToBeSaved = true;
    }

    private static void move() {
        if (myArrList.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }
        int fromIndex = SafeInput.getRangedInt(in, "Enter index of item to move", 0, myArrList.size()-1);
        int toIndex = SafeInput.getRangedInt(in, "Enter new index for the item", 0, myArrList.size()-1);

        String item = myArrList.remove(fromIndex);
        myArrList.add(toIndex, item);
        needsToBeSaved = true;
        System.out.println("Item moved successfully.");
    }

    private static void clear() {
        if (myArrList.isEmpty()) {
            System.out.println("List is already empty.");
            return;
        }
        boolean confirm = SafeInput.getYNConfirm(in, "Clear the entire list?");
        if (confirm) {
            myArrList.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static void view() {
        if (myArrList.isEmpty()) {
            System.out.println("The list is empty");
        } else {
            System.out.println("\nCurrent List: " + myArrList + "\n");
        }
    }
    private static boolean quit() {
        if (needsToBeSaved) {
            boolean save = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before quitting?");
            if (save)
                saveCurrentFile();
        }
        return true;
    }
    private static void openFile() {
        if (needsToBeSaved) {
            boolean save = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before opening another file?");
            if (save)
                saveCurrentFile();
        }

        String fileName = SafeInput.getNonZeroLenString(in, "Enter filename to open") + ".txt";
        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Path.of(workingDirectory.getPath() + "\\src\\" + fileName);
        try {
            myArrList = new ArrayList<>(Files.readAllLines(file));
            currentFileName = fileName;
            needsToBeSaved = false;
            System.out.println("File loaded: " + fileName);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
        catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }
    }

    private static void saveCurrentFile() {
        if (currentFileName == null) {
            currentFileName = SafeInput.getNonZeroLenString(in,
                    "Enter filename to save as") + ".txt";
        }

        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Path.of(workingDirectory.getPath() + "\\src\\" + currentFileName);

        try {
            Files.write(file, myArrList);
            needsToBeSaved = false;
            System.out.println("Data file written: " + currentFileName);
        }
        catch (IOException e) {
            System.out.println("Error writing file: " + currentFileName);
        }
    }
}