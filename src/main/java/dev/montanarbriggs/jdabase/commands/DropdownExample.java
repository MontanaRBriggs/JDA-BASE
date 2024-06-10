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

import dev.montanarbriggs.jdabase.interfaces.IEntitySelectInteraction;
import dev.montanarbriggs.jdabase.interfaces.ISlashCommandInteraction;
import dev.montanarbriggs.jdabase.interfaces.IStringSelectInteraction;
import dev.montanarbriggs.jdabase.utility.Embeds;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;

public class DropdownExample implements ISlashCommandInteraction, IEntitySelectInteraction, IStringSelectInteraction {
    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.replyEmbeds(Embeds.informationEmbed(
                "Hello, here are some dropdown menus!"
        )).addActionRow(
                EntitySelectMenu.create("exampledropdownone", SelectTarget.USER)
                        .setMaxValues(1)
                        .build()
        ).addActionRow(
                StringSelectMenu.create("exampledropdowntwo")
                        .addOption("Option 01", "optionone", "The first selectable option.")
                        .addOption("Option 02", "optiontwo", "The second selectable option.")
                        .setMaxValues(1)
                        .build()
        ).setEphemeral(true).queue();
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash("dropdownexample", "View and use different dropdown menus!");
    }

    @Override
    public void handleCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent commandAutoCompleteInteractionEvent) {
        throw new UnsupportedOperationException("This command does not support CommandAutoCompleteInteractionEvent(s).");
    }

    @Override
    public void handleEntitySelectInteraction(EntitySelectInteractionEvent entitySelectInteractionEvent) {
        List<User> selectedUsers = entitySelectInteractionEvent.getMentions().getUsers();

        entitySelectInteractionEvent.replyEmbeds(Embeds.informationEmbed(String.format(
                "You selected %s.", selectedUsers.get(0).getEffectiveName()
        ))).setEphemeral(true).queue();
    }

    @Override
    public List<String> getEntityDropdownIds() {
        return List.of("exampledropdownone");
    }

    @Override
    public void handleStringSelectInteraction(StringSelectInteractionEvent stringSelectInteractionEvent) {
        stringSelectInteractionEvent.replyEmbeds(Embeds.informationEmbed(String.format(
                "You selected %s", stringSelectInteractionEvent.getValues().get(0)
        ))).setEphemeral(true).queue();
    }

    @Override
    public List<String> getStringDropdownIds() {
        return List.of("exampledropdowntwo");
    }
}
