package Service;

import constants.AppConstants;
import enums.Roles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

import static Service.AdminService.*;
import static Service.AdminService.AssignTask;
import static Service.AdminService.DeleteMember;
import static Service.AdminService.ViewTasks;
import static Service.MemberService.UpdateTaskStatus;

public class CommonService {
    static Scanner scanner = new Scanner(System.in);
    static boolean isRunning = true;
    static boolean isAdmin = false;
    static boolean isMember = false;

    public  void CommonMain() throws IOException {
        while (isRunning){
            System.out.println("Welcome to the Task Management System!");
            System.out.println("1. Login");
            Login();
        }

        while (isAdmin){
            System.out.println("Admin Menu:");
            System.out.println("1. Create Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Add Member");
            System.out.println("4. Delete Member");
            System.out.println("5. Assign Task");
            System.out.println("6. View all progress and deadline");
            System.out.println("7. Logout");
            int adminChoice = scanner.nextInt();
            switch (adminChoice) {
                case 1:
                    CreateTask();
                    break;

                case 2:
                    ViewTasks();
                    break;
                case 3:
                    AddMember();
                    break;
                case 4:
                    DeleteMember();
                    break;
                case 5:
                    AssignTask();
                    break;
                case 6:
                    try {
                        ViewProgress();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 7:
                    isAdmin = false;
                    isRunning = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        while (isMember){
            System.out.println("Member Menu:");
            System.out.println("1. View Tasks");
            System.out.println("2. Update Task Status");
            System.out.println("3. View progress: ");
            System.out.println("4. Logout");
            int memberChoice = scanner.nextInt();
            switch (memberChoice) {
                case 1:
                    ViewTasks();
                    break;
                case 2:
                    UpdateTaskStatus();
                    break;
                case 3:
                    try {
                        ViewProgress();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:
                    isMember = false;
                    isRunning = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    public static void Login() throws IOException {
        File file = new File(AppConstants.FILE_PATH, AppConstants.Users_file);
        if(!file.exists()){
            return;
        }
        List<String> lines = Files.readAllLines(file.toPath());
        scanner.nextLine(); // buffer temizle
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        for(String line : lines){
            String[] parts = line.split(",");
            if(parts.length < 4) continue;
            String fileUsername = parts[1].trim();
            String filePassword = parts[2].trim();
            String role = parts[3].trim();

            if(fileUsername.equals(username) && filePassword.equals(password)){
                if(role.equals(Roles.MEMBER.name())){
                    System.out.println("Login successful for Member Welcome, " + username + "!");
                    isMember = true;
                    isRunning = false;
                    return;
                } else if(role.equals(Roles.ADMIN.name())){
                    System.out.println("Login successful for Admin Welcome, " + username + "!");
                    isAdmin = true;
                    isRunning = false;
                    return;
                }
            }
        }

        System.out.println("Invalid username or password!");
    }

    public static void ViewProgress() throws IOException {
        File file_progress = new File(AppConstants.FILE_PATH, AppConstants.Progress_file);
        if (!file_progress.exists()) {
            System.out.println("No progress found.");
            return;
        }
        List<String> lines = Files.readAllLines(file_progress.toPath());
        if (lines.isEmpty()) {
            System.out.println("No progress found.");
            return;
        }
        System.out.println("=== Progress ===");
        for (String line : lines) {
            String[] tasks = line.split(",");
            for (int i = 0; i < tasks.length; i++) {
                switch (i) {
                    case 0 -> System.out.println("Task ID: " + tasks[i]);
                    case 1 -> System.out.println("Title: " + tasks[i]);
                    case 2 -> System.out.println("Description: " + tasks[i]);
                    case 3 -> System.out.println("Status: " + tasks[i]);
                    case 4 -> System.out.println("Assigned to: " + tasks[i].replace("Member id: ",""));
                    default -> System.out.println(tasks[i]);
                }

            }

            System.out.println("-------------");
        }
    }

}
