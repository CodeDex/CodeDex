package com.github.codedex.sourceparser.fetcher;

import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * @author Patrick "IPat" Hein
 *
 * Used as marker and for utility methods.
 */

public abstract class Fetcher {
    protected <T extends MetaModel> T fixParentDependency(T child) {
        MetaModel iterator = child;
        while (!iterator.isRoot()) {
            if (!iterator.getParent().getChildren().contains(iterator))
                iterator.getParent().getUpdater().getChildren().add(iterator);
            iterator = iterator.getParent();
        }
        return child;
    }
}
