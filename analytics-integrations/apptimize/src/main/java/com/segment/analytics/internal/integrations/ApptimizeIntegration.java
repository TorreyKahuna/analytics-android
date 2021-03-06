package com.segment.analytics.internal.integrations;

import com.apptimize.Apptimize;
import com.apptimize.Apptimize.OnExperimentRunListener;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.ValueMap;
import com.segment.analytics.internal.AbstractIntegration;
import com.segment.analytics.internal.model.payloads.IdentifyPayload;
import com.segment.analytics.internal.model.payloads.ScreenPayload;
import com.segment.analytics.internal.model.payloads.TrackPayload;
import java.util.Map.Entry;

/**
 * Apptimize allows you to instantly update your native app without waiting for
 * App or Play Store approvals, and easily see if the change improved the app
 * with robust A/B testing analytics.
 *
 * @see <a href="http://www.apptimize.com/">Apptimize</a>
 * @see <a href="https://segment.com/docs/integrations/apptimize/">Apptimize Integration</a>
 */
public class ApptimizeIntegration extends AbstractIntegration<Void>
    implements OnExperimentRunListener {
  static final String APPTIMIZE_KEY = "Apptimize";
  Analytics analytics;

  @Override public void initialize(Analytics analytics, ValueMap settings)
      throws IllegalStateException {
    this.analytics = analytics;
    Apptimize.setup(analytics.getApplication(), settings.getString("appkey"));
    if (settings.getBoolean("listen", false)) {
      Apptimize.setOnExperimentRunListener(this);
    }
  }

  @Override public String key() {
    return APPTIMIZE_KEY;
  }

  @Override public void identify(IdentifyPayload identify) {
    super.identify(identify);
    for (Entry<String, Object> entry : identify.traits().entrySet()) {
      if (entry.getValue() instanceof Integer) {
        Apptimize.setUserAttribute(entry.getKey(), (Integer) entry.getValue());
      } else {
        Apptimize.setUserAttribute(entry.getKey(), String.valueOf(entry.getValue()));
      }
    }
  }

  @Override public void track(TrackPayload track) {
    super.track(track);
    double value = track.properties().getDouble("value", Double.MIN_VALUE);
    if (value == Double.MIN_VALUE) {
      Apptimize.track(track.event());
    } else {
      Apptimize.track(track.event(), value);
    }
  }

  @Override public void screen(ScreenPayload screen) {
    super.screen(screen);
    Apptimize.track(screen.event());
  }

  @Override
  public void onExperimentRun(String experimentName, String variantName, boolean firstRun) {
    analytics.track("Experiment Viewed", new Properties()
        .putValue("experimentName", experimentName)
        .putValue("variationName", variantName));
  }
}
