package me.xydesu.core.Dialog;

import io.papermc.paper.dialog.Dialog;
import me.xydesu.core.Dialog.Dialogs.test;

import java.util.HashMap;
import java.util.Map;

public abstract class DialogManager {

    private static final Map<String, DialogManager> dialogs = new HashMap<>();

    static {
        dialogs.put("test", new test());
    }
    
    public static DialogManager get(String id) {
        return dialogs.get(id);
    }
    
    public abstract String getID();
    public abstract Dialog getDialog();
}
