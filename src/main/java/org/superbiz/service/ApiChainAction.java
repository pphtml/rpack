package org.superbiz.service;

import org.superbiz.vo.Response;
import org.superbiz.vo.TextMatchResult;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;

import javax.inject.Inject;
import java.util.logging.Logger;

public class ApiChainAction implements Action<Chain> {
    @Inject
    Logger logger;

    @Override
    public void execute(Chain chain) throws Exception {
        chain.get("regex", ctx -> {
            final String regex = ctx.getRequest().getQueryParams().get("regex");
            final String text = ctx.getRequest().getQueryParams().get("text");
            logger.info(String.format("Regex: %s, text: %s#", regex, text));

            Promise<Response> promise = Blocking.get(() -> {
                final RegexService regexService = ctx.get(RegexService.class);
                final String markRegexOccurences = regexService.markRegexOccurences(regex, text);
                final Response result = Response.of(new TextMatchResult(markRegexOccurences));
                return result;
            });

//        } catch (IllegalArgumentException e) {
//            return Response.error(new ProcessingError(ErrorCode.EC_REG_MISSING_ARGUMENT,
//                    e.getMessage(),
//                    javax.ws.rs.core.Response.Status.BAD_REQUEST
//            ));
//        } catch (ComputationExceededException e) {
//            return Response.error(new ProcessingError(ErrorCode.EC_REG_COMPUTATION_EXCEEDED,
//                    e.getMessage(),
//                    javax.ws.rs.core.Response.Status.FORBIDDEN));
//        }
            promise
                .map(Jackson::json)
                .then(ctx::render);
        });
    }
}
