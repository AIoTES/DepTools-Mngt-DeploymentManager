package eu.hopu.activage.metadataStorageClient.dto;

public class DeploymentMetadata {
    private String id;
    private String context;

    public DeploymentMetadata(String id, String context) {
        this.id = id;
        this.context = context;
    }

    public DeploymentMetadata() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "DeploymentMetadata{" +
                "id='" + id + '\'' +
                ", context='" + context + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && id.equals(((DeploymentMetadata)obj).id) && context.equals(((DeploymentMetadata)obj).context);
    }
}
