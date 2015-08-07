package com.gwtplatform.mvp.rebind;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.gwtplatform.mvp.client.annotations.NameToken;

@SupportedAnnotationTypes("com.gwtplatform.mvp.client.annotations.NameToken")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class NametokensProcessor extends AbstractProcessor {
    private Set<? extends TypeElement> annotations;
    private RoundEnvironment roundEnv;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.annotations = annotations;
        this.roundEnv = roundEnv;

        Set<? extends Element> elementsAnnotatedWithNameToken = roundEnv.getElementsAnnotatedWith(NameToken.class);
        for (Element element : elementsAnnotatedWithNameToken) {
            NameToken annotation = element.getAnnotation(NameToken.class);
            for (String place : annotation.value()) {
                validatePlace(place, element);
            }
        }

        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "************************* PROCESSING YAY ****************");

        return true;
    }

    private void validatePlace(String place, Element element) {
        if (!place.startsWith("/") && !place.startsWith("!/")) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Ta place c'est d'la marde", element);
        }
    }
}
