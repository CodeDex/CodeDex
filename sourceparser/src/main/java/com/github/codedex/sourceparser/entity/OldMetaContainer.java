package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.codedex.sourceparser.entity.OldMetaContainer.Type.PACKAGE;

/**
 * Contains metadata for package.
 */

public class OldMetaContainer {

    public static @NonNull
    OldMetaContainer createContainer(@NonNull Type type, Iterator<String> name, OldMetaContainer parent, @Nullable URL docURL) {
        if (name == null) return OldMetaContainer.getNewRootPackage();
        if (parent == null) parent = OldMetaContainer.getNewRootPackage();
        if (name.hasNext()) {
            String currentName = name.next();
            for (int a = 0; a < parent.getChildPackages().size(); a++) {
                OldMetaContainer child = parent.getChildPackages().get(a);
                if (child.getName().equals(currentName)) {         // If the child package is the one we search for...
                    parent.getChildPackages().remove(child);           // Remove the original child package from parent
                    parent.addChild(createContainer(type, name, child, docURL));   // Add the new one (Basically the same, but recursively updated)
                    return parent;                                               // Return updated parent package
                }
            }
            // If it reaches this point, the package couldn't be found and needs to be created as a child package

            parent.addChild(createContainer(type, name, new OldMetaContainer(PACKAGE, currentName), docURL));
            // What happened here:
            // The parent package got a child package, which is gonna contain its subpackages thanks to recursion (clean code nay, recursion yay)
            return parent;
        }
        parent.setDocURL(docURL);
        return parent;
    }

    private ParentReference data;

    public interface ParentReference {
        OldMetaContainer getParentPackage();
    }

    public enum Type {
        PACKAGE,
        CLASS,
        INTERFACE
    }

    private final Type type;
    private String name;
    private List<OldMetaContainer> packages;
    private List<OldMetaContainer> classes;
    private List<OldMetaContainer> interfaces;
    private URL docURL;

    public void setData(ParentReference data) {
        this.data = data;
    }

    /**
     * Method for getting an instance of a root container for MetaInheritables
     */
    public static @NonNull
    OldMetaContainer getNewRootPackage() {
        return new OldMetaContainer();
    }

    private OldMetaContainer() {
        this.type = PACKAGE;
        this.name = null;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        setData(new ParentReference() {
                @Override
                public OldMetaContainer getParentPackage() {
                    return null;
                }
            });
    }

    // Test
    // When calling this constructor, make sure you declare the parent package some point later
    public OldMetaContainer(@NonNull Type type, @NonNull String name) {
        this(type, name, new ParentReference() {
            @Override
            public OldMetaContainer getParentPackage() {
                return null;
            }
        });
    }

    public OldMetaContainer(@NonNull Type type, @NonNull String name, @Nullable URL docURL) {// When calling this constructor, make sure you declare the parent package some point later
        this(type, name, new ParentReference() {
            @Override
            public OldMetaContainer getParentPackage() {
                return null;
            }
        }, docURL);
    }

    public OldMetaContainer(@NonNull Type type, @NonNull String name, @NonNull ParentReference data) {
        this(type, name, data, null);
    }

    public OldMetaContainer(@NonNull Type type, @NonNull String name, @NonNull ParentReference data, @Nullable URL docURL) {
        this.type = type;
        this.name = name;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        this.docURL = docURL;
        setData(data);
        this.data.getParentPackage().addChild(this);
    }

    public void addChild(@NonNull OldMetaContainer childContainer) {
        if (!childContainer.getParentPackage().equals(this)) {
            childContainer.getParentPackage().removePackage(childContainer);
            childContainer.setData(new ParentReference() {
                @Override
                public OldMetaContainer getParentPackage() {
                    return OldMetaContainer.this;
                }
            });
        }
        if (!packages.contains(childContainer)) packages.add(childContainer);
    }

    public boolean removePackage(@NonNull OldMetaContainer childPackage) {
        return packages.remove(childPackage);
    }

    public void setDocURL(URL docURL) {
        this.docURL = docURL;
    }

    public URL getDocURL() {
        return docURL;
    }

    public boolean containsPackage(@NonNull OldMetaContainer childPackage) {
        return packages.contains(childPackage);
    }

    public List<OldMetaContainer> getChildPackages() {     // Use Type enum for param, generalize method
        return packages;
    }

    public String getName() {
        return name;
    }

    public OldMetaContainer getParentPackage() {
        return data.getParentPackage();
    }

    public boolean isRootPackage() {
        return this.data.getParentPackage() == null;
    }

    public Type getType() {
        return this.type;
    }
}
