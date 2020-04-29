package test;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Version;
import org.bson.types.ObjectId;

/**
 * @author Saurav Singh
 **/
public class BaseMongoDO {
    @Id
    @Property("id")
    private ObjectId id;

    @Version
    @Property("version")
    private Long version;

    public BaseMongoDO() {
        super();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
