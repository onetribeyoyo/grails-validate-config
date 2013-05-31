package com.onetribeyoyo.util

import org.codehaus.groovy.control.ConfigurationException

/**
 *  Config.groovy can include something like
 *  validate  {
 *      required = [ "a", "b", "c" ]
 *      expected = [ "p":123, "d":"foobar", "q":"/dev/null" ]
 *  }
 */
class ConfigUtils {

    // NOTE: Logging is not yet configured when called from Config.groovy, so don't use the logger in these methods.

    /** call this from Config.groovy, passing grails.config.locations as an argument. */
    static void validateExternalFiles(def locations) {

        def missingLocations = []
        locations.each { location ->
            def locationType = location.tokenize(":")[0]
            switch(locationType) {
            case "classpath":
                break

            case "file":
                def file = new File(location[5..-1])
                if (!file.exists() || !file.isFile()) missingLocations << location
                break

            default:
                missingLocations << location
            }
        }
        if (missingLocations) {
            throw new ConfigurationException("Cannot locate external configuration file${missingLocations.size() > 1 ? 's' : ''}: ${missingLocations}.")
        }
    }

    static void validateRequiredProperties(def grailsConfig) {
        def missingValues = []
        grailsConfig.validate?.required?.each { propertyName ->
            if (propertyValue(grailsConfig, propertyName) == [:]) {
                println """ERROR: validateRequiredProperties: No value specified for required config property "${propertyName}"."""
                missingValues << propertyName
            }
        }
        if (missingValues) {
            throw new ConfigurationException("Values must be provided for required properties: ${missingValues}.")
        }
    }

    static void validateExpectedProperties(def grailsConfig) {
        grailsConfig.validate?.expected?.each { propertyName, defaultValue ->
            if (propertyValue(grailsConfig, propertyName) == [:]) {
                println """WARN: validateExpectedProperties: No value specified for expected config property "${propertyName}".  Using default: ${defaultValue}"""
                setPropertyValue(grailsConfig, propertyName, defaultValue)
            }
        }
    }

    private static propertyValue(def grailsConfig, String propertyName) {
        propertyName.tokenize(".").inject(grailsConfig) { config, token ->
            (config instanceof Map) ? (config[token] ?: [:]) : [:]
        }
    }

    private static void setPropertyValue(def grailsConfig, String propertyName, value) {

        println "setPropertyValue(.., \"${propertyName}\", ${value})"

        def tokens = propertyName.tokenize(".")
        if (tokens.size() == 1) {
            grailsConfig[propertyName] = value
        } else {
            setPropertyValue(grailsConfig[tokens[0]], propertyName.substring(tokens[0].length() + 1), value)
        }
    }

}
