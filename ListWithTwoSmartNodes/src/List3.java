import java.util.Iterator;
import java.util.NoSuchElementException;

import components.list.List;
import components.list.ListSecondary;

/**
 * {@code List} represented as a singly linked list, done "bare-handed", with
 * implementations of primary methods.
 *
 * <p>
 * Execution-time performance of all methods implemented in this class is O(1).
 * </p>
 *
 * @param <T>
 *            type of {@code List} entries
 * @convention <pre>
 * $this.leftLength >= 0  and
 * [$this.rightLength >= 0] and
 * [$this.preStart is not null]  and
 * [$this.lastLeft is not null]  and
 * [$this.finish is not null]  and
 * [$this.preStart points to the first node of a singly linked list
 *  containing $this.leftLength + $this.rightLength + 1 nodes]  and
 * [$this.lastLeft points to the ($this.leftLength + 1)-th node in
 *  that singly linked list]  and
 * [$this.finish points to the last node in that singly linked list]  and
 * [$this.finish.next is null]
 * </pre>
 * @correspondence <pre>
 * this =
 *  ([data in nodes starting at $this.preStart.next and running through
 *    $this.lastLeft],
 *   [data in nodes starting at $this.lastLeft.next and running through
 *    $this.finish])
 * </pre>
 */
public class List3<T> extends ListSecondary<T> {

    /**
     * Node class for singly linked list nodes.
     */
    private final class Node {

        /**
         * Data in node.
         */
        private T data;

        /**
         * Next node in singly linked list, or null.
         */
        private Node next;

    }

    /**
     * "Smart node" before front node of singly linked list.
     */
    private Node preFront;

    /**
     * Last left node of singly linked list in this.left.
     */
    private Node lastLeft;

    /**
     * Finish node of linked list.
     */
    private Node postFinish;

    /**
     * Length of this.left.
     */
    private int leftLength;

    /**
     * Length of this.right.
     */
    private int rightLength;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.preFront = new Node();
        this.postFinish = new Node();
        this.preFront.next = this.postFinish;
        this.postFinish = this.preFront;
        this.lastLeft = this.preFront;
        this.leftLength = 0;
        this.rightLength = 0;
    }

    /**
     * No-argument constructor.
     */
    public List3() {
        this.createNewRep();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final List3<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(List<T> source) {
        assert source instanceof List3<?> : ""
                + "Violation of: source is of dynamic type List2<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type List2<?>, and
         * the ? must be T or the call would not have compiled.
         */
        List3<T> localSource = (List3<T>) source;
        this.preFront = localSource.preFront;
        this.lastLeft = localSource.lastLeft;
        this.postFinish = localSource.postFinish;
        this.rightLength = localSource.rightLength;
        this.leftLength = localSource.leftLength;
        localSource.createNewRep();
    }

    @Override
    public final void addRightFront(T x) {
        assert x != null : "Violation of: x is not null";
        Node p = new Node();
        Node q = this.lastLeft;
        p.data = x;
        p.next = q.next;
        q.next = p;
        if (this.rightLength == 0) {
            this.postFinish = p;
        }
        this.rightLength++;
    }

    @Override
    public final T removeRightFront() {
        assert this.rightLength() > 0 : "Violation of: this.right /= <>";
        Node p = this.lastLeft;
        Node q = p.next;
        p.next = q.next;
        T x = q.data;
        if (this.rightLength == 1) {
            this.postFinish = this.lastLeft;
        }
        this.rightLength--;
        return x;
    }

    @Override
    public final void advance() {
        assert this.rightLength() > 0 : "Violation of: this.right /= <>";
        Node p = this.lastLeft;
        this.lastLeft = p.next;
        this.leftLength++;
        this.rightLength--;
    }

    @Override
    public final void moveToStart() {
        this.lastLeft = this.preFront;
        this.rightLength += this.leftLength;
        this.leftLength = 0;
    }

    @Override
    public final int rightLength() {
        return this.rightLength;
    }

    @Override
    public final int leftLength() {
        return this.leftLength;
    }

    @Override
    public final Iterator<T> iterator() {
        return new List2Iterator();
    }

    /**
     * Implementation of {@code Iterator} interface for {@code List2}.
     */
    private final class List2Iterator implements Iterator<T> {

        /**
         * Current node in the linked list.
         */
        private Node current;

        /**
         * No-argument constructor.
         */
        private List2Iterator() {
            this.current = List3.this.preFront.next;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public T next() {
            assert this.hasNext() : "Violation of: ~this.unseen /= <>";
            if (!this.hasNext()) {
                /*
                 * Exception is supposed to be thrown in this case, but with
                 * assertion-checking enabled it cannot happen because of assert
                 * above.
                 */
                throw new NoSuchElementException();
            }
            T x = this.current.data;
            this.current = this.current.next;
            return x;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "remove operation not supported");
        }

    }

    /*
     * Other methods (overridden for performance reasons) ---------------------
     */

    @Override
    public final void moveToFinish() {
        this.lastLeft = this.postFinish;
        this.leftLength += this.rightLength;
        this.rightLength = 0;
    }

}
