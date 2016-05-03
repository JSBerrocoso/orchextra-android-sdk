/*
 * Created by Orchextra
 *
 * Copyright (C) 2016 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gigigo.com.orchextra.data.datasources.db.auth;

import com.gigigo.ggglib.mappers.ModelToExternalClassMapper;
import com.gigigo.orchextra.domain.model.entities.authentication.ClientAuthData;
import com.gigigo.orchextra.domain.model.entities.authentication.Crm;
import com.gigigo.orchextra.domain.model.entities.authentication.SdkAuthData;
import com.gigigo.orchextra.domain.model.entities.credentials.ClientAuthCredentials;
import com.gigigo.orchextra.domain.model.entities.credentials.SdkAuthCredentials;

import gigigo.com.orchextra.data.datasources.db.RealmDefaultInstance;
import gigigo.com.orchextra.data.datasources.db.model.ClientAuthCredentialsRealm;
import gigigo.com.orchextra.data.datasources.db.model.ClientAuthRealm;
import gigigo.com.orchextra.data.datasources.db.model.CrmRealm;
import gigigo.com.orchextra.data.datasources.db.model.SdkAuthCredentialsRealm;
import gigigo.com.orchextra.data.datasources.db.model.SdkAuthRealm;
import io.realm.Realm;
import io.realm.RealmObject;


public class SessionUpdater {
    //TODO LIB_CRUNCH gggLib ini
    private final ModelToExternalClassMapper<SdkAuthData, SdkAuthRealm> sdkAuthRealmMapper;
    private final ModelToExternalClassMapper<ClientAuthData, ClientAuthRealm> clientAuthRealmMapper;
    private final ModelToExternalClassMapper<Crm, CrmRealm> crmRealmMapper;
    private final ModelToExternalClassMapper<SdkAuthCredentials, SdkAuthCredentialsRealm>
            sdkCredentialsRealmMapper;
    private final ModelToExternalClassMapper<ClientAuthCredentials, ClientAuthCredentialsRealm>
            clientCredentialsRealmMapper;

    public SessionUpdater(ModelToExternalClassMapper<SdkAuthData, SdkAuthRealm> sdkAuthRealmMapper,
                          ModelToExternalClassMapper<ClientAuthData, ClientAuthRealm> clientAuthRealmMapper,
                          ModelToExternalClassMapper<Crm, CrmRealm> crmRealmMapper,
                          ModelToExternalClassMapper<SdkAuthCredentials, SdkAuthCredentialsRealm> sdkCredentialsRealmMapper,
                          ModelToExternalClassMapper<ClientAuthCredentials, ClientAuthCredentialsRealm> clientCredentialsRealmMapper) {

        this.sdkAuthRealmMapper = sdkAuthRealmMapper;
        this.clientAuthRealmMapper = clientAuthRealmMapper;
        this.crmRealmMapper = crmRealmMapper;
        this.sdkCredentialsRealmMapper = sdkCredentialsRealmMapper;
        this.clientCredentialsRealmMapper = clientCredentialsRealmMapper;
    }

    //TODO LIB_CRUNCH gggLib end
    //TODO LIB_CRUNCH realm
    public void updateSdkAuthCredentials(Realm realm, SdkAuthCredentials sdkAuthCredentials) {
        SdkAuthCredentialsRealm realmItem = getRealmItem(realm, SdkAuthCredentialsRealm.class);

        SdkAuthCredentialsRealm sdkAuthCredentialsRealm =
                sdkCredentialsRealmMapper.modelToExternalClass(sdkAuthCredentials);
        if (realmItem == null) {
            sdkAuthCredentialsRealm.setId(
                    RealmDefaultInstance.getNextKey(realm, SdkAuthCredentialsRealm.class));
        } else {
            sdkAuthCredentialsRealm.setId(realmItem.getId());
        }
//TODO LIB_CRUNCH realm
        realm.copyToRealmOrUpdate(sdkAuthCredentialsRealm);
    }

    //TODO LIB_CRUNCH realm
    public void updateSdkAuthResponse(Realm realm, SdkAuthData sdkAuthData) {
        SdkAuthRealm realmItem = getRealmItem(realm, SdkAuthRealm.class);

        SdkAuthRealm sdkAuthRealm = sdkAuthRealmMapper.modelToExternalClass(sdkAuthData);
        if (realmItem == null) {
            sdkAuthRealm.setId(RealmDefaultInstance.getNextKey(realm, SdkAuthRealm.class));
        } else {
            sdkAuthRealm.setId(realmItem.getId());
        }
//TODO LIB_CRUNCH realm
        realm.copyToRealmOrUpdate(sdkAuthRealm);
    }

    //TODO LIB_CRUNCH realm
    public void updateClientAuthCredentials(Realm realm, ClientAuthCredentials clientAuthCred) {
        ClientAuthCredentialsRealm realmItem = getRealmItem(realm, ClientAuthCredentialsRealm.class);

        ClientAuthCredentialsRealm clientAuthCredentialsRealm =
                clientCredentialsRealmMapper.modelToExternalClass(clientAuthCred);
        if (realmItem == null) {
            clientAuthCredentialsRealm.setId(
                    RealmDefaultInstance.getNextKey(realm, ClientAuthCredentialsRealm.class));
        } else {
            clientAuthCredentialsRealm.setId(realmItem.getId());
        }
//TODO LIB_CRUNCH realm
        realm.copyToRealmOrUpdate(clientAuthCredentialsRealm);
    }

    //TODO LIB_CRUNCH realm
    public void updateClientAuthResponse(Realm realm, ClientAuthData clientAuthData) {
        ClientAuthRealm realmItem = getRealmItem(realm, ClientAuthRealm.class);

        ClientAuthRealm clientAuthRealm = clientAuthRealmMapper.modelToExternalClass(clientAuthData);
        if (realmItem == null) {
            clientAuthRealm.setId(RealmDefaultInstance.getNextKey(realm, ClientAuthRealm.class));
        } else {
            clientAuthRealm.setId(realmItem.getId());
        }
//TODO LIB_CRUNCH realm
        realm.copyToRealmOrUpdate(clientAuthRealm);
    }

    //TODO LIB_CRUNCH realm
    public void updateCrm(Realm realm, Crm crm) {
        CrmRealm realmItem = getRealmItem(realm, CrmRealm.class);

        CrmRealm crmRealm = crmRealmMapper.modelToExternalClass(crm);
        if (realmItem == null) {
            crmRealm.setId(RealmDefaultInstance.getNextKey(realm, CrmRealm.class));
        } else {
            crmRealm.setId(realmItem.getId());
        }
//TODO LIB_CRUNCH realm
        realm.copyToRealmOrUpdate(crmRealm);
    }

    //TODO LIB_CRUNCH realm
    public void updateCrm(Realm realm, String crmId) {

        CrmRealm realmItem = getRealmItem(realm, CrmRealm.class);

        if (realmItem == null) {
            realmItem = new CrmRealm();
            realmItem.setId(RealmDefaultInstance.getNextKey(realm, CrmRealm.class));
        }

        realmItem.setCrmId(crmId);
//TODO LIB_CRUNCH realm
        realm.copyToRealmOrUpdate(realmItem);
    }

    //TODO LIB_CRUNCH realm
    private <T extends RealmObject> T getRealmItem(Realm realm, Class<T> classType) {
        return realm.where(classType).findFirst();
    }
}
