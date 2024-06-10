package dev.montanarbriggs.jdabase.handlers;

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
