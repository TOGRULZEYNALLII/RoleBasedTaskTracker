package Service;

import constants.AppConstants;
import enums.ProjectStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class MemberService {

    static Scanner scanner = new Scanner(System.in);

    public static void UpdateTaskStatus(String currentuser) {
        scanner.nextLine(); // buffer temizleme
        File file_progress        = new File(AppConstants.FILE_PATH, AppConstants.Progress_file);
        File file_progress_member = new File(AppConstants.FILE_PATH, "member" + currentuser + ".txt");

        try {
            if (!file_progress.exists()) {
                System.out.println("No progress found.");
                return;
            }

            List<String> lines = Files.readAllLines(file_progress.toPath());
            List<String> lines_member = Files.exists(file_progress_member.toPath()) ? Files.readAllLines(file_progress_member.toPath()) : List.of();
            if (lines.isEmpty()) {
                System.out.println("No progress found.");
                return;
            }

            // Sadece bu usere ait taskları göster
            System.out.println("=== Your Tasks ===");
            boolean hasTasks = false;
            for (String line : lines_member) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].trim().equals(currentuser.trim())) {
                    System.out.println("Task ID: "     + parts[0].trim());
                    System.out.println("Title: "       + parts[1].trim());
                    System.out.println("Description: " + parts[2].trim());
                    System.out.println("Status: "      + parts[3].trim());
                    System.out.println("-------------");
                    hasTasks = true;
                }
            }

            if (!hasTasks) {
                System.out.println("No tasks assigned to you.");
                return;
            }

            System.out.print("Write task ID to update: ");
            String taskId = scanner.nextLine().trim();

            StringBuilder updatedContent = new StringBuilder();
            StringBuilder updatedMember = new StringBuilder();
            boolean updated = false;

            for (String line : lines) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");

                if (parts.length < 5) {
                    updatedContent.append(line).append("\n");
                    continue;
                }

                if (parts[0].trim().equals(taskId) && parts[4].trim().equals(currentuser.trim())) {
                    System.out.println("Choose new status:");
                    System.out.println("1. Not Started");
                    System.out.println("2. In Progress");
                    System.out.println("3. Completed");
                    System.out.println("4. On Hold");
                    System.out.println("5. Cancelled");
                    System.out.print("Choice: ");
                    int statusChoice = scanner.nextInt();

                    String newStatus = switch (statusChoice) {
                        case 1 -> ProjectStatus.NOT_STARTED.name();
                        case 2 -> ProjectStatus.IN_PROGRESS.name();
                        case 3 -> ProjectStatus.COMPLETED.name();
                        case 4 -> ProjectStatus.ON_HOLD.name();
                        case 5 -> ProjectStatus.CANCELLED.name();
                        default -> {
                            System.out.println("Invalid choice. Status not changed.");
                            yield parts[3];
                        }
                    };

                    String updatedLine = parts[0] + "," + parts[1] + "," + parts[2] + "," + newStatus + "," + parts[4] + "\n";
                   String updatedLineMember = parts[0] + "," + parts[1] + "," + parts[2] + "," + newStatus + "," + parts[4] + "\n";
                    updatedMember.append(updatedLineMember);
                    updatedContent.append(updatedLine);

                    System.out.println("Task status updated successfully!");
                    updated = true;

                } else {
                    updatedContent.append(line).append("\n");
                }
            }

            if (!updated) {
                System.out.println("Task not found or you are not authorized to update it.");
                return;
            }

            Files.write(file_progress.toPath(), updatedContent.toString().getBytes());
            Files.write(file_progress_member.toPath(), updatedMember.toString().getBytes());

        } catch (IOException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    public static void ViewTaskMember() throws IOException {
        File file_progress_member = new File(AppConstants.FILE_PATH, "member" + CommonService.currentUser + ".txt");
        if (!file_progress_member.exists()) {
            System.out.println("No tasks assigned to you.");
            return;
        }
        List<String> lines_member = Files.readAllLines(file_progress_member.toPath());
        if (lines_member.isEmpty()) {
            System.out.println("No tasks assigned to you.");
            return;
        }
        System.out.println("=== Your Tasks ===");
        for (String line : lines_member) {
            String[] parts = line.split(",");
            if (parts.length >= 5 && parts[4].trim().equals(CommonService.currentUser.trim())) {
                System.out.println("Task ID: "     + parts[0].trim());
                System.out.println("Title: "       + parts[1].trim());
                System.out.println("Description: " + parts[2].trim());
                System.out.println("Status: "      + parts[3].trim());
                System.out.println("-------------");
            }
        }
    }
}