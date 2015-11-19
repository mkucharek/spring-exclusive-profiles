package pl.mkucharek.springexclusiveprofiles
import org.springframework.boot.SpringApplication
import spock.lang.Specification
/**
 * Copyright (c) 2007-2015 by AMG.net S.A.
 * All rights reserved.
 */
class SpringBootAppTest extends Specification {

    def "should load context successfully if one exclusive profile is active"() {
        given:
        def sa = new SpringApplication(SpringBootApp)
        sa.setAdditionalProfiles("dev")

        expect:
        sa.run()
    }

    def "should throw ISE if no exclusive profile active"() {
        when:
        SpringApplication.run(SpringBootApp)

        then:
        thrown(IllegalStateException)
    }

    def "should throw ISE if more than one exclusive profiles are active"() {
        given:
        def sa = new SpringApplication(SpringBootApp)
        sa.setAdditionalProfiles("dev", "test")

        when:
        sa.run()

        then:
        thrown(IllegalStateException)

    }
}
