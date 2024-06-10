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

package dev.montanarbriggs.jdabase.interfaces;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.List;

public interface IButtonInteraction {
    // If multiple buttons are used, a call to buttonInteractionEvent.getComponentId() will be required to differentiate
    // between the buttons in an implementation of this interface. The best solution may be a switch/case on button IDs
    // which then calls an abstracted function that handles the event on a per-button basis.

    // TODO: Documentation surrounding the behavior above will be necessary.
    void handleButtonInteraction(ButtonInteractionEvent buttonInteractionEvent);

    // Get a list of strings for the button IDs that the implementer of this interface is handling. See comments
    // above for details on how multiple buttons within one implementation should be handled.
    List<String> getButtonIds();
}
