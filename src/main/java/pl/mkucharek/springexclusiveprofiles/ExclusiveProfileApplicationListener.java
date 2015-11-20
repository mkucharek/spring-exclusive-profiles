package pl.mkucharek.springexclusiveprofiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExclusiveProfileApplicationListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExclusiveProfileApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        validateExclusiveProfiles(event.getEnvironment());
    }

    private void validateExclusiveProfiles(ConfigurableEnvironment environment) {
        Set<String> envProfiles = environment.getProperty("envProfiles.all", Set.class, Collections.emptySet());

        final List<String> activeExclusiveProfiles = Arrays.stream(environment.getActiveProfiles())
                .filter(envProfiles::contains)
                .collect(Collectors.toList());

        if (activeExclusiveProfiles.size() == 0) {
            LOGGER.error("One of {} profiles needs to be active for the application to work", envProfiles);
            throw new IllegalStateException("No exclusive Spring Profile is active");

        } else if (activeExclusiveProfiles.size() > 1) {
            LOGGER.error("Only one of {} exclusive profiles can be active at the same time, but got {}",
                    envProfiles, activeExclusiveProfiles);
            throw new IllegalStateException("More than one exclusive profile active");

        }

        LOGGER.info("Current exclusive profile is: {}", activeExclusiveProfiles.get(0));

    }

}
