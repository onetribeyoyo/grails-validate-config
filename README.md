Grails validate-config Plugin
=============================

Adds methods to `ConfigObject` for validating expected and required
properties.  This is especially useful when the config has been
externalized.

To use it simply add something like this to `Config.groovy`

    validate  {
        required = [ "a", "b", "c" ]
        expected = [ "p":123, "d":"foobar", "q":"/dev/null" ]
    }

Then (usually in `BootStrap`) call

    grailsApplication.config.validateExpectedProperties()

to set defaults for missing expected properties. and call

    grailsApplication.config.validateRequiredProperties()

to check for required properties  A `ConfigurationException` will be
thrown is any are missing.

See the sample app at https://github.com/onetribeyoyo/validate-config/tree/master/example .
