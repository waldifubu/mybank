package model;

public enum ApplicationVariant {
    DESKTOP("desktop"), CONSOLE("console");

    public final String label;

    ApplicationVariant(String label) {
        this.label = label;
    }

    public static ApplicationVariant valueOfLabel(String label) {
        for (ApplicationVariant e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }

        return null;
    }
}
