import com.onetribeyoyo.util.ConfigUtils

class ValidateConfigGrailsPlugin {

    def version = "0.1"
    def grailsVersion = "2.0 > *"
    def title = "Validate Config Plugin"
    def author = "Andy Miller"
    def authorEmail = "onetribeyoyo@gmail.com"
    def description = '''\
Adds methods to ConfigObject for validating expected and required properties.

To use it simply add something like this to Config.groovy
validate  {
    required = [ "a", "b", "c" ]
    expected = [ "p":123, "d":"foobar", "q":"/dev/null" ]
}

Then, ussually in BootStrap, calling grailsApplication.config.validateExpectedProperties() will set defaults for missing expected properties.
and calling grailsApplication.config.validateRequiredProperties() will throw ConfigurationException for missing required properties.

See the sample app at https://github.com/onetribeyoyo/validate-config/tree/master/example .
'''

    def documentation = "http://grails.org/plugin/validate-config"

    def license = "APACHE"
    def issueManagement= [ system: "github", url: "https://github.com/onetribeyoyo/validate-config/issues" ]
    def scm = [ url: "https://github.com/onetribeyoyo/validate-config" ]

    def doWithDynamicMethods = { ctx ->
        ConfigObject.metaClass.validateRequiredProperties = { ->
            ConfigUtils.validateRequiredProperties(delegate)
        }
        ConfigObject.metaClass.validateExpectedProperties = { ->
            ConfigUtils.validateExpectedProperties(delegate)
        }

        //Config.metaClass.static.validateExternalFiles = { ->
        //    org.onetribeyoyo.util.ConfigUtils.validateExternalFiles(grails?.config?.locations)
        //}
    }
}
