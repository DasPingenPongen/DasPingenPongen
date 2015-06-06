package com.daspingenpongen

import spock.lang.Specification

class GameEndCheckerSpec extends Specification {

    def "Game should be ended when one of the players reach 11 points"() {
        expect:
        new GameEndChecker().isGameEnd(leftScore, rightScore) == result
        where:
        leftScore | rightScore | result
        0         | 0          | false
        1         | 1          | false
        5         | 11         | true
        11        | 5          | true
        11        | 10         | false
        12        | 10         | true
        12        | 11         | false

    }
}
