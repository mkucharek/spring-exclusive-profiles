package pl.mkucharek.springframework.exclusiveprofiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by mkucharek.
 */
@Component
class ExclusiveProfileResolver {

    private final Environment environment;

    private final List<String> exclusiveProfiles;

    @Autowired
    public ExclusiveProfileResolver(Environment environment, @Value("${profiles.exclusive") String[] exclusiveProfiles) {
        this.environment = environment;
        this.exclusiveProfiles = Arrays.asList(exclusiveProfiles);
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

    public String getEnvironmentProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .filter(exclusiveProfiles::contains)
                .collect(singletonCollector());

    }
}
