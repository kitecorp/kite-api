package cloud.kitelang.api.schema;

import cloud.kitelang.api.annotations.Schema;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("cloud.kitelang.Schema")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class SchemaProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var element : roundEnv.getElementsAnnotatedWith(Schema.class)) {
            var myObject = element.getAnnotation(Schema.class);


        }
        return true;
    }
}
