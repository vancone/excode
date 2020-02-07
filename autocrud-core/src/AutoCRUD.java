import controller.Logger;
import controller.ProjectBuilder;

public class AutoCRUD {
    public static void main(String[] args) {
        if (args.length == 0) {
            Logger.error("Project path required.");
        }
        final String projectUrl = args[0];
        ProjectBuilder projectBuilder = new ProjectBuilder(projectUrl);
        projectBuilder.build();
    }
}