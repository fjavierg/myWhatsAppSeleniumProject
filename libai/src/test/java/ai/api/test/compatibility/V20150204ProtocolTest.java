/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package ai.api.test.compatibility;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Calendar;

import ai.api.AIConfiguration;
import ai.api.AIServiceException;
import ai.api.GsonFactory;
import ai.api.model.AIRequest;
import ai.api.test.compatibility.v20150204_protocol_model.AIResponseV20150204;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Test for backward compatibility to the first protocol version ("20150204")
 */
public class V20150204ProtocolTest {

    private static final String PROTOCOL_VERSION = "20150204";

    final Gson gson = GsonFactory.getDefaultFactory().getGson();

    @Test
    public void legacySpeechTest() throws MalformedURLException, AIServiceException {
        final AIConfiguration config = new AIConfiguration(
                "3485a96fb27744db83e78b8c4bc9e7b7",
                AIConfiguration.SupportedLanguages.English);

        config.setProtocolVersion(PROTOCOL_VERSION);

        final SimpleProtocolTestingService aiService = new SimpleProtocolTestingService(config);

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("hello");
        prepareRequest(aiRequest, config);

        final String textRequest = gson.toJson(aiRequest);
        final String textResponse = aiService.doDefaultProtocolTextRequest(textRequest);

        final AIResponseV20150204 aiResponse = gson.fromJson(textResponse, AIResponseV20150204.class);

        assertNotNull(aiResponse);
        assertEquals("Hi! How are you?", aiResponse.getResult().getSpeech());
    }


    private void prepareRequest(final AIRequest request, final AIConfiguration config) {
        request.setLanguage(config.getApiAiLanguage());
        request.setTimezone(Calendar.getInstance().getTimeZone().getID());
    }
}
