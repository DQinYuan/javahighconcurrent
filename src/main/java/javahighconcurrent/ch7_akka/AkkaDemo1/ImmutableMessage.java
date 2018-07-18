package javahighconcurrent.ch7_akka.AkkaDemo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 燃烧杯 on 2018/5/18.
 */
public final class ImmutableMessage {
    private final int sequenceNumber;

    private final List<String> values;

    public ImmutableMessage(int sequenceNumber, List<String> values) {
        this.sequenceNumber = sequenceNumber;
        this.values = Collections.unmodifiableList(new ArrayList<>(values));
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public List<String> getValues() {
        return values;
    }
}
