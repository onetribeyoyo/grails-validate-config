package com.onetribeyoyo.util

import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

/**
 *  Config.groovy can include something like
 *  validateConfig  {
 *      required = [ "a", "b", "c" ]
 *      expected = [ p:123, d:"foobar", q:"/dev/null" ]
 *  }
 */
class ConfigUtils {

    // NOTE: Logging is not yet configured when called from Config.groovy, so don't use the logger in these methods.

    /** call this from Config.groovy, passing grails.config.locations as an argument. */
    static void validateExternalFiles(def locations, params = [failWhenMissing: false]) {

        def missingLocations = []
        locations?.each { location ->
            def locationType = location.tokenize(":")[0]
            switch(locationType) {
            case "classpath":
                break

            case "file":
                def file = new File(location[5..-1])
                if (!file.exists() || !file.isFile()) missingLocations << location
                break

            default:
                println "ERROR: validateLocations: unknonw location type: \"${locationType}\".  Expected \"classpath\" or \"file\"."
                missingLocations << location
            }
        }
        if (missingLocations) {
            def message = "ERROR: validateLocations: Cannot locate external configuration file${missingLocations.size() > 1 ? 's' : ''}: ${missingLocations}."
            println "ERROR:"
            println message
            println "ERROR:"
            if (params.failWhenMissing) {
                throw new RuntimeException(message)
            }
        }
    }

    /** Call this from Bootstrap.groovy... */
    static void validateProperties() {
        def grailsConfig = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT).grailsApplication.config
        validateRequiredProperties(grailsConfig)
        validateExpectedProperties(grailsConfig)
    }

    static void validateRequiredProperties(def grailsConfig) {
        boolean missingRequiredProperties = false
        grailsConfig.validateConfig.required?.each { propertyName ->
            if (propertyValue(grailsConfig, propertyName) == [:]) {
                println "ERROR: validateProperties: No value specified for required config property \"${propertyName}\".".toString()
                missingRequiredProperties = true
            }
        }
        if (missingRequiredProperties) {
            throw new RuntimeException("ERROR: validateRequiredProperties(): Values must be provided for all validateConfig.required properties.")
        }
    }

    static void validateExpectedProperties(def grailsConfig) {
        grailsConfig.validateConfig.expected?.each { propertyName, defaultValue ->
            if (propertyValue(grailsConfig, propertyName) == [:]) {
                println "WARN: validateProperties: No value specified for expected config property \"${propertyName}\".  Using default: ${defaultValue}".toString()
                setPropertyValue(grailsConfig, propertyName, defaultValue)
                assert propertyValue(grailsConfig, propertyName) == defaultValue
            }
        }
    }

    private static def propertyValue(def grailsConfig, String propertyName) {
        propertyName.tokenize(".").inject(grailsConfig) { config, token ->
            (config instanceof Map) ? (config[token] ?: [:]) : [:]
        }
    }

    private static void setPropertyValue(def grailsConfig, String propertyName, def value) {
        def tokens = propertyName.tokenize(".")
        if (tokens.size() == 1) {
            grailsConfig[propertyName] = value
        } else {
            def end = tokens[-1]
            tokens[0..-2].inject(grailsConfig) { config, token ->
                (config instanceof Map) ? (config[token] ?: [:]) : [:]
            }[end] = value
        }
    }

}
