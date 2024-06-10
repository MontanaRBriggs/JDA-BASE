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

import dev.montanarbriggs.jdabase.commands.ModalExample;
import dev.montanarbriggs.jdabase.interfaces.IModalInteraction;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModalHandler extends ListenerAdapter {
    private final Map<String, IModalInteraction> modalInteractionMap = new HashMap<>();

    public ModalHandler() {
        initModalMap();
    }

    private void registerModals(IModalInteraction modalInteractionEvent) {
        modalInteractionEvent.getModalIds().forEach(modalId ->
                modalInteractionMap.put(modalId, modalInteractionEvent)
        );
    }

    private void initModalMap() {
        registerModals(new ModalExample());
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent modalInteractionEvent) {
        IModalInteraction modalInteraction = modalInteractionMap.get(modalInteractionEvent.getModalId());

        if (Objects.nonNull(modalInteraction)) { // Sanity check, as this should ALWAYS be true.
            modalInteraction.handleModalInteraction(modalInteractionEvent);
        }
    }
}
