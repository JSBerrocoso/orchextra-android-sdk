package com.gigigo.orchextra.control.controllers.proximity;

import com.gigigo.orchextra.control.InteractorResult;
import com.gigigo.orchextra.control.controllers.base.Controller;
import com.gigigo.orchextra.control.invoker.InteractorExecution;
import com.gigigo.orchextra.control.invoker.InteractorInvoker;
import com.gigigo.orchextra.domain.model.entities.proximity.OrchextraGeofence;
import com.gigigo.orchextra.domain.model.vo.OrchextraPoint;
import com.gigigo.orchextra.domain.model.actions.strategy.BasicAction;
import com.gigigo.orchextra.domain.model.triggers.params.GeoPointEventType;
import com.gigigo.orchextra.domain.model.triggers.strategy.types.Trigger;
import com.gigigo.orchextra.domain.interactors.actions.GetActionInteractor;
import com.gigigo.orchextra.domain.interactors.base.InteractorError;
import com.gigigo.orchextra.domain.interactors.geofences.RetrieveGeofenceTriggerInteractor;
import com.gigigo.orchextra.domain.interactors.geofences.errors.RetrieveProximityItemError;

import java.util.List;

import me.panavtec.threaddecoratedview.views.ThreadSpec;

public class ProximityItemController extends Controller<ProximityItemDelegate> {

    private final InteractorInvoker interactorInvoker;
    private final GetActionInteractor getActionInteractor;

    private final RetrieveGeofenceTriggerInteractor retrieveGeofenceTriggerInteractor;

    public ProximityItemController(ThreadSpec mainThreadSpec, InteractorInvoker interactorInvoker,
                                   GetActionInteractor getActionInteractor,
                                   RetrieveGeofenceTriggerInteractor retrieveGeofenceTriggerInteractor) {
        super(mainThreadSpec);
        this.interactorInvoker = interactorInvoker;
        this.getActionInteractor = getActionInteractor;
        this.retrieveGeofenceTriggerInteractor = retrieveGeofenceTriggerInteractor;
    }

    @Override
    public void onDelegateAttached() {
        getDelegate().onControllerReady();
    }

    private void registerGeofences(List<OrchextraGeofence> geofenceList) {
        getDelegate().registerGeofences(geofenceList);
    }

    private void doConfigurationRequest() {
        //TODO Call configuration interactor
    }

    public void processTriggers(List<String> triggeringGeofenceIds, OrchextraPoint triggeringPoint,
                                final GeoPointEventType geofenceTransition) {

        retrieveGeofenceTriggerInteractor.setTriggeringGeofenceIds(triggeringGeofenceIds);
        retrieveGeofenceTriggerInteractor.setTriggeringPoint(triggeringPoint);
        retrieveGeofenceTriggerInteractor.setGeofenceTransition(geofenceTransition);

        new InteractorExecution<>(retrieveGeofenceTriggerInteractor)
                .result(new InteractorResult<List<Trigger>>() {
                    @Override
                    public void onResult(List<Trigger> triggers) {
                        executeActions(triggers);
                    }
                })
                .error(RetrieveProximityItemError.class, new InteractorResult<InteractorError>() {
                  @Override public void onResult(InteractorError result) {
                    // TODO Do nothing when the trigger doesn't exist¿?
                  }
                })
                .execute(interactorInvoker);
    }

    private void executeActions(List<Trigger> triggers) {
        for (Trigger trigger : triggers) {
            executeAction(trigger);
        }
    }

    private void executeAction(Trigger trigger) {
        getActionInteractor.setActionCriteria(trigger);

        new InteractorExecution<>(getActionInteractor)
                .result(new InteractorResult<BasicAction>() {
                  @Override public void onResult(BasicAction result) {
                    //TODO Do the action obtained
                  }
                })
                .execute(interactorInvoker);
    }

}
