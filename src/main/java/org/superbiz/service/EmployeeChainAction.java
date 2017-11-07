package org.superbiz.service;

import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;

public class EmployeeChainAction implements Action<Chain> {
    @Override
    public void execute(Chain chain) throws Exception {
        chain
            .path(ctx -> {
                EmployeeService service = ctx.get(EmployeeService.class);
                ctx
                    .byMethod(method -> method
                        .get(() -> service
                            .getEmployees()
                            .map(Jackson::json)
                            .then(ctx::render)
                        )
//                        .post(() -> ctx
//                                .parse(Jackson.fromJson(Meeting.class))
//                                .nextOp(service::addMeeting)
//                                .map(m -> "Added meeting for " + m.getOrganizer())
//                                .then(ctx::render)
//                        )
                    );
            });
//            .get(":id:\\d+/rate/:rating:[1-5]", ctx -> {
//                EmployeeService service = ctx.get(EmployeeService.class);
//                PathTokens pathTokens = ctx.getPathTokens();
//                service
//                        .rateMeeting(pathTokens.get("id"), pathTokens.get("rating"))
//                        .then(() -> ctx.redirect("/meeting"));
//            });
    }
}
