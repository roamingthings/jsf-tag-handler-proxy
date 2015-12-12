package de.roamingthings.hello.presentation;

import javax.enterprise.inject.Model;

/**
 * A backing bean for the test page.
 */
@Model
public class HelloPageBean {
    /** Just a name property. */
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
