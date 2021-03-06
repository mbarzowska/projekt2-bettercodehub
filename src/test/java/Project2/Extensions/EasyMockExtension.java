package Project2.Extensions;

/* https://stackoverflow.com/a/47243856 */
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class EasyMockExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        EasyMockSupport.injectMocks(testInstance);
    }
}