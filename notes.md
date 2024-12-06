

To build and test the plugin...

from src
* `purge-caches validata-config`
* `grails package-plugin`

from an example dir
* ensure the BuildConfig refers to the right plugin version
* `grails install-plugin ../../src/grails-validate-config-0.4.2.zip`




http://grails.1312388.n4.nabble.com/plugin-s-doWithDynamicMethods-not-called-when-run-as-war-td4644023.html



Hey Andy,

Long story short, this is kind of a bug (kind of). In run-war much of
the application context is initialized outside of the Groovy ecosystem,
so the objects are never enriched with the GroovyObject support that you
would expect.

The plugin's doWithDynamicMethods closure is indeed being called, but
since the grailsApplication config doesn't have a metaClass, the new
methods that you've offered aren't available, and hence the BootStrap
calls fail. You may notice from the error log that Groovy suggests your
dynamic methods as possible solutions to the exception.

I can offer the following as a quick fix for your
ValidateConfigGrailsPlugin.groovy class:

    def doWithDynamicMethods = { ctx ->
        ConfigObject.metaClass.validateRequiredProperties = { ->
            ConfigUtils.validateRequiredProperties(delegate)
        }
        ConfigObject.metaClass.validateExpectedProperties = { ->
            ConfigUtils.validateExpectedProperties(delegate)
        }

        def grailsApplication = ctx.getBean(GrailsApplication.APPLICATION_ID)

        grailsApplication.config = imposeMetaClass(new ConfigObject().merge(grailsApplication.config))
    }

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

This will trade out the grailsApplication config instance with a
groovy-enriched one, and will inherit your dynamic methods as well.

I'll look into a long term fix for this, but it's going to involve
substantial changes to how we're setting up the IsolatedTomcat server
instance. In the long run, it's probably a worthwhile thing to do. In
the short run, you can use the above code modifications to suit your
needs.

Dan, t { "@danveloper" }
