package gigigo.com.orchextra.data.datasources.api.model.mappers;

import com.gigigo.ggglib.network.mappers.RequestMapper;
import com.gigigo.ggglib.network.mappers.ResponseMapper;
import com.gigigo.orchextra.domain.entities.OrchextraPoint;
import gigigo.com.orchextra.data.datasources.api.model.responses.ApiPoint;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 17/12/15.
 */
public class PointMapper implements RequestMapper<OrchextraPoint, ApiPoint>, ResponseMapper<OrchextraPoint, ApiPoint>{

  @Override public ApiPoint modelToData(OrchextraPoint point) {
    ApiPoint apiPoint = new ApiPoint();

    apiPoint.setLat(String.valueOf(point.getLat()));
    apiPoint.setLng(String.valueOf(point.getLng()));

    return apiPoint;
  }

  @Override public OrchextraPoint dataToModel(ApiPoint apiPoint) {
    OrchextraPoint point = new OrchextraPoint();

    Double lat, lon;

    try{

      lat = Double.valueOf(apiPoint.getLat());
      lon =  Double.valueOf(apiPoint.getLng());

    }catch (NumberFormatException nfe){
      lat = lon = 0.0;
    }

    point.setLat(lat);
    point.setLng(lon);

    return point;
  }
}
