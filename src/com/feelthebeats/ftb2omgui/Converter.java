package com.feelthebeats.ftb2omgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JTextArea;

public class Converter {

    public static final String ERROR_ARGUMENTS = "Required arguments: [input file] [output file] [BPM]";
    public static final String ERROR_FILETYPE = "ERROR: Invalid file, is the file a .txt Feel The Beats difficulty?";
    public static final String ERROR_IO = "ERROR: File not found, or error opening file.";
    public static final String ERROR_BPM = "ERROR: BPM is not a number.";

    public static final String MSG_GREET = "ftb->osumania converter by JS. js.nexen@gmail.com";
    public static final String MSG_SCANNING = "Scanning input file...";
    public static final String MSG_SCANNING_DONE = "Done scanning. Generating converted text...";
    public static final String MSG_DONE = "Done! Now make a new file in osu if you haven't already, and paste the text in.";

    private static File inFile;
    private static File outFile;
    private static BufferedReader reader;
    private static PrintWriter writer;

    private static double mainBpm;

    private static LinkedList<BPM> bpms;
    private static LinkedList<Note> notes;

    public void Convert(String input, String output, String BPM, JTextArea textArea) {
        textArea.append(MSG_GREET + "\n");
        bpms = new LinkedList<BPM>();
        notes = new LinkedList<Note>();

        if (input.isEmpty() || output.isEmpty() || BPM.isEmpty()) {
            textArea.append(ERROR_ARGUMENTS + "\n");
        } else {
            try {
                mainBpm = Double.parseDouble(BPM);
                inFile = new File(input);
                outFile = new File(output);
                reader = new BufferedReader(new FileReader(inFile));
                writer = new PrintWriter(outFile);

                try {
                    textArea.append(MSG_SCANNING + "\n");
                    String readLine;
                    while ((readLine = reader.readLine()) != null) {
                        if (readLine.startsWith("###")) {
                            continue;
                        } else if (readLine.startsWith("BPM")) {
                            String[] bpmData = readLine.split(" ");
                            bpms.add(new BPM(
                                    (int) Math.round(Double.parseDouble(bpmData[1])),
                                    mainBpm * Double.parseDouble(bpmData[2]) / 120
                            ));
                        } else {
                            String[] noteData = readLine.split(" ");
                            String[] noteTimeData = noteData[0].split("-");
                            double beatLength = 0;
                            int noteTimeStart = (int) Math.round(Double.parseDouble(noteTimeData[0]));
                            if (noteTimeData.length > 1) {
                                int noteTimeEnd = (int) Math.round(Double.parseDouble(noteTimeData[1]));
                                boolean bpmFound = false;
                                for (Iterator<BPM> bpmIterator = bpms.descendingIterator(); bpmIterator.hasNext();) {
                                    BPM bpm = bpmIterator.next();
                                    if (bpm.getTime() <= noteTimeStart) {
                                        double noteTimeLength = (noteTimeEnd - noteTimeStart);
                                        beatLength = noteTimeLength * bpm.getValue() / 60000;
                                        bpmFound = true;
                                    }
                                    if (bpmFound) {
                                        break;
                                    }
                                }
                            }
                            notes.add(new Note(
                                    noteTimeStart,
                                    Integer.parseInt(noteData[2]),
                                    beatLength
                            ));
                        }
                    }
                    textArea.append(MSG_SCANNING_DONE + "\n");
                    
                    //Adds "General" Section to the difficulty file
                    AppendGeneralInfo(writer);
                    
                    //Converts the file                    
                    writer.println("[TimingPoints]");
                    while (!bpms.isEmpty()) {
                        BPM bpm = bpms.remove();
                        writer.println(bpm.getTime() + ","
                                + 60000 / ((bpm.getValue() != 0) ? bpm.getValue() : 0.01)
                                + ",4,1,0,100,1,0"
                        );
                    }
                    writer.println();
                    writer.println("[HitObjects]");
                    while (!notes.isEmpty()) {
                        Note note = notes.remove();
                        int noteX = (512 / 14) * ((note.getColumn() - 1) * 2 + 1);
                        int noteY = 192;
                        writer.println(noteX + ","
                                + noteY + ","
                                + note.getTime()
                                + ((note.getBeatLength() > 0)
                                ? (",2,0,B|" + noteX + ":32,8," + note.getBeatLength() * 70 / 4) : ",1,0")
                        );
                    }
                    textArea.append(MSG_DONE + "\n");
                } catch (NumberFormatException | IOException e) {
                    textArea.append(ERROR_FILETYPE + "\n");
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException ex) {
                        textArea.append(ERROR_IO + "\n");
                    }
                }
            } catch (IOException ex) {
                textArea.append(ERROR_IO + "\n");
            } catch (NumberFormatException ex) {
                textArea.append(ERROR_BPM + "\n");
            }
        }
    }
    private void AppendGeneralInfo(PrintWriter writer) throws IOException {
        InputStream generalFile = getClass().getResourceAsStream("../resources/General");
        InputStreamReader isr = new InputStreamReader(generalFile);
        BufferedReader read = new BufferedReader(isr);
        String line;
        while ((line = read.readLine()) != null) {
            writer.println(line);
        }     
        read.close();
    }
}
