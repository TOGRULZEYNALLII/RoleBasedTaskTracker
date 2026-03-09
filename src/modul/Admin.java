package modul;
import interfaces.Uuid;

public class Admin implements Uuid {
    private String uuid;
    private String name;
    private String password;

    public Admin(String name, String password) {
        this.uuid = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
