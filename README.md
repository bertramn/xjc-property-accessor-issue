# Issue with XJC Property Accessor Plugin

Running the XJC compiler with the `-Xpropertyaccessors` plugin will generate Java code that the JAXB runtime cannot process.

The generated Java class has the right property accessor at the top of the class but XJC still adds all annotations to the Java fields.

```java
@XmlRootElement(name = "Model")
@XmlAccessorType(XmlAccessType.PROPERTY)  <-- set correctly
@XmlType(name = "", propOrder = {"code", "status", "description"})
public class Model {

    @XmlElement(name = "Code", required = true) <-- this is in the wrong place
    protected String code;

    <-- needs to go here
    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        this.code = value;
    }

   ...

}
```

There are quite a few posts [here](http://stackoverflow.com/questions/6768544/jaxb-class-has-two-properties-of-the-same-name) and [here](http://stackoverflow.com/questions/12392235/illegalannotationsexception-class-has-two-properties-of-same-name) and specifically the accepted answer in [this one](http://stackoverflow.com/questions/24366814/spring-jaxb-integration-class-has-two-properties-of-the-same-name) suggests that if one puts `XmlAccessType.PROPERTY` onto the class then the fields that contain getters/setters must not be annotated and the annotation moved to the getter/setter instead.

Whilst that makes sense, why would the XJC Property Accessors plugin take care of this, instead of generating faulty code?

```
com.sun.xml.bind.v2.runtime.IllegalAnnotationsException: 6 counts of IllegalAnnotationExceptions

There are two properties named "code" 
	this problem is related to the following location:
		at public java.lang.String com.github.bertramn.issues.Model.getCode()
		at com.github.bertramn.issues.Model
	this problem is related to the following location:
		at protected java.lang.String com.github.bertramn.issues.Model.code
		at com.github.bertramn.issues.Model
  ...
```

Just to exclude the maven plugin, compiling it by hand using the xjc command line executable will produce exact same broken output.

```sh
xjc -extension -Xpropertyaccessors -verbose -d target/generated-sources/xjc src/main/resources/xsd/Model.xsd`
```