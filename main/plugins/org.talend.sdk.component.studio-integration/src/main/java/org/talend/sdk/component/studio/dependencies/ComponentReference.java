package org.talend.sdk.component.studio.dependencies;

public class ComponentReference {
    private final String family;

    private final String name;

    private final String mavenReferences;

    public ComponentReference(String family, String name, String mavenReferences) {
        this.family = family;
        this.name = name;
        this.mavenReferences = mavenReferences;
    }

    public String getFamily() {
        return family;
    }

    public String getName() {
        return name;
    }

    public String getMavenReferences() {
        return mavenReferences;
    }
}
