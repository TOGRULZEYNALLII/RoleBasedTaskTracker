
import Tasks.Tasks;
import users.Member;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import  java.util.Scanner;
import java.io.File;

public class Main {

    private static final String Tasks_file = "tasks.txt";
    private static final String Users_file = "users.txt";
    private static final String Progress_file = "progress.txt";
    static Scanner scanner= new Scanner(System.in);
        static boolean isRunning = true;
      static   boolean isAdmin = false;
       static boolean isMember = false;
    public static void main() throws IOException {
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
        File file = new File("src/file", Users_file);
        if(!file.exists()) return;
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
                if(role.equals("member")){
                    System.out.println("Login successful for Member Welcome, " + username + "!");
                    isMember = true;
                    isRunning = false;
                    return;
                } else if(role.equals("admin")){
                    System.out.println("Login successful for Admin Welcome, " + username + "!");
                    isAdmin = true;
                    isRunning = false;
                    return;
                }
            }
        }

        System.out.println("Invalid username or password!");
    }
    public static void CreateTask() {
        scanner.nextLine(); // buffer temizleme
        System.out.println("Enter task title:");
        String title = scanner.nextLine();
        System.out.println("Enter task description:");
        String description = scanner.nextLine();
        String status="not started";
        Tasks task = new Tasks(title, description,status);
        File file= new File("src/file",Tasks_file);
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
       File file=new File("src/file",Tasks_file);
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
         Member member = new Member(name, password);
         File file = new File("src/file", Users_file);
         StringBuilder StringBuilder = new StringBuilder();
         try {
             if (!file.exists()) {
                 file.createNewFile();
             }
             String memberData =member.getUuid()+","+ member.getName() + "," + member.getPassword() + ",member \n";
             StringBuilder.append(memberData);
                Files.write(file.toPath(), StringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
             System.out.println("Member added successfully!");
         } catch (IOException ex) {
             throw new RuntimeException(ex);
         }
         ;
     }

    public static void DeleteMember() {
        scanner.nextLine(); // buffer temizleme
        File file = new File("src/file", Users_file);
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
            System.out.println("Write member id to delete:");
            String memberId = scanner.nextLine();
            StringBuilder updatedContent = new StringBuilder();
            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].trim().equals(memberId) && parts[3].trim().equals("member"))
                {
                    found = true;
                    System.out.println("Member with ID " + memberId + " deleted successfully!");
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
            Files.write(file.toPath(), updatedContent.toString().getBytes());
            if (!found) {
                System.out.println("Member not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void AssignTask() {
        scanner.nextLine(); // buffer temizleme
        File file_progress = new File("src/file", Progress_file);
        File file_tasks = new File("src/file", Tasks_file);
        File file_users = new File("src/file", Users_file);
        try {
            if (!file_tasks.exists()) {
                System.out.println("No tasks found.");
                return;
            }
            List<String> tasks = Files.readAllLines(file_tasks.toPath());
            List<String> users = Files.readAllLines(file_users.toPath());
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

    public static void ViewProgress() throws IOException {
        File file_progress = new File("src/file", Progress_file);
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

     public static void UpdateTaskStatus() {
         scanner.nextLine(); // buffer temizleme
         File file_progress = new File("src/file", Progress_file);
         try {
             if (!file_progress.exists()) {
                 System.out.println("No progress found.");
                 return;
             }
             List<String> lines = Files.readAllLines(file_progress.toPath());
             if (lines.isEmpty()) {
                 System.out.println("No progress found.");
                 return;
             }
             for (String line : lines) {
                 System.out.println(line);
             }
             System.out.println("Write task id to update, Please verify it is your assigned task: ");
             String taskId = scanner.nextLine();
             StringBuilder updatedContent = new StringBuilder();
             for (String line : lines) {
                 String[] parts = line.split(",");
                 if (parts[0].equals(taskId)) {
                     System.out.println("Write new status:");
                     String newStatus = scanner.nextLine();
                     String updatedLine = parts[0]+","+parts[1]+","+parts[2]+","+newStatus+","+parts[4]+"\n";
                     updatedContent.append(updatedLine);
                     System.out.println("Task status updated successfully!");
                 } else {
                     updatedContent.append(line).append("\n");
                 }
             }
             Files.write(file_progress.toPath(), updatedContent.toString().getBytes());

         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
}



