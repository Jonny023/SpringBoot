package utils;

public class FileInfo {

    /**
     *  文件名
     */
    private String fileName;

    /**
     *  文件路径
     */
    private String path;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
