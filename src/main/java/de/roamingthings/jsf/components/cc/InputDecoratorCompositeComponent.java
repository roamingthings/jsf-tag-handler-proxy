package de.roamingthings.jsf.components.cc;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

/**
 * Backing bean implementation for the {@code inputDecorator} composite component.
 *
 * This bean actually does nothing and exists for the sole purpose of demonstrating the composite component having a
 * backing bean implementation.
 */
@FacesComponent(value = "de.roamingthings.cc.inputDecorator")
public class InputDecoratorCompositeComponent extends UINamingContainer {

    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }
}
