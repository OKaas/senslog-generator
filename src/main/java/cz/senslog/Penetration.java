package cz.senslog;

public class Penetration {
    public static void main(final String[] args) {
        int numberOfSimultaneousExecutions = Integer.parseInt(args[0]);
        java.util.concurrent.Executor executor = java.util.concurrent.Executors.newFixedThreadPool(numberOfSimultaneousExecutions);
        for (int i = 0; i < numberOfSimultaneousExecutions; i++) {
            executor.execute(() -> Application.main(args));
        }
    }

}
