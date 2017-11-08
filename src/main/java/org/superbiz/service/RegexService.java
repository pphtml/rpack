package org.superbiz.service;

public interface RegexService {
    String markRegexOccurences(String regex, String text) throws ComputationExceededException;
}
