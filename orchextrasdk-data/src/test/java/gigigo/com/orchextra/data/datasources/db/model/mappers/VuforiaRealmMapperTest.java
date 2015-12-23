package gigigo.com.orchextra.data.datasources.db.model.mappers;

import com.gigigo.orchextra.domain.entities.Vuforia;

import org.junit.Test;

import gigigo.com.orchextra.data.datasources.builders.VuforiaBuilder;
import gigigo.com.orchextra.data.datasources.db.model.VuforiaRealm;

import static org.junit.Assert.assertEquals;

public class VuforiaRealmMapperTest {

    @Test
    public void should_map_model_to_data() throws Exception {
        Vuforia vuforia = VuforiaBuilder.Builder().build();

        VuforiaRealmMapper mapper = new VuforiaRealmMapper();
        VuforiaRealm vuforiaRealm = mapper.modelToData(vuforia);

        assertEquals(VuforiaBuilder.ACCESS_KEY, vuforiaRealm.getClientAccessKey());
        assertEquals(VuforiaBuilder.SECRET_KEY, vuforiaRealm.getClientSecretKey());
        assertEquals(VuforiaBuilder.LICENSE_KEY, vuforiaRealm.getLicenseKey());
        assertEquals(VuforiaBuilder.SERVER_ACCESS_KEY, vuforiaRealm.getServerAccessKey());
        assertEquals(VuforiaBuilder.SERVER_SECRET_KEY, vuforiaRealm.getServerSecretKey());
    }

    @Test
    public void should_map_data_to_model() throws Exception {
        VuforiaRealm vuforiaRealm = new VuforiaRealm();
        vuforiaRealm.setClientAccessKey(VuforiaBuilder.ACCESS_KEY);
        vuforiaRealm.setClientSecretKey(VuforiaBuilder.SECRET_KEY);
        vuforiaRealm.setLicenseKey(VuforiaBuilder.LICENSE_KEY);
        vuforiaRealm.setServerAccessKey(VuforiaBuilder.SERVER_ACCESS_KEY);
        vuforiaRealm.setServerSecretKey(VuforiaBuilder.SERVER_SECRET_KEY);

        VuforiaRealmMapper mapper = new VuforiaRealmMapper();
        Vuforia vuforia = mapper.dataToModel(vuforiaRealm);

        assertEquals(VuforiaBuilder.ACCESS_KEY, vuforia.getClientAccessKey());
        assertEquals(VuforiaBuilder.SECRET_KEY, vuforia.getClientSecretKey());
        assertEquals(VuforiaBuilder.LICENSE_KEY, vuforia.getLicenseKey());
        assertEquals(VuforiaBuilder.SERVER_ACCESS_KEY, vuforia.getServerAccessKey());
        assertEquals(VuforiaBuilder.SERVER_SECRET_KEY, vuforia.getServerSecretKey());
    }
}