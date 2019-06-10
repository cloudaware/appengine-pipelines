// Copyright 2011 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License. You may obtain a copy of
// the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations under
// the License.

package com.google.appengine.tools.pipeline.impl.servlets;

import com.google.appengine.tools.pipeline.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Servlet that handles all requests for the Pipeline framework.
 * Dispatches all requests to {@link TaskHandler}, {@link JsonTreeHandler} or
 * {@link StaticContentHandler} as appropriate
 *
 * @author rudominer@google.com (Mitch Rudominer)
 */
@SuppressWarnings("serial")
public final class PipelineServlet extends HttpServlet {

    public static final String BASE_URL_PROPERTY = "com.google.appengine.tools.pipeline.BASE_URL";
    public static final String BASE_URL = baseUrl();

    /**
     * Returns the Pipeline's BASE URL.
     * This must match the URL in web.xml
     */
    public static String baseUrl() {
        String baseURL = System.getProperty(BASE_URL_PROPERTY, "/_ah/pipeline/");
        if (!baseURL.endsWith("/")) {
            baseURL += "/";
        }
        return baseURL;
    }

    public static String makeViewerUrl(final UUID rootJobKey, final UUID jobKey) {
        return baseUrl() + "status.html?root=" + rootJobKey + "#pipeline-" + jobKey;
    }

    private Pair<String, RequestType> parseRequestType(final HttpServletRequest req) {
        String path = req.getPathInfo();
        path = path == null ? "" : path.substring(1); // Take off the leading '/'
        RequestType requestType = RequestType.HANDLE_STATIC;
        for (final RequestType rt : RequestType.values()) {
            if (rt.matches(path)) {
                requestType = rt;
                break;
            }
        }
        return Pair.of(path, requestType);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final Pair<String, RequestType> pair = parseRequestType(req);
        final RequestType requestType = pair.getSecond();
        final String path = pair.getFirst();
        switch (requestType) {
            case HANDLE_TASK:
                TaskHandler.doPost(req);
                break;
            case GET_JSON:
                JsonTreeHandler.doGet(req, resp);
                break;
            case GET_JSON_LIST:
                JsonListHandler.doGet(req, resp);
                break;
            case GET_JSON_CLASS_FILTER:
                JsonClassFilterHandler.doGet(req, resp);
                break;
            case ABORT_JOB:
                AbortJobHandler.doGet(req, resp);
                break;
            case DELETE_JOB:
                DeleteJobHandler.doGet(req, resp);
                break;
            case HANDLE_STATIC:
                StaticContentHandler.doGet(resp, path);
                break;
            default:
                throw new ServletException("Unknown request type: " + requestType);
        }
    }

    private enum RequestType {

        HANDLE_TASK(TaskHandler.PATH_COMPONENT),
        GET_JSON(JsonTreeHandler.PATH_COMPONENT),
        GET_JSON_LIST(JsonListHandler.PATH_COMPONENT),
        GET_JSON_CLASS_FILTER(JsonClassFilterHandler.PATH_COMPONENT),
        ABORT_JOB(AbortJobHandler.PATH_COMPONENT),
        DELETE_JOB(DeleteJobHandler.PATH_COMPONENT),
        HANDLE_STATIC("");

        private final String pathComponent;

        RequestType(final String pathComponent) {
            this.pathComponent = pathComponent;
        }

        public boolean matches(final String path) {
            return pathComponent.equals(path);
        }
    }
}