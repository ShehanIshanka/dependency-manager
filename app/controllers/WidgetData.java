package controllers;

import play.data.validation.Constraints;

/**
 * A form processing DTO that maps to the widget form.
 *
 * Using a class specifically for form binding reduces the chances
 * of a parameter tampering attack and makes code clearer, because
 * you can define constraints against the class.
 */
public class WidgetData {

    private String name;

    public WidgetData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
