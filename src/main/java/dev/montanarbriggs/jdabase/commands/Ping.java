package dev.montanarbriggs.jdabase.commands;

import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import dev.montanarbriggs.jdabase.utility.Embeds;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class Ping implements ISlashCommandInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        long gatewayPing = slashCommandInteractionEvent.getJDA().getGatewayPing();

        slashCommandInteractionEvent.getJDA().getRestPing().queue(restResponsePing -> {
            slashCommandInteractionEvent.replyEmbeds(Embeds.informationEmbed(String.format(
                    "Gateway ping was **%d** milliseconds, and the REST response was **%d** milliseconds.", gatewayPing, restResponsePing
            ))).setEphemeral(true).queue();
        });
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("ping", "Calculate the gateway and REST response ping in milliseconds.");
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        throw new UnsupportedOperationException("This command does not support CommandAutoCompleteInteractionEvent(s).");
    }
}
