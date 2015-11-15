package pl.mkucharek.springframework.exclusiveprofiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by mkucharek.
 */
public class ProfileValidatingRunListener extends NoOpRunListener {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileValidatingRunListener.class);

    private final SpringApplication application;
    private final String[] args;

    public ProfileValidatingRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.application = application;
        this.args = args;
    }

    /*
     * Courtesy of https://stackoverflow.com/a/22695031
     */
    public static <T> Collector<T, ?, T> singletonCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

        String[] exclusiveProfilesArr = environment.getProperty("spring.envProfiles.all", String[].class, new String[0]);
        Set<String> exclusiveProfiles = new HashSet<>(Arrays.asList(exclusiveProfilesArr));

        try {
            String activeEnvProfile = Arrays.stream(environment.getActiveProfiles())
                    .filter(exclusiveProfiles::contains)
                    .collect(singletonCollector());

            LOG.info("Current env profile is: {}", activeEnvProfile);

        } catch (IllegalStateException e) {
            LOG.warn("Caught exception when determining active env profile", e);
        }

    }


}
