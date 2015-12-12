package de.roamingthings.jsf.components.cc;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Backing bean for the composite component.
 *
 * This implementation will handle the values set as properties and forward all pass through attributes to the input
 * component.
 */
@FacesComponent(value = "de.roamingthings.cc.helloCC")
public class HelloCompositeComponent extends UINamingContainer {

    /** Input component of the composite. */
    protected UIInput inputComponent;

    /** Public property keys of the component. */
    enum PropertyKeys {
        helloName
    }

    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }

    /**
     *  Name to be printed out.
     *
     *  This method returns "Mr. X" if the attribute has not been set.
     */
    public String getHelloName() {
        return (String) getStateHelper().eval(PropertyKeys.helloName, "Mr. X");
    }

    public void setHelloName(String name) {
        getStateHelper().put(PropertyKeys.helloName, name);
    }

    public UIInput getInputComponent() {
        return inputComponent;
    }

    public void setInputComponent(UIInput inputComponent) {
        this.inputComponent = inputComponent;
    }

    /**
     * Passes the pass through attributes to the bound {@link #inputComponent} component.
     *
     * {@see UINamingContainer#encodeBegin}
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        // Copy all pass through attributes from this component to the input if set
        if (inputComponent != null) {
            inputComponent.getPassThroughAttributes().putAll(getPassThroughAttributes());
        }

        super.encodeBegin(context);
    }
}
