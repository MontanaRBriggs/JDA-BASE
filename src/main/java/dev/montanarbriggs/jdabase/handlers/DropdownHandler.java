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

import dev.montanarbriggs.jdabase.commands.DropdownExample;
import dev.montanarbriggs.jdabase.interfaces.IEntitySelectInteraction;
import dev.montanarbriggs.jdabase.interfaces.IStringSelectInteraction;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DropdownHandler extends ListenerAdapter {
    private final Map<String, IEntitySelectInteraction> entitySelectInteractionMap = new HashMap<>();
    private final Map<String, IStringSelectInteraction> stringSelectInteractionMap = new HashMap<>();

    public DropdownHandler() {
        initEntitySelectDropdownMap();
        initStringSelectDropdownMap();
    }

    private void registerEntitySelectDropdowns(IEntitySelectInteraction entitySelectInteraction) {
        entitySelectInteraction.getEntityDropdownIds().forEach(entityDropdownId ->
            entitySelectInteractionMap.put(entityDropdownId, entitySelectInteraction)
        );
    }

    private void initEntitySelectDropdownMap() {
        registerEntitySelectDropdowns(new DropdownExample());
    }

    private void registerStringSelectDropdowns(IStringSelectInteraction stringSelectInteraction) {
        stringSelectInteraction.getStringDropdownIds().forEach(stringDropdownId ->
                stringSelectInteractionMap.put(stringDropdownId, stringSelectInteraction)
        );
    }

    private void initStringSelectDropdownMap() {
        registerStringSelectDropdowns(new DropdownExample());
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent entitySelectInteractionEvent) {
        IEntitySelectInteraction entitySelectInteraction =
                entitySelectInteractionMap.get(entitySelectInteractionEvent.getComponentId());

        if (Objects.nonNull(entitySelectInteraction)) { // Sanity check, as this should ALWAYS be true.
            entitySelectInteraction.handleEntitySelectInteraction(entitySelectInteractionEvent);
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent stringSelectInteractionEvent) {
        IStringSelectInteraction stringSelectInteraction =
                stringSelectInteractionMap.get(stringSelectInteractionEvent.getComponentId());

        if (Objects.nonNull(stringSelectInteraction)) { // Sanity check, as this should ALWAYS be true.
            stringSelectInteraction.handleStringSelectInteraction(stringSelectInteractionEvent);
        }
    }
}
