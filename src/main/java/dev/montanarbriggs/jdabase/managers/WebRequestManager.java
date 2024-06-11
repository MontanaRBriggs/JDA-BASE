/*
 * The MIT License (MIT)
 *
 * Copyright © 2024 - Montana R. Briggs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package dev.montanarbriggs.jdabase.managers;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebRequestManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestManager.class);

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static boolean isValidUrl(String requestUrl) {
        // TODO: Apparently this was commonplace... in 2008. I dislike this solution as it constructs and destructs
        // TODO: an object for the sole purpose of checking if an exception is thrown upon construction... This is
        // TODO: frankly just a poor way of doing things.

        // Relevant StackOverflow post with over 157,000 views.
        // https://stackoverflow.com/questions/2230676/how-to-check-for-a-valid-url-in-java.

        // TODO: Upon testing, this will throw an exception (IAE) if the URI is not absolute... even though a URL
        // TODO: such as "google.com" is *technically* valid, and something that a user would most certainly
        // TODO: enter at *some* point. This solution is unworkable, and a better one must be implemented.

        // A catch for IAE was added temporarily.

        try {
            new URI(requestUrl).toURL();

            return true;
        } catch(URISyntaxException | MalformedURLException | IllegalArgumentException urlException) {
            return false;
        }
    }

    // TODO: All three error states return empty string objects, meaning there is no way for the caller
    // TODO: of these methods to get important data such as the HTTP status code (i.e., 404). This should
    // TODO: be refactored to ensure that all relevant error information is passed to the caller.
    public static String getRequest(String requestUrl, Headers requestHeaders) {
        Request getRequest = new Request.Builder()
                .headers(requestHeaders)
                .url(requestUrl)
                .get()
                .build();

        try (Response requestResponse = HTTP_CLIENT.newCall(getRequest).execute()) {
            if (!requestResponse.isSuccessful()) {
                LOGGER.warn(String.format(
                        "Web request to %s was unsuccessful with response %d.", requestUrl, requestResponse.code()
                ));

                return "";
            }

            if (requestResponse.body() == null) {
                LOGGER.warn(String.format(
                        "Web request to %s returned an empty body.", requestUrl
                ));

                return "";
            }

            return requestResponse.body().string();
        } catch (IOException requestException) {
            LOGGER.error(String.format(
                    "Request to %s suffered an exception, \"%s\".", requestUrl, requestException.getMessage()
            ));
        }

        return "";
    }
}
