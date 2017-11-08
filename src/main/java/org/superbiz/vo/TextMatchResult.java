package org.superbiz.vo;

public class TextMatchResult {
    private String maskedText;

    public TextMatchResult(String maskedText) {
        this.maskedText = maskedText;
    }

    public String getMaskedText() {
        return maskedText;
    }

    public void setMaskedText(String maskedText) {
        this.maskedText = maskedText;
    }
}
