package core.entities;

public interface IFilter {
    boolean isMatch(IMessage message);
    IMessage execute(IMessage message);
}
