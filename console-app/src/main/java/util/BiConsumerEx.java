package util;

/**
 * Functional interface, It take two params with possible exception.
 *
 * @param <L> first arg
 * @param <R> second arg
 */
@FunctionalInterface
public interface BiConsumerEx<L, R> {
    /**
     * Accept two args.
     *
     * @param left  first.
     * @param right second.
     * @throws Exception possible exception.
     */
    void accept(L left, R right) throws Exception;
}