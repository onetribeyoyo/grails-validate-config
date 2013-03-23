import com.onetribeyoyo.util.ConfigUtils

import groovy.util.ConfigObject
import grails.util.Holders

class ValidateConfigGrailsPlugin {

    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Validate Config Plugin" // Headline display name of the plugin
    def author = "Andy Miller"
    def authorEmail = "onetribeyoyo@gmail.com"
    def description = '''
Brief summary/description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/validate-config"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement= [ system: "github", url: "https://github.com/onetribeyoyo/validate-config/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/onetribeyoyo/validate-config" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        ConfigObject.metaClass.validateRequiredProperties = { ->
            ConfigUtils.validateRequiredProperties(delegate)
        }
        ConfigObject.metaClass.validateExpectedProperties = { ->
            ConfigUtils.validateExpectedProperties(delegate)
        }
        ConfigObject.metaClass.validateConfig = { ->
            ConfigObject.validateRequiredProperties()
            ConfigObject.validateExpectedProperties()
        }

        //Config.metaClass.static.validateExternalFiles = { ->
        //    org.onetribeyoyo.util.ConfigUtils.validateExternalFiles(grails?.config?.locations)
        //}
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
