package com.space;

import io.vertx.core.file.FileSystem;

public class FileUpload {
    private FileSystem fileSystem;
    
    public FileUpload() {
    }

    public FileUpload(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

}
