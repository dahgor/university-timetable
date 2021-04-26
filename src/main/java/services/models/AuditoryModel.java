package services.models;

public class AuditoryModel {
    public static final String EMPTY_LOCATION = "";
    public static final int EMPTY_ID = 0;

    private int id;
    private String location;

    public AuditoryModel() {
    }

    public AuditoryModel(int id) {
        this.id = id;
        this.location = EMPTY_LOCATION;
    }

    public AuditoryModel(String location) {
        this.id = EMPTY_ID;
        this.location = location;
    }

    public AuditoryModel(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuditoryModel other = (AuditoryModel) obj;
        if (id != other.id)
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Auditory [id=" + id + ", location=" + location + "]";
    }

}
