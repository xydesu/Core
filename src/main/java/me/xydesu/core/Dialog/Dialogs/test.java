package me.xydesu.core.Dialog.Dialogs;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.xydesu.core.Dialog.DialogManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class test extends DialogManager {
    @Override
    public String getID() {
        return "test";
    }

    @Override
    public Dialog getDialog() {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Configure your new experience value"))
                        .inputs(List.of(
                                DialogInput.numberRange("level", Component.text("Level", NamedTextColor.GREEN), 0f, 100f)
                                        .step(1f)
                                        .initial(0f)
                                        .width(300)
                                        .build(),
                                DialogInput.numberRange("experience", Component.text("Experience", NamedTextColor.GREEN), 0f, 100f)
                                        .step(1f)
                                        .initial(0f)
                                        .labelFormat("%s: %s percent to the next level")
                                        .width(300)
                                        .build()
                        ))
                        .build()
                )
                .type(DialogType.confirmation(
                        ActionButton.create(
                                Component.text("Confirm", TextColor.color(0xAEFFC1)),
                                Component.text("Click to confirm your input."),
                                100,
                                DialogAction.customClick(Key.key("papermc:user_input/confirm"), null)
                        ),
                        ActionButton.create(
                                Component.text("Discard", TextColor.color(0xFFA0B1)),
                                Component.text("Click to discard your input."),
                                100,
                                null // If we set the action to null, it doesn't do anything and closes the dialog
                        )
                ))
        );

        return dialog;
    }

}
