package Service;

import constants.AppConstants;
import enums.ProjectStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static Service.AdminService.scanner;

public class MemberService {
    public static void UpdateTaskStatus() {
        scanner.nextLine(); // buffer temizleme
        File file_progress = new File(AppConstants.FILE_PATH, AppConstants.Progress_file);
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
                    System.out.println("Write new status: 1. Not Started, 2. In Progress, 3. Completed,4.On Hold,5.Cancelled");
                    int statusChoice = scanner.nextInt();
                    String newStatus = switch (statusChoice) {
                        case 1 -> ProjectStatus.NOT_STARTED.name();
                        case 2 -> ProjectStatus.IN_PROGRESS.name();
                        case 3 -> ProjectStatus.COMPLETED.name();
                        case 4 -> ProjectStatus.ON_HOLD.name();
                        case 5 -> ProjectStatus.CANCELLED.name();
                        default -> {
                            System.out.println("Invalid choice. Status not updated.");
                            yield parts[3];
                        }
                    };


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
