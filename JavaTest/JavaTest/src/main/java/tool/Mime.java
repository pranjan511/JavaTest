package tool;

public enum Mime {
    JSON("application/json");

    private String mimeType;

    Mime(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
