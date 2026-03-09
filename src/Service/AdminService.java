package Service;
import enums.Roles;
import enums.ProjectStatus;
import constants.AppConstants;
import modul.Member;
import modul.Tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class AdminService {
  static  Scanner scanner = new Scanner(System.in);
    public static void CreateTask() {

        scanner.nextLine();
        System.out.println("Enter task title:");
        String title = scanner.nextLine();
        System.out.println("Enter task description:");
        String description = scanner.nextLine();
        String status= ProjectStatus.NOT_STARTED.name();
        Tasks task = new Tasks(title, description,status);
        File file= new File(AppConstants.FILE_PATH,AppConstants.Tasks_file);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String taskData = task.getUuid() + "," + task.getTitle() + "," + task.getDescription() + ","+task.getStatus() +"\n";
            Files.write(file.toPath(), taskData.getBytes(), java.nio.file.StandardOpenOption.APPEND);
            System.out.println("Task created successfully!");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static void ViewTasks() {
        File file=new File(AppConstants.FILE_PATH,AppConstants.Tasks_file);
        try {
            if (!file.exists()) {
                System.out.println("No tasks found.");
                return;
            }
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                System.out.println("No tasks found.");
                return;
            }
            System.out.println("Tasks:");
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void AddMember() {
        scanner.nextLine(); // buffer temizleme
        System.out.println("Enter member name:");
        String name = scanner.nextLine();
        System.out.println("Enter member password:");
        String password = scanner.nextLine();
        modul.Member member = new Member(name, password);
        File file = new File(AppConstants.FILE_PATH, "member"+member.getUuid()+".txt");
        File file_users_for_admin = new File(AppConstants.FILE_PATH, AppConstants.Users_file);
        StringBuilder StringBuilder = new StringBuilder();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String memberData =member.getUuid()+","+ member.getName() + "," + member.getPassword() + ","+ Roles.MEMBER.name() + "\n";
            StringBuilder.append(memberData);
            Files.write(file.toPath(), StringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
            Files.write(file_users_for_admin.toPath(), StringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
            System.out.println("Member added successfully!");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ;
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
    public static void DeleteMember() {
        scanner.nextLine();
        System.out.println("Write member id to delete:");
        String memberId = scanner.nextLine();
        File file = new File(AppConstants.FILE_PATH, "member" + memberId + ".txt");

        try {
            if (!file.exists()) {
                System.out.println("No members found.");
                return;
            }

            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                System.out.println("No members found.");
                return;
            }

            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[4].trim().equals(memberId)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Member not found.");
                return;
            }

            if (file.delete()) {
                System.out.println("Member with ID " + memberId + " deleted successfully!");
            } else {
                System.out.println("Failed to delete member file.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void AssignTask() {
        scanner.nextLine(); // buffer temizleme
        File file_progress = new File(  AppConstants.FILE_PATH , AppConstants.Progress_file);

        File file_tasks = new File(AppConstants.FILE_PATH, AppConstants.Tasks_file);
        try {
            if (!file_tasks.exists()) {
                System.out.println("No tasks found.");
                return;
            }
            List<String> tasks = Files.readAllLines(file_tasks.toPath());
            System.out.println("Write task id to assign:");
            String taskId = scanner.nextLine();
            boolean taskFound = false;
            for (String line : tasks) {
                String[] parts = line.split(",");
                if (parts[0].equals(taskId))
                {
                    taskFound = true;

                    System.out.println("Write member id:");
                    String memberId = scanner.nextLine();
                    File memberFile = new File(AppConstants.FILE_PATH, "member" + memberId + ".txt");
                    File UserFile = new File(AppConstants.FILE_PATH, AppConstants.Users_file);
                    if (!memberFile.exists()) {
                        System.out.println("Member not found");
                        return;
                    }
                    List<String> users = Files.readAllLines(UserFile.toPath());
                    boolean memberExists =
                            users.stream().anyMatch(
                                    u -> u.split(",")[0].equals(memberId)
                            );
                    if (!memberExists) {
                        System.out.println("Member not found");
                        return;
                    }
                    String progressLine= String.join(",",
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            memberId
                    ) + "\n";
                    Files.write(
                            file_progress.toPath(),
                            progressLine.getBytes(),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND
                    );
                    Files.write(memberFile.toPath(), progressLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    System.out.println("Task assigned successfully!");
                }
            }
            if (!taskFound) {
                System.out.println("Task not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
