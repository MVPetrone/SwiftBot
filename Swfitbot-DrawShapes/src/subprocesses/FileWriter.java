package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class FileWriter {
    private static SwiftBotAPI swiftBot;
	private Prompt prompt = new Prompt();
    private Scanner reader = new Scanner(System.in);
    
    public FileWriter(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;

        start();
    }

    public void start() {
    }
}
