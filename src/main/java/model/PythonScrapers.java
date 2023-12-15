package model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PythonScrapers {
    public void runPythonScript(String scriptPath, List<Object> args) throws IOException {
        try {
            // Build the command to run the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath);

            // Append each argument to the command
            for (Object arg : args) {
                processBuilder.command().add(arg.toString());
            }

            // Start and wait for the process to finish
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
