package com.segment.android.integration;

import com.segment.android.errors.InvalidSettingsException;
import com.segment.android.integration.Integration;
import com.segment.android.integration.SimpleIntegration;
import com.segment.android.models.EasyJSONObject;

import android.content.Context;
import android.text.TextUtils;

public class IntegrationTest extends BaseIntegrationTest {

	
	@Override
	public Integration getIntegration() {
		return new SimpleIntegration() {

			@Override
			public String getKey() {
				return "provider";
			}
			
			@Override
			public void validate(EasyJSONObject settings)
					throws InvalidSettingsException {

				if (TextUtils.isEmpty(settings.getString("apiKey"))) {
					throw new InvalidSettingsException("apiKey", "Test Provider requires the apiKey setting.");
				}
			}

			@Override
			public void onCreate(Context context) {
				ready();
			}
		};
	}

	@Override
	public EasyJSONObject getSettings() {
		EasyJSONObject settings = new EasyJSONObject();
		settings.put("apiKey", "testkey");
		return settings;
	}
	
	

}