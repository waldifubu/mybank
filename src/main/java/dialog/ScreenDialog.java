package dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public abstract class ScreenDialog extends JDialog {

    private String methodName;
    private HashMap<String, String> mapping = new HashMap<String, String>();

    public ScreenDialog(Frame frame, boolean modal) {
        super(frame, modal);
    }


    private static final long serialVersionUID = -2064520509083773888L;

//    public abstract void addConfirmListener(ActionListener listener);

    /*
    public void set(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public HashMap<String, String> getMap() {
        return map;
    }
*/
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public HashMap<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(HashMap<String, String> mapping) {
        this.mapping = mapping;
    }
}
