import com.mongodb.*;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import test.BaseMongoDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;

/**
 * @author Saurav Singh
 **/
public class MongoConnection {
    private static Logger logger = LoggerFactory.getLogger(MongoConnection.class);
    private static MongoConnection instance = new MongoConnection();

    private MongoClient mongo = null;
    private Datastore dataStore = null;
    private Morphia morphia = null;

    private MongoConnection() {
    }

    public MongoClient getMongo() throws RuntimeException {
        if (mongo == null) {
            logger.debug("Starting Mongo");
            MongoClientOptions.Builder options = MongoClientOptions.builder()
                    .connectionsPerHost(4)
                    .maxConnectionIdleTime((30 * 1000))
                    .writeConcern(WriteConcern.ACKNOWLEDGED)
                    .maxConnectionLifeTime((120 * 1000));

            MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/admin", options);

            logger.info("About to connect to MongoDB @ " + uri.toString());

            try {
                mongo = new MongoClient(uri);
                System.out.println("Connected");
            } catch (MongoException ex) {
                logger.error("An error occoured when connecting to MongoDB", ex);
            } catch (Exception ex) {
                logger.error("An error occoured when connecting to MongoDB", ex);
            }
        }

        return mongo;
    }

    public Morphia getMorphia() {
        if (morphia == null) {
            logger.debug("Starting Morphia");
            morphia = new Morphia();

            logger.debug(format("Mapping packages for clases within %s", BaseMongoDO.class.getName()));
            morphia.mapPackageFromClass(BaseMongoDO.class);
            System.out.println(morphia);
        }

        return morphia;
    }

    public Datastore getDatastore() {
        if (dataStore == null) {
            String dbName = "testdb";
            logger.debug(format("Starting DataStore on DB: %s", dbName));
            dataStore = getMorphia().createDatastore(getMongo(), dbName);
        }

        return dataStore;
    }

    public void init() {
        System.out.println("Bootstraping");
        logger.info("Bootstraping");
        getMongo();
        getMorphia();
        getDatastore();
    }

    public void close() {
        logger.info("Closing MongoDB connection");
        if (mongo != null) {
            try {
                mongo.close();
                logger.debug("Nulling the connection dependency objects");
                mongo = null;
                morphia = null;
                dataStore = null;
            } catch (Exception e) {
                logger.error(format("An error occurred when closing the MongoDB connection\n%s", e.getMessage()));
            }
        } else {
            logger.warn("mongo object was null, wouldn't close connection");
        }
    }

    public static MongoConnection getInstance() {
        return instance;
    }
}
