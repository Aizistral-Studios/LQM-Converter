package com.aizistral.lqmconverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LQMConverter {

    public static void main(String... args) {
        new ConverterGUI();
    }

    public static void convert(String inputPath, String outputPath, ConverterGUI gui, JProgressBar bar) throws Exception {
        bar.setString("Evaluating files...");
        bar.setMaximum(100);
        bar.setValue(0);

        trySleep(200);

        File input = Paths.get(inputPath).toFile();
        File output = Paths.get(outputPath).toFile();
        List<File> inputs = new ArrayList<>();

        if (!input.exists())
            throw new IllegalArgumentException("Input file " + input.getName() + " does not exist");

        if (input.isDirectory()) {
            inputs.addAll(FileUtils.listFiles(input, new String[] { "lqm" }, false));
        } else if (input.getName().endsWith(".lqm")) {
            inputs.add(input);
        } else
            throw new IllegalArgumentException("File " + input.getName() + " is not an .lqm file, nor directory");

        bar.setMaximum(inputs.size());
        output.mkdirs();

        for (File lqmFile : inputs) {
            bar.setString("Reading " + lqmFile.getName());
            trySleep(10);

            try (ZipFile zip = new ZipFile(lqmFile)) {
                InputStream stream = zip.getInputStream(zip.getEntry("memoinfo.jlqm"));
                String text = "";
                boolean noError = true;

                try {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
                    JsonArray array = (JsonArray) jsonObject.get("MemoObjectList");

                    for (int i = 0; i< array.size(); i++) {
                        JsonElement element = ((JsonObject) array.get(i)).get("DescRaw");

                        if (element != null) {
                            String raw = element.getAsString();
                            if (!text.equals("")) {
                                text += System.lineSeparator() + System.lineSeparator() + System.lineSeparator();

                            }

                            text += raw;
                        }
                    }
                } catch (Exception ex) {
                    noError = false;
                    showException(gui, "Unexpected error when parsing " + lqmFile.getName() + ":", ex);
                }

                if (noError) {
                    File outputFile = new File(output, lqmFile.getName().substring(0, lqmFile.getName().length()-4) + ".txt");
                    bar.setString("Writing " + outputFile.getName());
                    trySleep(10);
                    FileUtils.write(outputFile, text, Charset.forName("UTF-8"));
                }
            } catch (IOException ex) {
                showException(gui, "Unexpected error when processing " + lqmFile.getName() + ":", ex);
            } finally {
                bar.setValue(bar.getValue() + 1);
            }
        }

        trySleep(500);
        bar.setString("Done!");
    }

    static void showException(ConverterGUI gui, String message, Exception exception) {
        message += System.lineSeparator() + exception;

        for (StackTraceElement element : exception.getStackTrace()) {
            message += System.lineSeparator() + element.toString();
        }

        JOptionPane.showMessageDialog(gui, message, "Encountered Exception", JOptionPane.ERROR_MESSAGE);
    }

    static void trySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            // NO-OP
        }
    }

}
