package org.superbiz.chain;

import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.session.Session;
import ratpack.session.SessionKey;

public class SessionChainAction implements Action<Chain> {
    private static final SessionKey<Integer> SESSION_KEY = SessionKey.of("req-count", Integer.class);

    @Override
    public void execute(Chain chain) throws Exception {
        chain
                .all(ctx -> {
                    final Session session = ctx.get(Session.class);
                    session.get(SESSION_KEY)
                            .map(o -> o.orElse(0))
                            .flatMap(count -> session.set(SESSION_KEY, count + 1).promise())
                            .then(p -> ctx.next());
                })
                .get(ctx -> {
                    final Session session = ctx.get(Session.class);
                    session.get(SESSION_KEY).then(o -> {
                        ctx.render(o.get().toString());
                    });
                });
    }
}
