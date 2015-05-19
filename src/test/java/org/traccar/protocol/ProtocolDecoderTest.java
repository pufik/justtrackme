package org.traccar.protocol;

import org.traccar.ContextFactory;
import org.traccar.helper.TestDataManager;

public class ProtocolDecoderTest {

    static {
        try {
            ContextFactory.init(new TestDataManager());
        } catch(Exception error) {
        }
    }

}
