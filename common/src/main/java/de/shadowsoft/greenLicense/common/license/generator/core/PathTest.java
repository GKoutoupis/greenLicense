package de.shadowsoft.greenLicense.common.license.generator.core;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class PathTest {
    @Test
    public String[] DetectDevices() {
        String[] signatures = new String[0];
        try {
            Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
            System.out.println(path);
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "usbdump64.exe");
            builder.directory(new File(path + "/src/main/java/de/shadowsoft/greenLicense/common/license/generator/core"));
            Process p = builder.start();
            Charset inputCharset = StandardCharsets.ISO_8859_1;
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), inputCharset));
            String line = r.readLine();
            int sigs = 0;
            sigs = Integer.parseInt(
                    line.replaceAll("[^0-9]", "")
            );
            if (sigs > 0) {
                signatures = new String[sigs];
                for (int i = 0; i < sigs; i++) {
                    line = r.readLine();
                    signatures[i] = line;
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return signatures;
    }
}
