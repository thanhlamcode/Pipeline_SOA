package pipes;

import core.entities.IFilter;
import core.entities.IMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class PipelineBase {
    protected List<IFilter> filters = new ArrayList<>();

    public void addFilter(IFilter filter) {
        filters.add(filter);
    }

    public abstract IMessage process(IMessage message);
}
