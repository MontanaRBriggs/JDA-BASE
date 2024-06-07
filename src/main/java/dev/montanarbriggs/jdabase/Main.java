/*
 * The MIT License (MIT)
 *
 * Copyright © 2024 - Montana R. Briggs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package dev.montanarbriggs.jdabase;

import dev.montanarbriggs.jdabase.configuration.Configuration;
import dev.montanarbriggs.jdabase.handlers.SlashCommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

/*
 * TEMPORARY DOCUMENTATION.
 *
 * DEFINED EXIT CODES:
 *
 * - 0x0 - SUCCESS.
 * - 0x1 - CONFIGURATION EXCEPTION.
 * - 0x2 - TOKEN EXCEPTION.
 * - 0x3 - JDA SHUTDOWN FAILURE.
 * - 0x4 - THREAD INTERRUPTED EXCEPTION.
 */

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            new Configuration(new File("Configuration.json"));
        } catch (IOException configurationLoadException) {
            LOGGER.error("IOException occurred whilst loading Configuration.json.");

            System.exit(0x1);
        }

        try {
            JDA jdaObject = JDABuilder.createDefault(
                    Configuration.getConfigurationInstance().optString("botToken")
            ).build();

            jdaObject.addEventListener(new SlashCommandHandler(jdaObject));

            // TODO: Abstract console command handling.

            Scanner terminalScanner = new Scanner(System.in);

            while (terminalScanner.hasNextLine()) {
                String terminalCommand = terminalScanner.nextLine();

                switch (terminalCommand.toLowerCase()) {
                    case "help": {
                        break; // TODO: Implement terminal 'help' command.
                    } case "shutdown": {
                        Shutdown(jdaObject); // Function logic always calls System.exit(), no break necessary.
                    } default: {
                        LOGGER.error("Unknown terminal command entered, type \"help\" for a list of commands.");
                    }
                }
            }
        } catch (InvalidTokenException invalidTokenException) {
            LOGGER.error("InvalidTokenException occurred whilst initializing JDA, please ensure botToken is valid.");

            System.exit(0x2);
        }
    }

    private static void Shutdown(JDA jdaObject) {
        LOGGER.info("Initializing JDA graceful shutdown.");

        jdaObject.shutdown();

        try {
            if (!jdaObject.awaitShutdown(Duration.ofMinutes(5))) {
                LOGGER.warn("JDA took more than five (5) minutes to shut down gracefully, forcing shutdown.");

                jdaObject.shutdownNow();

                if (jdaObject.awaitShutdown(Duration.ofMinutes(5))) {
                    LOGGER.error("JDA took more than five (5) minutes to forcibly shut down. Terminating process.");

                    System.exit(0x3);
                }
            }
        } catch(InterruptedException threadInterruptedException) {
            LOGGER.error("InterruptedException thrown whilst awaiting JDA shutdown (thread interruption).");

            System.exit(0x4);
        }

        LOGGER.info("Graceful shutdown successful, exiting the JVM.");

        System.exit(0x0);
    }
}
