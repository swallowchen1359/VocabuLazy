package com.wishcan.www.vocabulazy.ga;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wishcan.www.vocabulazy.ga.manager.GAManager;
import com.wishcan.www.vocabulazy.ga.tags.GAAction;
import com.wishcan.www.vocabulazy.ga.tags.GACategory;
import com.wishcan.www.vocabulazy.utility.log.Logger;

/**
 * GABaseFragment is an abstract class used for sending Lifecycle event to Google Analysis.
 * All fragments extends the GABaseFragment will automatically sending their Lifecycle event as long as
 * they implement {@link GABaseFragment#getGALabel()}
 */
public abstract class GABaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();

//        // send resume event to GA
//        GAManager.getInstance().sendEvent(GACategory.LIFE_CYCLE, GAAction.RESUME, getGALabel(), 1);
    }

    @Override
    public void onPause() {
        super.onPause();

//        // send pause event to GA
//        GAManager.getInstance().sendEvent(GACategory.LIFE_CYCLE, GAAction.PAUSE, getGALabel(), 1);
    }

    protected void sendException(Context context, Exception e, boolean isFatal) {
        Logger.sendException(context, e, isFatal);
    }

    /**
     * The abstract function should be implement to specified which Fragment is being running their
     * life cycle.
     * @return The String return will be used as Label for Google Analysis used
     */
    protected abstract String getGALabel();
}