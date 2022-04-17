package com.se397.ultil;

import org.springframework.stereotype.Component;

@Component
public class FormatNumberRound {
    public double roundNumber(double a , double b) {
        return Math.ceil(a-(a*b/100));
    }
}
