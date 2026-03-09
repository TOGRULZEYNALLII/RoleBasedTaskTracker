package modul;

import interfaces.Uuid;

public class Tasks implements Uuid {
    private String uuid;
    private String title;
    private String description;
    private String status;

    public Tasks(String title, String description, String status) {
        this.uuid = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public String getStatus() {
        return status;
    }
}
