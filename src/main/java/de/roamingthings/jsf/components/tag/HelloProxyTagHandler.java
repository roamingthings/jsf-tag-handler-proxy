package de.roamingthings.jsf.components.tag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.facelets.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link TagHandler} implementation for the proxy tag.
 * <p>
 * This {@link TagHandler} will create a component as specified by the {@code componentNameSpace} and  {@code tagName}
 * attributes and add it into the view hierarchy at the current location as a child.
 * <p>
 * After the component has been created the {@code id} of the newly created component is set to the value set in the
 * {@code id} attribute.
 * <p>
 * At last the value of the {@code helloName} attribute and the {@code value} attributes are set on the new component
 * if present.
 */
public class HelloProxyTagHandler extends TagHandler {
    private static final Logger log = Logger.getLogger(HelloProxyTagHandler.class.getName());

    /** Default namespace for the composite components in this demo application. */
    protected static final String NAMESPACE_COMPOSITE_COMPONENT_TAGS = "http://xmlns.jcp.org/jsf/composite/myTaglib";

    /** Namespace of the pass through attributes. */
    protected static final String NAMESPACE_PASSTHROUGH = "http://xmlns.jcp.org/jsf/passthrough";

    protected final TagAttribute helloNameAttr;
    protected final TagAttribute idAttr;
    protected final TagAttribute componentNameSpaceAttr;
    protected final TagAttribute componentTagNameAttr;
    protected final TagAttribute valueAttr;

    protected final TagAttribute[] passThroughAttributes;

    /**
     * Initialize the tag and get all required, pass thorough and optional attributes.
     *
     * @see TagHandler
     */
    public HelloProxyTagHandler(TagConfig config) {
        super(config);

        // Get and store all pass through attributes
        Tag tag = config.getTag();
        TagAttributes attributes = tag.getAttributes();
        passThroughAttributes = attributes.getAll(NAMESPACE_PASSTHROUGH);

        // Get the required attributes
        this.idAttr = getRequiredAttribute("id");
        this.componentTagNameAttr = getRequiredAttribute("componentTagName");
        this.valueAttr = getRequiredAttribute("value");

        // Get all other attributes
        this.helloNameAttr = getAttribute("helloName");
        this.componentNameSpaceAttr = getAttribute("componentNameSpace");
    }

    /**
     * Get all pass through attributes from the tag.
     *
     * @param ctx Context to be used
     * @return Pass through attributes from the tag as a {@link Map}
     */
    protected Map<String, Object> getPassThroughAttributes(FaceletContext ctx) {
        Map<String, Object> resultMap =
                Stream.of(passThroughAttributes)
                        .collect(Collectors.toMap(TagAttribute::getLocalName, tagAttribute -> tagAttribute.getObject(ctx)));

        return resultMap;
    }

    /**
     * Create the {@link UIComponent} in the given {@link FacesContext} using {@code namespace} and {@code tagName}.
     *
     * @param namespace    Namespace of the component
     * @param tagName      Tag name of the component
     * @param attributes   Attributes to be set during creation {@see ViewDeclarationLanguage#createComponent}
     * @param facesContext Context providing the {@link ViewDeclarationLanguage}
     * @return
     */
    protected UIComponent createComponent(
            String namespace,
            String tagName,
            Map<String, Object> attributes,
            FacesContext facesContext) {
        // Now use namespace and tag name to create a new component
        final ViewDeclarationLanguage
                vdl = facesContext.getApplication().getViewHandler()
                .getViewDeclarationLanguage(facesContext, facesContext.getViewRoot().getViewId());

        // Create the actual input component by
        UIComponent createdComponent = vdl.createComponent(facesContext,
                namespace,
                tagName,
                attributes);

        return createdComponent;
    }

    /**
     * Generate the component, configure and add it to the given {@code parent}.
     * <p>
     * The component will be created using the namespace as set by the {@code namespace} attribute. If no namespace
     * has been set the namespace {@link #NAMESPACE_COMPOSITE_COMPONENT_TAGS} will be used. The tag name is taken from
     * the {@code tagName} attribute.
     */
    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        // Get tag name and namespace from tag attributes
        final String namespace = (componentNameSpaceAttr != null) ?
                componentNameSpaceAttr.getNamespace() : NAMESPACE_COMPOSITE_COMPONENT_TAGS;
        final String tagName = componentTagNameAttr.getValue();

        // Create the composite component
        final FacesContext facesContext = ctx.getFacesContext();
        final UIComponent createdComponent =
                createComponent(namespace, tagName, null, facesContext);

        // Set pass through attributes
        createdComponent.getPassThroughAttributes().putAll(getPassThroughAttributes(ctx));

        // Important: Set Id of the component for UIForm to handle inputs
        final String id = idAttr.getValue();
        createdComponent.setId(id);

        // Pass the value to the created component
        final ValueExpression valueVE = valueAttr.getValueExpression(ctx, Object.class);
        createdComponent.setValueExpression("value", valueVE);

        // Pass the helloName to the created component
        if (helloNameAttr != null) {
            final ValueExpression helloNameVE = helloNameAttr.getValueExpression(ctx, String.class);
            createdComponent.setValueExpression("helloName", helloNameVE);
        }

        // Add component to te parent
        parent.getChildren().add(createdComponent);

        // Call the next handler
        nextHandler.apply(ctx, parent);
    }
}
