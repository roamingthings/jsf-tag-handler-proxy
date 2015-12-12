# JSF proxying tag handler example

This project demonstrates how to implement a JSF 2.2 Facelets tag handler that dynamically creates a (composite) component and passes some attributes to it.

### Important artifacts

`de.roamingthings.jsf.components.tag.HelloProxyTagHandler`
:   The tag handler implementation responsible for creating and configuring the actual component.

`de.roamingthings.jsf.components.cc.HelloCompositeComponent`
:   The backing bean implementation of the composite component created by the tag handler.

`/WEB-INF/hello.taglib.xml`
:   Deployment descriptor for the tag lib.

