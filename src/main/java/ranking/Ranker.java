package ranking;

public interface Ranker<T> {
    int calculateRank(T obj);
}
