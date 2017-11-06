package org.superbiz.utils;

import ratpack.error.ClientErrorHandler;
import ratpack.handling.Context;

public class CustomErrorHandler implements ClientErrorHandler {
    @Override
    public void error(Context ctx, int statusCode) throws Exception {
        ctx.getResponse().status(404);
        ctx.render(ctx.file("static/404.html"));
    }
}
