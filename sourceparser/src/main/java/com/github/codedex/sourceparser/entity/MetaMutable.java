package com.github.codedex.sourceparser.entity;

/**
 * @author Patrick "IPat" Hein
 */

public interface MetaMutable {
    interface MetaInfo {
        void replace();
    }

    MetaInfo getContainer();
}
