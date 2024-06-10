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

package dev.montanarbriggs.jdabase.handlers;

import dev.montanarbriggs.jdabase.commands.ButtonExample;
import dev.montanarbriggs.jdabase.commands.DropdownExample;
import dev.montanarbriggs.jdabase.commands.ModalExample;
import dev.montanarbriggs.jdabase.commands.Ping;
import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SlashCommandHandler extends ListenerAdapter {
    private final Map<String, ISlashCommandInteraction> slashCommandMap = new HashMap<>();

    public SlashCommandHandler(JDA jdaObject) {
        initSlashCommandMap();

        // TODO: https://github.com/discord-jda/JDA/wiki/Interactions#slash-commands - Do not call on startup.
        jdaObject.updateCommands().addCommands(slashCommandMap.values().stream()
                .map(ISlashCommandInteraction::getSlashCommandData)
                .collect(Collectors.toList())
        ).queue();
    }

    private void registerSlashCommand(ISlashCommandInteraction slashCommandInteractionEvent) {
        slashCommandMap.put(slashCommandInteractionEvent.getSlashCommandData().getName(), slashCommandInteractionEvent);
    }

    private void initSlashCommandMap() {
        registerSlashCommand(new Ping());
        registerSlashCommand(new ModalExample());
        registerSlashCommand(new ButtonExample());
        registerSlashCommand(new DropdownExample());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent slashCommandInteractionEvent) {
        ISlashCommandInteraction slashCommandInteraction = slashCommandMap.get(slashCommandInteractionEvent.getName());

        if (Objects.nonNull(slashCommandInteraction)) { // Sanity check, as this should ALWAYS be true.
            slashCommandInteraction.handleSlashCommandInteraction(slashCommandInteractionEvent);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        // TODO: Handle CommandAutoCompleteInteraction events.

        super.onCommandAutoCompleteInteraction(commandAutoCompleteInteractionEvent);
    }
}
