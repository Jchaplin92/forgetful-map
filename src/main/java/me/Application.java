package me;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Application {

    private Scanner scanner;
    private PrintStream printStream;
    private UserEntryForgetfulMap userEntryForgetfulMap;


    public Application(InputStream inputStream, PrintStream printStream) {
        this.scanner = new Scanner(inputStream);
        this.printStream = printStream;
    }

    public void handleAssociations() {
        printStream.println("Enter the max number of associations in your forgetful map:");
        Integer maxAssociations = scanner.nextInt();
        this.userEntryForgetfulMap = new UserEntryForgetfulMap(maxAssociations);

        requestUserInput();
    }

    private void requestUserInput() {
        printStream.println("Enter number to \n1. 'add' association\n2. 'find' association via key\n3. print all associations\n4. exit the application");
        int answer = scanner.nextInt();
        if (answer == 1) {
            addAssociation();
            requestUserInput();
        } else if (answer == 2) {
            printStream.println("enter key to search:\n");
            String key = scanner.next();
            findAssociationFor(key);
            requestUserInput();
        } else if (answer == 3) {
            printStream.printf("All associations:\n%s\n", userEntryForgetfulMap.allAssociations());
            requestUserInput();
        }
    }

    private void addAssociation() {
        printStream.println("Enter the 'key' part of the association");
        String x = scanner.next();
        printStream.println("Enter the 'content' part of the association");
        String y = scanner.next();
        userEntryForgetfulMap.add(x, y);
        printStream.printf("association added: %s : %s\n", x, y);
    }

    private void findAssociationFor(String key) {
        try {
            String findResult = userEntryForgetfulMap.find(key);
            printStream.printf("found content associated with key: %s\n", findResult);
        } catch (IllegalArgumentException e) {
            printStream.printf("cannot find content for key %s\n", key);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application application = new Application(System.in, System.out);
        application.handleAssociations();
    }
}