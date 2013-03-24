Grails validate-config Plugin
=============================

Adds methods to `ConfigObject` for validating expected and required
properties.  This is especially useful when the config has been
externalized and you want to ensure values are provided for all the
required/expected properties.

To use it simply add something like this to `Config.groovy`

    validate  {
        required = [ "a", "b", "c" ]
        expected = [ "p":123, "d":"foobar", "q":"/dev/null" ]
    }

Then (usually in `BootStrap`) to set defaults for missing expected
properties call

    grailsApplication.config.validateExpectedProperties()


To check for required properties call

grailsApplication.config.validateRequiredProperties()

A `ConfigurationException` will be thrown when required properties are
missing.

See the sample app at https://github.com/onetribeyoyo/validate-config/tree/master/example .
