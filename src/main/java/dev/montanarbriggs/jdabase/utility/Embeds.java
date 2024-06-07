package dev.montanarbriggs.jdabase.utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Embeds {
    private static final Color INFORMATION_COLOR = Color.decode("#5865F2");
    private static final Color WARNING_COLOR = Color.decode("#888800");
    private static final Color ERROR_COLOR = Color.decode("#880000");

    private static MessageEmbed createEmbed(String message, Color color) {
        return new EmbedBuilder()
                .setDescription(message)
                .setColor(color)
                .build();
    }

    public static MessageEmbed informationEmbed(String informationMessage) {
        return createEmbed(informationMessage, INFORMATION_COLOR);
    }

    public static MessageEmbed warningEmbed(String warningMessage) {
        return createEmbed(warningMessage, WARNING_COLOR);
    }

    public static MessageEmbed errorEmbed(String errorMessage) {
        return createEmbed(errorMessage, ERROR_COLOR);
    }
}
