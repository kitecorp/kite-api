open module kite.api {
    exports io.kite.api;
    exports io.kite.api.schema;
    exports io.kite.api.resource;
    exports io.kite.api.annotations;

    requires java.logging;
    requires static lombok;
    requires org.pf4j;
    requires java.compiler;
    requires org.apache.commons.lang3;
}