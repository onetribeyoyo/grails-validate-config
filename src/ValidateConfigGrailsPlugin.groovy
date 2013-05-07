import com.onetribeyoyo.util.ConfigUtils

import org.codehaus.groovy.grails.commons.GrailsApplication

class ValidateConfigGrailsPlugin {

    def version = "0.4.2"
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

        // See doc for imposeMetaClass
        def grailsApplication = ctx.getBean(GrailsApplication.APPLICATION_ID)
        grailsApplication.config = imposeMetaClass(new ConfigObject().merge(grailsApplication.config))
    }

    /**
     *  Ran into a problem where it appeared that doWithDynamicMethods was not being called when run as war.
     *
     *  Thanks to Danial Woods for explaining the problem (and offering a solution.)
     *
     *  http://grails.1312388.n4.nabble.com/plugin-s-doWithDynamicMethods-not-called-when-run-as-war-td4644023.html
     *
     *  Danial Woods wrote:
     *  Long story short, this is kind of a bug (kind of).  In run-war much of the application context is
     *  initialized outside of the Groovy ecosystem, so the objects are never enriched with the GroovyObject
     *  support that you would expect.
     *
     *  The plugin's doWithDynamicMethods closure is indeed being called, but since the grailsApplication
     *  config doesn't have a metaClass, the new methods that you've offered aren't available, and hence the
     *  BootStrap calls fail.  You may notice from the error log that Groovy suggests your dynamic methods as
     *  possible solutions to the exception.
     *
     *  I can offer the following as a quick fix [snip]
     *
     *  This will trade out the grailsApplication config instance with a groovy-enriched one, and will inherit
     *  your dynamic methods as well.
     *
     *  I'll look into a long term fix for this, but it's going to involve substantial changes to how we're
     *  setting up the IsolatedTomcat server instance. In the long run, it's probably a worthwhile thing to
     *  do.  In the short run, you can use the above code modifications to suit your needs.
     */
    private static final def imposeMetaClass = { m ->
        if (m instanceof ConfigObject) {
            m.keySet().each { k ->
                def obj = m[k]
                if (obj instanceof ConfigObject) {
                    m[k] = ValidateConfigGrailsPlugin.imposeMetaClass(new ConfigObject().merge(obj))
                }
            }
        }
        m
    }

}
