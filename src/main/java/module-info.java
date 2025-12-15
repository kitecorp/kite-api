open module kite.api {
    exports cloud.kitelang.api;
    exports cloud.kitelang.api.schema;
    exports cloud.kitelang.api.resource;
    exports cloud.kitelang.api.annotations;

    requires java.logging;
    requires static lombok;
    requires java.compiler;
    requires org.apache.commons.lang3;
}
