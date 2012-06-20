package org.smartly.packages.velocity.config;

import org.smartly.commons.repository.deploy.FileDeployer;

public class Deployer extends FileDeployer {

    public Deployer(final String targetFolder) {
        super("", targetFolder,
                false, false, false);
    }

    @Override
    public byte[] compress(byte[] data, final String filename) {
        return null;
    }
}
