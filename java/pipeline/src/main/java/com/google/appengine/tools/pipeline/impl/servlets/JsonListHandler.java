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

import com.google.appengine.tools.pipeline.impl.PipelineManager;
import com.google.appengine.tools.pipeline.impl.model.JobRecord;
import com.google.appengine.tools.pipeline.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tkaitchuck@google.com (Tom Kaitchuck)
 */
public final class JsonListHandler {

    public static final String PATH_COMPONENT = "rpc/list";
    private static final String CLASS_FILTER_PARAMETER = "class_path";
    private static final String CURSOR_PARAMETER = "cursor";
    private static final String LIMIT_PARAMETER = "limit";
    private static final int DEFAULT_LIMIT = 100;

    private JsonListHandler() {
    }

    public static void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException {
        final String classFilter = getParam(req, CLASS_FILTER_PARAMETER);
        final String cursor = getParam(req, CURSOR_PARAMETER);
        final String limit = getParam(req, LIMIT_PARAMETER);
        final Pair<? extends Iterable<JobRecord>, String> pipelineRoots = PipelineManager.queryRootPipelines(
                classFilter, cursor, limit == null ? DEFAULT_LIMIT : Integer.parseInt(limit));
        final String asJson = JsonGenerator.pipelineRootsToJson(pipelineRoots);
        try {
            resp.getWriter().write(asJson);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private static String getParam(final HttpServletRequest req, final String name) {
        String value = req.getParameter(name);
        if (value != null) {
            value = value.trim();
            if (value.isEmpty()) {
                value = null;
            }
        }
        return value;
    }
}