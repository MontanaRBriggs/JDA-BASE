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

package dev.montanarbriggs.jdabase.commands;

import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import dev.montanarbriggs.jdabase.utility.Embeds;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Uptime implements ISlashCommandInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.deferReply(true).queue();

        String formattedUptime = getFormattedUptime();

        slashCommandInteractionEvent.getHook().editOriginalEmbeds(Embeds.informationEmbed(String.format(
                "JDA-BASE has been online for %s.", formattedUptime
        ))).queue();
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("uptime", "Display the bot's current uptime.");
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        throw new UnsupportedOperationException("This command does not support CommandAutoCompleteInteractionEvent(s).");
    }

    private String getFormattedUptime() {
        List<String> timeUnitStrings = getTimeUnitStrings();

        final int listSize = timeUnitStrings.size();

        if (listSize == 1) { // Handle an optimization case where no string building is necessary.
            return timeUnitStrings.get(0);
        }

        if (listSize == 2) { // Handle a grammatical case where no comma is required before "and."
            return String.format("%s and %s", timeUnitStrings.get(0), timeUnitStrings.get(1));
        }

        StringBuilder stringBuilder = new StringBuilder();

        // "(x) (unit), (y) (unit), and (z) (unit)."
        for (int idx = 0; idx < listSize; ++idx) {
            stringBuilder.append(timeUnitStrings.get(idx));

            if (idx < (listSize - 2)) {
                stringBuilder.append(", ");
            } else if (idx == (listSize - 2)) {
                stringBuilder.append(", and ");
            }
        }

        return stringBuilder.toString();
    }

    private List<String> getTimeUnitStrings() {
        // This command technically returns the uptime of the JVM itself, as it is simpler.
        Duration uptimeDuration = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());

        long days = uptimeDuration.toDays();
        long hours = uptimeDuration.toHours() % 24;
        long minutes = uptimeDuration.toMinutes() % 60;
        long seconds = uptimeDuration.toSeconds() % 60;

        List<String> formattedUnitStrings = new ArrayList<>();

        // Append a grammatical 's' if (timeUnit > 1).
        if (days > 0) formattedUnitStrings.add(days + " day" + (days > 1 ? "s" : ""));             // (n) day/s.
        if (hours > 0) formattedUnitStrings.add(hours + " hour" + (hours > 1 ? "s" : ""));         // (n) hour/s.
        if (minutes > 0) formattedUnitStrings.add(minutes + " minute" + (minutes > 1 ? "s" : "")); // (n) minute/s.
        if (seconds > 0) formattedUnitStrings.add(seconds + " second" + (seconds > 1 ? "s" : "")); // (n) second/s.

        return formattedUnitStrings;
    }
}