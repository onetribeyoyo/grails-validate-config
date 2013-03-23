package com.onetribeyoyo.util

import groovy.util.ConfigObject
import org.codehaus.groovy.control.ConfigurationException

class ConfigUtilsTest extends GroovyTestCase {

    @Test
    void expected_topLevelProperty_missing() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":"defaultValue"]

        assert !config.foo
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == "defaultValue"
    }
    @Test
    void expected_topLevelProperty_found() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":"defaultValue"]
        config.foo = "definedValue"

        assert config.foo == "definedValue"
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == "definedValue"
    }

    @Test
    void expected_secondLevelProperty_missing() {
        def config = new ConfigObject()
        config.validate.expected = ["foo.bar":"defaultValue"]

        assert !config.foo.bar
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo.bar == "defaultValue"
    }
    @Test
    void expected_secondLevelProperty_found() {
        def config = new ConfigObject()
        config.validate.expected = ["foo.bar":"defaultValue"]
        config.foo.bar = "definedValue"

        assert config.foo.bar == "definedValue"
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo.bar == "definedValue"
    }

    @Test
    void expected_deepProperty_missing() {
        def config = new ConfigObject()
        config.validate.expected = ["foo.bar.baz":"defaultValue"]

        assert !config.foo.bar.baz
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo.bar.baz == "defaultValue"
    }
    @Test
    void expected_deepProperty_found() {
        def config = new ConfigObject()
        config.validate.expected = ["foo.bar":"defaultValue"]
        config.foo.bar = "definedValue"

        assert config.foo.bar == "definedValue"
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo.bar == "definedValue"
    }

    @Test
    void expected_property_with_empty_string_default() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":""]

        assert config.foo != ""
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == ""
    }

    @Test
    void expected_property_with_false_default() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":false]

        assert config.foo != false
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == false
    }
    @Test
    void expected_property_with_true_default() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":true]

        assert config.foo != true
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == true
    }

    @Test
    void requiredMissing() {
        def config = new ConfigObject()
        config.validate.required = ["foo"]

        assert !config.foo
        try {
            ConfigUtils.validateRequiredProperties(config)
            fail()
        } catch (ConfigurationException ex) {
            assert ex.message == "Values must be provided for required properties: [foo]."
        }
    }
    @Test
    void requiredFound() {
        def config = new ConfigObject()
        config.validate.required = ["foo"]
        config.foo = "definedValue"

        assert config.foo == "definedValue"
        try {
            ConfigUtils.validateRequiredProperties(config)
        } catch (ConfigurationException ex) {
            fail()
        }
    }



    @Test
    void expect_several_none_found() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":"defaultValue1", "bar":"defaultValue2", "baz":"defaultValue3"]

        assert !config.foo
        assert !config.bar
        assert !config.baz
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == "defaultValue1"
        assert config.bar == "defaultValue2"
        assert config.baz == "defaultValue3"
    }
    @Test
    void expect_several_all_found() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":"defaultValue1", "bar":"defaultValue2", "baz":"defaultValue3"]
        config.foo = "defined1"
        config.bar = "defined2"
        config.baz = "defined3"

        assert config.foo == "defined1"
        assert config.bar == "defined2"
        assert config.baz == "defined3"
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == "defined1"
        assert config.bar == "defined2"
        assert config.baz == "defined3"
    }
    @Test
    void expect_several_some_found() {
        def config = new ConfigObject()
        config.validate.expected = ["foo":"defaultValue1", "bar":"defaultValue2", "baz":"defaultValue3"]
        config.foo = "defined1"
        config.baz = "defined3"

        assert config.foo == "defined1"
        assert !config.bar
        assert config.baz == "defined3"
        ConfigUtils.validateExpectedProperties(config)
        assert config.foo == "defined1"
        assert config.bar == "defaultValue2"
        assert config.baz == "defined3"
    }

    @Test
    void require_several_none_found() {
        def config = new ConfigObject()
        config.validate.required = ["foo", "bar", "baz"]

        assert !config.foo
        assert !config.bar
        assert !config.baz
        try {
            ConfigUtils.validateRequiredProperties(config)
            fail()
        } catch (ConfigurationException ex) {
            assert ex.message == "Values must be provided for required properties: [foo, bar, baz]."
        }
    }
    @Test
    void require_several_all_found() {
        def config = new ConfigObject()
        config.validate.required = ["foo", "bar", "baz"]
        config.foo = "defined1"
        config.bar = "defined2"
        config.baz = "defined3"

        assert config.foo == "defined1"
        assert config.bar == "defined2"
        assert config.baz == "defined3"
        try {
            ConfigUtils.validateRequiredProperties(config)
        } catch (ConfigurationException ex) {
            fail()
        }
    }
    @Test
    void require_several_some_found() {
        def config = new ConfigObject()
        config.validate.required = ["foo", "bar", "baz"]
        config.foo = "defined1"
        config.baz = "defined3"

        assert config.foo == "defined1"
        assert !config.bar
        assert config.baz == "defined3"
        try {
            ConfigUtils.validateRequiredProperties(config)
            fail()
        } catch (ConfigurationException ex) {
            assert ex.message == "Values must be provided for required properties: [bar]."
        }
    }

}
