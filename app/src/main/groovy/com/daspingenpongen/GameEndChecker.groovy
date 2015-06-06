package com.daspingenpongen

import groovy.transform.CompileStatic

@CompileStatic
final class GameEndChecker {

    boolean isGameEnd(int leftScore, int rightScore) {
        return (leftScore >= 11 || rightScore >= 11) && Math.abs(leftScore - rightScore) > 1
    }

    boolean shouldLeftServe(int leftScore, int rightScore) {
        int sum = leftScore + rightScore
        int dividedByTwo = (sum / 2).intValue()
        int moduloTwo = dividedByTwo % 2
        return moduloTwo == 0
    }
}