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
import dev.montanarbriggs.jdabase.interfaces.IButtonInteraction;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ButtonHandler extends ListenerAdapter {
    private final Map<String, IButtonInteraction> buttonInteractionMap = new HashMap<>();

    public ButtonHandler() {
        initButtonMap();
    }

    private void registerButtons(IButtonInteraction buttonInteractionEvent) {
        buttonInteractionEvent.getButtonIds().forEach(buttonId ->
            buttonInteractionMap.put(buttonId, buttonInteractionEvent)
        );
    }

    private void initButtonMap() {
        registerButtons(new ButtonExample());
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent buttonInteractionEvent) {
        IButtonInteraction buttonInteraction = buttonInteractionMap.get(buttonInteractionEvent.getComponentId());

        if (Objects.nonNull(buttonInteraction)) { // Sanity check, as this should ALWAYS be true.
            buttonInteraction.handleButtonInteraction(buttonInteractionEvent);
        }
    }
}
