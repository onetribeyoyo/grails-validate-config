Grails validate-config Plugin
=============================

Adds methods to `ConfigObject` for validating expected and required
properties.  This is especially useful when the config has been
externalized and you want to ensure values are provided for all the
required/expected properties.


Validating Expected and Required Properties
-------------------------------------------

Simply add something like this to `Config.groovy`

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

The `validate.expected` and `validate.required` data can be specified at
lower levels too...

    grails {
        mongo {
            validate {
                required = [
                    "grails.mongo.host",
                    "grails.mongo.databaseName"
                ]
                expected = [
                    "grails.mongo.port": 27017,
                    "grails.mongo.bucket": "project"
                ]
            }
        }
    }

This way you can validate portions of the config by calling

    grailsApplication.config.grails.mongo.validateExpectedProperties()

and so on.


Validating Existence of External Files
--------------------------------------

The `ConfigUtils.validateExternalFiles` method will check that a list of
files does exist.  Use it like this in `Config.groovy`.

    grails.config.locations << "file:${userHome}/.emacs"
    grails.config.locations << "file:${userHome}/.grails/${appName}-config.groovy"

    ConfigUtils.validateExternalFiles(grails.config.locations)

A `ConfigurationException` will be thrown when any of the files does not
exist.


Example
-------

See the sample apps for various grails versions at
https://github.com/onetribeyoyo/validate-config/tree/master/examples .


Changes
-------

### 0.4.3 ###

Fixes a bug with assigning default values for expected properties.

### 0.4.1 ###

Relaxing ConfigUtil method signatures: No longer requiring a
`ConfigObject`, now anything like a map is OK.

### 0.4 ###

Initial release.
