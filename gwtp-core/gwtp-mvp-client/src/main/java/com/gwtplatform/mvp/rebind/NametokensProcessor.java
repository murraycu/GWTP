package com.gwtplatform.mvp.rebind;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.gwtplatform.dispatch.rest.processors.domain.Type;
import com.gwtplatform.dispatch.rest.processors.logger.Logger;
import com.gwtplatform.dispatch.rest.processors.outputter.Outputter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.shared.proxy.PlaceTokenRegistry;

@SupportedAnnotationTypes("com.gwtplatform.mvp.client.annotations.NameToken")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions(Logger.DEBUG_OPTION)
@AutoService(Processor.class)
public class NametokensProcessor extends AbstractProcessor {
    final String PACKAGE_NAME = PlaceTokenRegistry.class.getPackage().getName()
            .replace(".shared.", ".client.");
    final String SIMPLE_NAME = PlaceTokenRegistry.class.getSimpleName() + "Impl";
    private Set<? extends TypeElement> annotations;
    private RoundEnvironment roundEnv;
    private Logger logger;
    private Outputter outputter;
    private Set<String> allPlaces;
    private JavaFileObject file;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        logger = new Logger(processingEnv.getMessager(), processingEnv.getOptions());
        outputter = new Outputter(logger, new Type(NametokensProcessor.class), processingEnv.getFiler());
        allPlaces = Sets.newHashSet();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.annotations = annotations;
        this.roundEnv = roundEnv;

        if (file == null) {
            file = outputter.prepareSourceFile(new Type(PACKAGE_NAME, SIMPLE_NAME));
        }

        try {
            Set<? extends Element> elementsAnnotatedWithNameToken = roundEnv.getElementsAnnotatedWith(NameToken.class);
            for (Element element : elementsAnnotatedWithNameToken) {
                NameToken annotation = element.getAnnotation(NameToken.class);

                for (String place : annotation.value()) {
                    validatePlace(place, element);

                    allPlaces.add(place);
                }
            }

            if (roundEnv.processingOver()) {
                logger.debug("Trying to create the file");
                outputter
                        .withTemplateFile("com/gwtplatform/mvp/rebind/PlaceTokenRegistryImpl.vm")
                        .withParam("placeTokens", allPlaces)
                        .writeTo(new Type(PACKAGE_NAME, SIMPLE_NAME), file);
            }
        } catch (RuntimeException ex) {
            return false;
        }

        return false;
    }

    private void validatePlace(String place, Element element) {
        if (!place.startsWith("/") && !place.startsWith("!/")) {
            logger.error().context(element).log("Ta place c'est d'la marde");
            throw new RuntimeException();
        }
    }
}
