class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        grailsApplication.config.validateExpectedProperties() // will set defaults for missing expected properties
        grailsApplication.config.validateRequiredProperties() // will throw ConfigurationException for missing required properties

        grailsApplication.config.grails.mongo.validateExpectedProperties() // will set defaults for grails.mongo properties
        grailsApplication.config.grails.mongo.validateRequiredProperties() // will throw ConfigurationException for missing required grails.mongo properties
    }

    def destroy = {
    }
}
