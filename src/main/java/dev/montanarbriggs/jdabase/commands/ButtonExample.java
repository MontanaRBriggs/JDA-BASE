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

import dev.montanarbriggs.jdabase.interfaces.IButtonInteraction;
import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import dev.montanarbriggs.jdabase.utility.Embeds;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class ButtonExample implements ISlashCommandInteraction, IButtonInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.replyEmbeds(Embeds.informationEmbed(
                "Hello, here are some buttons!"
        )).addActionRow(
                Button.primary("examplebuttonone", "Example 01"),
                Button.primary("examplebuttontwo", "Example 02"),
                // As per the documentation, link buttons do NOT send ButtonInteractionEvent(s).
                Button.link("https://github.com/MontanaRBriggs/JDA-BASE", "GitHub")
        ).setEphemeral(true).queue();
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("buttonexample", "View and use different buttons!");
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        throw new UnsupportedOperationException("This command does not support CommandAutoCompleteInteractionEvent(s).");
    }

    @Override
    public void handleButtonInteraction(ButtonInteractionEvent buttonInteractionEvent) {
        switch (buttonInteractionEvent.getComponentId()) {
            case "examplebuttonone": {
                handleExampleButtonOne(buttonInteractionEvent);

                break;
            } case "examplebuttontwo": {
                handleExampleButtonTwo(buttonInteractionEvent);

                break;
            } default: {
                buttonInteractionEvent.replyEmbeds(Embeds.errorEmbed(
                        "Uh oh! Invalid Button ID was passed to handleButtonInteraction!"
                )).setEphemeral(true).queue();
            }
        }
    }

    @Override
    public List<String> getButtonIds() {
        return List.of("examplebuttonone", "examplebuttontwo");
    }

    private void handleExampleButtonOne(ButtonInteractionEvent buttonInteractionEvent) {
        buttonInteractionEvent.replyEmbeds(Embeds.informationEmbed(
                "You clicked Button 01!"
        )).setEphemeral(true).queue();
    }

    private void handleExampleButtonTwo(ButtonInteractionEvent buttonInteractionEvent) {
        buttonInteractionEvent.replyEmbeds(Embeds.informationEmbed(
                "You clicked Button 02!"
        )).setEphemeral(true).queue();
    }
}
