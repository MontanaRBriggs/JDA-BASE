package dev.montanarbriggs.jdabase.commands;

import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import dev.montanarbriggs.jdabase.managers.WebRequestManager;
import dev.montanarbriggs.jdabase.utility.Embeds;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import okhttp3.Headers;

public class WebRequestExample implements ISlashCommandInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.deferReply(true).queue();

        String requestUrl = slashCommandInteractionEvent.getOption("url", OptionMapping::getAsString);

        if (WebRequestManager.isValidUrl(requestUrl)) {
            Headers requestHeaders = Headers.of("User-Agent", "JDA-BASE/1.0-DEVELOPMENT");

            String requestResponse = WebRequestManager.getRequest(requestUrl, requestHeaders);

            if (requestResponse.isEmpty()) {
               slashCommandInteractionEvent.getHook().editOriginalEmbeds(Embeds.errorEmbed(
                       "GET response body was empty, please try again."
               )).queue();

               return;
            }

            requestResponse = requestResponse.substring(0, 2048); // Limit the requestResponse to 2048 characters
                                                                  // 50% of the Discord description embed limit.

            slashCommandInteractionEvent.getHook().editOriginalEmbeds(Embeds.informationEmbed(String.format(
                    "Here's the request's response. ```%s```", requestResponse
            ))).queue();
        } else {
            slashCommandInteractionEvent.getHook().editOriginalEmbeds(Embeds.errorEmbed(
                    "Invalid URL format, please ensure the URL is correctly formatted."
            )).queue();
        }
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("webrequestexample", "Send a GET request to a website!")
                .addOption(OptionType.STRING, "url", "The URL to send a GET request to.", true);
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        throw new UnsupportedOperationException("This command does not support CommandAutoCompleteInteractionEvent(s).");
    }
}
