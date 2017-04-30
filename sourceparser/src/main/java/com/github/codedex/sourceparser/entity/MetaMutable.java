package com.github.codedex.sourceparser.entity;

/**
 * @author Patrick "IPat" Hein
 *
 * The MetaMutable can be implemented by Meta*s whose attributes can be modified.
 * This should be implemented by all Meta*s so the MetaFetchers can work properly.
 */
public interface MetaMutable {

    /**
     * Implement MetaUpdater for custom Updater objects which can modify a Meta*s attributes.
     */
    interface MetaUpdater { /* Marker interface */ }

    /**
     * The custom Updater object can modify a Meta* object
     * @return the Updater object.
     */
    MetaUpdater getUpdater();
}
