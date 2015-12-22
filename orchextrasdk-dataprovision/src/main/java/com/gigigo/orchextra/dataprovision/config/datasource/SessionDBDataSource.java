package com.gigigo.orchextra.dataprovision.config.datasource;

import com.gigigo.orchextra.domain.entities.ClientAuthCredentials;
import com.gigigo.orchextra.domain.entities.ClientAuthData;
import com.gigigo.orchextra.domain.entities.Crm;
import com.gigigo.orchextra.domain.entities.SdkAuthCredentials;
import com.gigigo.orchextra.domain.entities.SdkAuthData;
import com.gigigo.orchextra.domain.entities.SessionToken;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 21/12/15.
 */
public interface SessionDBDataSource {

  boolean saveSdkAuthCredentials(SdkAuthCredentials sdkAuthCredentials);
  boolean saveSdkAuthResponse(SdkAuthData sdkAuthData);
  boolean saveClientAuthCredentials(ClientAuthCredentials clientAuthCredentials);
  boolean saveClientAuthResponse(ClientAuthData clientAuthData);
  boolean saveUser(Crm crm);
  SessionToken getSessionToken();
  SdkAuthData getDeviceToken();
}
