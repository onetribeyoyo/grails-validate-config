import com.onetribeyoyo.util.ConfigUtils

class ValidateConfigGrailsPlugin {

    def version = "0.4"
    def grailsVersion = "2.0 > *"
    def title = "Validate Config Plugin"
    def author = "Andy Miller"
    def authorEmail = "onetribeyoyo@gmail.com"
    def organization = [ name: "Object Partners", url: "http://www.objectpartners.com/" ]

    def description = '''\
Adds methods to ConfigObject for validating expected and required properties.
To use it, specify validate.required and validate.expected in Config.groovy
'''

    def documentation = "https://github.com/onetribeyoyo/validate-config"

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
    }

}
