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

import dev.montanarbriggs.jdabase.interfaces.IModalInteraction;
import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import dev.montanarbriggs.jdabase.utility.Embeds;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class ModalExample implements ISlashCommandInteraction, IModalInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        // Ensure your IDs match, otherwise NullPointerException will be thrown!
        int modalSelection = slashCommandInteractionEvent.getOption("modal").getAsInt();

        if (modalSelection < 1 || modalSelection > 2) {
            slashCommandInteractionEvent.replyEmbeds(Embeds.errorEmbed(
                    "Please select between Modal 01 (1) and 02 (2)."
            )).setEphemeral(true).queue();

            return;
        } else {
            TextInput userName = TextInput.create("name", "What's your name?", TextInputStyle.SHORT)
                    .setPlaceholder("Enter your name here!")
                    .setMaxLength(32)
                    .setMinLength(1)
                    .build();

            // The modal ID here must match the one provided in getModalIds!
            Modal exampleModalOne = Modal.create("examplemodalone", "Example Modal 01")
                    .addComponents(ActionRow.of(userName))
                    .build();

            if (modalSelection == 1) {
                slashCommandInteractionEvent.replyModal(exampleModalOne).queue();
            } else {
                TextInput userStory = TextInput.create("story", "Write me a story!", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Write a story!")
                        .setMaxLength(1000)
                        .setMinLength(1)
                        .build();

                // The modal ID here must match the one provided in getModalIds!
                Modal exampleModalTwo = Modal.create("examplemodaltwo", "Example Modal 02")
                        .addComponents(ActionRow.of(userName), ActionRow.of(userStory))
                        .build();

                slashCommandInteractionEvent.replyModal(exampleModalTwo).queue();
            }
        }
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("modalexample", "Choose between two different modal examples, handled by one command!")
                .addOption(OptionType.INTEGER, "modal", "Choose between Example Modal 01 and 02.");
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        throw new UnsupportedOperationException("This command does not support CommandAutoCompleteInteractionEvent(s).");
    }

    @Override
    public void handleModalInteraction(ModalInteractionEvent modalInteractionEvent) {
        switch (modalInteractionEvent.getModalId()) {
            case "examplemodalone": {
                handleExampleModalOne(modalInteractionEvent);

                break;
            } case "examplemodaltwo": {
                handleExampleModalTwo(modalInteractionEvent);

                break;
            } default: {
                modalInteractionEvent.replyEmbeds(Embeds.errorEmbed(
                        "Uh oh! Invalid Modal ID was passed to handleModalInteraction!"
                )).queue();

                break;
            }
        }
    }

    @Override
    public List<String> getModalIds() {
        return List.of("examplemodalone", "examplemodaltwo");
    }

    private void handleExampleModalOne(ModalInteractionEvent modalInteractionEvent) {
        // Ensure your IDs match, otherwise NullPointerException will be thrown!
        String userName = modalInteractionEvent.getValue("name").getAsString();

        modalInteractionEvent.replyEmbeds(Embeds.informationEmbed(String.format(
                "Hello, %s, nice to meet you!", userName
        ))).setEphemeral(true).queue();
    }

    private void handleExampleModalTwo(ModalInteractionEvent modalInteractionEvent) {
        // Ensure your IDs match, otherwise NullPointerException will be thrown!
        String userName = modalInteractionEvent.getValue("name").getAsString();
        String userStory = modalInteractionEvent.getValue("story").getAsString();

        modalInteractionEvent.replyEmbeds(Embeds.informationEmbed(String.format(
                "Hello, %s, nice to meet you! Here's a copy of that story! ```%s```", userName, userStory
        ))).setEphemeral(true).queue();
    }
}
