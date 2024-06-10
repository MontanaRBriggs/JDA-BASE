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
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;
import java.util.stream.Stream;

public class AutoCompleteExample implements ISlashCommandInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.replyEmbeds(Embeds.informationEmbed(
                "This command is used to showcase command autocomplete."
        )).setEphemeral(true).queue();
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("autocompleteexample", "View and use the autocomplete feature.")
                .addOption(OptionType.STRING, "phonetic-letter", "Autocomplete A-E phonetic alphabet letters.", true, true);
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        String[] phoneticAlphabet = new String[] {
                "alpha", "bravo", "charlie", "delta", "echo" // F-Z omitted for brevity.
        };

        // With multiple options, it is  important to check which option is the currently focused option.
        if (commandAutoCompleteInteractionEvent.getFocusedOption().getName().equals("phonetic-letter")) {
            List<Command.Choice> autocompleteOptions = Stream.of(phoneticAlphabet)
                    // The filter determines what will appear based on the user's current input, this filter checks
                    // for what the user's current text starts with and compares it to the choices above.
                    .filter(phoneticLetter -> phoneticLetter.startsWith(commandAutoCompleteInteractionEvent.getFocusedOption().getValue()))
                    .map(phoneticLetter -> new Command.Choice(phoneticLetter, phoneticLetter))
                    .toList();

            commandAutoCompleteInteractionEvent.replyChoices(autocompleteOptions).queue();
        }

        // Other options that support autocomplete should be handled separately. Their handling should likely be
        // abstracted into other private functions to keep this handler function concise.
    }
}
