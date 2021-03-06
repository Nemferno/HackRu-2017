package ddi.hackru.edu.drugtodrugapplication;

import java.util.List;

/**
 * Created by Jasper Bae on 4/22/2017.
 */

public class MedicationController
{

    public interface OnMedicationChangedListener
    {
        void onMedicationQueried(Medication medication);
    }

    private WebService webService;
    private Medication currentMedication;
    private OnMedicationChangedListener listener;

    public MedicationController()
    {
        webService = WebService.getInstance();
        webService.setOnRequestAdversitiesRespondedListener(new WebService.OnRequestAdversitiesRespondedListener() {
            @Override
            public void onRequestAdversitiesResponded(List<Adversity> adversities)
            {
                currentMedication.setAdversityList(adversities);

                if(listener != null)
                    listener.onMedicationQueried(currentMedication);
            }
        });
        webService.setOnRequestRxNormIdRespondedListener(new WebService.OnRequestRxNormIdRespondedListener() {
            @Override
            public void onRequestRxNormIdResponded(Medication medication)
            {
                if(medication != null) {
                    currentMedication = medication;
                    webService.requestAdversities(currentMedication.getRXNormID()); // redirect and try to get the list of adversity list
                }else
                {
                    currentMedication = null;

                    if(listener != null)
                        listener.onMedicationQueried(currentMedication);
                }
            }
        });
    }

    public void queryMedication(String drugName)
    {
        webService.requestRxNormId(drugName);
    }

    public void queryMedicationFromFile(Byte[] image)
    {
        webService.requestPhotoRecognition(image);
    }

    public void setOnMedicationChangedListener(OnMedicationChangedListener listener)
    {
        this.listener = listener;
    }

    public Medication getCurrentMedication()
    {
        return currentMedication;
    }

}
