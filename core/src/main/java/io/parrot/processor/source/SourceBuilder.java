package io.parrot.processor.source;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.exception.ParrotException;

public class SourceBuilder {

    public static IParrotSource Build(ParrotProcessorApi pProcessor) {
        try {
            IParrotSource source = (IParrotSource) Class.forName(pProcessor.getSource().getSourceClass())
                    .getConstructor(ParrotProcessorApi.class).newInstance(pProcessor);
            return source;
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

}
