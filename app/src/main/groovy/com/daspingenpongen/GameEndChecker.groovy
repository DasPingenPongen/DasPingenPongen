package com.daspingenpongen

import groovy.transform.CompileStatic

@CompileStatic
final class GameEndChecker {

    boolean isGameEnd(int leftScore, int rightScore) {
        return (leftScore >= 11 || rightScore >= 11) && Math.abs(leftScore - rightScore) > 1
    }

}